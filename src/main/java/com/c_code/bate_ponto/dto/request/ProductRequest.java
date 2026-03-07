package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String nome;
    private String descricao;
    private Double preco;
    private Integer estoque;
    private String categoria;
}
