package com.bootcamp.repository;

import com.bootcamp.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements IUsuarioRepository {
    // CORREÇÃO PRINCIPAL: Usar conexão compartilhada e persistente
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Singleton para garantir uma única instância da conexão
    private static Connection connectionInstance = null;
    private static boolean tabelaCriada = false;

    public UsuarioRepository() {
        if (!tabelaCriada) {
            System.out.println("Inicializando UsuarioRepository...");
            inicializarBanco();
            tabelaCriada = true;
        }
    }

    private synchronized Connection getConnection() throws SQLException {
        try {
            if (connectionInstance == null || connectionInstance.isClosed()) {
                Class.forName("org.h2.Driver");
                connectionInstance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ Nova conexão estabelecida com sucesso!");
            }
            return connectionInstance;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ ERRO CRÍTICO: Driver H2 não encontrado!");
            throw new SQLException("Driver H2 não encontrado!", e);
        }
    }

    private void inicializarBanco() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE)";

        try {
            Connection conn = getConnection();
            System.out.println("Executando SQL de criação da tabela...");
            System.out.println("SQL: " + sql);

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("✓✓✓ TABELA 'usuarios' CRIADA COM SUCESSO! ✓✓✓");


                // Verificar se a tabela foi realmente criada
                verificarTabela(conn);
            }

        } catch (SQLException e) {
            System.err.println("✗✗✗ ERRO CRÍTICO AO CRIAR TABELA ✗✗✗");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            throw new RuntimeException("Falha ao inicializar banco de dados", e);
        }
    }

    private void verificarTabela(Connection conn) {
        String sqlVerificar = "SELECT COUNT(*) as total FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'USUARIOS'";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlVerificar)) {

            if (rs.next()) {
                int total = rs.getInt("total");
                if (total > 0) {
                    System.out.println("✓ Verificação: Tabela 'USUARIOS' existe no banco!");
                } else {
                    System.err.println("✗ ATENÇÃO: Tabela NÃO foi encontrada após criação!");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao verificar tabela: " + e.getMessage());
        }
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try {
            Connection conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getEmail());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            usuario.setId(rs.getLong(1));
                        }
                    }
                }
                System.out.println("✓ Usuário salvo com sucesso!");
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao salvar usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";

        try {
            Connection conn = getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao listar usuários: " + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try {
            Connection conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ? WHERE id = ?";

        try {
            Connection conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getEmail());
                stmt.setLong(3, usuario.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("✓ Usuário atualizado com sucesso!");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remover(Long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try {
            Connection conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("✓ Usuário removido com sucesso!");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao remover usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try {
            Connection conn = getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao buscar usuário por email: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        return usuario;
    }

    // Método para fechar a conexão quando necessário
    public static void fecharConexao() {
        try {
            if (connectionInstance != null && !connectionInstance.isClosed()) {
                connectionInstance.close();
                System.out.println("✓ Conexão com banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao fechar conexão: " + e.getMessage());
        }
    }
}