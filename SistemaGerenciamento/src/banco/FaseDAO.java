package banco;

import java.sql.*;

public class FaseDAO {

    public int cadastrarFase(String nomeFase, int cursoId, int quantidadeDisciplinas) throws SQLException {
        String sql = "INSERT INTO tb_fases (nome_fase, curso_id, quantidade_disciplinas) VALUES (?, ?, ?)";
        int idGerado = -1;
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nomeFase);
            stmt.setInt(2, cursoId);
            stmt.setInt(3, quantidadeDisciplinas);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }
            System.out.println("Fase '" + nomeFase + "' cadastrada com ID: " + idGerado);
        }
        return idGerado;
    }
}