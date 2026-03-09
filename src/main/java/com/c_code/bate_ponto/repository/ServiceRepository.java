package com.c_code.bate_ponto.repository;

import com.c_code.bate_ponto.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceItem, Long> {

    Optional<ServiceItem> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT s FROM ServiceItem s WHERE s.active = true")
    List<ServiceItem> findActiveServices();

    @Query("SELECT s FROM ServiceItem s WHERE s.active = false")
    List<ServiceItem> findInactiveServices();
}
