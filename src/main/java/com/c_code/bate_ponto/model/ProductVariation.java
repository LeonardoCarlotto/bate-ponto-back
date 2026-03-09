package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Table(name = "product_variations")
@Data
@Getter
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

    // Getters e Setters manuais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public java.util.List<String> getValues() { return values; }
    public void setValues(java.util.List<String> values) { this.values = values; }

    public ProductVariation(Product product, String name, java.util.List<String> values) {
        this.product = product;
        this.name = name;
        this.values = values;
    }
}
