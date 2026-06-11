package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.CategoryRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.CategoryResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Category;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.CategoryRepository;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }


    // METODO: CRIAR CATEGORIA

    public CategoryResponseDTO createCategory(CategoryRequestDTO dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Category category = new Category();
        category.setName(dto.getName());
        category.setUser(user);

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }


    // METODO: BUSCAR CATEGORIA POR ID

    public CategoryResponseDTO getCategoryById(Long id, Long userId) {

        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada ou acesso negado"));

        return convertToDTO(category);
    }


    // METODO: LISTAR CATEGORIAS DO USUARIO LOGADO

    public List<CategoryResponseDTO> getAllCategories(Long userId) {

        List<Category> categories = categoryRepository.findByUserId(userId);

        return categories.stream()
                .map(this::convertToDTO)
                .toList();
    }


    // METODO: ATUALIZAR CATEGORIA

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto, Long userId) {

        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada ou acesso negado"));

        category.setName(dto.getName());

        Category updated = categoryRepository.save(category);
        return convertToDTO(updated);
    }


    // METODO: DELETAR CATEGORIA

    public void deleteCategory(Long id, Long userId) {

        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada ou acesso negado"));

        categoryRepository.delete(category);
    }

    // METODO AUXILIAR: CONVERTER PARA DTO

    private CategoryResponseDTO convertToDTO(Category category) {
        // Passamos os dois argumentos esperados (id e name) direto no construtor
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

}