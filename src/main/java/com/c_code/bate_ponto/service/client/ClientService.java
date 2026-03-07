package com.c_code.bate_ponto.service.client;

import com.c_code.bate_ponto.dto.request.ClientUpdateRequest;
import com.c_code.bate_ponto.model.Client;
import com.c_code.bate_ponto.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client createClient(String nome, String cpfCnpj, String email, String telefone, String dataAbertura) {
        // Verificar se CPF/CNPJ já existe
        if (clientRepository.existsByCpfCnpj(cpfCnpj)) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }

        // Verificar se email já existe (se fornecido)
        if (email != null && !email.trim().isEmpty() && clientRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já cadastrado");
        }

        Client client = new Client(nome, cpfCnpj, email, telefone, dataAbertura);
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Atualizar nome se fornecido
        if (request.getNome() != null && !request.getNome().trim().isEmpty()) {
            client.setName(request.getNome());
        }

        // Atualizar email se fornecido e for diferente
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (!request.getEmail().equals(client.getEmail()) && 
                clientRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email já cadastrado para outro cliente");
            }
            client.setEmail(request.getEmail());
        }

        // Atualizar status ativo se fornecido
        if (request.getAtivo() != null) {
            client.setActive(request.getAtivo());
        }

        return clientRepository.save(client);
    }

    @Transactional
    public Client toggleActive(Long id, Boolean ativo) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        client.setActive(ativo);
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        clientRepository.delete(client);
    }
}
