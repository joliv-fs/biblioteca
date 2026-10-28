package com.exemplo.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.exemplo.api.model.Livro;

/**
 * Acesso ao banco para a entidade Livro.
 * JpaRepository<Livro, Long> → cuida de Livro, cujo id é Long.
 * Traz os mesmos métodos prontos do AutorRepository (findAll, findById, save, delete...).
 */
public interface LivroRepository extends JpaRepository<Livro, Long> {

    /**
     * Sobrescreve o findAll() padrão para trazer cada livro JÁ COM o autor.
     *
     * O problema que isso resolve (chamado de "N+1"):
     * como o autor é LAZY, listar 10 livros e depois pedir o autor de cada um faria
     * 1 consulta para os livros + 10 consultas, uma para cada autor = 11 consultas.
     *
     * @EntityGraph(attributePaths = {"autor"}) → "quando buscar os livros, traga o autor junto".
     * Resultado: UMA consulta só, com JOIN entre LIVRO e AUTOR.
     *
     * @Override → avisa que este método substitui o findAll() que veio do JpaRepository.
     */
    @Override
    @EntityGraph(attributePaths = {"autor"})
    List<Livro> findAll();
}