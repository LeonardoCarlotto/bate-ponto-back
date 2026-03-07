package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Table(name = "product_variations")
@Data
@NoArgsConstructor
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "variation_values", joinColumns = @JoinColumn(name = "variation_id"))
    @Column(name = "value")
    private java.util.List<String> values;

    public ProductVariation(Product product, String name, java.util.List<String> values) {
        this.product = product;
        this.name = name;
        this.values = values;
    }
}
