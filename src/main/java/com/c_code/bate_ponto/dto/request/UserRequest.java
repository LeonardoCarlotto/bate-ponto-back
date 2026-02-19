package com.c_code.bate_ponto.dto.request;

import com.c_code.bate_ponto.model.Role;
import com.c_code.bate_ponto.model.UserType;

import lombok.Data;

@Data
public class UserRequest {
    public String name;
    public String email;
    public String password;
    public UserType type;
    public Role role;
    public boolean active;
}
