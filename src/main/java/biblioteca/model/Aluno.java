package biblioteca.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Aluno - mapeamento JPA da tabela aluno.
 *
 * Relacionamento: Um Aluno pode ter muitos Empréstimos (1:N).
 * A navegação é unidirecional: Emprestimo -> Aluno (conforme diagrama).
 */
@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matricula_aluno", nullable = false, unique = true)
    private int matriculaAluno;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    // Navegação bidirecional opcional — usada para facilitar consultas
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprestimo> emprestimos = new ArrayList<>();

    // ----------------------------- Construtores -----------------------------

    public Aluno() {}

    public Aluno(int matriculaAluno, String nome) {
        this.matriculaAluno = matriculaAluno;
        this.nome = nome;
    }

    // ----------------------------- Getters / Setters ------------------------

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public int getMatriculaAluno()             { return matriculaAluno; }
    public void setMatriculaAluno(int m)       { this.matriculaAluno = m; }

    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    public List<Emprestimo> getEmprestimos()   { return emprestimos; }
    public void setEmprestimos(List<Emprestimo> e) { this.emprestimos = e; }

    // ----------------------------- toString ---------------------------------

    @Override
    public String toString() {
        return "Aluno{id=" + id
                + ", matricula=" + matriculaAluno
                + ", nome='" + nome + "'}";
    }
}
