package com.bootcamp.model;

import java.util.Objects;

public class Usuario {
    private Long id;
    private String nome;
    private String email;

    // Construtores
    public Usuario() {
    }

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Usuario(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters com validação
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio!");
        }
        if (!validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido!");
        }
        this.email = email.trim().toLowerCase();
    }

    // Método de validação de email
    private boolean validarEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    // Método para verificar se o usuário está completo
    public boolean isValido() {
        return nome != null && !nome.trim().isEmpty()
                && email != null && !email.trim().isEmpty();
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
                Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}