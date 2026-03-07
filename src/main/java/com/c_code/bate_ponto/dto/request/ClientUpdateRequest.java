package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ClientUpdateRequest {
    private String nome;
    private String email;
    private Boolean ativo;
}
