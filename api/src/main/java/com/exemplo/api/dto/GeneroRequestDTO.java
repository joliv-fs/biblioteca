package com.exemplo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * O que ENTRA na API para cadastrar ou atualizar um gênero.
 *
 * Exemplo de JSON:
 * { "nome": "Romance" }
 *
 * As regras de validação são as mesmas da coluna no banco (obrigatório, até 50
 * caracteres).
 * Nome repetido NÃO é validado aqui: quem barra é o banco (unique), com
 * resposta 409.
 */
public record GeneroRequestDTO(

        @NotBlank(message = "O nome é obrigatório") @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres") String nome) {
}