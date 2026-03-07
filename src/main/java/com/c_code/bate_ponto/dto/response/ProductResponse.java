package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer estoque;
    private String categoria;
    private Boolean ativo;

    public ProductResponse(Long id, String nome, String descricao, Double preco, Integer estoque, String categoria, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.ativo = ativo;
    }
}
