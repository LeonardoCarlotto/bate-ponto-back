package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_addresses")
@Data
@NoArgsConstructor
public class SupplierAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    private String street;

    private String number;

    private String neighborhood;

    private String city;

    private String state;

    private String zipCode;

    private String type;

    public SupplierAddress(Supplier supplier, String street, String number, String neighborhood, 
                          String city, String state, String zipCode, String type) {
        this.supplier = supplier;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.type = type;
    }
}
