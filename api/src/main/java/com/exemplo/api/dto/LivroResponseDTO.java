package com.exemplo.api.dto;

import java.util.Comparator;
import java.util.List;

import com.exemplo.api.model.Livro;

/**
 * O que SAI da API quando o assunto é livro.
 *
 * Exemplo de JSON gerado:
 * {
 * "id": 1,
 * "titulo": "Dom Casmurro",
 * "anoPublicacao": 1899,
 * "autor": { "id": 1, "nome": "Machado de Assis", "nacionalidade": "Brasileira"
 * },
 * "generos": [ { "id": 2, "nome": "Realismo" }, { "id": 1, "nome": "Romance" }
 * ]
 * }
 */
public record LivroResponseDTO(
        Long id,
        String titulo,
        Integer anoPublicacao,
        AutorResponseDTO autor,
        List<GeneroResponseDTO> generos // NOVO NO M3
) {

    public static LivroResponseDTO from(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAnoPublicacao(),
                AutorResponseDTO.from(livro.getAutor()),

                // NOVO NO M3: converte cada Genero do livro em GeneroResponseDTO.
                livro.getGeneros()
                        .stream() // percorre os gêneros
                        .map(GeneroResponseDTO::from) // Genero → GeneroResponseDTO
                        .sorted(Comparator.comparing(GeneroResponseDTO::nome)) // ordena pelo nome (A → Z)
                        .toList() // junta numa lista
        );
        // Por que ordenar? O Set não tem ordem garantida: sem o sorted, os gêneros
        // poderiam vir numa ordem diferente a cada consulta.
    }
}