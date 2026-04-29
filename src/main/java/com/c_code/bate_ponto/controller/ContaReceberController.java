package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.ContaReceberPagamentoRequest;
import com.c_code.bate_ponto.model.ContaReceber;
import com.c_code.bate_ponto.service.ContaReceberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas-receber")
public class ContaReceberController {

    private final ContaReceberService contaReceberService;

    public ContaReceberController(ContaReceberService contaReceberService) {
        this.contaReceberService = contaReceberService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaReceber> getAllContasReceber() {
        return contaReceberService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContaReceber> getContaReceberById(@PathVariable Long id) {
        return contaReceberService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pagamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ContaReceber registrarPagamentoIndividual(@RequestBody ContaReceberPagamentoRequest request) {
        return contaReceberService.registrarPagamentoIndividual(request);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaReceber> getContasReceberByClienteId(@PathVariable Long clienteId) {
        return contaReceberService.findByClienteId(clienteId);
    }

    @GetMapping("/pedido/{pedidoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaReceber> getContasReceberByPedidoId(@PathVariable Long pedidoId) {
        return contaReceberService.findByPedidoId(pedidoId);
    }
}
