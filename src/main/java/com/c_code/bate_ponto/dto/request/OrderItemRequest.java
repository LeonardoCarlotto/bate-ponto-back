package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long id;
    private String tipo;
    private String nome;
    private Integer quantidade;
    private Double preco;
    private Double subtotal;
}
