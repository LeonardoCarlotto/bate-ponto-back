package com.c_code.bate_ponto.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkedHoursRequest {

    private Long userId;
    private String date; // formato: yyyy-MM-dd

}
