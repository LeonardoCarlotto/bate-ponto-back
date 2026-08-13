package com.c_code.bate_ponto.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContaReceberResponse {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Double valor;
    private String formaPagamento;
    private String descricao;
    private String data;
    private Long pedidoId;
    private String tipoPagamento;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContaReceberResponse(Long id, Long clienteId, String clienteNome, Double valor,
                                String formaPagamento, String descricao, String data,
                                Long pedidoId, String tipoPagamento, String status,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.descricao = descricao;
        this.data = data;
        this.pedidoId = pedidoId;
        this.tipoPagamento = tipoPagamento;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
