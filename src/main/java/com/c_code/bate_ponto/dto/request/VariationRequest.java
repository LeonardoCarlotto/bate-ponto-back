package com.c_code.bate_ponto.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class VariationRequest {
    private String nome;
    private List<String> valores;
}
