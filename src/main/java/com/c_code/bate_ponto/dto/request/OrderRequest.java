package com.c_code.bate_ponto.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long clientId;
    private String description;
    private List<OrderItemRequest> items;
}
