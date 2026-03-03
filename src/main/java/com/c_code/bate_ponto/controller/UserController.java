package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c_code.bate_ponto.dto.request.ChangePasswordRequest;
import com.c_code.bate_ponto.dto.request.UserRequest;
import com.c_code.bate_ponto.dto.response.UserResponse;
import com.c_code.bate_ponto.model.User;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;
import com.c_code.bate_ponto.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public User register(@RequestBody UserRequest request) {
        return userService.register(
                request.name,
                request.email,
                request.type,
                request.password,
                request.role);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal UserDetailsImpl user) {
        return new UserResponse(
                user.getId(),
                user.getUser().getName(),
                user.getUser().getEmail(),
                user.getUser().getType().name(),
                null);
    }

    @GetMapping("/all")
    public List<UserResponse> all(@AuthenticationPrincipal UserDetailsImpl user) {
        return userService.getAllUsers()
            .stream()
            .map(u -> new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getType().name(),
                null
            ))
            .toList();
    }

    @PostMapping("/change-password")
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
                              @RequestBody ChangePasswordRequest request) {
        
        // Se targetUserId for nulo, altera a própria senha
        if (request.getTargetUserId() == null) {
            userService.changePasswordWithValidation(
                userDetailsImpl.getId(), 
                request.getCurrentPassword(), 
                request.getNewPassword()
            );
        } else {
            userService.changePassword(request.getTargetUserId(), request.getNewPassword());
        }
    }
}
