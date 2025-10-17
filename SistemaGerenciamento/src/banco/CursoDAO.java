package banco;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class CursoDAO {

    public int cadastrarCurso(String nome, int fases, int anos) throws SQLException {
        String sql = "INSERT INTO tb_cursos (nome_curso, quantidade_fases, periodo_total_anos) VALUES (?, ?, ?)";
        int idGerado = -1;
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nome);
            stmt.setInt(2, fases);
            stmt.setInt(3, anos);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }
            System.out.println("Curso '" + nome + "' cadastrado com ID: " + idGerado);
        }
        return idGerado;
    }

    // O restante da classe permanece o mesmo...
    public DefaultTableModel listarCursos() {
        String[] colunas = {"ID", "Nome do Curso", "Data Cadastro", "Fases", "Anos"};
        DefaultTableModel modelo = new DefaultTableModel(null, colunas);
        String sql = "SELECT * FROM tb_cursos";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Object[] linha = {
                        rs.getInt("id"),
                        rs.getString("nome_curso"),
                        rs.getTimestamp("data_cadastro").toLocalDateTime().toLocalDate().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        ),
                        rs.getInt("quantidade_fases"),
                        rs.getInt("periodo_total_anos")
                };
                modelo.addRow(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelo;
    }

    public void deletarCurso(int id) {
        String sql = "DELETE FROM tb_cursos WHERE id = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Curso deletado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarCurso(int id, String nome, int fases, int anos) {
        String sql = "UPDATE tb_cursos SET nome_curso = ?, quantidade_fases = ?, periodo_total_anos = ? WHERE id = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, fases);
            stmt.setInt(3, anos);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            System.out.println("Curso atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int buscarCursoIdPorNome(String nomeCurso) {
        String sql = "SELECT id FROM tb_cursos WHERE nome_curso = ?";
        int cursoId = -1;
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cursoId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursoId;
    }
    
    public List<String> listarNomesCursos() {
        List<String> cursos = new ArrayList<>();
        String sql = "SELECT nome_curso FROM tb_cursos";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cursos.add(rs.getString("nome_curso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }
}