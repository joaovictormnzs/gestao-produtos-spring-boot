package com.joao.gestao_produtos.controller;

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

    // Rota 1: Cadastrar um produto.
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Produto> criar(@PathVariable Long usuarioId, @Valid @RequestBody Produto produto) {

        Usuario usuario = usuarioRepository.findById(usuarioId) // Busca o usuário no banco. Se não existir, retorna erro 404.
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        produto.setUsuario(usuario); // Vincula o usuário encontrado ao produto que está sendo criado

        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto); // Retorna o produto criado com o status HTTP 211 (Created)
    }

    // Rota 2: Listar todos os produtos.
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Produto>> listarPorUsuario(@PathVariable Long usuarioId) {

        // Busca todos os produtos e filtra para trazer apenas os que têm o ID do usuário correto.
        List<Produto> produtos = produtoRepository.findAll().stream()
                .filter(p -> p.getUsuario().getId().equals(usuarioId))
                .toList();

        // Retorna a lista de produtos com o status HTTP 200 (OK)
        return ResponseEntity.ok(produtos);
    }

    //Rota 3: Atualizar produto.
    @PutMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @PathVariable Long usuarioId, @Valid @RequestBody Produto produtoAtualizado) {
        // @PathVariable captura o ID da URL. findById busca o produto no banco.
        return produtoRepository.findById(id)
                .map(produtoExistente -> {
                    // Validação de segurança: o produto pertence ao utilizador informado?
                    if (!produtoExistente.getUsuario().getId().equals(usuarioId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Produto>build(); // Retorna 403 Proibido
                    }

                    // Atualiza os campos do produto antigo com os novos dados recebidos
                    produtoExistente.setNome(produtoAtualizado.getNome());
                    produtoExistente.setDescricao(produtoAtualizado.getDescricao());
                    produtoExistente.setPreco(produtoAtualizado.getPreco());
                    produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());

                    // Salva as alterações e retorna 200 OK com o produto atualizado
                    Produto salvo = produtoRepository.save(produtoExistente);
                    return ResponseEntity.ok(salvo); // retorna 200 ok
                })

                // Se o Id nao existe no banco, returna 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @PathVariable Long usuarioId) {
        return produtoRepository.findById(id)
                .map(produtoExistente -> {
                    // Validação de segurança: o produto pertence ao utilizador informado?
                    if (!produtoExistente.getUsuario().getId().equals(usuarioId)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build(); // Retorna 403 Proibido
                    }

                    produtoRepository.delete(produtoExistente);
                    return ResponseEntity.noContent().<Void>build(); // Retorna 204 Sucesso
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
