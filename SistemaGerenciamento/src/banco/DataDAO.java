package banco;

import java.sql.*;

public class DataDAO {

    public void cadastrarData(String nomeDia) {
        String sql = "INSERT INTO tb_data (nome_dia) VALUES (?)";

        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeDia);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}