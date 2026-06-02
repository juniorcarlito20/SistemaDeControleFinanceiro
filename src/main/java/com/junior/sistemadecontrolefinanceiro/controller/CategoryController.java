package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.CategoryRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.CategoryResponseDTO;
import com.junior.sistemadecontrolefinanceiro.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Criar categoria
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO createCategory(
            @RequestBody CategoryRequestDTO dto) {

        return categoryService.createCategory(dto);
    }

    // Listar todas as categorias
    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Buscar categoria por ID
    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(
            @PathVariable Long id) {

        return categoryService.getCategoryById(id);
    }

    // Atualizar categoria
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto) {

        return categoryService.updateCategory(id, dto);
    }

    // Deletar categoria
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);
    }
}
