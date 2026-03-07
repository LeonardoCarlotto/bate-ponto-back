package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.AddressRequest;
import com.c_code.bate_ponto.dto.request.ClientRequest;
import com.c_code.bate_ponto.dto.request.ClientUpdateRequest;
import com.c_code.bate_ponto.dto.response.ClientResponse;
import com.c_code.bate_ponto.model.Client;
import com.c_code.bate_ponto.repository.ClientRepository;
import com.c_code.bate_ponto.service.client.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ClientResponse createClient(@RequestBody ClientRequest request) {
        Client client = clientService.createClient(
            request.getNome(),
            request.getCpfCnpj(),
            request.getEmail(),
            request.getTelefone(),
            request.getDataAbertura()
        );
        
        return new ClientResponse(
            client.getId(),
            client.getName(),
            client.getCpfCnpj(),
            client.getEmail(),
            client.getTelefone(),
            client.getDataAbertura(),
            client.getActive(),
            client.getDataCadastro()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientResponse> getAllClients(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo) {
        
        List<Client> clients;
        if (nome != null || ativo != null) {
            clients = clientRepository.findByFilters(nome, ativo);
        } else {
            clients = clientRepository.findAll();
        }
        
        return clients.stream()
            .map(client -> new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getTelefone(),
                client.getDataAbertura(),
                client.getActive(),
                client.getDataCadastro()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
            .map(client -> ResponseEntity.ok(new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getTelefone(),
                client.getDataAbertura(),
                client.getActive(),
                client.getDataCadastro()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id, 
            @RequestBody ClientUpdateRequest request) {
        
        try {
            Client client = clientService.updateClient(id, request);
            return ResponseEntity.ok(new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getTelefone(),
                client.getDataAbertura(),
                client.getActive(),
                client.getDataCadastro()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Cliente deletado com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", "Cliente não encontrado");
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ativo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> toggleActive(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Client client = clientService.toggleActive(id, request.get("ativo"));
            return ResponseEntity.ok(new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getTelefone(),
                client.getDataAbertura(),
                client.getActive(),
                client.getDataCadastro()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endereços
    @GetMapping("/{id}/enderecos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getClientAddresses(@PathVariable Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ResponseEntity.ok(client.getAddresses());
    }

    @PostMapping("/{id}/enderecos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addClientAddress(@PathVariable Long id, @RequestBody AddressRequest request) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        // Implementar lógica para adicionar endereço
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/enderecos/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeClientAddress(@PathVariable Long id, @PathVariable Long addressId) {
        // Implementar lógica para remover endereço
        return ResponseEntity.ok().build();
    }

    // Contatos
    @GetMapping("/{id}/contatos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getClientContacts(@PathVariable Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ResponseEntity.ok(client.getContacts());
    }

    @PostMapping("/{id}/contatos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addClientContact(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        // Implementar lógica para adicionar contato
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/contatos/{contactId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeClientContact(@PathVariable Long id, @PathVariable Long contactId) {
        // Implementar lógica para remover contato
        return ResponseEntity.ok().build();
    }

    // Busca por documento
    @GetMapping("/documento/{documento}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> getClientByDocument(@PathVariable String documento) {
        return clientRepository.findByCpfCnpj(documento)
            .map(client -> ResponseEntity.ok(new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getTelefone(),
                client.getDataAbertura(),
                client.getActive(),
                client.getDataCadastro()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    // Busca por email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> getClientByEmail(@PathVariable String email) {
        return clientRepository.findByEmail(email)
            .map(client -> ResponseEntity.ok(new ClientResponse(
                client.getId(),
                client.getName(),
                client.getCpfCnpj(),
                client.getEmail(),
                client.getTelefone(),
                client.getDataAbertura(),
                client.getActive(),
                client.getDataCadastro()
            )))
            .orElse(ResponseEntity.notFound().build());
    }
}
