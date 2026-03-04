package com.c_code.bate_ponto.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEditRequest {

    private Long registerId;
    private String observation;
    private String newRegistro;
    private String type;  // NOVO: permite mudar tipo (ENTRADA/SAIDA)

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

}
