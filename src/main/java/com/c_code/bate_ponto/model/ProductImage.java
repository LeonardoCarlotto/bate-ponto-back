package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean principal = false;

    @Column(name = "image_order", nullable = false)
    private Integer order = 0;

    public ProductImage(Product product, String url, Boolean principal, Integer order) {
        this.product = product;
        this.url = url;
        this.principal = principal;
        this.order = order;
    }
}
