package com.c_code.bate_ponto.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEditRequest {

    private Long registerId;
    private String observation;
    private String newRegistro;

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

}
