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
        list<Produto> produtos = produtoRepository.findAll();

        // Retorna a lista de produtos com o status HTTP 200 (OK)
        return ResponseEntity.ok(produtos);
    }

}
