package com.c_code.bate_ponto.dto.request;

import java.util.List;

public class PackageRequest {
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean ativo;
    private Double precoPersonalizado;
    private List<PackageItemRequest> itens;
    
    // Getters e Setters manuais
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public Double getPrecoPersonalizado() { return precoPersonalizado; }
    public void setPrecoPersonalizado(Double precoPersonalizado) { this.precoPersonalizado = precoPersonalizado; }
    
    public List<PackageItemRequest> getItens() { return itens; }
    public void setItens(List<PackageItemRequest> itens) { this.itens = itens; }
    
    public static class PackageItemRequest {
        private String id;
        private String tipo; // "produto" ou "servico"
        private Long itemId; // ID do produto ou null para serviços
        private String nome;
        private Double preco;
        private Integer quantidade;
        private Double subtotal;
        
        // Getters e Setters manuais
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getDescricao() { 
            // Para compatibilidade com o código que espera getDescricao()
            return null; // Serviços não têm descrição individual no item
        }
        
        public Double getPreco() { return preco; }
        public void setPreco(Double preco) { this.preco = preco; }
        
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
        
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }
}
