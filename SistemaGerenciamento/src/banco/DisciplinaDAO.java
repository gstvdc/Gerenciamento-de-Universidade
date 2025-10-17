package banco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    public void cadastrarDisciplina(String nome, int faseId, int dataId, int codigoDisciplina) throws RuntimeException {
        // ... este método permanece o mesmo
        String sql = "INSERT INTO tb_disciplinas (nome_disciplina, codigo_disciplina, fase_id, data_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, codigoDisciplina);
            stmt.setInt(3, faseId);
            stmt.setInt(4, dataId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar disciplina no banco: " + e.getMessage(), e);
        }
    }

    public int importarDisciplina(String nome, int codigoDisciplina, int faseId) throws SQLException {
        String sql = "INSERT INTO tb_disciplinas (nome_disciplina, codigo_disciplina, fase_id) VALUES (?, ?, ?)";
        int idGerado = -1;
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nome);
            stmt.setInt(2, codigoDisciplina);
            stmt.setInt(3, faseId);
            stmt.executeUpdate();
    
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }
        }
        return idGerado;
    }

    // O restante da classe permanece o mesmo...
    public int buscarDataIdPorDia(String diaSemana) {
        String sql = "SELECT id FROM tb_data WHERE UPPER(TRIM(nome_dia)) = UPPER(TRIM(?))";
        int dataId = -1;
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, diaSemana);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dataId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataId;
    }

    public java.util.List<String> listarNomesDisciplinas() {
        List<String> disciplinas = new ArrayList<>();
        String sql = "SELECT nome_disciplina FROM tb_disciplinas";
        try (Connection conn = conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                disciplinas.add(rs.getString("nome_disciplina"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disciplinas;
    }
    
    public int buscarUltimaDisciplinaIdPorFase(int faseId) {
        String sql = "SELECT id FROM tb_disciplinas WHERE fase_id = ? ORDER BY id DESC LIMIT 1";
        int disciplinaId = -1;
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, faseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                disciplinaId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disciplinaId;
    }

    public java.util.List<String> listarFasesPorCurso(int cursoId) {
        List<String> fases = new ArrayList<>();
        String sql = "SELECT id, nome_fase, quantidade_disciplinas FROM tb_fases WHERE curso_id = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeFase = rs.getString("nome_fase");
                int qtdDisciplinas = rs.getInt("quantidade_disciplinas");
                fases.add(nomeFase + " (" + qtdDisciplinas + " disciplinas)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fases;
    }
}