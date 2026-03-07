package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ClientRequest {
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private String dataAbertura;
}
