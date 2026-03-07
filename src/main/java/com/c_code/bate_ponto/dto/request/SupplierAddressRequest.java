package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class SupplierAddressRequest {
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String tipo;
}
