package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private Double preco;
    private String descricao;
    private Integer estoque;
}
