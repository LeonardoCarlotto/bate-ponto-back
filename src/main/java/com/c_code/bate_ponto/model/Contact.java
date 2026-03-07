package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactType tipo;

    @Column(nullable = false)
    private String valor;

    @Column(nullable = false)
    private Boolean principal = false;

    public Contact(Client client, ContactType tipo, String valor, Boolean principal) {
        this.client = client;
        this.tipo = tipo;
        this.valor = valor;
        this.principal = principal;
    }
}
