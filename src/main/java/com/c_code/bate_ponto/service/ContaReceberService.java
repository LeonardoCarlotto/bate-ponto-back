package com.c_code.bate_ponto.service;

import com.c_code.bate_ponto.dto.request.ContaReceberPagamentoRequest;
import com.c_code.bate_ponto.dto.response.ContaReceberClienteResponse;
import com.c_code.bate_ponto.dto.response.ContaReceberResponse;
import com.c_code.bate_ponto.dto.response.OrderItemResponse;
import com.c_code.bate_ponto.dto.response.OrderResponse;
import com.c_code.bate_ponto.model.Client;
import com.c_code.bate_ponto.model.ContaReceber;
import com.c_code.bate_ponto.model.Order;
import com.c_code.bate_ponto.model.OrderItem;
import com.c_code.bate_ponto.model.OrderStatus;
import com.c_code.bate_ponto.repository.ClientRepository;
import com.c_code.bate_ponto.repository.ContaReceberRepository;
import com.c_code.bate_ponto.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContaReceberService {
    private static final double CENT_TOLERANCE = 0.009;

    private final ContaReceberRepository contaReceberRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

    public ContaReceberService(ContaReceberRepository contaReceberRepository, 
                              ClientRepository clientRepository,
                              OrderRepository orderRepository) {
        this.contaReceberRepository = contaReceberRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<ContaReceberResponse> findAll() {
        return contaReceberRepository.findAll().stream()
            .map(this::toPaymentResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ContaReceberResponse> findById(Long id) {
        return contaReceberRepository.findById(id)
            .map(this::toPaymentResponse);
    }

    @Transactional(readOnly = true)
    public List<ContaReceberClienteResponse> getResumoPorCliente() {
        Map<Long, List<Order>> pedidosPorCliente = orderRepository.findAll().stream()
            .filter(order -> order.getStatus() != OrderStatus.CANCELADO)
            .collect(Collectors.groupingBy(order -> order.getClient().getId(), LinkedHashMap::new, Collectors.toList()));

        contaReceberRepository.findAll().stream()
            .filter(payment -> payment.getClienteId() != null)
            .forEach(payment -> pedidosPorCliente.putIfAbsent(payment.getClienteId(), new ArrayList<>()));

        return pedidosPorCliente.keySet().stream()
            .sorted()
            .map(this::getResumoCliente)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaReceberClienteResponse getResumoCliente(Long clienteId) {
        Client client = clientRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<Order> pedidos = orderRepository.findAll().stream()
            .filter(order -> order.getClient().getId().equals(clienteId))
            .filter(order -> order.getStatus() != OrderStatus.CANCELADO)
            .sorted(Comparator.comparing(Order::getDate).reversed())
            .collect(Collectors.toList());

        List<ContaReceber> pagamentos = contaReceberRepository.findByClienteId(clienteId).stream()
            .sorted(Comparator.comparing(ContaReceber::getCreatedAt).reversed())
            .collect(Collectors.toList());

        double totalEmAberto = pedidos.stream()
            .mapToDouble(order -> order.getValue() != null ? order.getValue() : 0.0)
            .sum();
        double totalPago = pagamentos.stream()
            .filter(this::isPagamentoValido)
            .mapToDouble(payment -> payment.getValor() != null ? payment.getValor() : 0.0)
            .sum();

        return new ContaReceberClienteResponse(
            client.getId(),
            client.getName(),
            pedidos.stream().map(this::toOrderResponse).collect(Collectors.toList()),
            pagamentos.stream().map(this::toPaymentResponse).collect(Collectors.toList()),
            totalEmAberto,
            totalPago,
            totalEmAberto - totalPago
        );
    }

    public ContaReceberResponse registrarPagamentoIndividual(ContaReceberPagamentoRequest request) {
        validatePagamento(request);

        Client client = clientRepository.findById(request.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        double saldoDisponivel = getSaldoDisponivel(request);
        if (request.getValor() - saldoDisponivel > CENT_TOLERANCE) {
            throw new RuntimeException("Valor do pagamento não pode ser maior que o saldo devedor");
        }

        if (request.getPedidoId() != null) {
            orderRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        }

        ContaReceber contaReceber = new ContaReceber(
            request.getClienteId(),
            request.getClienteNome() != null ? request.getClienteNome() : client.getName(),
            request.getValor(),
            request.getFormaPagamento(),
            request.getDescricao(),
            request.getData(),
            request.getPedidoId(),
            request.getTipoPagamento() != null ? request.getTipoPagamento() : "pedido_individual"
        );

        contaReceber.setStatus("PAGO");

        return toPaymentResponse(contaReceberRepository.save(contaReceber));
    }

    @Transactional(readOnly = true)
    public List<ContaReceberResponse> findByClienteId(Long clienteId) {
        return contaReceberRepository.findByClienteId(clienteId).stream()
            .map(this::toPaymentResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContaReceberResponse> findByPedidoId(Long pedidoId) {
        return contaReceberRepository.findByPedidoId(pedidoId).stream()
            .map(this::toPaymentResponse)
            .collect(Collectors.toList());
    }

    public ContaReceberResponse estornarPagamento(Long pagamentoId) {
        ContaReceber pagamento = contaReceberRepository.findById(pagamentoId)
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if ("ESTORNADO".equalsIgnoreCase(pagamento.getStatus())) {
            throw new RuntimeException("Pagamento já foi estornado");
        }

        pagamento.setStatus("ESTORNADO");
        String descricao = pagamento.getDescricao() != null ? pagamento.getDescricao() : "";
        pagamento.setDescricao((descricao + " [ESTORNADO]").trim());
        return toPaymentResponse(contaReceberRepository.save(pagamento));
    }

    private void validatePagamento(ContaReceberPagamentoRequest request) {
        if (request == null) {
            throw new RuntimeException("Dados do pagamento são obrigatórios");
        }
        if (request.getClienteId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }
        if (request.getValor() == null || request.getValor() <= 0) {
            throw new RuntimeException("Valor do pagamento deve ser maior que zero");
        }
        if (request.getFormaPagamento() == null || request.getFormaPagamento().trim().isEmpty()) {
            throw new RuntimeException("Forma de pagamento é obrigatória");
        }
        if (request.getData() == null || request.getData().trim().isEmpty()) {
            throw new RuntimeException("Data do pagamento é obrigatória");
        }
    }

    private double getSaldoDisponivel(ContaReceberPagamentoRequest request) {
        if (request.getPedidoId() != null) {
            Order order = orderRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
            double totalPagoPedido = contaReceberRepository.findByPedidoIdAndStatus(request.getPedidoId(), "PAGO").stream()
                .mapToDouble(payment -> payment.getValor() != null ? payment.getValor() : 0.0)
                .sum();
            return (order.getValue() != null ? order.getValue() : 0.0) - totalPagoPedido;
        }

        return getResumoCliente(request.getClienteId()).getSaldoDevedor();
    }

    private boolean isPagamentoValido(ContaReceber payment) {
        return "PAGO".equalsIgnoreCase(payment.getStatus());
    }

    private ContaReceberResponse toPaymentResponse(ContaReceber payment) {
        return new ContaReceberResponse(
            payment.getId(),
            payment.getClienteId(),
            payment.getClienteNome(),
            payment.getValor(),
            payment.getFormaPagamento(),
            payment.getDescricao(),
            payment.getDataPagamento(),
            payment.getPedidoId(),
            payment.getTipoPagamento(),
            payment.getStatus(),
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> itens = order.getItems().stream()
            .map(this::toOrderItemResponse)
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

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
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
}
