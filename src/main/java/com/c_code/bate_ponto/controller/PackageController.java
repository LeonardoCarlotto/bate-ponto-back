package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.PackageRequest;
import com.c_code.bate_ponto.dto.request.PackageUpdateRequest;
import com.c_code.bate_ponto.dto.response.PackageResponse;
import com.c_code.bate_ponto.model.Package;
import com.c_code.bate_ponto.model.Product;
import com.c_code.bate_ponto.model.ServiceItem;
import com.c_code.bate_ponto.repository.PackageRepository;
import com.c_code.bate_ponto.service.pkg.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacotes")
public class PackageController {

    private final PackageService packageService;
    private final PackageRepository packageRepository;

    public PackageController(PackageService packageService, PackageRepository packageRepository) {
        this.packageService = packageService;
        this.packageRepository = packageRepository;
    }

    @PostMapping
    public PackageResponse createPackage(@RequestBody PackageRequest request) {
        Package pacote = packageService.createPackage(request);
        return convertToResponse(pacote);
    }

    @GetMapping
    @Transactional
    public List<PackageResponse> getAllPackages(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo) {
        
        List<Package> pacotes;
        if (nome != null || ativo != null) {
            pacotes = packageRepository.findByFilters(nome, ativo);
        } else {
            pacotes = packageRepository.findAll();
        }
        
        return pacotes.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/ativos")
    public List<PackageResponse> getActivePackages() {
        return packageService.findActivePackages().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable Long id) {
        return packageRepository.findById(id)
            .map(this::convertToResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable Long id, 
            @RequestBody PackageUpdateRequest request) {
        
        try {
            Package pacote = packageService.updatePackage(id, request);
            return ResponseEntity.ok(convertToResponse(pacote));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deletePackage(@PathVariable Long id) {
        try {
            packageService.deletePackage(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Pacote deletado com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("erro", "Pacote não encontrado");
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ativo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageResponse> toggleActive(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Package pacote = packageService.toggleActive(id, request.get("ativo"));
            return ResponseEntity.ok(convertToResponse(pacote));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private PackageResponse convertToResponse(Package pacote) {
        List<PackageResponse.ItemResponse> itens = new ArrayList<>();
        
        // Adicionar produtos como itens
        if (pacote.getProducts() != null) {
            for (Product product : pacote.getProducts()) {
                itens.add(new PackageResponse.ItemResponse(
                    String.valueOf(product.getId()),
                    "produto",
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    1, // quantidade padrão
                    product.getPrice() // subtotal igual ao preço
                ));
            }
        }
        
        // Adicionar serviços como itens
        if (pacote.getServices() != null) {
            for (ServiceItem service : pacote.getServices()) {
                itens.add(new PackageResponse.ItemResponse(
                    String.valueOf(service.getId()),
                    "servico",
                    service.getId(),
                    service.getName(),
                    service.getPrice(),
                    1, // quantidade padrão
                    service.getPrice() // subtotal igual ao preço
                ));
            }
        }

        return new PackageResponse(
            pacote.getId(),
            pacote.getName(),
            pacote.getDescription(),
            pacote.getPrice(),
            pacote.getDurationDays(),
            pacote.getActive(),
            pacote.getDataCadastro(),
            itens
        );
    }
}
