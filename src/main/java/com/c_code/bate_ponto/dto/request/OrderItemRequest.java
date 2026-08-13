package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long id;
    private Long itemId;
    private Long produtoId;
    private Long pacoteId;
    private String tipo;
    private String nome;
    private Integer quantidade;
    private Double preco;
    private Double precoUnitario;
    private Double subtotal;

    public Long getReferenciaId() {
        if (itemId != null) {
            return itemId;
        }
        if ("pacote".equalsIgnoreCase(tipo) && pacoteId != null) {
            return pacoteId;
        }
        if (produtoId != null) {
            return produtoId;
        }
        return id;
    }

    public Double getPrecoFinal() {
        return preco != null ? preco : precoUnitario;
    }
}
