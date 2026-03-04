package com.c_code.bate_ponto.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterRequest {
    private LocalDateTime dataTime;
}
