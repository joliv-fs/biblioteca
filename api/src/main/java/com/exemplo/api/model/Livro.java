package com.exemplo.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidade Livro → tabela LIVRO.
 *
 * Mesmo molde do Autor (id, colunas, construtor vazio, getters e setters),
 * mais o relacionamento com Autor.
 */
@Entity
public class Livro {

    // Chave primária gerada pelo banco (1, 2, 3...), igual ao Autor.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coluna TITULO: obrigatória, no máximo 150 caracteres.
    @Column(nullable = false, length = 150)
    private String titulo;

    /**
     * Coluna ANO_PUBLICACAO.
     * O Hibernate transforma o nome em camelCase (anoPublicacao) em ANO_PUBLICACAO no banco.
     *
     * É Integer (objeto), e não int (primitivo), porque o ano é OPCIONAL:
     * Integer aceita null ("sem ano"); int não aceita, ele viraria 0.
     */
    private Integer anoPublicacao;

    /**
     * O RELACIONAMENTO: cada livro tem UM autor.
     *
     * @ManyToOne → "muitos livros para um autor". Fica no lado N (o Livro),
     *              porque é a tabela LIVRO que guarda a ligação.
     *
     *   fetch = FetchType.LAZY → o autor só é buscado no banco quando alguém
     *              chamar livro.getAutor(). Isso evita consultas desnecessárias.
     *   optional = false       → todo livro TEM que ter um autor.
     *
     * @JoinColumn(name = "autor_id") → nome da coluna da chave estrangeira na tabela LIVRO.
     *   nullable = false → o banco também não aceita livro sem autor.
     *
     * Repare: o atributo é do tipo Autor (o objeto inteiro), e não Long.
     * No Java a gente trabalha com objetos; quem transforma isso em autor_id é o Hibernate.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    // Construtor vazio: o JPA exige.
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
}