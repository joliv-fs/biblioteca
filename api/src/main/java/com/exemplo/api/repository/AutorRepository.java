package com.exemplo.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemplo.api.model.Autor;

/**
 * Acesso ao banco para a entidade Autor.
 *
 * É só uma INTERFACE, sem nenhum código dentro. O Spring Data cria a implementação
 * sozinho quando a API sobe.
 *
 * JpaRepository<Autor, Long> quer dizer:
 *   Autor → a entidade que este repository cuida
 *   Long  → o tipo do id dessa entidade
 *
 * Métodos que já vêm prontos (e vamos usar no service):
 *   findAll()          → lista todos os autores
 *   findById(id)       → busca um autor pelo id (devolve um Optional: pode ter ou não ter)
 *   save(autor)        → insere (se o id for null) ou atualiza (se o id existir)
 *   delete(autor)      → apaga o autor
 *   existsById(id)     → true se existe um autor com esse id
 */
public interface AutorRepository extends JpaRepository<Autor, Long> {
}