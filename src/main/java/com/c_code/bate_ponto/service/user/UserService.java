package com.c_code.bate_ponto.service.user;

import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String name, String email, UserType type, String password, Role role) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setType(type);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);
    }
}
