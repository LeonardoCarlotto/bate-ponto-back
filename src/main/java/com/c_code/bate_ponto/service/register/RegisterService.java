package com.c_code.bate_ponto.service.register;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.c_code.bate_ponto.dto.request.RegisterEditRequest;
import com.c_code.bate_ponto.dto.request.RegisterManualRequest;
import com.c_code.bate_ponto.dto.request.RegisterRequest;
import com.c_code.bate_ponto.dto.response.RegisterResponse;
import com.c_code.bate_ponto.dto.request.WorkedHoursRequest;
import com.c_code.bate_ponto.dto.response.WorkedHoursResponse;
import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.*;

@Service
public class RegisterService {

    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final RegisterAuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    public RegisterService(RegisterRepository registerRepository,
            UserRepository userRepository, RegisterAuditRepository auditRepository,
            ObjectMapper objectMapper) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("null")
    public RegisterResponse registerPoint(RegisterRequest request, UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        RegisterType nextType = calculateNextType(user.getId());

        Register register = new Register();
        register.setUser(user);
        
        // Usa horário enviado pelo front, ou horário atual se não for enviado
        LocalDateTime dataTime = request.getDataTime() != null ? request.getDataTime() : LocalDateTime.now();
        register.setDataTime(dataTime);
        
        register.setType(nextType);

        registerRepository.save(register);

        return new RegisterResponse(
                register.getId(),
                user.getName(),
                user.getEmail(),
                register.getDataTime(),
                register.getType(), false, null, user.getUrlPhoto());
    }

    private RegisterType calculateNextType(Long userId) {

        List<Register> registers = registerRepository.findByUserIdOrderByDataTimeDesc(userId);

        if (registers.isEmpty()) {
            return RegisterType.ENTRADA;
        }

        Register last = registers.get(0);

        return last.getType() == RegisterType.ENTRADA
                ? RegisterType.SAIDA
                : RegisterType.ENTRADA;
    }

    public List<RegisterResponse> findByUser(Long userId) {

        return registerRepository.findByUserIdOrderByDataTimeDesc(userId)
                .stream()
                .map(r -> new RegisterResponse(r))
                .toList();
    }

    public List<RegisterResponse> findByUserAndPeriodo(Long userId, Integer mes, Integer ano) {
        List<Register> registros = registerRepository.findByUserIdOrderByDataTimeDesc(userId);

        if (mes != null && ano != null) {
            registros = registros.stream()
                    .filter(r -> r.getDataTime().getMonthValue() == mes && r.getDataTime().getYear() == ano)
                    .collect(Collectors.toList());
        }

        Collections.reverse(registros);
        return registros.stream()
                .map(r -> new RegisterResponse(r))
                .collect(Collectors.toList());
    }

    public List<RegisterResponse> findAllRegisterEdited() {

        return registerRepository.findAll()
                .stream()
                .map(r -> new RegisterResponse(r))
                .filter(r -> r.isEdited())
                .toList();
    }

    public WorkedHoursResponse calculateWorkedHours(WorkedHoursRequest request, Long userId) {

        LocalDate date = LocalDate.parse(request.getDate());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Register> registers = registerRepository.findByUserIdAndDataTimeBetweenOrderByDataTimeAsc(
                userId,
                start,
                end);

        Duration total = Duration.ZERO;
        LocalDateTime entryTime = null;

        for (Register r : registers) {
            if (r.getType() == RegisterType.ENTRADA) {
                entryTime = r.getDataTime();
            } else if (r.getType() == RegisterType.SAIDA && entryTime != null) {
                total = total.plus(Duration.between(entryTime, r.getDataTime()));
                entryTime = null;
            }
        }

        String formatted = formatDuration(total);

        return new WorkedHoursResponse(request.getDate(), formatted);
    }

    public WorkedHoursResponse calculateWorkedHoursForMonth(Integer mes, Integer ano, Long userId) {

        LocalDate startDate = LocalDate.of(ano, mes, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Register> registers = registerRepository
                .findByUserIdAndDataTimeBetweenOrderByDataTimeAsc(userId, start, end);

        Duration total = Duration.ZERO;
        LocalDateTime entryTime = null;

        for (Register r : registers) {
            if (r.getType() == RegisterType.ENTRADA) {
                entryTime = r.getDataTime();
            } else if (r.getType() == RegisterType.SAIDA && entryTime != null) {
                total = total.plus(Duration.between(entryTime, r.getDataTime()));
                entryTime = null;
            }
        }

        String formatted = formatDuration(total);

        // Retorna algo como "02/2026" e total de horas
        String mesAno = String.format("%02d/%d", mes, ano);
        return new WorkedHoursResponse(mesAno, formatted);
    }

    private String formatDuration(Duration duration) {

        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();

        return String.format("%02d:%02d", hours, minutes);
    }

    @SuppressWarnings("null")
    public RegisterResponse editRegister(RegisterEditRequest request, Long editorUserId) throws Exception {
        Register register = registerRepository.findById(request.getRegisterId())
                .orElseThrow(() -> new RuntimeException("Register not found"));

        String oldData = objectMapper.writeValueAsString(register);
        
        // Editar horário se fornecido
        if (request.getNewRegistro() != null) {
            LocalTime localTime = LocalTime.parse(request.getNewRegistro());
            LocalDate dataOriginal = register.getDataTime().toLocalDate();
            LocalDateTime dataHora = LocalDateTime.of(dataOriginal, localTime);
            register.setDataTime(dataHora);
        }
        
        // Editar tipo se fornecido
        if (request.getType() != null) {
            register.setType(RegisterType.valueOf(request.getType()));
        }

        String newData = objectMapper.writeValueAsString(register);

        RegisterAudit audit = new RegisterAudit();
        audit.setRegisterId(register.getId());
        audit.setUserId(register.getUser().getId());
        audit.setOldData(oldData);
        audit.setNewData(newData);
        audit.setObservation(request.getObservation());
        audit.setEditedAt(LocalDateTime.now());
        audit.setEditedByUserId(editorUserId);

        auditRepository.save(audit);
        register.setEdited(true);
        register.setObservation(request.getObservation());
        registerRepository.save(register);

        return new RegisterResponse(register);
    }

    public RegisterResponse createManualRegister(
            RegisterManualRequest request,
            Long userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Register register = new Register();
        register.setUser(user);
        register.setDataTime(request.getDataTime());
        register.setType(RegisterType.valueOf(request.getType()));
        register.setEdited(true);
        register.setObservation(request.getObservation());

        registerRepository.save(register);

        // Auditoria opcional
        RegisterAudit audit = new RegisterAudit();
        audit.setRegisterId(register.getId());
        audit.setUserId(userId);
        audit.setOldData("{}");
        audit.setNewData(objectMapper.writeValueAsString(register));
        audit.setObservation("Registro manual criado");
        audit.setEditedAt(LocalDateTime.now());
        audit.setEditedByUserId(userId);

        auditRepository.save(audit);

        return new RegisterResponse(register);
    }

}
