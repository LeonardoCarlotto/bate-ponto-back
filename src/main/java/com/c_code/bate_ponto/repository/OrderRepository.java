package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.Order;
import com.c_code.bate_ponto.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:dataInicio IS NULL OR o.date >= :dataInicio) AND " +
           "(:dataFim IS NULL OR o.date <= :dataFim)")
    List<Order> findByFilters(@Param("status") OrderStatus status,
                              @Param("dataInicio") LocalDateTime dataInicio,
                              @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT o FROM Order o WHERE " +
           "(:clienteId IS NULL OR o.client.id = :clienteId) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:dataInicio IS NULL OR o.date >= :dataInicio) AND " +
           "(:dataFim IS NULL OR o.date <= :dataFim)")
    List<Order> findByFiltersWithClient(@Param("clienteId") Long clienteId,
                                        @Param("status") OrderStatus status,
                                        @Param("dataInicio") LocalDateTime dataInicio,
                                        @Param("dataFim") LocalDateTime dataFim);
}
