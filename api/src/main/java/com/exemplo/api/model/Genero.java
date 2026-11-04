package com.exemplo.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidade Gênero → tabela GENERO.
 *
 * O nome da classe não tem acento (Genero), porque nomes com acento em código
 * costumam dar problema. O acento fica só nos textos que o usuário vê.
 *
 * Mesmo molde do Autor. A única novidade é o unique na coluna nome.
 */
@Entity
public class Genero {

    // Chave primária gerada pelo banco (1, 2, 3...).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Coluna NOME.
     * nullable = false → obrigatório.
     * length = 50 → no máximo 50 caracteres.
     * unique = true → NOVO: o banco não aceita dois gêneros com o mesmo nome.
     * Se alguém tentar, o banco recusa e o GlobalExceptionHandler
     * responde 409 (o tratamento criado na M2.5).
     */
    @Column(nullable = false, length = 50, unique = true)
    private String nome;

    // Construtor vazio: o JPA exige.
    public Genero() {
    }

    // ---------------------------------------------------------------
    // Getters e setters
    // ---------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}