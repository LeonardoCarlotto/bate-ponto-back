package com.c_code.bate_ponto.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class ContaReceberClienteResponse {
    private Long clienteId;
    private String clienteNome;
    private List<OrderResponse> pedidos;
    private List<ContaReceberResponse> pagamentos;
    private Double totalEmAberto;
    private Double totalPago;
    private Double saldoDevedor;

    public ContaReceberClienteResponse(Long clienteId, String clienteNome,
                                       List<OrderResponse> pedidos,
                                       List<ContaReceberResponse> pagamentos,
                                       Double totalEmAberto, Double totalPago,
                                       Double saldoDevedor) {
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.pedidos = pedidos;
        this.pagamentos = pagamentos;
        this.totalEmAberto = totalEmAberto;
        this.totalPago = totalPago;
        this.saldoDevedor = saldoDevedor;
    }
}
