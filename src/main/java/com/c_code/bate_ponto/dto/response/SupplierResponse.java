package com.c_code.bate_ponto.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierResponse {
    private Long id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String inscricaoEstadual;
    private Boolean ativo;
    private LocalDateTime dataCadastro;

    public SupplierResponse(Long id, String nome, String cnpj, String email, String telefone, 
                          String inscricaoEstadual, Boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.inscricaoEstadual = inscricaoEstadual;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }
}
