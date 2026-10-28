package com.exemplo.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.exemplo.api.dto.AutorRequestDTO;
import com.exemplo.api.dto.AutorResponseDTO;
import com.exemplo.api.service.AutorService;

import jakarta.validation.Valid;

/**
 * Endereços HTTP de Autor.
 *
 * @RestController → esta classe responde requisições HTTP, e o que os métodos
 *                   devolvem vira JSON automaticamente.
 * @RequestMapping("/autores") → todos os endereços desta classe começam com /autores.
 *                   Padrão: nome da entidade no plural, em minúsculas.
 *
 * Todos os métodos devolvem ResponseEntity<...>: a resposta HTTP completa,
 * com status, cabeçalhos e corpo. Assim o status de cada resposta fica explícito no código.
 */
@RestController
@RequestMapping("/autores")
public class AutorController {

    // O controller só conversa com o service. Nunca com o repository.
    private final AutorService service;

    // Injeção pelo construtor: o Spring entrega o AutorService pronto.
    public AutorController(AutorService service) {
        this.service = service;
    }

    /**
     * GET /autores
     * Lista todos os autores.
     * Resposta: 200 OK + lista em JSON (ou [] se não houver nenhum).
     */
    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listar() {
        List<AutorResponseDTO> autores = service.listar();
        return ResponseEntity.ok(autores); // ok(...) = status 200 com este corpo
    }

    /**
     * GET /autores/{id}     ex.: GET /autores/1
     * Busca um autor.
     * Resposta: 200 OK + o autor, ou 404 se não existir (o 404 vem do service,
     * pela RecursoNaoEncontradoException, e é montado pelo GlobalExceptionHandler).
     *
     * @PathVariable("id") → pega o pedaço {id} do endereço e coloca no parâmetro id.
     * O nome "id" escrito por extenso evita erro de compilação em algumas configurações do VS Code.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscar(@PathVariable("id") Long id) {
        AutorResponseDTO autor = service.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    /**
     * POST /autores
     * Cadastra um autor.
     * Resposta: 201 Created + o autor criado (já com id)
     *           + cabeçalho Location com o endereço dele (ex.: http://localhost:8080/autores/1).
     * Se os dados forem inválidos: 400 (validação).
     *
     * @RequestBody → o JSON do corpo da requisição vira um AutorRequestDTO.
     * @Valid       → LIGA a validação das anotações do DTO (@NotBlank, @Size).
     *                Sem o @Valid, as regras do DTO são ignoradas!
     * UriComponentsBuilder → o Spring entrega este ajudante já com o endereço
     *                base da API (http://localhost:8080), para montar o Location.
     */
    @PostMapping
    public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO dto,
                                                  UriComponentsBuilder uriBuilder) {
        AutorResponseDTO criado = service.criar(dto);

        // Monta o endereço do autor novo: /autores/{id}, trocando {id} pelo id gerado.
        URI uri = uriBuilder.path("/autores/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        // created(uri) = status 201 + cabeçalho Location; body(...) = corpo da resposta.
        return ResponseEntity.created(uri).body(criado);
    }

    /**
     * PUT /autores/{id}     ex.: PUT /autores/1
     * Atualiza um autor.
     * Resposta: 200 OK + o autor atualizado; 400 se os dados forem inválidos; 404 se não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(@PathVariable("id") Long id,
                                                      @Valid @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * DELETE /autores/{id}     ex.: DELETE /autores/1
     * Exclui um autor.
     * Resposta: 204 No Content (deu certo, sem corpo), ou 404 se não existir.
     *
     * ResponseEntity<Void> → a resposta não tem corpo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); // noContent() = status 204
    }
}