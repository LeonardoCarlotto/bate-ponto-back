package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class PagamentoRequest {
    private Double valor;
    private String formaPagamento;
    private String descricao;
    private String data;
}
