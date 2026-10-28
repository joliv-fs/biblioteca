package com.exemplo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * O que ENTRA na API para cadastrar ou atualizar um livro.
 *
 * Exemplo de JSON:
 * {
 *   "titulo": "Dom Casmurro",
 *   "anoPublicacao": 1899,
 *   "autorId": 1
 * }
 *
 * Do autor, chega SÓ O ID. O service usa esse id para buscar o autor no banco.
 */
public record LivroRequestDTO(

        // Título: obrigatório e com no máximo 150 caracteres (igual à coluna no banco).
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String titulo,

        // Ano: opcional (pode não vir), mas, se vier, tem que ser maior que zero.
        // @Positive ignora valores null: por isso o campo continua opcional.
        @Positive(message = "O ano de publicação deve ser positivo")
        Integer anoPublicacao,

        // Id do autor: obrigatório.
        // @NotNull, e não @NotBlank: @NotBlank é só para texto (String); para números, usa-se @NotNull.
        @NotNull(message = "O autor é obrigatório")
        Long autorId
) {
}