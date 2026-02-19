package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c_code.bate_ponto.dto.request.UserRequest;
import com.c_code.bate_ponto.dto.response.UserResponse;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;
import com.c_code.bate_ponto.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService user;

    public UserController(UserService user) {
        this.user = user;
    }

    @PostMapping
    public User register(@RequestBody UserRequest request) {
        System.out.println(request);
        return user.register(
                request.name,
                request.email,
                request.type,
                request.password,
                request.role);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal UserDetailsImpl user) {
        return new UserResponse(
                user.getUser().getName(),
                user.getUser().getEmail(),
                user.getUser().getType().name());
    }
}
