package com.c_code.bate_ponto.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClientResponse {
    private Long id;
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private String dataAbertura;
    private Boolean ativo;
    private LocalDateTime dataCadastro;

    public ClientResponse(Long id, String nome, String cpfCnpj, String email, String telefone, 
                        String dataAbertura, Boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
        this.dataAbertura = dataAbertura;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }
}
