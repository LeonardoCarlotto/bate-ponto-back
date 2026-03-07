package com.c_code.bate_ponto.dto.response;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Boolean principal;

    public AddressResponse(Long id, String rua, String numero, String complemento, String bairro,
                         String cidade, String estado, String cep, Boolean principal) {
        this.id = id;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.principal = principal;
    }
}
