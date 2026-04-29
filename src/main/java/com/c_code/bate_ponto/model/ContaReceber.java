package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "contas_receber")
@Data
@NoArgsConstructor
public class ContaReceber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clienteId", nullable = false)
    private Long clienteId;

    @Column(name = "clienteNome")
    private String clienteNome;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "formaPagamento")
    private String formaPagamento;

    private String descricao;

    @Column(name = "dataPagamento")
    private String dataPagamento;

    @Column(name = "pedidoId")
    private Long pedidoId;

    @Column(name = "tipoPagamento")
    private String tipoPagamento;

    @Column(nullable = false)
    private String status = "PENDENTE";

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "clienteId", insertable = false, updatable = false)
    private Client cliente;

    @ManyToOne
    @JoinColumn(name = "pedidoId", insertable = false, updatable = false)
    private Order pedido;

    public ContaReceber(Long clienteId, String clienteNome, Double valor, String formaPagamento, 
                        String descricao, String dataPagamento, Long pedidoId, String tipoPagamento) {
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.descricao = descricao;
        this.dataPagamento = dataPagamento;
        this.pedidoId = pedidoId;
        this.tipoPagamento = tipoPagamento;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
