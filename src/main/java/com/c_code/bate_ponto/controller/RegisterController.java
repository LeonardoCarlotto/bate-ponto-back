package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.c_code.bate_ponto.dto.request.RegisterEditRequest;
import com.c_code.bate_ponto.dto.request.RegisterManualRequest;
import com.c_code.bate_ponto.dto.request.RegisterRequest;
import com.c_code.bate_ponto.dto.response.RegisterResponse;
import com.c_code.bate_ponto.dto.request.WorkedHoursRequest;
import com.c_code.bate_ponto.dto.response.WorkedHoursResponse;
import com.c_code.bate_ponto.model.Register;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.repository.UserRepository;
import com.c_code.bate_ponto.service.register.RegisterService;
import com.c_code.bate_ponto.service.report.PdfService;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;

import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@RequestMapping("/registers")
public class RegisterController {

    private final RegisterService registerService;
    private final PdfService pdfService;
    private final UserRepository userRepository;

    @PostMapping
    public RegisterResponse register(@RequestBody RegisterRequest request, @AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.registerPoint(request, user);
    }

    @GetMapping("/user")
    public List<RegisterResponse> findByUser(@AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.findByUser(user.getId());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegisterResponse> findByUserId(@PathVariable Long userId) {
        return registerService.findByUser(userId);
    }

    @GetMapping("/user/pdf")
    public ResponseEntity<byte[]> reportPdf(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam int mes,
            @RequestParam int ano) throws Exception {

        List<RegisterResponse> registros = registerService.findByUserAndPeriodo(user.getId(), mes, ano);

        String nameUser = user.getUser().getName();

        ByteArrayOutputStream pdf = pdfService.gerarRelatorioPonto(registros, nameUser,
                registerService.calculateWorkedHoursForMonth(mes, ano, user.getId()));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio_ponto_" + mes + "_" + ano + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.toByteArray());
    }

    @GetMapping("/user/{userId}/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> reportPdfByUserId(
            @PathVariable Long userId,
            @RequestParam int mes,
            @RequestParam int ano) throws Exception {

        // Buscar dados do colaborador
        User collaborator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        List<RegisterResponse> registros = registerService.findByUserAndPeriodo(userId, mes, ano);

        String nameUser = collaborator.getName();

        ByteArrayOutputStream pdf = pdfService.gerarRelatorioPonto(registros, nameUser,
                registerService.calculateWorkedHoursForMonth(mes, ano, userId));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio_ponto_" + collaborator.getName().replace(" ", "_") + "_" + mes + "_" + ano + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.toByteArray());
    }

    @PostMapping("/worked-hours")
    public WorkedHoursResponse workedHours(@RequestBody WorkedHoursRequest request,
            @AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.calculateWorkedHours(request, user.getId());
    }

    @PutMapping("/{id}/edit")
    public RegisterResponse editRegister(@RequestBody RegisterEditRequest request,
            @AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long id) throws Exception {
        request.setRegisterId(id);
        return registerService.editRegister(request, user.getId());
    }

    @GetMapping("/edited/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegisterResponse> findAllRegisterEdited() {
        return registerService.findAllRegisterEdited();
    }

    @PostMapping("/manual")
    public ResponseEntity<RegisterResponse> createManual(
            @RequestBody RegisterManualRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        RegisterResponse response = registerService.createManualRegister(
                request,
                userDetails.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public RegisterResponse getRegister(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.findById(id, user.getId());
    }

    @GetMapping("/last")
    public ResponseEntity<RegisterResponse> getLastRegister(@AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.findLastByUser(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteRegister(@PathVariable Long id) {
        registerService.deleteRegister(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Registro deletado com sucesso");
        return ResponseEntity.ok(response);
    }

}
