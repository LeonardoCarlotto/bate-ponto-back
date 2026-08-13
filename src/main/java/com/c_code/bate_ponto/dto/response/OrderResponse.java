package com.c_code.bate_ponto.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private LocalDateTime data;
    private String status;
    private Double valor;
    private String descricao;
    private String formaPagamento;
    private Integer parcelas;
    private List<OrderItemResponse> itens;

    public OrderResponse(Long id, Long clienteId, String clienteNome, LocalDateTime data, 
                        String status, Double valor, String descricao, List<OrderItemResponse> itens) {
        this(id, clienteId, clienteNome, data, status, valor, descricao, null, null, itens);
    }

    public OrderResponse(Long id, Long clienteId, String clienteNome, LocalDateTime data,
                        String status, Double valor, String descricao, String formaPagamento,
                        Integer parcelas, List<OrderItemResponse> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.data = data;
        this.status = status;
        this.valor = valor;
        this.descricao = descricao;
        this.formaPagamento = formaPagamento;
        this.parcelas = parcelas;
        this.itens = itens;
    }
}
