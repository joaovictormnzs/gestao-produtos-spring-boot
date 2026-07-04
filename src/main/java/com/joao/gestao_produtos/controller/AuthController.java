package com.joao.gestao_produtos.controller;

import com.joao.gestao_produtos.dto.AuthDTO;
import com.joao.gestao_produtos.model.Usuario;
import com.joao.gestao_produtos.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.Response> login(@RequestBody @Valid AuthDTO.Request dados) {
        // Cria o token de credenciais que o Spring Security exige
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());

        // O Spring Security valida se o e-mail existe e se a senha bate
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Se deu certo, extrai o usuário e gera o Token JWT real
        String token = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // Retorna a instância interna estática
        return ResponseEntity.ok(new AuthDTO.Response(token));
    }
}
