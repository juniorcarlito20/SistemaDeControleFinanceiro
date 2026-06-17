package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.UserRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.UserResponseDTO;
import com.junior.sistemadecontrolefinanceiro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔓 Cadastro público (registro)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(userService.createUser(dto));
    }

    // 🔒 Apenas ADMIN vê todos os usuários
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // 🔒 Apenas ADMIN busca por ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 🔒 Apenas ADMIN pode atualizar usuários
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO updatedUser) {

        return userService.updateUser(id, updatedUser);
    }

    // 🔒 Apenas ADMIN pode deletar usuários
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // 👤 Usuário logado vê apenas ele mesmo
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me")
    public UserResponseDTO getMe(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }
}