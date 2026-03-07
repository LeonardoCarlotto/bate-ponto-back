package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class ProductImageResponse {
    private Long id;
    private String url;
    private Boolean principal;
    private Integer ordem;

    public ProductImageResponse(Long id, String url, Boolean principal, Integer ordem) {
        this.id = id;
        this.url = url;
        this.principal = principal;
        this.ordem = ordem;
    }
}
