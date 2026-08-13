package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.SupplierAddressRequest;
import com.c_code.bate_ponto.dto.request.SupplierContactRequest;
import com.c_code.bate_ponto.dto.request.SupplierRequest;
import com.c_code.bate_ponto.dto.response.SupplierResponse;
import com.c_code.bate_ponto.model.SupplierAddress;
import com.c_code.bate_ponto.model.SupplierContact;
import com.c_code.bate_ponto.model.Supplier;
import com.c_code.bate_ponto.service.supplier.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/fornecedores")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<SupplierResponse> listSuppliers(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer limite) {
        return supplierService.listSuppliers(nome).stream()
            .map(this::convertToResponse)
            .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponse> getSupplier(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(convertToResponse(supplierService.findById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SupplierResponse createSupplier(@RequestBody SupplierRequest request) {
        Supplier supplier = supplierService.createSupplier(request);
        return convertToResponse(supplier);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id, 
            @RequestBody SupplierRequest request) {
        try {
            Supplier supplier = supplierService.updateSupplier(id, request);
            return ResponseEntity.ok(convertToResponse(supplier));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Fornecedor inativado com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cnpj/{cnpj}")
    @PreAuthorize("hasRole('ADMIN')")
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
            firstContactName(supplier),
            firstAddressStreet(supplier),
            firstAddressCity(supplier),
            firstAddressState(supplier),
            firstAddressZipCode(supplier),
            supplier.getActive(),
            supplier.getDataCadastro()
        );
    }

    // Contatos
    @GetMapping("/{id}/contatos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getSupplierContacts(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.listContacts(id).stream()
            .map(this::convertContactToResponse)
            .toList());
    }

    @PostMapping("/{id}/contatos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSupplierContact(@PathVariable Long id, @RequestBody SupplierContactRequest request) {
        return ResponseEntity.ok(convertContactToResponse(supplierService.addContact(id, request)));
    }

    @DeleteMapping("/{id}/contatos/{contactId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeSupplierContact(@PathVariable Long id, @PathVariable Long contactId) {
        supplierService.removeContact(id, contactId);
        return ResponseEntity.ok(Map.of("mensagem", "Contato removido com sucesso"));
    }

    // Endereços
    @GetMapping("/{id}/enderecos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getSupplierAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.listAddresses(id).stream()
            .map(this::convertAddressToResponse)
            .toList());
    }

    @PostMapping("/{id}/enderecos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSupplierAddress(@PathVariable Long id, @RequestBody SupplierAddressRequest request) {
        return ResponseEntity.ok(convertAddressToResponse(supplierService.addAddress(id, request)));
    }

    @DeleteMapping("/{id}/enderecos/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeSupplierAddress(@PathVariable Long id, @PathVariable Long addressId) {
        supplierService.removeAddress(id, addressId);
        return ResponseEntity.ok(Map.of("mensagem", "Endereço removido com sucesso"));
    }

    private String firstContactName(Supplier supplier) {
        return supplier.getContacts().isEmpty() ? null : supplier.getContacts().get(0).getName();
    }

    private String firstAddressStreet(Supplier supplier) {
        return supplier.getAddresses().isEmpty() ? null : supplier.getAddresses().get(0).getStreet();
    }

    private String firstAddressCity(Supplier supplier) {
        return supplier.getAddresses().isEmpty() ? null : supplier.getAddresses().get(0).getCity();
    }

    private String firstAddressState(Supplier supplier) {
        return supplier.getAddresses().isEmpty() ? null : supplier.getAddresses().get(0).getState();
    }

    private String firstAddressZipCode(Supplier supplier) {
        return supplier.getAddresses().isEmpty() ? null : supplier.getAddresses().get(0).getZipCode();
    }

    private Map<String, Object> convertContactToResponse(SupplierContact contact) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", contact.getId());
        response.put("nome", contact.getName());
        response.put("cargo", contact.getPosition());
        response.put("email", contact.getEmail());
        response.put("telefone", contact.getPhone());
        return response;
    }

    private Map<String, Object> convertAddressToResponse(SupplierAddress address) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", address.getId());
        response.put("logradouro", address.getStreet());
        response.put("numero", address.getNumber());
        response.put("bairro", address.getNeighborhood());
        response.put("cidade", address.getCity());
        response.put("estado", address.getState());
        response.put("cep", address.getZipCode());
        response.put("tipo", address.getType());
        return response;
    }
}
