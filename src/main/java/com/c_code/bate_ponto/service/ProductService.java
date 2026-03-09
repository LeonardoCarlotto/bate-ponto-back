package com.c_code.bate_ponto.service;

import com.c_code.bate_ponto.dto.request.*;
import com.c_code.bate_ponto.dto.response.*;
import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductVariationRepository productVariationRepository;

    // === PRODUTOS ===

    public List<ProductResponse> getProducts(String categoria, Integer pagina, Integer limite) {
        List<Product> products;
        
        if (categoria != null && !categoria.trim().isEmpty()) {
            products = productRepository.findByCategoryName(categoria);
        } else {
            products = productRepository.findAll();
        }
        
        // Aplicar paginação se necessário
        if (pagina != null && limite != null) {
            int start = (pagina - 1) * limite;
            int end = Math.min(start + limite, products.size());
            if (start < products.size()) {
                products = products.subList(start, end);
            } else {
                products = List.of();
            }
        }
        
        return products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return convertToProductResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findByName(request.getCategoria())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + request.getCategoria()));

        Product product = new Product();
        product.setName(request.getNome());
        product.setDescription(request.getDescricao());
        product.setPrice(request.getPreco());
        product.setStock(request.getEstoque());
        product.setCategory(category);
        
        // Definir status: usa request.getStatus() ou inativa se estoque zerado, senão ativa
        if (request.getStatus() != null) {
            product.setActive(request.getStatus());
        } else if (request.getEstoque() != null && request.getEstoque() == 0) {
            product.setActive(false);
        } else {
            product.setActive(true);
        }

        product = productRepository.save(product);
        return convertToProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (request.getPreco() != null) {
            product.setPrice(request.getPreco());
        }
        if (request.getDescricao() != null) {
            product.setDescription(request.getDescricao());
        }
        if (request.getEstoque() != null) {
            product.setStock(request.getEstoque());
        }
        if (request.getStatus() != null) {
            product.setActive(request.getStatus());
        }
        
        // Inativar produto automaticamente se estoque ficar zerado
        if (product.getStock() != null && product.getStock() == 0) {
            product.setActive(false);
        }

        product = productRepository.save(product);
        return convertToProductResponse(product);
    }

    public Map<String, String> deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        productRepository.delete(product);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Produto deletado com sucesso");
        return response;
    }

    public ProductResponse updateStock(Long id, StockUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setStock(request.getQuantidade());
        
        // Inativar produto automaticamente se estoque ficar zerado
        if (product.getStock() != null && product.getStock() == 0) {
            product.setActive(false);
        }
        
        product = productRepository.save(product);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getActive()
        );
    }

    public List<ProductResponse> getProductsByCategory(String categoria) {
        List<Product> products = productRepository.findByCategoryName(categoria);

        return products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    // === CATEGORIAS ===

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getNome())) {
            throw new RuntimeException("Categoria já existe: " + request.getNome());
        }

        Category category = new Category();
        category.setName(request.getNome());
        category.setDescription(request.getDescricao());

        category = categoryRepository.save(category);
        return convertToCategoryResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        category.setName(request.getNome());
        category.setDescription(request.getDescricao());

        category = categoryRepository.save(category);
        return convertToCategoryResponse(category);
    }

    public Map<String, String> deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoryRepository.delete(category);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Categoria deletada com sucesso");
        return response;
    }

    // === IMAGENS ===

    public List<ProductImageResponse> getProductImages(Long productId) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderByOrderAsc(productId);
        
        return images.stream()
                .map(this::convertToProductImageResponse)
                .collect(Collectors.toList());
    }

    public ProductImageResponse addProductImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(imageUrl);
        image.setPrincipal(false);
        image.setOrder(productImageRepository.findByProductId(productId).size());

        image = productImageRepository.save(image);
        return convertToProductImageResponse(image);
    }

    public Map<String, String> removeProductImage(Long productId, Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));

        if (!image.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Imagem não pertence a este produto");
        }

        productImageRepository.delete(image);

        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Imagem removida com sucesso");
        return response;
    }

    // === VARIAÇÕES ===

    public List<ProductVariationResponse> getProductVariations(Long productId) {
        List<ProductVariation> variations = productVariationRepository.findByProductId(productId);
        
        return variations.stream()
                .map(this::convertToProductVariationResponse)
                .collect(Collectors.toList());
    }

    public ProductVariationResponse createProductVariation(Long productId, VariationRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ProductVariation variation = new ProductVariation();
        variation.setProduct(product);
        variation.setName(request.getNome());
        variation.setValues(request.getValores());

        variation = productVariationRepository.save(variation);
        return convertToProductVariationResponse(variation);
    }

    public ProductVariationResponse updateProductVariation(Long productId, Long variationId, VariationRequest request) {
        ProductVariation variation = productVariationRepository.findById(variationId)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada"));

        if (!variation.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Variação não pertence a este produto");
        }

        variation.setValues(request.getValores());
        variation = productVariationRepository.save(variation);

        return convertToProductVariationResponse(variation);
    }

    public Map<String, String> deleteProductVariation(Long productId, Long variationId) {
        ProductVariation variation = productVariationRepository.findById(variationId)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada"));

        if (!variation.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Variação não pertence a este produto");
        }

        productVariationRepository.delete(variation);

        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Variação deletada com sucesso");
        return response;
    }

    // === CONVERSÕES ===

    private ProductResponse convertToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getActive()
        );
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    private ProductImageResponse convertToProductImageResponse(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getUrl(),
                image.getPrincipal(),
                image.getOrder()
        );
    }

    private ProductVariationResponse convertToProductVariationResponse(ProductVariation variation) {
        return new ProductVariationResponse(
                variation.getId(),
                variation.getName(),
                variation.getValues()
        );
    }
}
