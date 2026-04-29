package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.ContaPagarRequest;
import com.c_code.bate_ponto.dto.request.ContaPagarUpdateRequest;
import com.c_code.bate_ponto.dto.request.PagamentoRequest;
import com.c_code.bate_ponto.model.ContaPagar;
import com.c_code.bate_ponto.service.ContaPagarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/contas-pagar")
public class ContaPagarController {

    private final ContaPagarService contaPagarService;

    public ContaPagarController(ContaPagarService contaPagarService) {
        this.contaPagarService = contaPagarService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaPagar> getAllContasPagar() {
        return contaPagarService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContaPagar> getContaPagarById(@PathVariable Long id) {
        return contaPagarService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ContaPagar createContaPagar(@RequestBody ContaPagarRequest request) {
        return contaPagarService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContaPagar> updateContaPagar(@PathVariable Long id, @RequestBody ContaPagarUpdateRequest request) {
        try {
            ContaPagar contaPagar = contaPagarService.update(id, request);
            return ResponseEntity.ok(contaPagar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteContaPagar(@PathVariable Long id) {
        try {
            contaPagarService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Conta a pagar deletada com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", "Conta a pagar não encontrada");
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContaPagar> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            ContaPagar contaPagar = contaPagarService.updateStatus(id, request.get("status"));
            return ResponseEntity.ok(contaPagar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/pagar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContaPagar> registrarPagamento(@PathVariable Long id, @RequestBody PagamentoRequest pagamentoRequest) {
        try {
            ContaPagar contaPagar = contaPagarService.registrarPagamento(id, pagamentoRequest);
            return ResponseEntity.ok(contaPagar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vencidas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaPagar> getContasVencidas() {
        return contaPagarService.findContasVencidas();
    }

    @GetMapping("/a-vencer")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaPagar> getContasAVencer(@RequestParam(defaultValue = "7") Integer dias) {
        return contaPagarService.findContasAVencer(dias);
    }
}
