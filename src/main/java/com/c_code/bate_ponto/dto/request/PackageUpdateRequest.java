package com.c_code.bate_ponto.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PackageUpdateRequest {
    private String nome;
    private String descricao;
    private Double preco;
    private Integer duracaoDias;
    private Boolean ativo;
    private List<Long> produtosIds;
}
