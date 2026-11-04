package com.exemplo.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.exemplo.api.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    /**
     * Lista os livros JÁ COM o autor e os gêneros, numa consulta só (evita o
     * problema N+1).
     *
     * NOVO NO M3: "generos" entrou na lista do attributePaths.
     * Sem isso, listar 10 livros faria mais 10 consultas, uma para os gêneros de
     * cada livro.
     */
    @Override
    @EntityGraph(attributePaths = { "autor", "generos" })
    List<Livro> findAll();
}