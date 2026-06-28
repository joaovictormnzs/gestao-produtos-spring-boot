package com.joao.gestao_produtos.repository;

import com.joao.gestao_produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica ao spring que essa interface é um componente de acesso ao banco de dados.
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
}
