package com.joao.gestao_produtos.controller;

import com.joao.gestao_produtos.dto.ProdutoCreateDTO;
import com.joao.gestao_produtos.dto.ProdutoResponseDTO;
import com.joao.gestao_produtos.dto.ProdutoUpdateDTO;
import com.joao.gestao_produtos.model.Produto;
import com.joao.gestao_produtos.model.Usuario;
import com.joao.gestao_produtos.repository.ProdutoRepository;
import com.joao.gestao_produtos.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Define que esta classe é um controlador do tipo REST (retorna JSON).
@RequestMapping("/api/produtos") // Define a url base para acessar as rotas.
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Rota 1: Criar um produto.
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> criar(@PathVariable Long usuarioId, @Valid @RequestBody ProdutoCreateDTO dto) {

        return usuarioRepository.findById(usuarioId)
                .map(usuarioCriador -> {
                    Produto produto = new Produto();
                    produto.setNome(dto.getNome());
                    produto.setDescricao(dto.getDescricao());
                    produto.setPreco(dto.getPreco());
                    produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());

                    produto.setUsuario(usuarioCriador);

                    Produto salvo = produtoRepository.save(produto);
                    return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salvo));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Rota 2: Listar todos os produtos.
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProdutoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        // Busca todos os produtos do banco
        List<Produto> produtos = produtoRepository.findAll();

        List<ProdutoResponseDTO> dtos = produtos.stream()
                .filter(produto -> produto.getUsuario() != null && produto.getUsuario().getId().equals(usuarioId))
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // Rota 3: Listar produtos por id
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(produto -> ResponseEntity.ok(converterParaDTO(produto)))
                .orElse(ResponseEntity.notFound().build());
    }

    //Rota 4: Atualizar produto.
    @PutMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @PathVariable Long usuarioId, @Valid @RequestBody ProdutoUpdateDTO dto) {

        return produtoRepository.findById(id)
                .map(produtoExistente -> {
                    // o produto pertence ao utilizador informado?
                    if (!produtoExistente.getUsuario().getId().equals(usuarioId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Retorna 403 Proibido
                    }

                    // Atualiza os campos usando os dados que vieram do DTO
                    produtoExistente.setNome(dto.getNome());
                    produtoExistente.setDescricao(dto.getDescricao());
                    produtoExistente.setPreco(dto.getPreco());
                    produtoExistente.setQuantidadeEstoque(dto.getQuantidadeEstoque());

                    // Salva as alterações
                    Produto salvo = produtoRepository.save(produtoExistente);

                    return ResponseEntity.ok(converterParaDTO(salvo));// Retorna 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 Not Found
    }

    // Rota 5: Deletar produto.
    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<?> deletar(@PathVariable Long id, @PathVariable Long usuarioId) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    // o produto pertence ao utilizador informado?
                    if (!produto.getUsuario().getId().equals(usuarioId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Retorna 403 Proibido
                    }

                    produtoRepository.delete(produto);
                    return ResponseEntity.noContent().build(); // Retorna 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 Not Found
    }

    // Metodo auxiliar.
    private ProdutoResponseDTO converterParaDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());

        if (produto.getUsuario() != null) {
            dto.setUsuarioId(produto.getUsuario().getId());
            dto.setUsuarioNome(produto.getUsuario().getNome());
        }
        return dto;
    }
}
