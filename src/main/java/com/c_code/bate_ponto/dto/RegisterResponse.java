package com.c_code.bate_ponto.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.Data;

@Data
public class RegisterResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private String date;
    private String time;
    private String dayOfWeek;
    private String type;

    public RegisterResponse(Long id,
                            String userName,
                            String userEmail,
                            LocalDateTime dataTime,
                            String type) {

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm");

        DateTimeFormatter dayFormatter =
                DateTimeFormatter.ofPattern("EEEE", new Locale("pt", "BR"));

        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.date = dataTime.format(dateFormatter);
        this.time = dataTime.format(timeFormatter);
        this.dayOfWeek = capitalize(dataTime.format(dayFormatter));
        this.type = type;
    }

    private String capitalize(String value) {
        return value.substring(0,1).toUpperCase() + value.substring(1);
    }
}
