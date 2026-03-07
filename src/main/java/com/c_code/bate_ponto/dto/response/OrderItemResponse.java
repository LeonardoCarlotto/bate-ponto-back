package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Double precoUnitario;
    private Double subtotal;

    public OrderItemResponse(Long id, Long produtoId, String produtoNome, Integer quantidade, 
                           Double precoUnitario, Double subtotal) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }
}
