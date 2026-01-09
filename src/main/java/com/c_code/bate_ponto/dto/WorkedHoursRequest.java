package com.c_code.bate_ponto.dto;

import lombok.Data;

@Data
public class WorkedHoursRequest {

    private Long userId;
    private String date; // formato: yyyy-MM-dd
    
}
