package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class SupplierRequest {
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String inscricaoEstadual;
}
