package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.SupplierAddressRequest;
import com.c_code.bate_ponto.dto.request.SupplierContactRequest;
import com.c_code.bate_ponto.dto.request.SupplierRequest;
import com.c_code.bate_ponto.dto.response.SupplierResponse;
import com.c_code.bate_ponto.model.Supplier;
import com.c_code.bate_ponto.repository.SupplierRepository;
import com.c_code.bate_ponto.service.supplier.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/fornecedores")
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierService supplierService, SupplierRepository supplierRepository) {
        this.supplierService = supplierService;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public List<SupplierResponse> listSuppliers(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer limite) {
        
        List<Supplier> suppliers;
        if (nome != null) {
            suppliers = supplierService.findByFilters(nome);
        } else {
            suppliers = supplierRepository.findAll();
        }
        
        return suppliers.stream()
            .map(this::convertToResponse)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplier(@PathVariable Long id) {
        return supplierRepository.findById(id)
            .map(this::convertToResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SupplierResponse createSupplier(@RequestBody SupplierRequest request) {
        Supplier supplier = supplierService.createSupplier(
            request.getName(),
            request.getCnpj(),
            request.getEmail(),
            request.getPhone(),
            request.getStateRegistration()
        );
        return convertToResponse(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id, 
            @RequestBody SupplierRequest request) {
        try {
            Supplier supplier = supplierService.updateSupplier(id, request.getName(), request.getPhone());
            return ResponseEntity.ok(convertToResponse(supplier));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Fornecedor deletado com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<SupplierResponse> getSupplierByCnpj(@PathVariable String cnpj) {
        try {
            Supplier supplier = supplierService.findByCnpj(cnpj);
            return ResponseEntity.ok(convertToResponse(supplier));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private SupplierResponse convertToResponse(Supplier supplier) {
        return new SupplierResponse(
            supplier.getId(),
            supplier.getName(),
            supplier.getCnpj(),
            supplier.getEmail(),
            supplier.getPhone(),
            supplier.getStateRegistration(),
            supplier.getActive(),
            supplier.getDataCadastro()
        );
    }

    // Contatos
    @GetMapping("/{id}/contatos")
    public ResponseEntity<?> getSupplierContacts(@PathVariable Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        return ResponseEntity.ok(supplier.getContacts());
    }

    @PostMapping("/{id}/contatos")
    public ResponseEntity<?> addSupplierContact(@PathVariable Long id, @RequestBody SupplierContactRequest request) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        
        // Implementar lógica para adicionar contato
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/contatos/{contactId}")
    public ResponseEntity<?> removeSupplierContact(@PathVariable Long id, @PathVariable Long contactId) {
        // Implementar lógica para remover contato
        return ResponseEntity.ok().build();
    }

    // Endereços
    @GetMapping("/{id}/enderecos")
    public ResponseEntity<?> getSupplierAddresses(@PathVariable Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        return ResponseEntity.ok(supplier.getAddresses());
    }

    @PostMapping("/{id}/enderecos")
    public ResponseEntity<?> addSupplierAddress(@PathVariable Long id, @RequestBody SupplierAddressRequest request) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        
        // Implementar lógica para adicionar endereço
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/enderecos/{addressId}")
    public ResponseEntity<?> removeSupplierAddress(@PathVariable Long id, @PathVariable Long addressId) {
        // Implementar lógica para remover endereço
        return ResponseEntity.ok().build();
    }
}
