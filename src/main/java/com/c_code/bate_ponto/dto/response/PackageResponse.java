package com.c_code.bate_ponto.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PackageResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer duracaoDias;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private List<ProductBasicResponse> produtos;

    public PackageResponse(Long id, String nome, String descricao, Double preco, Integer duracaoDias, 
                          Boolean ativo, LocalDateTime dataCadastro, List<ProductBasicResponse> produtos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoDias = duracaoDias;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.produtos = produtos;
    }

    @Data
    public static class ProductBasicResponse {
        private Long id;
        private String nome;
        private Double preco;

        public ProductBasicResponse(Long id, String nome, Double preco) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
        }
    }
}
