package biblioteca.model;

import jakarta.persistence.*;

/**
 * Entidade Publicacao - mapeamento JPA da tabela publicacao.
 *
 * Relacionamento: Uma Publicacao pode aparecer em muitos Empréstimos (N:M).
 * A navegação é: Emprestimo -> Publicacao (conforme diagrama: seta + publicacao).
 */
@Entity
@Table(name = "publicacao")
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_pub", nullable = false, unique = true)
    private int codigoPub;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @Column(name = "ano", nullable = false)
    private int ano;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    // ----------------------------- Construtores -----------------------------

    public Publicacao() {}

    public Publicacao(int codigoPub, String titulo, String autor, int ano, String tipo) {
        this.codigoPub = codigoPub;
        this.titulo    = titulo;
        this.autor     = autor;
        this.ano       = ano;
        this.tipo      = tipo;
    }

    // ----------------------------- Getters / Setters ------------------------

    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public int getCodigoPub()            { return codigoPub; }
    public void setCodigoPub(int c)      { this.codigoPub = c; }

    public String getTitulo()            { return titulo; }
    public void setTitulo(String t)      { this.titulo = t; }

    public String getAutor()             { return autor; }
    public void setAutor(String a)       { this.autor = a; }

    public int getAno()                  { return ano; }
    public void setAno(int ano)          { this.ano = ano; }

    public String getTipo()              { return tipo; }
    public void setTipo(String tipo)     { this.tipo = tipo; }

    // ----------------------------- toString ---------------------------------

    @Override
    public String toString() {
        return "Publicacao{id=" + id
                + ", codigo=" + codigoPub
                + ", titulo='" + titulo + "'"
                + ", autor='" + autor + "'"
                + ", ano=" + ano
                + ", tipo='" + tipo + "'}";
    }
}
