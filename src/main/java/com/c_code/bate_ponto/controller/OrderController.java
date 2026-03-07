package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.*;
import com.c_code.bate_ponto.dto.response.*;
import com.c_code.bate_ponto.service.OrderService;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/pedidos")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> listOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) Long usuarioId,
            @AuthenticationPrincipal UserDetailsImpl user) {
        
        // Se não for admin, só pode ver seus próprios pedidos
        if (!user.getUser().getRole().name().equals("ADMIN")) {
            usuarioId = user.getId();
        }
        
        if (usuarioId != null) {
            return orderService.getOrdersByFilters(usuarioId, status, dataInicio, dataFim);
        } else {
            return orderService.getAllOrders(status, dataInicio, dataFim);
        }
    }

    @GetMapping("/{pedidoId}")
    public OrderResponse getOrder(@PathVariable Long pedidoId,
                                  @AuthenticationPrincipal UserDetailsImpl user) {
        return orderService.getOrderById(pedidoId);
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request,
                                     @AuthenticationPrincipal UserDetailsImpl user) {
        return orderService.createOrder(request);
    }

    @PutMapping("/{pedidoId}")
    public OrderResponse updateOrder(@PathVariable Long pedidoId,
                                     @RequestBody OrderUpdateRequest request,
                                     @AuthenticationPrincipal UserDetailsImpl user) {
        return orderService.updateOrder(pedidoId, request);
    }

    @DeleteMapping("/{pedidoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long pedidoId) {
        Map<String, String> response = orderService.deleteOrder(pedidoId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{pedidoId}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long pedidoId,
                                          @RequestBody OrderStatusRequest request,
                                          @AuthenticationPrincipal UserDetailsImpl user) {
        return orderService.updateOrderStatus(pedidoId, request);
    }

    @GetMapping("/{pedidoId}/itens")
    public List<OrderItemResponse> getOrderItems(@PathVariable Long pedidoId,
                                                  @AuthenticationPrincipal UserDetailsImpl user) {
        return orderService.getOrderItems(pedidoId);
    }

    @PostMapping("/{pedidoId}/itens")
    public OrderItemResponse addOrderItem(@PathVariable Long pedidoId,
                                          @RequestBody OrderItemRequest request,
                                          @AuthenticationPrincipal UserDetailsImpl user) {
        return orderService.addOrderItem(pedidoId, request);
    }

    @DeleteMapping("/{pedidoId}/itens/{itemId}")
    public ResponseEntity<Map<String, String>> removeOrderItem(@PathVariable Long pedidoId,
                                                               @PathVariable Long itemId,
                                                               @AuthenticationPrincipal UserDetailsImpl user) {
        Map<String, String> response = orderService.removeOrderItem(pedidoId, itemId);
        return ResponseEntity.ok(response);
    }
}
