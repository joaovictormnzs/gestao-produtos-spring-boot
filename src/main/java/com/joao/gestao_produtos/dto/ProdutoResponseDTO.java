package com.joao.gestao_produtos.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonPropertyOrder({ "id", "nome", "descricao", "preco", "quantidadeEstoque", "usuarioId", "usuarioNome"})
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private Long usuarioId;
    private String usuarioNome;
}
