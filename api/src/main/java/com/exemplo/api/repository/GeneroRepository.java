package com.exemplo.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemplo.api.model.Genero;

/**
 * Acesso ao banco para a entidade Gênero.
 *
 * Além dos métodos de sempre (findAll, findById, save, delete...),
 * vamos usar um que ainda não apareceu:
 *
 * findAllById(ids) → recebe VÁRIOS ids e devolve os gêneros encontrados, numa
 * consulta só.
 * Se algum id não existir, ele simplesmente não vem na lista
 * (não dá erro). Quem confere isso é o GeneroService, na M3.3.
 */
public interface GeneroRepository extends JpaRepository<Genero, Long> {
}