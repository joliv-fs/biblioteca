package com.exemplo.api.dto;

import java.util.Map;

/**
 * Formato de TODA resposta de erro da API.
 *
 * Exemplo de JSON gerado:
 * {
 *   "status": 400,
 *   "mensagem": "Dados inválidos",
 *   "campos": { "nome": "O nome é obrigatório" }
 * }
 *
 * "record" é um jeito curto de criar uma classe só para carregar dados:
 * o Java gera sozinho o construtor, os métodos de acesso (status(), mensagem(), campos()),
 * equals, hashCode e toString. Os valores não mudam depois de criados.
 */
public record ErroResponseDTO(

        // Código HTTP do erro (400, 404, 409...). Repetido no corpo para facilitar a leitura no app.
        int status,

        // Mensagem geral, que pode ser mostrada direto para o usuário.
        String mensagem,

        // Erros de cada campo, no formato "nome do campo" → "mensagem".
        // Usado nos erros de validação. Nos outros erros, vai vazio: {}
        Map<String, String> campos
) {
}
