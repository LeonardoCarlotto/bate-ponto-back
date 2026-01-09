package com.c_code.bate_ponto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.c_code.bate_ponto.model.Register;

import java.time.LocalDateTime;
import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Long> {

    List<Register> findByUserIdOrderByDataTimeDesc(Long userId);

    List<Register> findByUserIdAndDataTimeBetweenOrderByDataTimeAsc(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );
}
