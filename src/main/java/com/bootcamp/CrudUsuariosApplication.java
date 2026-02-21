package com.bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal do Spring Boot.
 *
 * @SpringBootApplication - Ativa:
 * - Configuração automática
 * - Escaneamento de componentes
 * - Configuração do Spring Boot
 */
@SpringBootApplication
public class CrudUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudUsuariosApplication.class, args);

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║  🚀 CRUD USUÁRIOS COM SPRING BOOT INICIADO!  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("\n✅ API REST disponível em: http://localhost:8080");
        System.out.println("✅ Console H2: http://localhost:8080/h2-console");
        System.out.println("✅ Swagger (se configurado): http://localhost:8080/swagger-ui.html");
        System.out.println("\n📚 Endpoints disponíveis:");
        System.out.println("   POST   /api/usuarios        - Criar usuário");
        System.out.println("   GET    /api/usuarios        - Listar todos");
        System.out.println("   GET    /api/usuarios/{id}   - Buscar por ID");
        System.out.println("   PUT    /api/usuarios/{id}   - Atualizar");
        System.out.println("   DELETE /api/usuarios/{id}   - Remover");
        System.out.println("   GET    /api/usuarios/count  - Contar usuários\n");
    }
}