package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
