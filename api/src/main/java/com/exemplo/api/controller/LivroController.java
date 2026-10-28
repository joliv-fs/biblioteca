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

import com.exemplo.api.dto.LivroRequestDTO;
import com.exemplo.api.dto.LivroResponseDTO;
import com.exemplo.api.service.LivroService;

import jakarta.validation.Valid;

/**
 * Endereços HTTP de Livro: todos começam com /livros.
 * Mesmo molde do AutorController: recebe, chama o service e devolve ResponseEntity.
 */
@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    /**
     * GET /livros
     * Lista todos os livros, cada um com o autor dentro.
     * Resposta: 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar() {
        List<LivroResponseDTO> livros = service.listar();
        return ResponseEntity.ok(livros);
    }

    /**
     * GET /livros/{id}
     * Resposta: 200 OK com o livro, ou 404 se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscar(@PathVariable("id") Long id) {
        LivroResponseDTO livro = service.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    /**
     * POST /livros
     * Corpo: { "titulo": "...", "anoPublicacao": 1899, "autorId": 1 }
     * Resposta: 201 Created + Location (/livros/{id}) + o livro criado, com o autor dentro.
     * Erros: 400 se os dados forem inválidos; 404 se o autorId não existir.
     */
    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO dto,
                                                  UriComponentsBuilder uriBuilder) {
        LivroResponseDTO criado = service.criar(dto);

        // Endereço do livro novo, para o cabeçalho Location.
        URI uri = uriBuilder.path("/livros/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(uri).body(criado);
    }

    /**
     * PUT /livros/{id}
     * Resposta: 200 OK com o livro atualizado.
     * Erros: 400 (dados inválidos) e 404 (livro ou autor não existe).
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable("id") Long id,
                                                      @Valid @RequestBody LivroRequestDTO dto) {
        LivroResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * DELETE /livros/{id}
     * Resposta: 204 No Content, ou 404 se não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}