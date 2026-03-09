package com.c_code.bate_ponto.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PackageResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer duracaoDias;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private List<ItemResponse> itens;

    public PackageResponse(Long id, String nome, String descricao, Double preco, Integer duracaoDias, 
                          Boolean ativo, LocalDateTime dataCadastro, List<ItemResponse> itens) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoDias = duracaoDias;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.itens = itens;
    }

    // Getters e Setters manuais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    
    public Integer getDuracaoDias() { return duracaoDias; }
    public void setDuracaoDias(Integer duracaoDias) { this.duracaoDias = duracaoDias; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public List<ItemResponse> getItens() { return itens; }
    public void setItens(List<ItemResponse> itens) { this.itens = itens; }

    public static class ItemResponse {
        private String id;
        private String tipo; // "produto" ou "servico"
        private Long itemId;
        private String nome;
        private Double preco;
        private Integer quantidade;
        private Double subtotal;

        public ItemResponse(String id, String tipo, Long itemId, String nome, Double preco, Integer quantidade, Double subtotal) {
            this.id = id;
            this.tipo = tipo;
            this.itemId = itemId;
            this.nome = nome;
            this.preco = preco;
            this.quantidade = quantidade;
            this.subtotal = subtotal;
        }
        
        // Getters e Setters manuais
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public Double getPreco() { return preco; }
        public void setPreco(Double preco) { this.preco = preco; }
        
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
        
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }

    @lombok.Data
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
