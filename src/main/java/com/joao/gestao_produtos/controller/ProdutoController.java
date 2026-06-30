package com.joao.gestao_produtos.controller;

import com.joao.gestao_produtos.model.Produto;
import com.joao.gestao_produtos.repository.ProdutoRepository;
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

    // Rota 1: Cadastrar um produto.
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        // @RequestBody captura o JSON enviado pelo Postman/React e o transforma em um objeto Produto
        Produto novoProduto = produtoRepository.save(produto);

        // Retorna o produto criado com o status HTTP 211 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    // Rota 2: Listar todos os produtos.
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();

        // Retorna a lista de produtos com o status HTTP 200 (OK)
        return ResponseEntity.ok(produtos);
    }

    //Rota 3: Atualizar produto.
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        // @PathVariable captura o ID da URL. findById busca o produto no banco.
        return produtoRepository.findById(id)
                .map(produtoExistente -> {

                    // Atualiza os campos do produto antigo com os novos dados recebidos
                    produtoExistente.setNome(produtoAtualizado.getNome());
                    produtoExistente.setDescricao(produtoAtualizado.getDescricao());
                    produtoExistente.setPreco(produtoAtualizado.getPreco());
                    produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());

                    // Salva as alterações e retorna 200 OK com o produto atualizado
                    Produto salvo = produtoRepository.save(produtoExistente);
                    return ResponseEntity.ok(salvo);
                })

                // Se o Id nao existe no banco, returna 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!produtoRepository.existsById(id)) { // existsById checa se o produto realmente existe no banco antes de tentar deletar
            return ResponseEntity.notFound().build(); // Retorna 404 se não encontrar
        }

        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna status 204 No Content (Deletado com sucesso)
    }
}
