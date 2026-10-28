package com.exemplo.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemplo.api.dto.LivroRequestDTO;
import com.exemplo.api.dto.LivroResponseDTO;
import com.exemplo.api.exception.RecursoNaoEncontradoException;
import com.exemplo.api.model.Autor;
import com.exemplo.api.model.Livro;
import com.exemplo.api.repository.LivroRepository;

/**
 * Regras de negócio de Livro.
 *
 * Mesmo molde do AutorService:
 *   - @Transactional(readOnly = true) na classe: por padrão, os métodos só leem.
 *   - @Transactional nos métodos que escrevem (criar, atualizar, excluir).
 */
@Service
@Transactional(readOnly = true)
public class LivroService {

    // Repository de Livro: para ler e gravar livros.
    private final LivroRepository repository;

    // Service de Autor: para buscar o autor do livro pelo id (com 404 se não existir).
    // Repare: usamos o SERVICE do autor, e não o AutorRepository direto.
    // Assim a regra "autor não encontrado = 404" fica escrita num lugar só.
    private final AutorService autorService;

    // O Spring entrega os dois prontos.
    public LivroService(LivroRepository repository, AutorService autorService) {
        this.repository = repository;
        this.autorService = autorService;
    }

    /**
     * Lista todos os livros, cada um com o seu autor.
     *
     * O findAll() é o do LivroRepository, com @EntityGraph: traz livros e autores numa consulta só.
     * A conversão para DTO acontece AQUI DENTRO, enquanto a transação está aberta.
     */
    public List<LivroResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(LivroResponseDTO::from)
                .toList();
    }

    /**
     * Busca um livro pelo id (404 se não existir).
     * O livro.getAutor() feito dentro do from() busca o autor no banco nessa hora (LAZY),
     * e funciona porque ainda estamos dentro da transação.
     */
    public LivroResponseDTO buscarPorId(Long id) {
        Livro livro = buscarEntidade(id);
        return LivroResponseDTO.from(livro);
    }

    /**
     * Cadastra um livro novo.
     */
    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        Livro livro = new Livro();
        preencher(livro, dto);                 // pode lançar 404 se o autor não existir
        Livro salvo = repository.save(livro);  // INSERT; o banco gera o id
        return LivroResponseDTO.from(salvo);   // converte depois de salvar, já com o id
    }

    /**
     * Atualiza um livro que já existe (inclusive trocando o autor, se vier outro autorId).
     */
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = buscarEntidade(id);      // 404 se o livro não existir
        preencher(livro, dto);                 // 404 se o autor não existir
        Livro salvo = repository.save(livro);  // UPDATE
        return LivroResponseDTO.from(salvo);
    }

    /**
     * Exclui um livro. O autor dele NÃO é apagado: só o livro.
     */
    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarEntidade(id);      // 404 se não existir
        repository.delete(livro);
    }

    /**
     * Busca a ENTIDADE Livro pelo id ou lança 404.
     * Mesmo padrão do AutorService.buscarEntidade.
     */
    public Livro buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: id " + id));
    }

    /**
     * Copia os dados do DTO para a entidade.
     *
     * A parte nova está na última linha: o DTO traz só o autorId (um número),
     * mas a entidade precisa do objeto Autor. Quem faz essa "tradução" é o AutorService.
     */
    private void preencher(Livro livro, LivroRequestDTO dto) {
        livro.setTitulo(dto.titulo());
        livro.setAnoPublicacao(dto.anoPublicacao());

        // autorId (número) → Autor (entidade). Se o id não existir: 404 "Autor não encontrado".
        Autor autor = autorService.buscarEntidade(dto.autorId());
        livro.setAutor(autor);
    }
}