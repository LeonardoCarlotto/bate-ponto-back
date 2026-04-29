package com.c_code.bate_ponto.service;

import com.c_code.bate_ponto.dto.request.ContaReceberPagamentoRequest;
import com.c_code.bate_ponto.model.ContaReceber;
import com.c_code.bate_ponto.repository.ClientRepository;
import com.c_code.bate_ponto.repository.ContaReceberRepository;
import com.c_code.bate_ponto.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaReceberService {

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

    public List<ContaReceber> findAll() {
        return contaReceberRepository.findAll();
    }

    public Optional<ContaReceber> findById(Long id) {
        return contaReceberRepository.findById(id);
    }

    public ContaReceber registrarPagamentoIndividual(ContaReceberPagamentoRequest request) {
        clientRepository.findById(request.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (request.getPedidoId() != null) {
            orderRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        }

        ContaReceber contaReceber = new ContaReceber(
            request.getClienteId(),
            request.getClienteNome(),
            request.getValor(),
            request.getFormaPagamento(),
            request.getDescricao(),
            request.getData(),
            request.getPedidoId(),
            request.getTipoPagamento() != null ? request.getTipoPagamento() : "pedido_individual"
        );

        contaReceber.setStatus("PAGO");

        return contaReceberRepository.save(contaReceber);
    }

    public List<ContaReceber> findByClienteId(Long clienteId) {
        return contaReceberRepository.findByClienteId(clienteId);
    }

    public List<ContaReceber> findByPedidoId(Long pedidoId) {
        return contaReceberRepository.findByPedidoId(pedidoId);
    }
}
