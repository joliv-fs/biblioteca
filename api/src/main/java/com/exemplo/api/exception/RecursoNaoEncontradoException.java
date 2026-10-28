package com.exemplo.api.exception;

/**
 * Exceção da NOSSA API para "procurei e não achei".
 *
 * O service lança esta exceção quando um id não existe no banco, por exemplo:
 *     throw new RecursoNaoEncontradoException("Autor não encontrado: id 5");
 *
 * O nome é genérico de propósito: a mesma exceção serve para Autor, Livro, Gênero...
 *
 * Ela estende RuntimeException (exceção "não verificada"), então quem chama
 * o método não é obrigado a escrever try/catch. Quem trata é o GlobalExceptionHandler.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    // Recebe a mensagem e repassa para a classe mãe (RuntimeException),
    // que guarda o texto. Depois ele é lido com ex.getMessage().
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}