package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @Query("SELECT c FROM ContaPagar c WHERE c.dataVencimento < :currentDate AND c.status != 'PAGO'")
    List<ContaPagar> findContasVencidas(@Param("currentDate") String currentDate);

    @Query("SELECT c FROM ContaPagar c WHERE c.dataVencimento BETWEEN :currentDate AND :futureDate AND c.status != 'PAGO'")
    List<ContaPagar> findContasAVencer(@Param("currentDate") String currentDate, @Param("futureDate") String futureDate);

    List<ContaPagar> findByFornecedorId(Long fornecedorId);
    
    List<ContaPagar> findByStatus(String status);
}
