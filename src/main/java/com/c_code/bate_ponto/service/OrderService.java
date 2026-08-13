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
    private final PackageRepository packageRepository;

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
        Order order = new Order();
        order.setItems(new ArrayList<>());
        applyOrderRequest(order, request);

        order = orderRepository.save(order);
        replaceOrderItems(order, request.getItens());
        order = orderRepository.save(order);

        return convertToOrderResponse(order);
    }

    public OrderResponse updateOrder(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (request.getClienteId() != null) {
            Client client = clientRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            order.setClient(client);
        }

        if (request.getDescricaoFinal() != null) {
            order.setDescription(request.getDescricaoFinal());
        }

        if (request.getDataPedido() != null && !request.getDataPedido().trim().isEmpty()) {
            order.setDate(parseDate(request.getDataPedido()));
        }

        if (request.getStatus() != null) {
            order.setStatus(OrderStatus.valueOf(request.getStatus().toUpperCase()));
        }

        if (request.getFormaPagamento() != null) {
            order.setFormaPagamento(trim(request.getFormaPagamento()));
        }

        if (request.getParcelas() != null) {
            validateParcelas(request.getParcelas());
            order.setParcelas(request.getParcelas());
        }

        if (request.getItens() != null) {
            replaceOrderItems(order, request.getItens());
        }

        order = orderRepository.save(order);
        return convertToOrderResponse(order);
    }

    public Map<String, String> deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(OrderStatus.CANCELADO);
        orderRepository.save(order);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Pedido cancelado com sucesso");
        return response;
    }

    public OrderResponse updateOrderStatus(Long id, OrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(OrderStatus.valueOf(request.getStatus().toUpperCase()));
        order = orderRepository.save(order);

        return convertToOrderResponse(order);
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

        OrderItem item = createOrderItem(order, request);
        item = orderItemRepository.save(item);
        order.getItems().add(item);

        recalculateOrderValue(order);
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
        order.getItems().removeIf(existingItem -> existingItem.getId().equals(itemId));

        recalculateOrderValue(order);
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
                order.getFormaPagamento(),
                order.getParcelas(),
                itens
        );
    }

    private OrderItemResponse convertToOrderItemResponse(OrderItem item) {
        Long produtoId = item.getProduct() != null ? item.getProduct().getId() : null;
        String produtoNome = item.getProduct() != null ? item.getProduct().getName() : null;
        Long pacoteId = item.getPackageItem() != null ? item.getPackageItem().getId() : null;
        String pacoteNome = item.getPackageItem() != null ? item.getPackageItem().getName() : null;
        String tipo = item.getItemType() != null ? item.getItemType() : (pacoteId != null ? "pacote" : "produto");
        String nome = item.getItemName() != null ? item.getItemName() : (pacoteNome != null ? pacoteNome : produtoNome);

        return new OrderItemResponse(
                item.getId(),
                produtoId,
                produtoNome,
                pacoteId,
                pacoteNome,
                tipo,
                nome,
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    private void applyOrderRequest(Order order, OrderRequest request) {
        validateOrderRequest(request);

        Client client = clientRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        order.setClient(client);
        order.setDescription(normalizeDescription(request.getDescricaoFinal()));
        order.setStatus(parseStatus(request.getStatus()));
        order.setDate(parseDate(request.getDataPedido()));
        order.setFormaPagamento(trim(request.getFormaPagamento()));
        validateParcelas(request.getParcelas());
        order.setParcelas(request.getParcelas());
        order.setValue(0.0);
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request == null) {
            throw new RuntimeException("Dados do pedido são obrigatórios");
        }
        if (request.getClienteId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new RuntimeException("Pedido deve ter pelo menos um item");
        }
        if (request.getFormaPagamento() == null || request.getFormaPagamento().trim().isEmpty()) {
            throw new RuntimeException("Forma de pagamento é obrigatória");
        }
    }

    private void replaceOrderItems(Order order, List<OrderItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            throw new RuntimeException("Pedido deve ter pelo menos um item");
        }

        order.getItems().clear();
        for (OrderItemRequest itemRequest : itemRequests) {
            order.getItems().add(createOrderItem(order, itemRequest));
        }
        recalculateOrderValue(order);
    }

    private OrderItem createOrderItem(Order order, OrderItemRequest request) {
        if (request == null) {
            throw new RuntimeException("Item do pedido é obrigatório");
        }
        if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade do item deve ser maior que zero");
        }

        Double preco = request.getPrecoFinal();
        if (preco == null || preco < 0) {
            throw new RuntimeException("Preço do item deve ser maior ou igual a zero");
        }

        String tipo = request.getTipo() != null ? request.getTipo().toLowerCase() : "produto";
        Long referenciaId = request.getReferenciaId();
        if (referenciaId == null) {
            throw new RuntimeException("Referência do item é obrigatória");
        }

        if ("pacote".equals(tipo)) {
            com.c_code.bate_ponto.model.Package pacote = packageRepository.findById(referenciaId)
                    .orElseThrow(() -> new RuntimeException("Pacote não encontrado: " + referenciaId));
            return new OrderItem(order, pacote, request.getQuantidade(), preco);
        }

        Product product = productRepository.findById(referenciaId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + referenciaId));
        return new OrderItem(order, product, request.getQuantidade(), preco);
    }

    private void recalculateOrderValue(Order order) {
        double totalValue = order.getItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setValue(totalValue);
    }

    private LocalDateTime parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return LocalDateTime.now();
        }
        String normalized = value.trim();
        if (normalized.length() == 10) {
            normalized = normalized + "T00:00:00";
        }
        return LocalDateTime.parse(normalized);
    }

    private OrderStatus parseStatus(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OrderStatus.PREPARACAO;
        }
        return OrderStatus.valueOf(value.toUpperCase());
    }

    private void validateParcelas(Integer parcelas) {
        if (parcelas == null || parcelas < 1 || parcelas > 12) {
            throw new RuntimeException("Parcelas deve estar entre 1 e 12");
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeDescription(String value) {
        String trimmed = trim(value);
        return trimmed == null || trimmed.isEmpty() ? "Pedido sem descrição" : trimmed;
    }
}
