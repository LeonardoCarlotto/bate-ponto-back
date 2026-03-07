package com.c_code.bate_ponto.service.pkg;

import com.c_code.bate_ponto.dto.request.PackageRequest;
import com.c_code.bate_ponto.dto.request.PackageUpdateRequest;
import com.c_code.bate_ponto.model.Package;
import com.c_code.bate_ponto.model.Product;
import com.c_code.bate_ponto.repository.PackageRepository;
import com.c_code.bate_ponto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final ProductRepository productRepository;

    public PackageService(PackageRepository packageRepository, ProductRepository productRepository) {
        this.packageRepository = packageRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Package createPackage(String nome, String descricao, Double preco, Integer duracaoDias, List<Long> produtosIds) {
        // Verificar se nome já existe
        if (packageRepository.existsByName(nome)) {
            throw new RuntimeException("Nome de pacote já cadastrado");
        }

        // Buscar produtos
        List<Product> produtos = productRepository.findAllById(produtosIds);
        if (produtos.size() != produtosIds.size()) {
            throw new RuntimeException("Um ou mais produtos não foram encontrados");
        }

        return packageRepository.save(new Package(nome, descricao, preco, duracaoDias, produtos));
    }

    @Transactional
    public Package createPackage(PackageRequest request) {
        // Verificar se nome já existe
        if (packageRepository.existsByName(request.getNome())) {
            throw new RuntimeException("Nome de pacote já cadastrado");
        }

        // Buscar produtos
        List<Product> produtos = new ArrayList<>();
        if (request.getProdutos() != null && !request.getProdutos().isEmpty()) {
            List<Long> produtosIds = request.getProdutos().stream()
                .map(PackageRequest.PackageProductRequest::getProdutoId)
                .collect(java.util.stream.Collectors.toList());
            
            produtos = productRepository.findAllById(produtosIds);
            if (produtos.size() != produtosIds.size()) {
                throw new RuntimeException("Um ou mais produtos não foram encontrados");
            }
        }

        Package pacote = new Package(request.getNome(), request.getDescricao(), request.getPreco(), 
            30, produtos); // duração padrão de 30 dias
        pacote.setActive(request.getAtivo() != null ? request.getAtivo() : true);
        
        return packageRepository.save(pacote);
    }

    @Transactional
    public Package updatePackage(Long id, PackageUpdateRequest request) {
        Package pacote = packageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pacote não encontrado"));

        // Atualizar nome se fornecido
        if (request.getNome() != null && !request.getNome().trim().isEmpty()) {
            if (!request.getNome().equals(pacote.getName()) && 
                packageRepository.existsByName(request.getNome())) {
                throw new RuntimeException("Nome de pacote já cadastrado para outro pacote");
            }
            pacote.setName(request.getNome());
        }

        // Atualizar descrição se fornecida
        if (request.getDescricao() != null) {
            pacote.setDescription(request.getDescricao());
        }

        // Atualizar preço se fornecido
        if (request.getPreco() != null) {
            pacote.setPrice(request.getPreco());
        }

        // Atualizar duração se fornecida
        if (request.getDuracaoDias() != null) {
            pacote.setDurationDays(request.getDuracaoDias());
        }

        // Atualizar status ativo se fornecido
        if (request.getAtivo() != null) {
            pacote.setActive(request.getAtivo());
        }

        // Atualizar produtos se fornecido
        if (request.getProdutosIds() != null) {
            List<Product> produtos = productRepository.findAllById(request.getProdutosIds());
            if (produtos.size() != request.getProdutosIds().size()) {
                throw new RuntimeException("Um ou mais produtos não foram encontrados");
            }
            pacote.setProducts(produtos);
        }

        return packageRepository.save(pacote);
    }

    @Transactional
    public Package toggleActive(Long id, Boolean ativo) {
        Package pacote = packageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pacote não encontrado"));
        
        pacote.setActive(ativo);
        return packageRepository.save(pacote);
    }

    @Transactional
    public void deletePackage(Long id) {
        Package pacote = packageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pacote não encontrado"));
        
        packageRepository.delete(pacote);
    }

    public List<Package> findActivePackages() {
        return packageRepository.findActivePackages();
    }
}
