package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByCpfCnpj(String cpfCnpj);
    
    Optional<Client> findByEmail(String email);
    
    boolean existsByCpfCnpj(String cpfCnpj);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT c FROM Client c WHERE " +
           "(:nome IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:ativo IS NULL OR c.active = :ativo)")
    List<Client> findByFilters(@Param("nome") String nome, @Param("ativo") Boolean ativo);
}
