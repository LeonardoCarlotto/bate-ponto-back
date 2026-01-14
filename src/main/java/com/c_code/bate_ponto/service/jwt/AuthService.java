package com.c_code.bate_ponto.service.jwt;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.c_code.bate_ponto.dto.request.LoginRequest;
import com.c_code.bate_ponto.dto.response.LoginResponse;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("user invalid"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("credential invalid");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}
