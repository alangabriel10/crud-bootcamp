package com.bootcamp.service;

import com.bootcamp.model.Usuario;
import com.bootcamp.repository.IUsuarioRepository;
import com.bootcamp.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final IUsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario criarUsuario(String nome, String email) {
        try {
            // Validar se o email já existe
            Optional<Usuario> usuarioExistente = repository.buscarPorEmail(email);
            if (usuarioExistente.isPresent()) {
                System.out.println("✗ Erro: Email já cadastrado!");
                return null;
            }

            // Criar e validar o usuário
            Usuario usuario = new Usuario(nome, email);

            if (!usuario.isValido()) {
                System.out.println("✗ Erro: Dados do usuário inválidos!");
                return null;
            }

            return repository.salvar(usuario);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erro de validação: " + e.getMessage());
            return null;
        }
    }

    public List<Usuario> listarTodos() {
        return repository.listarTodos();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            System.out.println("✗ ID inválido!");
            return Optional.empty();
        }
        return repository.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("✗ Email inválido!");
            return Optional.empty();
        }
        return repository.buscarPorEmail(email);
    }

    public boolean atualizarUsuario(Long id, String nome, String email) {
        try {
            // Verificar se o usuário existe
            Optional<Usuario> usuarioExistente = repository.buscarPorId(id);
            if (usuarioExistente.isEmpty()) {
                System.out.println("✗ Usuário não encontrado!");
                return false;
            }

            // Verificar se o novo email já está em uso por outro usuário
            Optional<Usuario> usuarioComEmail = repository.buscarPorEmail(email);
            if (usuarioComEmail.isPresent() && !usuarioComEmail.get().getId().equals(id)) {
                System.out.println("✗ Erro: Email já está em uso por outro usuário!");
                return false;
            }

            Usuario usuario = new Usuario(id, nome, email);
            return repository.atualizar(usuario);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erro de validação: " + e.getMessage());
            return false;
        }
    }

    public boolean removerUsuario(Long id) {
        Optional<Usuario> usuario = repository.buscarPorId(id);
        if (usuario.isEmpty()) {
            System.out.println("✗ Usuário não encontrado!");
            return false;
        }

        return repository.remover(id);
    }

    public long contarUsuarios() {
        return listarTodos().size();
    }

    public boolean existeUsuarioComEmail(String email) {
        return repository.buscarPorEmail(email).isPresent();
    }
}