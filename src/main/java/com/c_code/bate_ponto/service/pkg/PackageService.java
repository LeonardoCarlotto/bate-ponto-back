package com.c_code.bate_ponto.service.pkg;

import com.c_code.bate_ponto.dto.request.PackageRequest;
import com.c_code.bate_ponto.dto.request.PackageUpdateRequest;
import com.c_code.bate_ponto.model.Package;
import com.c_code.bate_ponto.model.Product;
import com.c_code.bate_ponto.model.ServiceItem;
import com.c_code.bate_ponto.repository.PackageRepository;
import com.c_code.bate_ponto.repository.ProductRepository;
import com.c_code.bate_ponto.repository.ServiceRepository;
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
    private final ServiceRepository serviceRepository;

    public PackageService(PackageRepository packageRepository, ProductRepository productRepository, ServiceRepository serviceRepository) {
        this.packageRepository = packageRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
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

        // Buscar produtos (apenas itens do tipo "produto")
        List<Product> produtos = new ArrayList<>();
        List<ServiceItem> servicos = new ArrayList<>();
        
        if (request.getItens() != null && !request.getItens().isEmpty()) {
            // Processar produtos
            List<Long> produtosIds = request.getItens().stream()
                .filter(item -> "produto".equals(item.getTipo()) && item.getItemId() != null)
                .map(PackageRequest.PackageItemRequest::getItemId)
                .collect(java.util.stream.Collectors.toList());
            
            if (!produtosIds.isEmpty()) {
                produtos = productRepository.findAllById(produtosIds);
                if (produtos.size() != produtosIds.size()) {
                    throw new RuntimeException("Um ou mais produtos não foram encontrados");
                }
            }

            // Processar serviços (criar ou buscar serviços existentes)
            for (PackageRequest.PackageItemRequest item : request.getItens()) {
                if ("servico".equals(item.getTipo()) && item.getNome() != null) {
                    // Verificar se serviço já existe pelo nome
                    ServiceItem servico = serviceRepository.findByName(item.getNome())
                        .orElse(new ServiceItem(item.getNome(), item.getDescricao(), item.getPreco() != null ? item.getPreco() : 0.0));
                    servicos.add(servico);
                }
            }
        }

        // Usar precoPersonalizado se fornecido, senão usa preco
        Double precoFinal = request.getPrecoPersonalizado() != null ? 
            request.getPrecoPersonalizado() : request.getPreco();

        Package pacote = new Package(request.getNome(), request.getDescricao(), precoFinal, 
            30, produtos); // duração padrão de 30 dias
        pacote.setServices(servicos);
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

        // Atualizar produtos e serviços se fornecidos
        if (request.getItens() != null) {
            List<Product> produtos = new ArrayList<>();
            List<ServiceItem> servicos = new ArrayList<>();
            
            // Processar produtos
            List<Long> produtosIds = request.getItens().stream()
                .filter(item -> "produto".equals(item.getTipo()) && item.getItemId() != null)
                .map(PackageRequest.PackageItemRequest::getItemId)
                .collect(java.util.stream.Collectors.toList());
            
            if (!produtosIds.isEmpty()) {
                produtos = productRepository.findAllById(produtosIds);
                if (produtos.size() != produtosIds.size()) {
                    throw new RuntimeException("Um ou mais produtos não foram encontrados");
                }
            }

            // Processar serviços (criar ou buscar/atualizar serviços existentes)
            for (PackageRequest.PackageItemRequest item : request.getItens()) {
                if ("servico".equals(item.getTipo()) && item.getNome() != null) {
                    // Verificar se serviço já existe pelo nome
                    ServiceItem servico = serviceRepository.findByName(item.getNome())
                        .orElse(new ServiceItem(item.getNome(), item.getDescricao(), item.getPreco() != null ? item.getPreco() : 0.0));
                    
                    // Atualizar preço e descrição do serviço se fornecidos
                    if (item.getPreco() != null) {
                        servico.setPrice(item.getPreco());
                    }
                    if (item.getDescricao() != null) {
                        servico.setDescription(item.getDescricao());
                    }
                    
                    servicos.add(servico);
                }
            }
            
            // Salvar serviços novos/atualizados
            if (!servicos.isEmpty()) {
                servicos = serviceRepository.saveAll(servicos);
            }
            
            // Atualizar produtos e serviços do pacote
            pacote.setProducts(produtos);
            pacote.setServices(servicos);
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
