package com.c_code.bate_ponto.service;

import com.c_code.bate_ponto.dto.request.ContaPagarRequest;
import com.c_code.bate_ponto.dto.request.ContaPagarUpdateRequest;
import com.c_code.bate_ponto.dto.request.PagamentoRequest;
import com.c_code.bate_ponto.model.ContaPagar;
import com.c_code.bate_ponto.model.Supplier;
import com.c_code.bate_ponto.repository.ContaPagarRepository;
import com.c_code.bate_ponto.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ContaPagarService {

    private final ContaPagarRepository contaPagarRepository;
    private final SupplierRepository supplierRepository;

    public ContaPagarService(ContaPagarRepository contaPagarRepository, SupplierRepository supplierRepository) {
        this.contaPagarRepository = contaPagarRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<ContaPagar> findAll() {
        // Primeiro, atualiza automaticamente o status das contas vencidas
        atualizarStatusContasVencidas();
        
        // Depois retorna a lista atualizada
        return contaPagarRepository.findAll();
    }
    
    private void atualizarStatusContasVencidas() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Busca todas as contas que precisam ser atualizadas (status vencido ou nome do fornecedor)
        List<ContaPagar> contasParaAtualizar = new ArrayList<>();
        
        // Verifica todas as contas
        List<ContaPagar> todasContas = contaPagarRepository.findAll();
        for (ContaPagar conta : todasContas) {
            boolean precisaAtualizar = false;
            
            // Verifica se precisa atualizar status para VENCIDO
            if (!"PAGO".equals(conta.getStatus()) && 
                !"CANCELADO".equals(conta.getStatus()) &&
                !"VENCIDO".equals(conta.getStatus()) &&
                conta.getDataVencimento() != null &&
                conta.getDataVencimento().compareTo(currentDate) < 0) {
                
                // Atualiza o status para VENCIDO
                conta.setStatus("VENCIDO");
                precisaAtualizar = true;
            }
            
            // Verifica se precisa atualizar nome do fornecedor
            if (conta.getFornecedorNome() == null || conta.getFornecedorNome().trim().isEmpty()) {
                try {
                    Supplier fornecedor = supplierRepository.findById(conta.getFornecedorId())
                        .orElse(null);
                    if (fornecedor != null) {
                        conta.setFornecedorNome(fornecedor.getName());
                        precisaAtualizar = true;
                    }
                } catch (Exception e) {
                    // Ignora erro e continua
                }
            }
            
            if (precisaAtualizar) {
                contasParaAtualizar.add(conta);
            }
        }
        
        // Salva todas as contas que precisam ser atualizadas
        if (!contasParaAtualizar.isEmpty()) {
            contaPagarRepository.saveAll(contasParaAtualizar);
        }
    }

    public Optional<ContaPagar> findById(Long id) {
        return contaPagarRepository.findById(id);
    }

    public ContaPagar create(ContaPagarRequest request) {
        Supplier fornecedor = supplierRepository.findById(request.getFornecedorId())
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        ContaPagar contaPagar = new ContaPagar(
            request.getFornecedorId(),
            fornecedor.getName(), // Usa o nome do fornecedor do banco de dados
            request.getDataVencimento(),
            "PENDENTE",
            request.getDescricao(),
            request.getValor(),
            request.getFormaPagamento(),
            request.getParcelas() != null ? request.getParcelas() : 1
        );

        return contaPagarRepository.save(contaPagar);
    }

    public ContaPagar update(Long id, ContaPagarUpdateRequest request) {
        ContaPagar contaPagar = contaPagarRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        if (request.getFornecedorId() != null) {
            Supplier fornecedor = supplierRepository.findById(request.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
            contaPagar.setFornecedorId(request.getFornecedorId());
            contaPagar.setFornecedorNome(fornecedor.getName()); // Atualiza nome do fornecedor
        }
        if (request.getDataVencimento() != null) {
            contaPagar.setDataVencimento(request.getDataVencimento());
        }
        if (request.getDataPagamento() != null) {
            contaPagar.setDataPagamento(request.getDataPagamento());
        }
        if (request.getStatus() != null) {
            contaPagar.setStatus(request.getStatus());
        }
        if (request.getDescricao() != null) {
            contaPagar.setDescricao(request.getDescricao());
        }
        if (request.getValor() != null) {
            contaPagar.setValor(request.getValor());
        }
        if (request.getFormaPagamento() != null) {
            contaPagar.setFormaPagamento(request.getFormaPagamento());
        }
        if (request.getParcelas() != null) {
            contaPagar.setParcelas(request.getParcelas());
        }

        return contaPagarRepository.save(contaPagar);
    }

    public void delete(Long id) {
        ContaPagar contaPagar = contaPagarRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));
        contaPagarRepository.delete(contaPagar);
    }

    public ContaPagar updateStatus(Long id, String status) {
        ContaPagar contaPagar = contaPagarRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        contaPagar.setStatus(status);
        return contaPagarRepository.save(contaPagar);
    }

    public ContaPagar registrarPagamento(Long id, PagamentoRequest pagamentoRequest) {
        ContaPagar contaPagar = contaPagarRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        contaPagar.setStatus("PAGO");
        contaPagar.setDataPagamento(pagamentoRequest.getData());
        
        return contaPagarRepository.save(contaPagar);
    }

    public List<ContaPagar> findContasVencidas() {
        // Primeiro atualiza o status das contas vencidas
        atualizarStatusContasVencidas();
        
        // Depois busca as contas com status VENCIDO
        return contaPagarRepository.findByStatus("VENCIDO");
    }

    public List<ContaPagar> findContasAVencer(Integer dias) {
        // Primeiro atualiza o status das contas vencidas
        atualizarStatusContasVencidas();
        
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(dias);
        String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String futureDateStr = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        return contaPagarRepository.findContasAVencer(currentDateStr, futureDateStr);
    }
}
