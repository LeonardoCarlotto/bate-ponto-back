package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;

import com.c_code.bate_ponto.dto.response.RegisterResponse;
import com.c_code.bate_ponto.service.register.RegisterService;
import com.c_code.bate_ponto.service.report.PdfService;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;

@RestController
@AllArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final RegisterService registerService;
    private final PdfService pdfService;
    private final UserRepository userRepository;

    @GetMapping("/point")
    public Map<String, Object> getUserPointReport(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @AuthenticationPrincipal UserDetailsImpl user) {

        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        
        List<RegisterResponse> registros = registerService.findByUserAndPeriodo(user.getId(), inicio.getMonthValue(), inicio.getYear());
        
        // Filtrar registros pelo período
        List<RegisterResponse> registrosFiltrados = registros.stream()
                .filter(r -> {
                    LocalDate dataRegistro = LocalDate.parse("2026-" + r.getDate().substring(3, 5) + "-" + r.getDate().substring(0, 2));
                    return !dataRegistro.isBefore(inicio) && !dataRegistro.isAfter(fim);
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("usuarioNome", user.getUser().getName());
        response.put("periodo", formatPeriod(inicio, fim));
        response.put("registros", formatRegistros(registrosFiltrados));
        response.put("totalHoras", calculateTotalHours(registrosFiltrados));
        response.put("diasTrabalhados", calculateWorkedDays(registrosFiltrados));

        return response;
    }

    @GetMapping("/point/general")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getGeneralPointReport(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @RequestParam(required = false) Long usuarioId) {

        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);

        List<User> usuarios;
        if (usuarioId != null) {
            User user = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuarios = List.of(user);
        } else {
            usuarios = userRepository.findAll();
        }

        List<Map<String, Object>> usuariosData = usuarios.stream()
                .map(user -> {
                    List<RegisterResponse> registros = registerService.findByUserAndPeriodo(
                            user.getId(), inicio.getMonthValue(), inicio.getYear());
                    
                    List<RegisterResponse> registrosFiltrados = registros.stream()
                            .filter(r -> {
                                LocalDate dataRegistro = LocalDate.parse("2026-" + r.getDate().substring(3, 5) + "-" + r.getDate().substring(0, 2));
                                return !dataRegistro.isBefore(inicio) && !dataRegistro.isAfter(fim);
                            })
                            .collect(Collectors.toList());

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("usuarioId", user.getId());
                    userData.put("usuarioNome", user.getName());
                    userData.put("totalHoras", calculateTotalHours(registrosFiltrados));
                    userData.put("dias", calculateWorkedDays(registrosFiltrados));
                    userData.put("registros", formatRegistros(registrosFiltrados));
                    
                    return userData;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("periodo", formatPeriod(inicio, fim));
        response.put("usuarios", usuariosData);

        return response;
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @AuthenticationPrincipal UserDetailsImpl user) throws Exception {

        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        
        List<RegisterResponse> registros = registerService.findByUserAndPeriodo(user.getId(), inicio.getMonthValue(), inicio.getYear());
        
        List<RegisterResponse> registrosFiltrados = registros.stream()
                .filter(r -> {
                    LocalDate dataRegistro = LocalDate.parse("2026-" + r.getDate().substring(3, 5) + "-" + r.getDate().substring(0, 2));
                    return !dataRegistro.isBefore(inicio) && !dataRegistro.isAfter(fim);
                })
                .collect(Collectors.toList());

        String csv = generateCsv(registrosFiltrados, user.getUser().getName());
        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio_ponto_" + dataInicio + "_" + dataFim + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.getBytes());
    }

    private String formatPeriod(LocalDate inicio, LocalDate fim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return inicio.format(formatter);
    }

    private List<Map<String, Object>> formatRegistros(List<RegisterResponse> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(RegisterResponse::getDate))
                .entrySet().stream()
                .map(entry -> {
                    List<RegisterResponse> diaRegistros = entry.getValue();
                    String entrada = diaRegistros.stream()
                            .filter(r -> r.getType() == com.c_code.bate_ponto.model.RegisterType.ENTRADA)
                            .map(RegisterResponse::getTime)
                            .findFirst()
                            .orElse("-");
                    
                    String saida = diaRegistros.stream()
                            .filter(r -> r.getType() == com.c_code.bate_ponto.model.RegisterType.SAIDA)
                            .map(RegisterResponse::getTime)
                            .findFirst()
                            .orElse("-");
                    
                    String horas = "-";
                    if (!entrada.equals("-") && !saida.equals("-")) {
                        horas = calculateHoursBetween(entrada, saida);
                    }

                    Map<String, Object> registroDia = new HashMap<>();
                    registroDia.put("data", entry.getKey());
                    registroDia.put("entrada", entrada);
                    registroDia.put("saida", saida);
                    registroDia.put("horas", horas);
                    
                    return registroDia;
                })
                .collect(Collectors.toList());
    }

    private String calculateTotalHours(List<RegisterResponse> registros) {
        Map<String, List<RegisterResponse>> agrupados = registros.stream()
                .collect(Collectors.groupingBy(RegisterResponse::getDate));
        
        long totalMinutos = 0;
        
        for (List<RegisterResponse> diaRegistros : agrupados.values()) {
            String entrada = diaRegistros.stream()
                    .filter(r -> r.getType() == com.c_code.bate_ponto.model.RegisterType.ENTRADA)
                    .map(RegisterResponse::getTime)
                    .findFirst()
                    .orElse(null);
            
            String saida = diaRegistros.stream()
                    .filter(r -> r.getType() == com.c_code.bate_ponto.model.RegisterType.SAIDA)
                    .map(RegisterResponse::getTime)
                    .findFirst()
                    .orElse(null);
            
            if (entrada != null && saida != null) {
                totalMinutos += calculateMinutesBetween(entrada, saida);
            }
        }
        
        long horas = totalMinutos / 60;
        long minutos = totalMinutos % 60;
        return horas + ":" + String.format("%02d", minutos);
    }

    private int calculateWorkedDays(List<RegisterResponse> registros) {
        return (int) registros.stream()
                .map(RegisterResponse::getDate)
                .distinct()
                .count();
    }

    private String calculateHoursBetween(String entrada, String saida) {
        long minutos = calculateMinutesBetween(entrada, saida);
        long horas = minutos / 60;
        long min = minutos % 60;
        return horas + ":" + String.format("%02d", min);
    }

    private long calculateMinutesBetween(String entrada, String saida) {
        String[] entradaParts = entrada.split(":");
        String[] saidaParts = saida.split(":");
        
        int entradaMinutos = Integer.parseInt(entradaParts[0]) * 60 + Integer.parseInt(entradaParts[1]);
        int saidaMinutos = Integer.parseInt(saidaParts[0]) * 60 + Integer.parseInt(saidaParts[1]);
        
        return saidaMinutos - entradaMinutos;
    }

    private String generateCsv(List<RegisterResponse> registros, String userName) {
        StringBuilder csv = new StringBuilder();
        csv.append("Data,Entrada,Saída,Horas\n");
        
        Map<String, List<RegisterResponse>> agrupados = registros.stream()
                .collect(Collectors.groupingBy(RegisterResponse::getDate));
        
        for (Map.Entry<String, List<RegisterResponse>> entry : agrupados.entrySet()) {
            List<RegisterResponse> diaRegistros = entry.getValue();
            
            String entrada = diaRegistros.stream()
                    .filter(r -> r.getType() == com.c_code.bate_ponto.model.RegisterType.ENTRADA)
                    .map(RegisterResponse::getTime)
                    .findFirst()
                    .orElse("-");
            
            String saida = diaRegistros.stream()
                    .filter(r -> r.getType() == com.c_code.bate_ponto.model.RegisterType.SAIDA)
                    .map(RegisterResponse::getTime)
                    .findFirst()
                    .orElse("-");
            
            String horas = "-";
            if (!entrada.equals("-") && !saida.equals("-")) {
                horas = calculateHoursBetween(entrada, saida);
            }
            
            csv.append(entry.getKey()).append(",")
               .append(entrada).append(",")
               .append(saida).append(",")
               .append(horas).append("\n");
        }
        
        return csv.toString();
    }
}
