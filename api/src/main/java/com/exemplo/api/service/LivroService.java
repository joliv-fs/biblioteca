package com.exemplo.api.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemplo.api.dto.LivroRequestDTO;
import com.exemplo.api.dto.LivroResponseDTO;
import com.exemplo.api.exception.RecursoNaoEncontradoException;
import com.exemplo.api.model.Autor;
import com.exemplo.api.model.Genero;
import com.exemplo.api.model.Livro;
import com.exemplo.api.repository.LivroRepository;

/**
 * Regras de negócio de Livro.
 * Usa o AutorService (para o autor) e, a partir do M3, o GeneroService (para os
 * gêneros).
 */
@Service
@Transactional(readOnly = true)
public class LivroService {

    private final LivroRepository repository;
    private final AutorService autorService;
    private final GeneroService generoService; // NOVO NO M3

    // NOVO NO M3: o construtor recebe também o GeneroService. O Spring entrega os
    // três prontos.
    public LivroService(LivroRepository repository, AutorService autorService, GeneroService generoService) {
        this.repository = repository;
        this.autorService = autorService;
        this.generoService = generoService;
    }

    // Lista os livros com autor e gêneros (o @EntityGraph traz tudo numa consulta).
    public List<LivroResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(LivroResponseDTO::from)
                .toList();
    }

    // Busca um livro (404 se não existir).
    public LivroResponseDTO buscarPorId(Long id) {
        Livro livro = buscarEntidade(id);
        return LivroResponseDTO.from(livro);
    }

    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        Livro livro = new Livro();
        preencher(livro, dto); // 404 se o autor ou algum gênero não existir
        Livro salvo = repository.save(livro); // grava o livro E as linhas de livro_genero
        return LivroResponseDTO.from(salvo);
    }

    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = buscarEntidade(id);
        preencher(livro, dto); // troca o autor e a lista de gêneros
        Livro salvo = repository.save(livro); // o Hibernate atualiza livro_genero sozinho
        return LivroResponseDTO.from(salvo);
    }

    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarEntidade(id);
        // Ao excluir o livro, o Hibernate apaga antes as linhas dele em livro_genero.
        // Os GÊNEROS continuam existindo: só a ligação com este livro some.
        repository.delete(livro);
    }

    public Livro buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: id " + id));
    }

    private void preencher(Livro livro, LivroRequestDTO dto) {
        livro.setTitulo(dto.titulo());
        livro.setAnoPublicacao(dto.anoPublicacao());

        // 1:N – autorId (um número) → Autor (404 se não existir)
        Autor autor = autorService.buscarEntidade(dto.autorId());
        livro.setAutor(autor);

        // NOVO NO M3
        // N:N – generoIds (vários números) → Set<Genero> (404 se algum não existir)
        // Se generoIds vier vazio ou não vier, o livro fica sem gêneros.
        Set<Genero> generos = generoService.buscarEntidades(dto.generoIds());
        livro.setGeneros(generos);
    }
}