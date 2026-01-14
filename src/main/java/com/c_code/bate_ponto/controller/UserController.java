package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c_code.bate_ponto.dto.request.UserRequest;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService user;

    public UserController(UserService user) {
        this.user = user;
    }

    @PostMapping
    public User register(@RequestBody UserRequest request) {
        return user.register(
                request.name,
                request.email,
                request.type);
    }
}
