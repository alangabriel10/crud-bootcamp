package com.bootcamp;

import com.bootcamp.model.Usuario;
import com.bootcamp.service.UsuarioService;
import com.bootcamp.repository.UsuarioRepositoryJPA;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final UsuarioService service = new UsuarioService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(" SISTEMA CRUD - ORIENTADO A OBJETOS \n");
        boolean executando = true;

        while (executando) {
            exibirMenu();
            int opcao = lerOpcao();

            try {
                switch (opcao) {
                    case 1 -> criarUsuario();
                    case 2 -> listarUsuarios();
                    case 3 -> buscarUsuario();
                    case 4 -> buscarPorEmail();
                    case 5 -> atualizarUsuario();
                    case 6 -> removerUsuario();
                    case 7 -> exibirEstatisticas();
                    case 0 -> {
                        System.out.println("\nEncerrando o sistema...");
                        executando = false;
                    }
                    default -> System.out.println("✗ Opção inválida! Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("✗ Erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println();
        }
        scanner.close();
        UsuarioRepositoryJPA.fecharFactory();
    }

    private static void exibirMenu() {
        System.out.println(" SISTEMA DE CADASTRO DE USUÁRIOS ");
        System.out.println("1. Criar usuário");
        System.out.println("2. Listar todos os usuários");
        System.out.println("3. Buscar usuário por ID");
        System.out.println("4. Buscar usuário por Email");
        System.out.println("5. Atualizar usuário");
        System.out.println("6. Remover usuário");
        System.out.println("7. Estatísticas");
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
        System.out.println("\n CRIAR USUÁRIO ");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Usuario usuario = service.criarUsuario(nome, email);
        if (usuario != null) {
            System.out.println("✓ Usuário criado: " + usuario);
        }
    }

    private static void listarUsuarios() {
        System.out.println("\n LISTA DE USUÁRIOS ");
        List<Usuario> usuarios = service.listarTodos();

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            System.out.println("\nID  | Nome                  | Email");

            for (Usuario u : usuarios) {
                System.out.printf("%-3d | %-20s | %s%n",
                        u.getId(),
                        limitarTexto(u.getNome(), 20),
                        u.getEmail());
            }
            System.out.println("Total: " + usuarios.size() + " usuário(s)");
        }
    }

    private static void buscarUsuario() {
        System.out.println("\n BUSCAR USUÁRIO POR ID ");
        System.out.print("ID do usuário: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            Optional<Usuario> usuario = service.buscarPorId(id);

            if (usuario.isPresent()) {
                System.out.println("✓ Usuário encontrado: " + usuario.get());
            } else {
                System.out.println("✗ Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ ID inválido!");
        }
    }

    private static void buscarPorEmail() {
        System.out.println("\n BUSCAR USUÁRIO POR EMAIL ");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Optional<Usuario> usuario = service.buscarPorEmail(email);

        if (usuario.isPresent()) {
            System.out.println("✓ Usuário encontrado: " + usuario.get());
        } else {
            System.out.println("✗ Usuário não encontrado.");
        }
    }

    private static void atualizarUsuario() {
        System.out.println("\n ATUALIZAR USUÁRIO ");
        System.out.print("ID do usuário: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());

            Optional<Usuario> usuarioExistente = service.buscarPorId(id);
            if (usuarioExistente.isEmpty()) {
                System.out.println("✗ Usuário não encontrado.");
                return;
            }

            System.out.println("Usuário atual: " + usuarioExistente.get());
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine();
            System.out.print("Novo email: ");
            String email = scanner.nextLine();

            service.atualizarUsuario(id, nome, email);
        } catch (NumberFormatException e) {
            System.out.println("✗ ID inválido!");
        }
    }

    private static void removerUsuario() {
        System.out.println("\n REMOVER USUÁRIO ");
        System.out.print("ID do usuário: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());

            Optional<Usuario> usuario = service.buscarPorId(id);
            if (usuario.isPresent()) {
                System.out.println("Usuário: " + usuario.get());
                System.out.print("Confirma a remoção? (S/N): ");
                String confirmacao = scanner.nextLine();

                if (confirmacao.equalsIgnoreCase("S")) {
                    service.removerUsuario(id);
                } else {
                    System.out.println("Operação cancelada.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ ID inválido!");
        }
    }

    private static void exibirEstatisticas() {
        System.out.println("\n ESTATÍSTICAS DO SISTEMA ");
        long total = service.contarUsuarios();
        System.out.println("Total de usuários cadastrados: " + total);

        if (total > 0) {
            List<Usuario> usuarios = service.listarTodos();
            System.out.println("Primeiro usuário: " + usuarios.get(0).getNome());
            System.out.println("Último usuário: " + usuarios.get(usuarios.size() - 1).getNome());
        }
    }

    private static String limitarTexto(String texto, int tamanho) {
        if (texto.length() > tamanho) {
            return texto.substring(0, tamanho - 3) + "...";
        }
        return texto;
    }
}