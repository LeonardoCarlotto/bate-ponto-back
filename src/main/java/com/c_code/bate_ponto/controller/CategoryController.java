package com.c_code.bate_ponto.controller;

import com.c_code.bate_ponto.dto.request.CategoryRequest;
import com.c_code.bate_ponto.dto.response.CategoryResponse;
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
@RequestMapping("/categorias")
public class CategoryController {

    private final ProductService productService;

    @GetMapping
    public List<CategoryResponse> getCategories(@AuthenticationPrincipal UserDetailsImpl user) {
        return productService.getCategories();
    }

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest request,
                                          @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.createCategory(request);
    }

    @PutMapping("/{categoriaId}")
    public CategoryResponse updateCategory(@PathVariable Long categoriaId,
                                          @RequestBody CategoryRequest request,
                                          @AuthenticationPrincipal UserDetailsImpl user) {
        return productService.updateCategory(categoriaId, request);
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long categoriaId,
                                                              @AuthenticationPrincipal UserDetailsImpl user) {
        Map<String, String> response = productService.deleteCategory(categoriaId);
        return ResponseEntity.ok(response);
    }
}
