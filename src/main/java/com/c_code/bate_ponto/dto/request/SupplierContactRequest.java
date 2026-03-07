package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class SupplierContactRequest {
    private String nome;
    private String cargo;
    private String email;
    private String telefone;
}
