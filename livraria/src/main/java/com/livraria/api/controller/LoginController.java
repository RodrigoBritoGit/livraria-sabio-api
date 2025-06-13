package com.livraria.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.livraria.application.dto.LoginRequestDTO;
import com.livraria.domain.model.Usuario;
import com.livraria.infrastructure.repository.UserRepository;
import com.livraria.infrastructure.security.JwtService;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository usuarioRepository;

	@PostMapping
	public String login(@RequestBody LoginRequestDTO loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		// Verifica se o usuário existe
		Usuario usuario = usuarioRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		// Autenticando
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Gera o JWT
		return jwtService.gerarToken(usuario.getId(), usuario.getUsername());
	}
}
