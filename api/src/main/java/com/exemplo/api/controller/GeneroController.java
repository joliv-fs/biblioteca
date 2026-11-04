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

import com.exemplo.api.dto.GeneroRequestDTO;
import com.exemplo.api.dto.GeneroResponseDTO;
import com.exemplo.api.service.GeneroService;

import jakarta.validation.Valid;

/**
 * Endereços HTTP de Gênero: todos começam com /generos (sem acento no
 * endereço).
 * Mesmo molde dos controllers de Autor e Livro.
 */
@RestController
@RequestMapping("/generos")
public class GeneroController {

    private final GeneroService service;

    public GeneroController(GeneroService service) {
        this.service = service;
    }

    // GET /generos → 200 OK com a lista de gêneros.
    @GetMapping
    public ResponseEntity<List<GeneroResponseDTO>> listar() {
        List<GeneroResponseDTO> generos = service.listar();
        return ResponseEntity.ok(generos);
    }

    // GET /generos/{id} → 200 OK com o gênero, ou 404.
    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> buscar(@PathVariable("id") Long id) {
        GeneroResponseDTO genero = service.buscarPorId(id);
        return ResponseEntity.ok(genero);
    }

    /**
     * POST /generos
     * Corpo: { "nome": "Romance" }
     * Resposta: 201 Created + Location (/generos/{id}) + o gênero criado.
     * Erros: 400 (nome vazio ou longo demais) e 409 (nome que já existe).
     */
    @PostMapping
    public ResponseEntity<GeneroResponseDTO> criar(@Valid @RequestBody GeneroRequestDTO dto,
            UriComponentsBuilder uriBuilder) {
        GeneroResponseDTO criado = service.criar(dto);

        URI uri = uriBuilder.path("/generos/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(uri).body(criado);
    }

    // PUT /generos/{id} → 200 OK com o gênero atualizado; 400, 404 ou 409 nos
    // erros.
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponseDTO> atualizar(@PathVariable("id") Long id,
            @Valid @RequestBody GeneroRequestDTO dto) {
        GeneroResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE /generos/{id} → 204 No Content; 404 se não existir; 409 se algum livro
    // usar o gênero.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}