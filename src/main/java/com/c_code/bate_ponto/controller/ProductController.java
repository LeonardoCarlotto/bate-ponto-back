package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.*;
import com.c_code.bate_ponto.dto.response.*;
import com.c_code.bate_ponto.service.ProductService;
import com.c_code.bate_ponto.service.user.UserDetailsImpl;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/produtos")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> listProducts(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer limite) {
        return productService.getProducts(categoria, pagina, limite);
    }

    @GetMapping("/{produtoId}")
    public ProductResponse getProduct(@PathVariable Long produtoId,
                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.getProductById(produtoId);
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request,
                                         @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.createProduct(request);
    }

    @PutMapping("/{produtoId}")
    public ProductResponse updateProduct(@PathVariable Long produtoId,
                                         @RequestBody ProductUpdateRequest request,
                                         @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.updateProduct(produtoId, request);
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long produtoId,
                                                             @AuthenticationPrincipal UserDetailsImpl user) {
        Map<String, String> response = productService.deleteProduct(produtoId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{produtoId}/estoque")
    public ProductResponse updateStock(@PathVariable Long produtoId,
                                      @RequestBody StockUpdateRequest request,
                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.updateStock(produtoId, request);
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProductResponse> getProductsByCategory(@PathVariable String categoria,
                                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.getProductsByCategory(categoria);
    }

    @GetMapping("/{produtoId}/imagens")
    public List<ProductImageResponse> getProductImages(@PathVariable Long produtoId,
                                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.getProductImages(produtoId);
    }

    @PostMapping("/{produtoId}/imagens")
    public ProductImageResponse addProductImage(@PathVariable Long produtoId,
                                                @RequestBody Map<String, String> request,
                                                @AuthenticationPrincipal UserDetailsImpl user) {
        String imageUrl = request.get("url");
        return productService.addProductImage(produtoId, imageUrl);
    }

    @DeleteMapping("/{produtoId}/imagens/{imagemId}")
    public ResponseEntity<Map<String, String>> removeProductImage(@PathVariable Long produtoId,
                                                                 @PathVariable Long imagemId,
                                                                 @AuthenticationPrincipal UserDetailsImpl user) {
        Map<String, String> response = productService.removeProductImage(produtoId, imagemId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{produtoId}/variacoes")
    public List<ProductVariationResponse> getProductVariations(@PathVariable Long produtoId,
                                                              @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.getProductVariations(produtoId);
    }

    @PostMapping("/{produtoId}/variacoes")
    public ProductVariationResponse createProductVariation(@PathVariable Long produtoId,
                                                          @RequestBody VariationRequest request,
                                                          @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.createProductVariation(produtoId, request);
    }

    @PutMapping("/{produtoId}/variacoes/{variacaoId}")
    public ProductVariationResponse updateProductVariation(@PathVariable Long produtoId,
                                                          @PathVariable Long variacaoId,
                                                          @RequestBody VariationRequest request,
                                                          @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.updateProductVariation(produtoId, variacaoId, request);
    }

    @DeleteMapping("/{produtoId}/variacoes/{variacaoId}")
    public ResponseEntity<Map<String, String>> deleteProductVariation(@PathVariable Long produtoId,
                                                                      @PathVariable Long variacaoId,
                                                                      @AuthenticationPrincipal UserDetailsImpl user) {
        Map<String, String> response = productService.deleteProductVariation(produtoId, variacaoId);
        return ResponseEntity.ok(response);
    }
}
