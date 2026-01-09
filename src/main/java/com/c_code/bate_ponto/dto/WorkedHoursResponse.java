package com.c_code.bate_ponto.dto;

import lombok.Data;

@Data
public class WorkedHoursResponse {

    private String date;
    private String totalWorked; // HH:mm

    public WorkedHoursResponse(String date, String totalWorked) {
        this.date = date;
        this.totalWorked = totalWorked;
    }

}
