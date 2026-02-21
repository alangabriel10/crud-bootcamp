package com.bootcamp.repository;

import com.bootcamp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data JPA
 *
 * JpaRepository já fornece:
 * - save() - Salvar/atualizar
 * - findAll() - Listar todos
 * - findById() - Buscar por ID
 * - deleteById() - Deletar por ID
 * - count() - Contar registros
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Método customizado para buscar por email.
     * Spring Data JPA cria a implementação automaticamente!
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se existe usuário com o email.
     */
    boolean existsByEmail(String email);
}