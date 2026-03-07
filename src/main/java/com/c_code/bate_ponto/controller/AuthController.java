package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.c_code.bate_ponto.dto.request.LoginRequest;
import com.c_code.bate_ponto.dto.response.LoginResponse;
import com.c_code.bate_ponto.service.jwt.AuthService;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@AuthenticationPrincipal UserDetailsImpl user) {
        Map<String, Object> response = new HashMap<>();
        response.put("valido", true);
        return ResponseEntity.ok(response);
    }
}
