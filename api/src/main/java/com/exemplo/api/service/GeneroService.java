package com.exemplo.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemplo.api.dto.GeneroRequestDTO;
import com.exemplo.api.dto.GeneroResponseDTO;
import com.exemplo.api.exception.RecursoNaoEncontradoException;
import com.exemplo.api.model.Genero;
import com.exemplo.api.repository.GeneroRepository;

/**
 * Regras de negócio de Gênero.
 * Mesmo molde do AutorService, mais o método buscarEntidades (vários ids de uma
 * vez).
 */
@Service
@Transactional(readOnly = true)
public class GeneroService {

    private final GeneroRepository repository;

    public GeneroService(GeneroRepository repository) {
        this.repository = repository;
    }

    // Lista todos os gêneros, convertidos em DTO.
    public List<GeneroResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(GeneroResponseDTO::from)
                .toList();
    }

    // Busca um gênero pelo id (404 se não existir).
    public GeneroResponseDTO buscarPorId(Long id) {
        Genero genero = buscarEntidade(id);
        return GeneroResponseDTO.from(genero);
    }

    // Cadastra um gênero. Se o nome já existir, o banco recusa → 409.
    @Transactional
    public GeneroResponseDTO criar(GeneroRequestDTO dto) {
        Genero genero = new Genero();
        preencher(genero, dto);
        Genero salvo = repository.save(genero);
        return GeneroResponseDTO.from(salvo);
    }

    // Atualiza um gênero (404 se não existir; 409 se o nome novo já for de outro
    // gênero).
    @Transactional
    public GeneroResponseDTO atualizar(Long id, GeneroRequestDTO dto) {
        Genero genero = buscarEntidade(id);
        preencher(genero, dto);
        Genero salvo = repository.save(genero);
        return GeneroResponseDTO.from(salvo);
    }

    // Exclui um gênero (404 se não existir; 409 se algum livro usar este gênero).
    @Transactional
    public void excluir(Long id) {
        Genero genero = buscarEntidade(id);
        repository.delete(genero);
    }

    // Busca UM gênero pelo id ou lança 404.
    public Genero buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Gênero não encontrado: id " + id));
    }

    /**
     * NOVO: busca VÁRIOS gêneros de uma vez, a partir de um conjunto de ids.
     * Usado pelo LivroService para transformar os generoIds do DTO em entidades
     * Genero.
     *
     * Regras:
     * - Sem ids (null ou vazio) → devolve um conjunto vazio: livro sem gênero é
     * permitido.
     * - Algum id não existe → lança 404 dizendo QUAIS ids não foram encontrados.
     * - Todos existem → devolve os gêneros.
     *
     * Exemplo: ids = {1, 2, 999}, e só existem os gêneros 1 e 2
     * → 404 "Gênero(s) não encontrado(s): ids [999]"
     */
    public Set<Genero> buscarEntidades(Set<Long> ids) {

        // 1. Nenhum gênero pedido: não há o que buscar.
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        // 2. Busca todos de uma vez (uma consulta só).
        // Os ids que não existem simplesmente não vêm na lista.
        List<Genero> encontrados = repository.findAllById(ids);

        // 3. Se vieram MENOS gêneros do que ids pedidos, algum id não existe.
        if (encontrados.size() != ids.size()) {

            // 3a. Pega os ids dos gêneros que vieram.
            // .map(Genero::getId) → de cada gênero, pega só o id
            // .collect(toSet()) → junta num Set
            Set<Long> idsEncontrados = encontrados.stream()
                    .map(Genero::getId)
                    .collect(Collectors.toSet());

            // 3b. Faltando = pedidos − encontrados.
            // Copia os pedidos e remove os que foram encontrados.
            Set<Long> faltando = new HashSet<>(ids);
            faltando.removeAll(idsEncontrados);

            throw new RecursoNaoEncontradoException("Gênero(s) não encontrado(s): ids " + faltando);
        }

        // 4. Todos existem: devolve como Set (o tipo que a entidade Livro usa).
        return new HashSet<>(encontrados);
    }

    // Copia os dados do DTO para a entidade.
    private void preencher(Genero genero, GeneroRequestDTO dto) {
        genero.setNome(dto.nome());
    }
}