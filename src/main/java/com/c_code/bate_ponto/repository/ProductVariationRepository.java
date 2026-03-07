package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
    
    List<ProductVariation> findByProductId(Long productId);
}
