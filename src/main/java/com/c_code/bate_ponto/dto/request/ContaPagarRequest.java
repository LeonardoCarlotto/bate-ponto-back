package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ContaPagarRequest {
    private Long fornecedorId;
    private String fornecedorNome;
    private String dataVencimento;
    private String descricao;
    private Double valor;
    private String formaPagamento;
    private Integer parcelas;
}
