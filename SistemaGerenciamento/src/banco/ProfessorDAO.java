package banco;

import java.sql.*;

public class ProfessorDAO {

    public void cadastrarProfessor(String nome, int disciplinaId, int tituloId) throws SQLException {
        String sql = "INSERT INTO tb_professores (nome_professor, disciplinas_id, titulo_id) VALUES (?, ?, ?)";

        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setInt(2, disciplinaId);
            stmt.setInt(3, tituloId);
            stmt.executeUpdate();
        }
    }
}