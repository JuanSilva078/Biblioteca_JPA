package biblioteca;

import biblioteca.dao.EmprestimoDAO;
import biblioteca.model.Aluno;
import biblioteca.model.Emprestimo;
import biblioteca.model.Publicacao;
import biblioteca.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Classe de testes para o EmprestimoDAO.
 *
 * Cobre os cenários:
 *   1. Incluir empréstimo
 *   2. Alterar empréstimo
 *   3. Deletar empréstimo
 *   4. Consultar por Id
 *   5. Consultar todos
 *   6. Consultar por nome do aluno
 */
public class EmprestimoDAOTest {

    private static EmprestimoDAO dao = new EmprestimoDAO();

    // Contadores de resultado
    private static int passados = 0;
    private static int falhos   = 0;

    // =========================================================================
    // MAIN
    // =========================================================================

    public static void main(String[] args) {

        System.out.println("=".repeat(60));
        System.out.println("  TESTES - EmprestimoDAO - Sistema de Biblioteca");
        System.out.println("=".repeat(60));

        // Pré-condição: persiste alunos e publicações para uso nos testes
        Aluno aluno1 = persistirAluno(101, "Maria Silva");
        Aluno aluno2 = persistirAluno(202, "João Souza");

        Publicacao pub1 = persistirPublicacao(1, "Clean Code",     "Robert Martin", 2008, "Livro");
        Publicacao pub2 = persistirPublicacao(2, "Design Patterns","GoF",           1994, "Livro");
        Publicacao pub3 = persistirPublicacao(3, "Java Efetivo",   "Joshua Bloch",  2018, "Livro");

        // ---- Testes ----
        Long idEmprestimo1 = testeIncluir(aluno1, pub1, pub2);
        Long idEmprestimo2 = testeIncluir(aluno2, pub3);

        testeConsultarPorId(idEmprestimo1);
        testeConsultarTodos();
        testeConsultarPorNomeAluno("Maria");
        testeConsultarPorNomeAluno("silva");   // case-insensitive

        testeAlterar(idEmprestimo1);

        testeDeletar(idEmprestimo2);
        testeConsultarIdInexistente(idEmprestimo2);

        // ---- Resultado final ----
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("  RESULTADO: %d passados | %d falhos%n", passados, falhos);
        System.out.println("=".repeat(60));

        JPAUtil.close();
    }

    // =========================================================================
    // TESTE 1 — INCLUIR
    // =========================================================================

    private static Long testeIncluir(Aluno aluno, Publicacao... pubs) {
        String cabecalho = "INCLUIR empréstimo para " + aluno.getNome();
        try {
            Emprestimo emp = new Emprestimo(
                LocalDate.now(),
                LocalDate.now().plusDays(14),
                aluno
            );
            for (Publicacao p : pubs) emp.adicionarPublicacao(p);

            Emprestimo salvo = dao.incluir(emp);

            assertNotNull("id gerado", salvo.getId());
            assertEquals("aluno correto", aluno.getNome(), salvo.getAluno().getNome());
            assertEquals("qtd publicações", pubs.length, salvo.getPublicacoes().size());

            exito(cabecalho);
            return salvo.getId();

        } catch (Exception e) {
            falha(cabecalho, e);
            return null;
        }
    }

    // =========================================================================
    // TESTE 2 — ALTERAR
    // =========================================================================

    private static void testeAlterar(Long id) {
        String cabecalho = "ALTERAR empréstimo id=" + id;
        try {
            Emprestimo emp = dao.consultarPorId(id)
                .orElseThrow(() -> new RuntimeException("Não encontrado"));

            LocalDate novaDevol = LocalDate.now().plusDays(30);
            emp.setDataDevolucao(novaDevol);
            Emprestimo atualizado = dao.alterar(emp);

            assertEquals("data devolução atualizada", novaDevol, atualizado.getDataDevolucao());
            exito(cabecalho);

        } catch (Exception e) {
            falha(cabecalho, e);
        }
    }

    // =========================================================================
    // TESTE 3 — DELETAR
    // =========================================================================

