package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ContaReceberPagamentoRequest {
    private Long clienteId;
    private String clienteNome;
    private Double valor;
    private String formaPagamento;
    private String descricao;
    private String data;
    private Long pedidoId;
    private String tipoPagamento;
}
