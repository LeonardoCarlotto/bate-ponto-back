package com.c_code.bate_ponto.service.user;

import com.c_code.bate_ponto.dto.request.UserRequest;
import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

    public void changePassword(UserRequest request) {
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changePasswordWithValidation(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