    private static void testeDeletar(Long id) {
        String cabecalho = "DELETAR empréstimo id=" + id;
        try {
            dao.deletar(id);
            Optional<Emprestimo> resultado = dao.consultarPorId(id);
            assertTrue("deve estar ausente após delete", resultado.isEmpty());
            exito(cabecalho);
        } catch (Exception e) {
            falha(cabecalho, e);
        }
    }

    // =========================================================================
    // TESTE 4 — CONSULTAR POR ID
    // =========================================================================

    private static void testeConsultarPorId(Long id) {
        String cabecalho = "CONSULTAR POR ID=" + id;
        try {
            Optional<Emprestimo> resultado = dao.consultarPorId(id);
            assertTrue("deve encontrar", resultado.isPresent());
            assertEquals("id correto", id, resultado.get().getId());
            exito(cabecalho);
        } catch (Exception e) {
            falha(cabecalho, e);
        }
    }

    // =========================================================================
    // TESTE 5 — CONSULTAR TODOS
    // =========================================================================

    private static void testeConsultarTodos() {
        String cabecalho = "CONSULTAR TODOS";
        try {
            List<Emprestimo> lista = dao.consultarTodos();
            assertTrue("lista não vazia", !lista.isEmpty());
            System.out.println("   → " + lista.size() + " empréstimo(s) encontrado(s):");
            lista.forEach(e -> System.out.println("     " + e));
            exito(cabecalho);
        } catch (Exception e) {
            falha(cabecalho, e);
        }
    }

    // =========================================================================
    // TESTE 6 — CONSULTAR POR NOME DO ALUNO
    // =========================================================================

    private static void testeConsultarPorNomeAluno(String nome) {
        String cabecalho = "CONSULTAR POR NOME ALUNO='" + nome + "'";
        try {
            List<Emprestimo> lista = dao.consultarPorNomeAluno(nome);
            assertTrue("deve retornar resultado", !lista.isEmpty());
            System.out.println("   → " + lista.size() + " empréstimo(s) para '" + nome + "':");
            lista.forEach(e -> System.out.println("     " + e));
            exito(cabecalho);
        } catch (Exception e) {
            falha(cabecalho, e);
        }
    }

    // =========================================================================
    // TESTE EXTRA — ID INEXISTENTE APÓS DELETE
    // =========================================================================

    private static void testeConsultarIdInexistente(Long id) {
        String cabecalho = "CONSULTAR ID INEXISTENTE=" + id;
        try {
            Optional<Emprestimo> resultado = dao.consultarPorId(id);
            assertTrue("deve estar vazio", resultado.isEmpty());
            exito(cabecalho);
        } catch (Exception e) {
            falha(cabecalho, e);
        }
    }

    // =========================================================================
    // HELPERS DE SETUP
    // =========================================================================

    private static Aluno persistirAluno(int matricula, String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Aluno a = new Aluno(matricula, nome);
            em.persist(a);
            tx.commit();
            return a;
        } finally {
            em.close();
        }
    }

    private static Publicacao persistirPublicacao(int cod, String titulo, String autor, int ano, String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Publicacao p = new Publicacao(cod, titulo, autor, ano, tipo);
            em.persist(p);
            tx.commit();
            return p;
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // HELPERS DE ASSERÇÃO
    // =========================================================================

    private static void assertNotNull(String descricao, Object valor) {
        if (valor == null) throw new AssertionError(descricao + " não deve ser null");
    }

    private static void assertEquals(String descricao, Object esperado, Object obtido) {
        if (!esperado.equals(obtido))
            throw new AssertionError(descricao + ": esperado=" + esperado + " obtido=" + obtido);
    }

    private static void assertTrue(String descricao, boolean condicao) {
        if (!condicao) throw new AssertionError(descricao + " = false");
    }

    // =========================================================================
    // SAÍDA FORMATADA
    // =========================================================================

    private static void exito(String teste) {
        passados++;
        System.out.printf("%n[✓] PASSOU  - %s%n", teste);
    }

    private static void falha(String teste, Exception e) {
        falhos++;
        System.out.printf("%n[✗] FALHOU  - %s%n    Motivo: %s%n", teste, e.getMessage());
    }
}
