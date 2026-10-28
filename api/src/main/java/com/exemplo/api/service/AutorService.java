package com.exemplo.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
// ATENÇÃO: o @Transactional certo é este, do Spring.
// Se o VS Code sugerir jakarta.transaction.Transactional, está ERRADO (ele não tem o readOnly).
import org.springframework.transaction.annotation.Transactional;

import com.exemplo.api.dto.AutorRequestDTO;
import com.exemplo.api.dto.AutorResponseDTO;
import com.exemplo.api.exception.RecursoNaoEncontradoException;
import com.exemplo.api.model.Autor;
import com.exemplo.api.repository.AutorRepository;

/**
 * Regras de negócio de Autor.
 *
 * @Service avisa o Spring: "crie um objeto desta classe e entregue para quem pedir".
 * Quem pede é o controller (M1.6), no construtor dele.
 *
 * @Transactional(readOnly = true) na CLASSE vale para todos os métodos:
 * por padrão, eles só LEEM o banco. Transação de leitura é mais leve para o banco.
 * Os métodos que ESCREVEM (criar, atualizar, excluir) recebem um @Transactional próprio,
 * que substitui o da classe.
 *
 * Transação = um "pacote" de operações no banco: ou tudo dá certo e é gravado,
 * ou algo falha e nada é gravado.
 */
@Service
@Transactional(readOnly = true)
public class AutorService {

    // O repository que este service usa para falar com o banco.
    // "final" = depois de recebido no construtor, nunca é trocado.
    private final AutorRepository repository;

    /**
     * Injeção de dependência pelo construtor:
     * o Spring vê que o service precisa de um AutorRepository e entrega um pronto.
     * Ninguém escreve "new AutorRepository()".
     */
    public AutorService(AutorRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todos os autores.
     *
     * findAll() devolve uma lista de ENTIDADES (List<Autor>).
     * O stream percorre essa lista e converte cada Autor em AutorResponseDTO:
     *   .stream()                     → começa a percorrer a lista
     *   .map(AutorResponseDTO::from)  → para cada autor, chama AutorResponseDTO.from(autor)
     *   .toList()                     → junta os resultados numa lista nova
     */
    public List<AutorResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(AutorResponseDTO::from)
                .toList();
    }

    /**
     * Busca um autor pelo id.
     * Se não existir, buscarEntidade lança RecursoNaoEncontradoException → resposta 404.
     */
    public AutorResponseDTO buscarPorId(Long id) {
        Autor autor = buscarEntidade(id);
        return AutorResponseDTO.from(autor);
    }

    /**
     * Cadastra um autor novo.
     *
     * @Transactional (sem readOnly): este método ESCREVE no banco.
     */
    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        // 1. Cria uma entidade vazia (o id fica null).
        Autor autor = new Autor();

        // 2. Copia os dados do DTO para a entidade.
        preencher(autor, dto);

        // 3. Salva. Como o id é null, o save faz um INSERT e o banco gera o id.
        //    O save devolve o autor JÁ COM O ID.
        Autor salvo = repository.save(autor);

        // 4. Converte DEPOIS de salvar, para a resposta levar o id gerado.
        return AutorResponseDTO.from(salvo);
    }

    /**
     * Atualiza um autor que já existe.
     */
    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        // 1. Busca o autor no banco. Se não existir: 404, e o método para aqui.
        //    (Não use "new Autor()" aqui: isso criaria um autor NOVO em vez de atualizar.)
        Autor autor = buscarEntidade(id);

        // 2. Troca os dados antigos pelos novos.
        preencher(autor, dto);

        // 3. Salva. Como o autor tem id, o save faz um UPDATE.
        Autor salvo = repository.save(autor);

        return AutorResponseDTO.from(salvo);
    }

    /**
     * Exclui um autor.
     */
    @Transactional
    public void excluir(Long id) {
        // Busca antes de apagar: se não existir, a resposta é 404 (e não um erro estranho).
        Autor autor = buscarEntidade(id);
        repository.delete(autor);
    }

    /**
     * Busca a ENTIDADE Autor pelo id ou lança a nossa exceção.
     *
     * findById devolve um Optional<Autor>: uma "caixa" que pode ter um autor ou estar vazia.
     * orElseThrow: se a caixa tiver um autor, devolve ele; se estiver vazia, lança a exceção.
     *
     * É PÚBLICO porque, na M2, o LivroService vai usar este método para achar
     * o autor de um livro (e responder 404 se o autor não existir).
     */
    public Autor buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado: id " + id));
    }

    /**
     * Copia os dados do DTO para a entidade.
     * Usado no criar e no atualizar, para não repetir código.
     * É privado porque só este service usa.
     */
    private void preencher(Autor autor, AutorRequestDTO dto) {
        // No record, o acesso é dto.nome(), e não dto.getNome().
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
    }
}