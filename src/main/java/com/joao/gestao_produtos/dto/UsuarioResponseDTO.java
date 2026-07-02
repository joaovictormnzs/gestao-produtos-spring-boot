package com.joao.gestao_produtos.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "nome", "email" })
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
}
