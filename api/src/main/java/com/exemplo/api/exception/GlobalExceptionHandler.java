package com.exemplo.api.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.exemplo.api.dto.ErroResponseDTO;

/**
 * Tratador de erros da API INTEIRA.
 *
 * @RestControllerAdvice = "esta classe vale para todos os controllers".
 * Quando qualquer controller (ou o service chamado por ele) lança uma exceção,
 * o Spring procura aqui um método marcado com @ExceptionHandler para aquele tipo de exceção.
 * Achou? Esse método monta a resposta. Não achou? O Spring usa a resposta padrão dele.
 *
 * Assim, controllers e services não precisam de try/catch: eles só lançam a exceção.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 – Not Found
     * Quando: o service lançou RecursoNaoEncontradoException (um id que não existe).
     */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        // A mensagem vem da própria exceção, ex.: "Autor não encontrado: id 999".
        // Map.of() cria um mapa vazio: aqui não há erros por campo.
        ErroResponseDTO erro = new ErroResponseDTO(status.value(), ex.getMessage(), Map.of());

        // ResponseEntity = a resposta HTTP completa: status + corpo (o JSON).
        return ResponseEntity.status(status).body(erro);
    }

    /**
     * 400 – Bad Request (validação)
     * Quando: um DTO marcado com @Valid no controller tem campos que não passaram
     * nas regras (@NotBlank, @Size...). O Spring lança MethodArgumentNotValidException
     * ANTES de entrar no método do controller.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarValidacao(MethodArgumentNotValidException ex) {

        // Monta o mapa "campo → mensagem" com todos os campos que falharam.
        // LinkedHashMap mantém a ordem em que os erros foram encontrados.
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erroCampo : ex.getBindingResult().getFieldErrors()) {
            // getField()          → nome do campo no DTO, ex.: "nome"
            // getDefaultMessage() → a mensagem escrita na anotação, ex.: "O nome é obrigatório"
            campos.put(erroCampo.getField(), erroCampo.getDefaultMessage());
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponseDTO erro = new ErroResponseDTO(status.value(), "Dados inválidos", campos);
        return ResponseEntity.status(status).body(erro);
    }

    /**
     * 400 – Bad Request (JSON inválido)
     * Quando: o corpo da requisição não é um JSON válido (falta vírgula, aspas abertas...)
     * ou tem um valor do tipo errado (texto num campo numérico).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponseDTO erro = new ErroResponseDTO(status.value(), "JSON inválido ou mal formatado", Map.of());
        return ResponseEntity.status(status).body(erro);
    }

    // Repare que NÃO existe um método para Exception (todas as exceções).
    // Ele capturaria também os erros que o Spring já trata do jeito certo,
    // como endereço inexistente (404) e método não permitido (405), e transformaria tudo em 500.
}