package com.livraria.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String SECRET_KEY = "segredo_jwt_1234567890_super_secreta_com_mais_de_256_bits_abcdef123456";

    public String gerarToken(Long id, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);

        // Gera o token JWT com a chave secreta
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Expira em 30 minutos
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}