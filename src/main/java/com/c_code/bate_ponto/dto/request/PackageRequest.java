package com.c_code.bate_ponto.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PackageRequest {
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean ativo;
    private List<PackageProductRequest> produtos;
    
    @Data
    public static class PackageProductRequest {
        private Long produtoId;
        private Integer quantidade;
    }
}
