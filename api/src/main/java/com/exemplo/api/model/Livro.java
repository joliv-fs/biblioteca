package com.exemplo.api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

/**
 * Entidade Livro → tabela LIVRO.
 * Relacionamentos (os dois ficam só aqui, no Livro):
 * - N:1 com Autor (cada livro tem um autor)
 * - N:N com Genero (cada livro tem vários gêneros)
 */
@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    // Opcional: Integer aceita null ("sem ano").
    private Integer anoPublicacao;

    // N:1 → muitos livros para um autor (coluna autor_id na tabela LIVRO). Criado
    // no M2.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    /**
     * NOVO NO M3: N:N → muitos livros para muitos gêneros.
     *
     * @ManyToMany → um livro tem vários gêneros e um gênero está em vários livros.
     *             (É LAZY por padrão: os gêneros só são buscados quando alguém
     *             pedir.)
     *
     * @JoinTable → descreve a TABELA DE LIGAÇÃO, que o Hibernate cria sozinho:
     *            name = "livro_genero" → nome da tabela
     *            joinColumns = @JoinColumn(name = "livro_id") → coluna que aponta
     *            para ESTA entidade (Livro)
     *            inverseJoinColumns = @JoinColumn(name = "genero_id") → coluna que
     *            aponta para a OUTRA (Genero)
     *
     *            Set (e não List):
     *            - um livro não pode ter o mesmo gênero duas vezes, e o Set não
     *            aceita repetidos;
     *            - o Hibernate trabalha melhor com Set quando busca várias coleções
     *            ao mesmo tempo.
     *
     *            = new HashSet<>() → começa com um conjunto VAZIO (e não null).
     *            Assim, um livro sem gêneros tem uma lista vazia, e o código nunca
     *            tropeça num null.
     */
    @ManyToMany
    @JoinTable(name = "livro_genero", joinColumns = @JoinColumn(name = "livro_id"), inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private Set<Genero> generos = new HashSet<>();

    public Livro() {
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    // NOVO NO M3
    public Set<Genero> getGeneros() {
        return generos;
    }

    // NOVO NO M3
    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }
}