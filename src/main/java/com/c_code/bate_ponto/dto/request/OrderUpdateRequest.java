package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class OrderUpdateRequest {
    private String description;
    private String status;
}
