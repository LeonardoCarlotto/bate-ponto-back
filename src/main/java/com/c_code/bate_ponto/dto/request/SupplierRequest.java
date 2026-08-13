package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class SupplierRequest {
    private String nome;
    private String name;
    private String cnpj;
    private String email;
    private String telefone;
    private String phone;
    private String inscricaoEstadual;
    private String stateRegistration;
    private String contato;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private Boolean ativo;

    public String getNome() {
        return firstNotBlank(nome, name);
    }

    public String getTelefone() {
        return firstNotBlank(telefone, phone);
    }

    public String getInscricaoEstadual() {
        return firstNotBlank(inscricaoEstadual, stateRegistration);
    }

    private String firstNotBlank(String primary, String fallback) {
        if (primary != null && !primary.trim().isEmpty()) {
            return primary;
        }
        return fallback;
    }
}
