package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {
    
    List<ContaReceber> findByClienteId(Long clienteId);
    
    List<ContaReceber> findByPedidoId(Long pedidoId);
    
    List<ContaReceber> findByStatus(String status);
}
