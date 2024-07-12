package com.produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.produto.model.Categoria;

import jakarta.transaction.Transactional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE c.nome = :nome")
    Boolean existsByCategoria(String nome);

    @Query("SELECT c FROM Categoria c WHERE c.nome = :nome")
    Categoria getReferenceByName(String nome);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM produtos_categorias WHERE categorias_id = :id", nativeQuery = true)
    void deletarRelacaoCategoriaProdutos( Long id);
    
}
