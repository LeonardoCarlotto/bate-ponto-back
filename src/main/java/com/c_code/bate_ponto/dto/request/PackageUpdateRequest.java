package com.c_code.bate_ponto.dto.request;

import java.util.List;

public class PackageUpdateRequest {
    private String nome;
    private String descricao;
    private Double preco;
    private Integer duracaoDias;
    private Boolean ativo;
    private Double precoPersonalizado;
    private List<PackageRequest.PackageItemRequest> itens;
    
    // Getters e Setters manuais
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
    
    public Double getPrecoPersonalizado() { return precoPersonalizado; }
    public void setPrecoPersonalizado(Double precoPersonalizado) { this.precoPersonalizado = precoPersonalizado; }
    
    public List<PackageRequest.PackageItemRequest> getItens() { return itens; }
    public void setItens(List<PackageRequest.PackageItemRequest> itens) { this.itens = itens; }
}
