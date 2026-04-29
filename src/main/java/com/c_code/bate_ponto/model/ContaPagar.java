package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "contas_pagar")
@Data
@NoArgsConstructor
public class ContaPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fornecedorId", nullable = false)
    private Long fornecedorId;

    @Column(name = "fornecedorNome")
    private String fornecedorNome;

    @Column(name = "dataVencimento", nullable = false)
    private String dataVencimento;

    @Column(name = "dataPagamento")
    private String dataPagamento;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "formaPagamento")
    private String formaPagamento;

    private Integer parcelas;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // Removido relacionamento para evitar ciclos de serialização
    // @ManyToOne
    // @JoinColumn(name = "fornecedorId", insertable = false, updatable = false)
    // private Supplier fornecedor;

    public ContaPagar(Long fornecedorId, String fornecedorNome, String dataVencimento, String status, 
                     String descricao, Double valor, String formaPagamento, Integer parcelas) {
        this.fornecedorId = fornecedorId;
        this.fornecedorNome = fornecedorNome;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.descricao = descricao;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.parcelas = parcelas;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
