package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.c_code.bate_ponto.dto.*;
import com.c_code.bate_ponto.service.RegisterService;

@RestController
@RequestMapping("/registers")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return registerService.registerPoint(request);
    }

    @GetMapping("/user/{userId}")
    public List<RegisterResponse> findByUser(@PathVariable Long userId) {
        return registerService.findByUser(userId);
    }

    @PostMapping("/worked-hours")
    public WorkedHoursResponse workedHours(@RequestBody WorkedHoursRequest request) {
        return registerService.calculateWorkedHours(request);
    }

}
