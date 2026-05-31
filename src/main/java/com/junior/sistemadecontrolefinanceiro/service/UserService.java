package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.UserRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.UserResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Metodo para criar os usuarios
    public UserResponseDTO createUser(UserRequestDTO dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        User savedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return response;
    }

    // metodo para listar todos os usuarios
    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            return dto;
        }).toList();
    }

    //Metodo para buscar usuario por Id
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;


    }

     //Metodo para atualizar usuario
     public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

         User user = userRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("User not found"));

         user.setName(dto.getName());
         user.setEmail(dto.getEmail());
         user.setPassword(dto.getPassword());

         User updated = userRepository.save(user);

         UserResponseDTO response = new UserResponseDTO();
         response.setId(updated.getId());
         response.setName(updated.getName());
         response.setEmail(updated.getEmail());

         return response;
     }

     //Metodo para deletar usuario
     public void deleteUser(Long id) {

         User user = userRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("User not found"));

         userRepository.delete(user);
     }

}
