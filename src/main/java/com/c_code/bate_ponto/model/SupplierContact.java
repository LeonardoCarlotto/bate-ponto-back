package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_contacts")
@Data
@NoArgsConstructor
public class SupplierContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    private String name;

    private String position;

    private String email;

    private String phone;

    public SupplierContact(Supplier supplier, String name, String position, String email, String phone) {
        this.supplier = supplier;
        this.name = name;
        this.position = position;
        this.email = email;
        this.phone = phone;
    }
}
