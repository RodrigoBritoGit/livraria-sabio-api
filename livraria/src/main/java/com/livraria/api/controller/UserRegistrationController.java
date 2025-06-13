package com.livraria.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.livraria.application.dto.UserResponseDTO;
import com.livraria.application.service.UserRegistrationService;
import com.livraria.domain.model.Usuario;

@RestController
@RequestMapping("/auth")
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService authService;

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody Usuario usuario) {
        return authService.register(usuario);
    }
}
