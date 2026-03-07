package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Boolean principal;
}
