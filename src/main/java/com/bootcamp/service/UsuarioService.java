package com.bootcamp.service;

import com.bootcamp.model.Usuario;
import com.bootcamp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço com Spring.
 *
 * @Service - Marca como componente Spring
 * @Transactional - Gerencia transações automaticamente
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    /**
     * Cria um novo usuário.
     */
    public Usuario criarUsuario(Usuario usuario) {
        // Verifica email duplicado
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        return repository.save(usuario);
    }

    /**
     * Lista todos os usuários.
     */
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    /**
     * Busca usuário por ID.
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Busca usuário por email.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Atualiza usuário.
     */
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        // Verifica se email já está em uso por outro usuário
        Optional<Usuario> usuarioComEmail = repository.findByEmail(usuarioAtualizado.getEmail());
        if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email já está em uso!");
        }

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());

        return repository.save(usuario);
    }

    /**
     * Remove usuário.
     */
    public void removerUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
        repository.deleteById(id);
    }

    /**
     * Conta total de usuários.
     */
    public long contarUsuarios() {
        return repository.count();
    }

    /**
     * Verifica se usuário existe.
     */
    public boolean existeUsuario(Long id) {
        return repository.existsById(id);
    }
}