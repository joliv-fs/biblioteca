package com.exemplo.api.dto;

import com.exemplo.api.model.Autor;

/**
 * O que SAI da API quando o assunto é autor.
 *
 * Exemplo de JSON gerado:
 * {
 *   "id": 1,
 *   "nome": "Machado de Assis",
 *   "nacionalidade": "Brasileira"
 * }
 *
 * A entidade Autor nunca vai direto para o JSON: ela é sempre convertida para este DTO.
 */
public record AutorResponseDTO(
        Long id,
        String nome,
        String nacionalidade
) {

    /**
     * Converte uma entidade Autor em AutorResponseDTO.
     *
     * É "static" porque não precisa de um DTO já existente para funcionar:
     * a gente chama direto pela classe, assim:
     *     AutorResponseDTO dto = AutorResponseDTO.from(autor);
     *
     * Fica aqui, e não no service, para ser reaproveitado:
     * na M2, o DTO de Livro vai usar este mesmo método para mostrar o autor de cada livro.
     */
    public static AutorResponseDTO from(Autor autor) {
        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getNacionalidade()
        );
    }
}