package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String urlPhoto;
    private Boolean active;
}
