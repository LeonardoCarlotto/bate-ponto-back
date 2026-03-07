package com.c_code.bate_ponto.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ProductVariationResponse {
    private Long id;
    private String nome;
    private List<String> valores;

    public ProductVariationResponse(Long id, String nome, List<String> valores) {
        this.id = id;
        this.nome = nome;
        this.valores = valores;
    }
}
