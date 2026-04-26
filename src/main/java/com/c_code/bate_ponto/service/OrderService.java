package com.c_code.bate_ponto.service;

import com.c_code.bate_ponto.dto.request.*;
import com.c_code.bate_ponto.dto.response.*;
import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public List<OrderResponse> getAllOrders(String status, String dataInicio, String dataFim) {
        List<Order> orders = orderRepository.findAll();
        
        if (status != null && !status.isEmpty()) {
            OrderStatus statusEnum = OrderStatus.valueOf(status.toUpperCase());
            orders = orders.stream()
                    .filter(o -> o.getStatus() == statusEnum)
                    .collect(Collectors.toList());
        }
        
        if (dataInicio != null && !dataInicio.isEmpty()) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio);
            orders = orders.stream()
                    .filter(o -> o.getDate().isAfter(inicio.minusSeconds(1)))
                    .collect(Collectors.toList());
        }
        
        if (dataFim != null && !dataFim.isEmpty()) {
            LocalDateTime fim = LocalDateTime.parse(dataFim);
            orders = orders.stream()
                    .filter(o -> o.getDate().isBefore(fim.plusSeconds(1)))
                    .collect(Collectors.toList());
        }

        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByFilters(Long clienteId, String status, String dataInicio, String dataFim) {
        List<Order> orders = orderRepository.findAll();
        
        // Filtrar por cliente
        if (clienteId != null) {
            orders = orders.stream()
                    .filter(o -> o.getClient().getId().equals(clienteId))
                    .collect(Collectors.toList());
        }
        
        // Filtrar por status
        if (status != null && !status.isEmpty()) {
            OrderStatus statusEnum = OrderStatus.valueOf(status.toUpperCase());
            orders = orders.stream()
                    .filter(o -> o.getStatus() == statusEnum)
                    .collect(Collectors.toList());
        }
        
        // Filtrar por data inicial
        if (dataInicio != null && !dataInicio.isEmpty()) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio);
            orders = orders.stream()
                    .filter(o -> o.getDate().isAfter(inicio.minusSeconds(1)))
                    .collect(Collectors.toList());
        }
        
        // Filtrar por data final
        if (dataFim != null && !dataFim.isEmpty()) {
            LocalDateTime fim = LocalDateTime.parse(dataFim);
            orders = orders.stream()
                    .filter(o -> o.getDate().isBefore(fim.plusSeconds(1)))
                    .collect(Collectors.toList());
        }

        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return convertToOrderResponse(order);
    }

    public OrderResponse createOrder(OrderRequest request) {
        Client client = clientRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Order order = new Order();
        order.setClient(client);
        order.setDescription(request.getObservacoes());
        order.setStatus(OrderStatus.valueOf(request.getStatus()));
        order.setDate(LocalDateTime.parse(request.getDataPedido()));
        order.setValue(request.getTotal());
        order.setItems(new ArrayList<>());

        order = orderRepository.save(order);

        double totalValue = 0.0;
        for (OrderItemRequest itemRequest : request.getItens()) {
            Product product = productRepository.findById(itemRequest.getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemRequest.getId()));

            OrderItem item = new OrderItem(order, product, itemRequest.getQuantidade(), itemRequest.getPreco());
            orderItemRepository.save(item);
            totalValue += itemRequest.getSubtotal();
        }

        order.setValue(totalValue);
        order = orderRepository.save(order);

        return convertToOrderResponse(order);
    }

    public OrderResponse updateOrder(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (request.getDescription() != null) {
            order.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            order.setStatus(OrderStatus.valueOf(request.getStatus().toUpperCase()));
        }

        order = orderRepository.save(order);
        return convertToOrderResponse(order);
    }

    public Map<String, String> deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        orderRepository.delete(order);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Pedido deletado com sucesso");
        return response;
    }

    public OrderResponse updateOrderStatus(Long id, OrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(OrderStatus.valueOf(request.getStatus().toUpperCase()));
        order = orderRepository.save(order);

        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getClient().getId(),
                order.getClient().getName(),
                order.getDate(),
                order.getStatus().name(),
                order.getValue(),
                order.getDescription(),
                order.getItems().stream()
                        .map(this::convertToOrderItemResponse)
                        .collect(Collectors.toList())
        );
        
        return response;
    }

    public List<OrderItemResponse> getOrderItems(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        
        return items.stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
    }

    public OrderItemResponse addOrderItem(Long orderId, OrderItemRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        OrderItem item = new OrderItem(order, product, request.getQuantidade(), request.getPreco());
        item = orderItemRepository.save(item);

        // Atualizar valor total do pedido
        double totalValue = order.getItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setValue(totalValue);
        orderRepository.save(order);

        return convertToOrderItemResponse(item);
    }

    public Map<String, String> removeOrderItem(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Order order = item.getOrder();
        if (!order.getId().equals(orderId)) {
            throw new RuntimeException("Item não pertence a este pedido");
        }

        orderItemRepository.delete(item);

        // Atualizar valor total do pedido
        double totalValue = order.getItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setValue(totalValue);
        orderRepository.save(order);

        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Item removido com sucesso");
        return response;
    }

    private OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> itens = order.getItems().stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getClient().getId(),
                order.getClient().getName(),
                order.getDate(),
                order.getStatus().name(),
                order.getValue(),
                order.getDescription(),
                itens
        );
    }

    private OrderItemResponse convertToOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
