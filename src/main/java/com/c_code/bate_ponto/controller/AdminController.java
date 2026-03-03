package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.c_code.bate_ponto.dto.request.UserRequest;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;
import com.c_code.bate_ponto.service.user.UserService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody UserRequest request, 
                          @AuthenticationPrincipal UserDetailsImpl admin) {
        // Apenas ADMIN pode criar usuários
        return userService.register(
                request.name,
                request.email,
                request.type,
                request.password,
                request.role);
    }
}
