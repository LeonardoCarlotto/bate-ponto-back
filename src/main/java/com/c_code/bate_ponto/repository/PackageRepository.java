package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    
    Optional<Package> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT p FROM Package p WHERE " +
           "(:nome IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:ativo IS NULL OR p.active = :ativo)")
    List<Package> findByFilters(@Param("nome") String nome, @Param("ativo") Boolean ativo);
    
    @Query("SELECT p FROM Package p WHERE p.active = true")
    List<Package> findActivePackages();
}
