package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String nome;
    private String descricao;

    public CategoryResponse(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
}
