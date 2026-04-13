package biblioteca.dao;

import biblioteca.model.Emprestimo;
import biblioteca.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) para a entidade {@link Emprestimo}.
 *
 * Responsabilidades:
 *   - incluir    → salvar novo empréstimo
 *   - alterar    → atualizar empréstimo existente
 *   - deletar    → remover empréstimo por id
 *   - consultar  → buscar por id / listar todos / buscar por nome do aluno
 */
public class EmprestimoDAO {

    // =========================================================================
    // INCLUIR
    // =========================================================================

    /**
     * Persiste um novo Emprestimo no banco de dados.
     *
     * @param emprestimo objeto a ser salvo (id deve ser null)
     * @return o próprio objeto após persistência (id preenchido)
     */
    public Emprestimo incluir(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(emprestimo);
            tx.commit();
            System.out.println("[DAO] Empréstimo incluído: " + emprestimo);
            return emprestimo;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erro ao incluir empréstimo.", e);
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // ALTERAR
    // =========================================================================

    /**
     * Atualiza um Emprestimo já existente no banco.
     *
     * @param emprestimo objeto com id preenchido e dados atualizados
     * @return objeto gerenciado após o merge
     */
    public Emprestimo alterar(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Emprestimo atualizado = em.merge(emprestimo);
            tx.commit();
            System.out.println("[DAO] Empréstimo alterado: " + atualizado);
            return atualizado;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erro ao alterar empréstimo.", e);
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // DELETAR
    // =========================================================================

    /**
     * Remove um Emprestimo pelo seu id.
     *
     * @param id identificador do empréstimo a ser removido
     * @throws RuntimeException se o empréstimo não for encontrado
     */
    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Emprestimo emprestimo = em.find(Emprestimo.class, id);
            if (emprestimo == null) {
                throw new RuntimeException("Empréstimo não encontrado para id=" + id);
            }
            em.remove(emprestimo);
            tx.commit();
            System.out.println("[DAO] Empréstimo deletado: id=" + id);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erro ao deletar empréstimo id=" + id, e);
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // CONSULTAR POR ID
    // =========================================================================

    /**
     * Busca um Emprestimo pelo id.
     *
     * @param id identificador
     * @return Optional com o empréstimo, ou vazio se não encontrado
     */
    public Optional<Emprestimo> consultarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Emprestimo emprestimo = em.find(Emprestimo.class, id);
            return Optional.ofNullable(emprestimo);
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // CONSULTAR TODOS
    // =========================================================================

    /**
     * Retorna todos os Empréstimos cadastrados.
     *
     * @return lista (possivelmente vazia) de empréstimos
     */
    public List<Emprestimo> consultarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                "SELECT e FROM Emprestimo e JOIN FETCH e.aluno ORDER BY e.dataEmprestimo DESC",
                Emprestimo.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // CONSULTAR POR NOME DO ALUNO
    // =========================================================================

    /**
     * Retorna todos os empréstimos cujo aluno tenha o nome informado
     * (busca parcial, case-insensitive).
     *
     * @param nomeAluno nome (ou parte do nome) do aluno
     * @return lista de empréstimos correspondentes
     */
    public List<Emprestimo> consultarPorNomeAluno(String nomeAluno) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                "SELECT e FROM Emprestimo e "
                + "JOIN FETCH e.aluno a "
                + "WHERE LOWER(a.nome) LIKE LOWER(:nome) "
                + "ORDER BY e.dataEmprestimo DESC",
                Emprestimo.class
            );
            query.setParameter("nome", "%" + nomeAluno + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
