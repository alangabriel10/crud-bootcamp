package com.bootcamp.repository;

import com.bootcamp.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    Usuario salvar(Usuario usuario);
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Long id);
    boolean atualizar(Usuario usuario);
    boolean remover(Long id);
    Optional<Usuario> buscarPorEmail(String email);
}