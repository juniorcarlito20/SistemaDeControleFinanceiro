package com.junior.sistemadecontrolefinanceiro.service;

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
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // metodo para listar todos os usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Metodo para buscar usuario por Id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

     //Metodo para atualizar usuario
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            return userRepository.save(user);
        }
        return null;
    }

     //Metodo para deletar usuario
     public void deleteUser(Long id) {
         userRepository.deleteById(id);
     }
}
