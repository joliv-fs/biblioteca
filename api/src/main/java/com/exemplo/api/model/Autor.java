package com.exemplo.api.model;

// ATENÇÃO aos imports: todas as anotações vêm de jakarta.persistence.
// Se o VS Code sugerir org.springframework.data.annotation.Id, está ERRADO.
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidade Autor.
 *
 * @Entity avisa o Hibernate: "esta classe é uma tabela".
 * Sem nome definido, a tabela recebe o nome da classe: AUTOR.
 * Quando a API sobe, o Hibernate (com ddl-auto=update) cria a tabela se ela ainda não existir.
 */
@Entity
public class Autor {

    /**
     * Chave primária: identifica cada autor de forma única.
     *
     * @Id             → esta é a chave primária.
     * @GeneratedValue → quem gera o valor é o BANCO, não a gente.
     * IDENTITY        → o banco usa um contador automático: 1, 2, 3...
     *
     * É Long (com L maiúsculo, um objeto) porque um autor ainda não salvo
     * não tem id: o valor fica null até o banco gerar.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Coluna NOME.
     * nullable = false → o banco não aceita autor sem nome.
     * length = 100     → no máximo 100 caracteres.
     * São as mesmas regras que o DTO vai validar na M1.4:
     * o DTO barra na entrada, o banco garante do lado dele.
     */
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Coluna NACIONALIDADE.
     * Sem nullable = false: o campo é opcional (pode ficar vazio).
     */
    @Column(length = 50)
    private String nacionalidade;

    /**
     * Construtor vazio.
     * O JPA EXIGE: quando ele lê uma linha do banco, primeiro cria um Autor vazio
     * e depois preenche os atributos com os setters.
     */
    public Autor() {
    }

    // ---------------------------------------------------------------
    // Getters e setters
    // O JPA usa para ler e preencher os atributos; o service também.
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

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
}