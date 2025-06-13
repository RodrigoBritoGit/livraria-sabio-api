package com.livraria.infrastructure.security;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.livraria.application.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final String SECRET_KEY = "segredo_jwt_1234567890_super_secreta_com_mais_de_256_bits_abcdef123456";

	private final CustomUserDetailsService userDetailsService;

	public JwtRequestFilter(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String jwtToken = getJwtFromRequest(request);

		if (jwtToken != null && validateToken(jwtToken)) {
			String username = getUsernameFromToken(jwtToken);

			// Carrega o usuário completo
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// Continua o fluxo da requisição
		chain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		var authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.replace("Bearer ", "");
		}
		return null;
	}

	private boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(SECRET_KEY.getBytes())
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			System.out.println("Token inválido ou expirado: " + e.getMessage());
			return false;
		}
	}

	private String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(SECRET_KEY.getBytes())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
}
