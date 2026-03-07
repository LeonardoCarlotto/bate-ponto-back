package com.c_code.bate_ponto.service.supplier;

import com.c_code.bate_ponto.model.Supplier;
import com.c_code.bate_ponto.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier createSupplier(String nome, String cnpj, String email, String telefone, String inscricaoEstadual) {
        if (supplierRepository.existsByCnpj(cnpj)) {
            throw new RuntimeException("CNPJ já cadastrado");
        }
        Supplier supplier = new Supplier(nome, cnpj, email, telefone, inscricaoEstadual);
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, String nome, String telefone) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        
        if (nome != null && !nome.trim().isEmpty()) {
            supplier.setName(nome);
        }
        if (telefone != null && !telefone.trim().isEmpty()) {
            supplier.setPhone(telefone);
        }
        
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        supplierRepository.delete(supplier);
    }

    public List<Supplier> findByFilters(String nome) {
        return supplierRepository.findByFilters(nome);
    }

    public Supplier findByCnpj(String cnpj) {
        return supplierRepository.findByCnpj(cnpj)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }
}
