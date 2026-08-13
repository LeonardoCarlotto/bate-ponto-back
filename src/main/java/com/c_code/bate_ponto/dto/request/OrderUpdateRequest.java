package com.c_code.bate_ponto.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderUpdateRequest {
    private Long clienteId;
    private String dataPedido;
    private String descricao;
    private String observacoes;
    private String description;
    private String status;
    private String formaPagamento;
    private Integer parcelas;
    private List<OrderItemRequest> itens;
    private Double total;

    public String getDescricaoFinal() {
        if (descricao != null && !descricao.trim().isEmpty()) {
            return descricao;
        }
        if (observacoes != null && !observacoes.trim().isEmpty()) {
            return observacoes;
        }
        return description;
    }
}
