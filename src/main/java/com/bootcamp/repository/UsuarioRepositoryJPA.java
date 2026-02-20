package com.bootcamp.repository;

import com.bootcamp.model.Usuario;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;


public class UsuarioRepositoryJPA implements IUsuarioRepository {

    // Factory é criado uma única vez e compartilhado
    private static EntityManagerFactory entityManagerFactory;


    static {
        try {
            System.out.println("========================================");
            System.out.println("Inicializando JPA/Hibernate...");
            System.out.println("========================================");

            entityManagerFactory = Persistence.createEntityManagerFactory("crud-bootcamp-pu");

            System.out.println("✓ JPA inicializado com sucesso!");
            System.out.println("✓ Conexão com H2 estabelecida!");
            System.out.println("✓ Schema do banco criado automaticamente!");
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.err.println("✗ ERRO ao inicializar JPA: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }


    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }


    private <T> T executeInTransaction(TransactionCallback<T> callback) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            T result = callback.execute(em);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("✗ Erro na transação: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar operação", e);
        } finally {
            em.close();
        }
    }


    private <T> T executeQuery(QueryCallback<T> callback) {
        EntityManager em = getEntityManager();
        try {
            return callback.execute(em);
        } catch (Exception e) {
            System.err.println("✗ Erro na consulta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar consulta", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return executeInTransaction(em -> {
            em.persist(usuario);
            System.out.println("✓ Usuário salvo com sucesso via JPA! ID: " + usuario.getId());
            return usuario;
        });
    }

    @Override
    public List<Usuario> listarTodos() {
        return executeQuery(em -> {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u ORDER BY u.id", Usuario.class
            );
            return query.getResultList();
        });
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return executeQuery(em -> {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        });
    }

    @Override
    public boolean atualizar(Usuario usuario) {
        return executeInTransaction(em -> {
            Usuario usuarioExistente = em.find(Usuario.class, usuario.getId());

            if (usuarioExistente == null) {
                System.out.println("✗ Usuário não encontrado para atualização!");
                return false;
            }

            // Atualiza os campos
            usuarioExistente.setNome(usuario.getNome());
            usuarioExistente.setEmail(usuario.getEmail());

            em.merge(usuarioExistente);
            System.out.println("✓ Usuário atualizado com sucesso via JPA!");
            return true;
        });
    }

    @Override
    public boolean remover(Long id) {
        return executeInTransaction(em -> {
            Usuario usuario = em.find(Usuario.class, id);

            if (usuario == null) {
                System.out.println("✗ Usuário não encontrado para remoção!");
                return false;
            }

            em.remove(usuario);
            System.out.println("✓ Usuário removido com sucesso via JPA!");
            return true;
        });
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return executeQuery(em -> {
            try {
                TypedQuery<Usuario> query = em.createQuery(
                        "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class
                );
                query.setParameter("email", email.toLowerCase());

                Usuario usuario = query.getSingleResult();
                return Optional.of(usuario);

            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }

    @Override
    public long contarUsuarios() {
        return executeQuery(em -> {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM Usuario u", Long.class
            );
            return query.getSingleResult();
        });
    }


    public static void fecharFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            System.out.println("✓ EntityManagerFactory fechado.");
        }
    }

    // ==================== INTERFACES FUNCIONAIS ====================


    @FunctionalInterface
    private interface TransactionCallback<T> {
        T execute(EntityManager em) throws Exception;
    }


    @FunctionalInterface
    private interface QueryCallback<T> {
        T execute(EntityManager em) throws Exception;
    }
}