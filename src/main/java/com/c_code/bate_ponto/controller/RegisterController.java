package com.c_code.bate_ponto.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.c_code.bate_ponto.dto.request.RegisterEditRequest;
import com.c_code.bate_ponto.dto.request.RegisterRequest;
import com.c_code.bate_ponto.dto.request.WorkedHoursRequest;
import com.c_code.bate_ponto.dto.response.RegisterResponse;
import com.c_code.bate_ponto.dto.response.WorkedHoursResponse;
import com.c_code.bate_ponto.service.register.RegisterService;

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

    @PutMapping("/{id}/edit")
    public RegisterResponse editRegister(@RequestBody RegisterEditRequest request,
            @RequestParam Long editorUserId, @PathVariable Long id) throws Exception {
        request.setRegisterId(id);
        return registerService.editRegister(request, editorUserId);
    }

    @GetMapping("/edited/all")
    public List<RegisterResponse> findAllRegisterEdited() {
        return registerService.findAllRegisterEdited();
    }

}
