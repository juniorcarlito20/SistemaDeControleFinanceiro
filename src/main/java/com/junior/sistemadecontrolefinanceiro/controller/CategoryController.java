package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.CategoryRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.CategoryResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import com.junior.sistemadecontrolefinanceiro.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public CategoryController(
            CategoryService categoryService,
            UserRepository userRepository) {

        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                categoryService.createCategory(dto, userLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                categoryService.getCategoryById(id, userLogado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                categoryService.getAllCategories(userLogado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                categoryService.updateCategory(id, dto, userLogado.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        categoryService.deleteCategory(id, userLogado.getId());

        return ResponseEntity.noContent().build();
    }
}