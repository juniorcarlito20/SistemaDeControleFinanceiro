package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.CategoryRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.CategoryResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Category;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Criar categoria
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {

        Category category = new Category();
        category.setName(dto.getName());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(
                savedCategory.getId(),
                savedCategory.getName()
        );
    }

    // Listar todas as categorias
    public List<CategoryResponseDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponseDTO(
                        category.getId(),
                        category.getName()
                ))
                .collect(Collectors.toList());
    }

    // Buscar categoria por ID
    public CategoryResponseDTO getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoria não encontrada com id: " + id));

        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }

    // Atualizar categoria
    public CategoryResponseDTO updateCategory(Long id,
                                              CategoryRequestDTO dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoria não encontrada com id: " + id));

        category.setName(dto.getName());

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(
                updatedCategory.getId(),
                updatedCategory.getName()
        );
    }

    // Deletar categoria
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Categoria não encontrada com id: " + id));

        categoryRepository.delete(category);
    }
}