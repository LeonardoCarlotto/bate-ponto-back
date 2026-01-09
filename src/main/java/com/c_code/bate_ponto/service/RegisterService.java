package com.c_code.bate_ponto.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.c_code.bate_ponto.dto.RegisterRequest;
import com.c_code.bate_ponto.dto.RegisterResponse;
import com.c_code.bate_ponto.dto.WorkedHoursRequest;
import com.c_code.bate_ponto.dto.WorkedHoursResponse;
import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class RegisterService {

    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public RegisterService(RegisterRepository registerRepository,
            UserRepository userRepository) {
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    public RegisterResponse registerPoint(RegisterRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
                register.getType().name());
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
                .map(r -> new RegisterResponse(
                        r.getId(),
                        r.getUser().getName(),
                        r.getUser().getEmail(),
                        r.getDataTime(),
                        r.getType().name()))
                .toList();
    }

    public WorkedHoursResponse calculateWorkedHours(WorkedHoursRequest request) {

        LocalDate date = LocalDate.parse(request.getDate());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Register> registers = registerRepository.findByUserIdAndDataTimeBetweenOrderByDataTimeAsc(
                request.getUserId(),
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

}
