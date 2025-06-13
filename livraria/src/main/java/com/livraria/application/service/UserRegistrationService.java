package com.livraria.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.livraria.application.dto.UserResponseDTO;
import com.livraria.domain.model.Usuario;
import com.livraria.infrastructure.repository.UserRepository;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponseDTO register(Usuario usuario) {
        // Verifica se o usuário já existe
        Optional<Usuario> existente = userRepository.findByUsername(usuario.getUsername());
        if (existente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já existe");
        }

        // Criptografa a senha
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Salva o usuário no banco de dados
        Usuario savedUsuario = userRepository.save(usuario);

        // Retorna resposta contendo apenas id e username
        return new UserResponseDTO(savedUsuario.getId(), savedUsuario.getUsername());
    }
}
