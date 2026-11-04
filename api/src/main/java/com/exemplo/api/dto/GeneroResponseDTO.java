package com.exemplo.api.dto;

import com.exemplo.api.model.Genero;

/**
 * O que SAI da API quando o assunto é gênero.
 *
 * Exemplo de JSON gerado:
 * { "id": 1, "nome": "Romance" }
 *
 * Também é usado DENTRO do LivroResponseDTO (M3.5), na lista de gêneros de cada
 * livro.
 */
public record GeneroResponseDTO(
        Long id,
        String nome) {

    // Converte a entidade Genero em GeneroResponseDTO.
    // Uso: GeneroResponseDTO.from(genero)
    public static GeneroResponseDTO from(Genero genero) {
        return new GeneroResponseDTO(
                genero.getId(),
                genero.getNome());
    }
}