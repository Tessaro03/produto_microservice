package com.produto.infra.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.produto.infra.validation.ValidacaoException;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class TokenService {
    
    
    @Value("${api.security.token.secret}")
    private String secret;
    
    public DecodedJWT decodificadorToken(String tokenJWT) {
        try {
        var algoritmo = Algorithm.HMAC256(secret);
        var verify = JWT.require(algoritmo)
        .withIssuer("FoodMicroservice")
            .build()
            .verify(tokenJWT);
        return verify;
        } catch (JWTVerificationException exception) {
            throw new ValidacaoException("Token JWT invalido ou expirado!");
        }
    }


     public String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
         return authorizationHeader.replace("Bearer ", "");
        } 
        throw new ValidacaoException("Token necessário");
        
    }

    public UsuarioDTO extrairInformacoes(HttpServletRequest request) {
        var token = recuperarToken(request);
        DecodedJWT decodedJWT = decodificadorToken(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String email = decodedJWT.getClaim("email").asString();
        String tipo = decodedJWT.getClaim("tipo").asString();
        String username = decodedJWT.getSubject();
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, username, email, tipo);
        return usuarioDTO;
    }


    public GrantedAuthority getAuthorities(String tokenJWT) {
        DecodedJWT decodedJWT = decodificadorToken(tokenJWT);
        String role = decodedJWT.getClaim("roles").asString();
        SimpleGrantedAuthority authority = new  SimpleGrantedAuthority(role);
        return authority;
    }
}
