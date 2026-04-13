package biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Emprestimo - mapeamento JPA da tabela emprestimo.
 *
 * Relacionamentos (conforme diagrama):
 *   - Emprestimo (*)  -> (1) Aluno           : ManyToOne  (+emprestimos)
 *   - Emprestimo (1)  -> (0..*) Publicacao   : ManyToMany (+publicacao)
 */
@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    // -------- Relacionamento Emprestimo -> Aluno (ManyToOne) --------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // -------- Relacionamento Emprestimo -> Publicacao (ManyToMany) --------
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "emprestimo_publicacao",
        joinColumns        = @JoinColumn(name = "emprestimo_id"),
        inverseJoinColumns = @JoinColumn(name = "publicacao_id")
    )
    private List<Publicacao> publicacoes = new ArrayList<>();

    // ----------------------------- Construtores -----------------------------

    public Emprestimo() {}

    public Emprestimo(LocalDate dataEmprestimo, LocalDate dataDevolucao, Aluno aluno) {
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao  = dataDevolucao;
        this.aluno          = aluno;
    }

    // ----------------------------- Helpers ----------------------------------

    public void adicionarPublicacao(Publicacao p) {
        publicacoes.add(p);
    }

    public void removerPublicacao(Publicacao p) {
        publicacoes.remove(p);
    }

    // ----------------------------- Getters / Setters ------------------------

    public Long getId()                            { return id; }
    public void setId(Long id)                     { this.id = id; }

    public LocalDate getDataDevolucao()            { return dataDevolucao; }
    public void setDataDevolucao(LocalDate d)      { this.dataDevolucao = d; }

    public LocalDate getDataEmprestimo()           { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate d)     { this.dataEmprestimo = d; }

    public Aluno getAluno()                        { return aluno; }
    public void setAluno(Aluno aluno)              { this.aluno = aluno; }

    public List<Publicacao> getPublicacoes()       { return publicacoes; }
    public void setPublicacoes(List<Publicacao> p) { this.publicacoes = p; }

    // ----------------------------- toString ---------------------------------

    @Override
    public String toString() {
        return "Emprestimo{id=" + id
                + ", dataEmprestimo=" + dataEmprestimo
                + ", dataDevolucao=" + dataDevolucao
                + ", aluno=" + (aluno != null ? aluno.getNome() : "null")
                + ", qtdPublicacoes=" + publicacoes.size() + "}";
    }
}
