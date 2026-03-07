package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findByCnpj(String cnpj);
    
    boolean existsByCnpj(String cnpj);
    
    @Query("SELECT s FROM Supplier s WHERE " +
           "(:nome IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :nome, '%')))")
    List<Supplier> findByFilters(@Param("nome") String nome);
}
