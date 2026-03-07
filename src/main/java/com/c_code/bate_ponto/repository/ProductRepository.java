package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.Product;
import com.c_code.bate_ponto.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategoryName(String categoryName);
    
    List<Product> findByCategoryNameAndStockGreaterThan(String categoryName, Integer stock);
    
    List<Product> findByStockGreaterThan(Integer stock);
    
    List<Product> findByActiveTrue();
    
    List<Product> findByActiveTrueAndStockGreaterThan(Integer stock);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoria IS NULL OR p.category.name = :categoria) AND " +
           "(:estoque IS NULL OR p.stock > 0)")
    List<Product> findByFilters(@Param("categoria") String categoria, @Param("estoque") Boolean estoque);
    
    Optional<Product> findByName(String name);
}
