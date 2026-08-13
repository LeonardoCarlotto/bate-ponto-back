package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Long pacoteId;
    private String pacoteNome;
    private Long itemId;
    private String tipo;
    private String nome;
    private Integer quantidade;
    private Double precoUnitario;
    private Double preco;
    private Double subtotal;

    public OrderItemResponse(Long id, Long produtoId, String produtoNome, Integer quantidade, 
                           Double precoUnitario, Double subtotal) {
        this(id, produtoId, produtoNome, null, null, "produto", produtoNome, quantidade, precoUnitario, subtotal);
    }

    public OrderItemResponse(Long id, Long produtoId, String produtoNome, Long pacoteId, String pacoteNome,
                           String tipo, String nome, Integer quantidade, Double precoUnitario, Double subtotal) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.pacoteId = pacoteId;
        this.pacoteNome = pacoteNome;
        this.itemId = "pacote".equalsIgnoreCase(tipo) ? pacoteId : produtoId;
        this.tipo = tipo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.preco = precoUnitario;
        this.subtotal = subtotal;
    }
}
