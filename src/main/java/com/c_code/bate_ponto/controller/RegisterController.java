package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.c_code.bate_ponto.dto.request.RegisterEditRequest;
import com.c_code.bate_ponto.dto.request.WorkedHoursRequest;
import com.c_code.bate_ponto.dto.response.RegisterResponse;
import com.c_code.bate_ponto.dto.response.WorkedHoursResponse;
import com.c_code.bate_ponto.service.register.RegisterService;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/registers")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public RegisterResponse register(@AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.registerPoint(user);
    }

    @GetMapping("/user")
    public List<RegisterResponse> findByUser(@AuthenticationPrincipal UserDetailsImpl user) {
        return registerService.findByUser(user.getId());
    }

    @PostMapping("/worked-hours")
    public WorkedHoursResponse workedHours(@RequestBody WorkedHoursRequest request,
            @AuthenticationPrincipal UserDetailsImpl user) {
        String a = "s";
        return registerService.calculateWorkedHours(request, user.getId());
    }

    @PutMapping("/{id}/edit")
    public RegisterResponse editRegister(@RequestBody RegisterEditRequest request,
            @AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long id) throws Exception {
        request.setRegisterId(id);
        return registerService.editRegister(request, user.getId());
    }

    @GetMapping("/edited/all")
    public List<RegisterResponse> findAllRegisterEdited() {
        return registerService.findAllRegisterEdited();
    }

}
