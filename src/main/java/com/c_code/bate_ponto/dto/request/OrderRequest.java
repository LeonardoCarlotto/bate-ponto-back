package com.c_code.bate_ponto.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long clienteId;
    private String dataPedido;
    private String status;
    private String observacoes;
    private List<OrderItemRequest> itens;
    private Double total;
}
