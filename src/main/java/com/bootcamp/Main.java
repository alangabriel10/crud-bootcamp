package com.bootcamp;

import com.bootcamp.model.Usuario;
import com.bootcamp.service.UsuarioService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final UsuarioService service = new UsuarioService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean executando = true;

        while (executando) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> criarUsuario();
                case 2 -> listarUsuarios();
                case 3 -> buscarUsuario();
                case 4 -> atualizarUsuario();
                case 5 -> removerUsuario();
                case 0 -> {
                    System.out.println("Encerrando o sistema...");
                    executando = false;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("=== SISTEMA DE CADASTRO DE USUÁRIOS ===");
        System.out.println("1. Criar usuário");
        System.out.println("2. Listar todos os usuários");
        System.out.println("3. Buscar usuário por ID");
        System.out.println("4. Atualizar usuário");
        System.out.println("5. Remover usuário");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void criarUsuario() {
        System.out.println("\\n--- CRIAR USUÁRIO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Usuario usuario = service.criarUsuario(nome, email);
        if (usuario != null) {
            System.out.println("Usuário criado: " + usuario);
        }
    }

    private static void listarUsuarios() {
        System.out.println("\\n--- LISTA DE USUÁRIOS ---");
        List<Usuario> usuarios = service.listarTodos();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            usuarios.forEach(System.out::println);
        }
    }

    private static void buscarUsuario() {
        System.out.println("\\n--- BUSCAR USUÁRIO ---");
        System.out.print("ID do usuário: ");
        Long id = Long.parseLong(scanner.nextLine());

        Usuario usuario = service.buscarPorId(id);
        if (usuario != null) {
            System.out.println("Usuário encontrado: " + usuario);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private static void atualizarUsuario() {
        System.out.println("\\n--- ATUALIZAR USUÁRIO ---");
        System.out.print("ID do usuário: ");
        Long id = Long.parseLong(scanner.nextLine());

        Usuario usuarioExistente = service.buscarPorId(id);
        if (usuarioExistente == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();

        service.atualizarUsuario(id, nome, email);
    }

    private static void removerUsuario() {
        System.out.println("\\n--- REMOVER USUÁRIO ---");
        System.out.print("ID do usuário: ");
        Long id = Long.parseLong(scanner.nextLine());

        Usuario usuarioExistente = service.buscarPorId(id);
        if (usuarioExistente == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        service.removerUsuario(id);
    }
}