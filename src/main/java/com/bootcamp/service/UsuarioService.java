package com.bootcamp.service;

import com.bootcamp.model.Usuario;
import com.bootcamp.repository.UsuarioRepository;

import java.util.List;

public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    public Usuario criarUsuario(String nome, String email) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        return repository.salvar(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public boolean atualizarUsuario(Long id, String nome, String email) {
        Usuario usuario = new Usuario(id, nome, email);
        return repository.atualizar(usuario);
    }

    public boolean removerUsuario(Long id) {
        return repository.remover(id);
    }
}