package com.exemplo.api.dto;

import com.exemplo.api.model.Livro;

/**
 * O que SAI da API quando o assunto é livro.
 *
 * Exemplo de JSON gerado:
 * {
 *   "id": 1,
 *   "titulo": "Dom Casmurro",
 *   "anoPublicacao": 1899,
 *   "autor": { "id": 1, "nome": "Machado de Assis", "nacionalidade": "Brasileira" }
 * }
 *
 * O autor vem ANINHADO: é um AutorResponseDTO dentro do LivroResponseDTO.
 */
public record LivroResponseDTO(
        Long id,
        String titulo,
        Integer anoPublicacao,
        AutorResponseDTO autor   // o DTO do autor, e não a entidade Autor
) {

    /**
     * Converte uma entidade Livro em LivroResponseDTO.
     *
     * Para o autor, REAPROVEITA a conversão que já existe no AutorResponseDTO.
     * Se um dia o AutorResponseDTO ganhar um campo novo, ele aparece aqui também,
     * sem mexer neste arquivo.
     *
     * Atenção: livro.getAutor() lê o autor, que é LAZY. Por isso esta conversão
     * precisa acontecer DENTRO do service (onde a transação com o banco está aberta).
     */
    public static LivroResponseDTO from(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAnoPublicacao(),
                AutorResponseDTO.from(livro.getAutor())
        );
    }
}