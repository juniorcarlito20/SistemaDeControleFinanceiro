package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.UserRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.UserResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo para criar usuario
    public UserResponseDTO createUser(UserRequestDTO dto) {

        // VALIDAÇÃO: Verifica se o e-mail já está cadastrado usando findByEmail
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outro usuário.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Criptografia da senha
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    // Metodo para listar todos os usuarios
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToResponseDTO) // Reaproveita o metodo de conversão (incluindo o ID)
                .toList();
    }

    // Metodo para buscar usuario por ID
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        return convertToResponseDTO(user);
    }

    // Metodo para atualizar usuario
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        // VALIDAÇÃO: Verifica se o novo e-mail já pertence a OUTRO usuário
        userRepository.findByEmail(dto.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new IllegalArgumentException("Este e-mail já está em uso por outro usuário.");
            }
        });

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Criptografia corrigida na atualização

        User updated = userRepository.save(user);
        return convertToResponseDTO(updated);
    }

    // Metodo para deletar usuario
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        userRepository.delete(user);
    }
    // Metodo auxiliar para converter entidade em DTO
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public UserResponseDTO findByEmail(String name) {
            return null;
    }
}