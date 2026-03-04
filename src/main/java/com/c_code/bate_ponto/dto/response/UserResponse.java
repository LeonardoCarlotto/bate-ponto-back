package com.c_code.bate_ponto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponse {
    Long id;
    String name;
    String email;
    String type;
    String urlPhoto;
    Boolean active;  // NOVO: status ativo/inativo
}
