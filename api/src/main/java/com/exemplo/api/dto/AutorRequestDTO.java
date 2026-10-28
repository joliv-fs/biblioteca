package com.exemplo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * O que ENTRA na API para cadastrar ou atualizar um autor.
 *
 * Exemplo de JSON que o app ou o .http manda:
 * {
 *   "nome": "Machado de Assis",
 *   "nacionalidade": "Brasileira"
 * }
 *
 * Os nomes dos campos do JSON precisam ser IGUAIS aos nomes aqui (nome, nacionalidade).
 *
 * Não tem id: quem gera o id é o banco.
 *
 * As anotações de validação só funcionam quando o controller marca o parâmetro com @Valid
 * (isso acontece na M1.6). Se alguma regra falhar, o GlobalExceptionHandler responde 400
 * com a mensagem de cada campo.
 */
public record AutorRequestDTO(

        // @NotBlank: não aceita null, "" (vazio) nem "   " (só espaços).
        // message: o texto que vai aparecer no JSON de erro, em "campos".
        @NotBlank(message = "O nome é obrigatório")
        // @Size(max = 100): no máximo 100 caracteres (o mesmo limite da coluna no banco).
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        // Sem @NotBlank: a nacionalidade é opcional. Mas, se vier, tem limite de tamanho.
        @Size(max = 50, message = "A nacionalidade deve ter no máximo 50 caracteres")
        String nacionalidade
) {
}