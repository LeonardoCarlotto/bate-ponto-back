package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class ContactResponse {
    private Long id;
    private String tipo;
    private String valor;
    private Boolean principal;

    public ContactResponse(Long id, String tipo, String valor, Boolean principal) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.principal = principal;
    }
}
