package biblioteca.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utilitário JPA — centraliza a criação e o fechamento do EntityManagerFactory.
 *
 * Padrão Singleton: apenas uma instância de EntityManagerFactory por aplicação.
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT = "bibliotecaPU";

    private static EntityManagerFactory factory;

    // Impede instanciação
    private JPAUtil() {}

    /**
     * Retorna (criando se necessário) o EntityManagerFactory.
     */
    public static EntityManagerFactory getFactory() {
        if (factory == null || !factory.isOpen()) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return factory;
    }

    /**
     * Cria e retorna um novo EntityManager.
     */
    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    /**
     * Fecha o EntityManagerFactory (chamar ao encerrar a aplicação).
     */
    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
