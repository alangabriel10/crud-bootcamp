package com.bootcamp.controller;

import com.bootcamp.model.Usuario;
import com.bootcamp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de usuários.
 *
 * Endpoints:
 * - POST   /api/usuarios        - Criar usuário
 * - GET    /api/usuarios        - Listar todos
 * - GET    /api/usuarios/{id}   - Buscar por ID
 * - PUT    /api/usuarios/{id}   - Atualizar
 * - DELETE /api/usuarios/{id}   - Remover
 * - GET    /api/usuarios/email/{email} - Buscar por email
 * - GET    /api/usuarios/count  - Contar total
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    /**
     * POST /api/usuarios
     * Cria um novo usuário.
     */
    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = service.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/usuarios
     * Lista todos os usuários.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = service.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * GET /api/usuarios/{id}
     * Busca usuário por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/usuarios/email/{email}
     * Busca usuário por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        return service.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/usuarios/{id}
     * Atualiza usuário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        try {
            Usuario atualizado = service.atualizarUsuario(id, usuario);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * DELETE /api/usuarios/{id}
     * Remove usuário.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerUsuario(@PathVariable Long id) {
        try {
            service.removerUsuario(id);
            return ResponseEntity.ok("Usuário removido com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/usuarios/count
     * Conta total de usuários.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> contarUsuarios() {
        long total = service.contarUsuarios();
        return ResponseEntity.ok(total);
    }
}