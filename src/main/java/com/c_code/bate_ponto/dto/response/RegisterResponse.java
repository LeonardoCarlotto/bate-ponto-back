package com.c_code.bate_ponto.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.c_code.bate_ponto.model.Register;
import com.c_code.bate_ponto.model.RegisterType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private String date;
    private String time;
    private String dayOfWeek;
    private RegisterType type;
    private boolean edited;
    private String observation;

    public RegisterResponse(Long id,
            String userName,
            String userEmail,
            LocalDateTime dataTime,
            RegisterType type,
            boolean edited,
            String observation) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", new Locale("pt", "BR"));

        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.date = dataTime.format(dateFormatter);
        this.time = dataTime.format(timeFormatter);
        this.dayOfWeek = capitalize(dataTime.format(dayFormatter));
        this.type = type;
        this.edited = edited;
        this.observation = observation;
    }

    public RegisterResponse(Register register) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", new Locale("pt", "BR"));

        this.id = register.getId();
        this.userName = register.getUser().getName();
        this.userEmail = register.getUser().getEmail();
        this.date = register.getDataTime().format(dateFormatter);
        this.time = register.getDataTime().format(timeFormatter);
        this.dayOfWeek = capitalize(register.getDataTime().format(dayFormatter));
        this.type = register.getType();
        this.edited = register.isEdited();
        this.observation = register.getObservation();
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}
