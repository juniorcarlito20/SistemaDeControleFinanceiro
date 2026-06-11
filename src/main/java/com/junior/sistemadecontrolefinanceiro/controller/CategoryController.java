package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.CategoryRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.CategoryResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal User userLogado) {
        return ResponseEntity.ok(categoryService.createCategory(dto, userLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User userLogado) {
        return ResponseEntity.ok(categoryService.getCategoryById(id, userLogado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll(
            @AuthenticationPrincipal User userLogado) {
        return ResponseEntity.ok(categoryService.getAllCategories(userLogado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal User userLogado) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto, userLogado.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User userLogado) {
        categoryService.deleteCategory(id, userLogado.getId());
        return ResponseEntity.noContent().build();
    }
}