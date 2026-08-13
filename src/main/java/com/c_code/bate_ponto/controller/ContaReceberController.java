package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.ContaReceberPagamentoRequest;
import com.c_code.bate_ponto.dto.response.ContaReceberClienteResponse;
import com.c_code.bate_ponto.dto.response.ContaReceberResponse;
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
    public List<ContaReceberResponse> getAllContasReceber() {
        return contaReceberService.findAll();
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaReceberClienteResponse> getResumoPorCliente() {
        return contaReceberService.getResumoPorCliente();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContaReceberResponse> getContaReceberById(@PathVariable Long id) {
        return contaReceberService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/pagamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ContaReceberResponse registrarPagamentoIndividual(@RequestBody ContaReceberPagamentoRequest request) {
        return contaReceberService.registrarPagamentoIndividual(request);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ContaReceberClienteResponse getContasReceberByClienteId(@PathVariable Long clienteId) {
        return contaReceberService.getResumoCliente(clienteId);
    }

    @GetMapping("/cliente/{clienteId}/pagamentos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaReceberResponse> getPagamentosByClienteId(@PathVariable Long clienteId) {
        return contaReceberService.findByClienteId(clienteId);
    }

    @GetMapping("/pedido/{pedidoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContaReceberResponse> getContasReceberByPedidoId(@PathVariable Long pedidoId) {
        return contaReceberService.findByPedidoId(pedidoId);
    }

    @PostMapping("/pagamento/{pagamentoId}/estornar")
    @PreAuthorize("hasRole('ADMIN')")
    public ContaReceberResponse estornarPagamento(@PathVariable Long pagamentoId) {
        return contaReceberService.estornarPagamento(pagamentoId);
    }
}
