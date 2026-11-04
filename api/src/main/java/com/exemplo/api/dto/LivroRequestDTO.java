package com.exemplo.api.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * O que ENTRA na API para cadastrar ou atualizar um livro.
 *
 * Exemplo de JSON:
 * {
 * "titulo": "Dom Casmurro",
 * "anoPublicacao": 1899,
 * "autorId": 1,
 * "generoIds": [1, 2]
 * }
 *
 * Do autor, chega o id. Dos gêneros, chega uma LISTA de ids.
 */
public record LivroRequestDTO(

                @NotBlank(message = "O título é obrigatório") @Size(max = 150, message = "O título deve ter no máximo 150 caracteres") String titulo,

                @Positive(message = "O ano de publicação deve ser positivo") Integer anoPublicacao,

                @NotNull(message = "O autor é obrigatório") Long autorId,

                /**
                 * NOVO NO M3: ids dos gêneros do livro.
                 * No JSON é uma lista ([1, 2]); aqui vira um Set, que descarta ids repetidos.
                 * Opcional: sem anotação de validação. Se não vier, o livro fica sem gêneros.
                 */
                Set<Long> generoIds) {
}