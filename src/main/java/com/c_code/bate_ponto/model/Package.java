package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "package_products",
        joinColumns = @JoinColumn(name = "package_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    public Package(String name, String description, Double price, Integer durationDays, List<Product> products) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
        this.products = products != null ? products : new java.util.ArrayList<>();
        this.dataCadastro = LocalDateTime.now();
    }
}
