package com.c_code.bate_ponto.service.register;

import org.springframework.stereotype.Service;

import java.util.List;

import com.c_code.bate_ponto.dto.request.RegisterEditRequest;
import com.c_code.bate_ponto.dto.request.WorkedHoursRequest;
import com.c_code.bate_ponto.dto.response.RegisterResponse;
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
    public RegisterResponse registerPoint(UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        RegisterType nextType = calculateNextType(user.getId());

        Register register = new Register();
        register.setUser(user);
        register.setDataTime(LocalDateTime.now());
        register.setType(nextType);

        registerRepository.save(register);

        return new RegisterResponse(
                register.getId(),
                user.getName(),
                user.getEmail(),
                register.getDataTime(),
                register.getType(), false, null);
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

        LocalTime localTime = LocalTime.parse(request.getNewRegistro());
        LocalDate hoje = LocalDate.now();
        LocalDateTime dataHora = LocalDateTime.of(hoje, localTime);
        register.setDataTime(dataHora);
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

}
