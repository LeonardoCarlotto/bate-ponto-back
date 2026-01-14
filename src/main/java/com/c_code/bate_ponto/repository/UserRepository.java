package com.c_code.bate_ponto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.c_code.bate_ponto.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
