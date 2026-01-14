package com.c_code.bate_ponto.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkedHoursResponse {

    private String date;
    private String totalWorked; // HH:mm

    public WorkedHoursResponse(String date, String totalWorked) {
        this.date = date;
        this.totalWorked = totalWorked;
    }

}
