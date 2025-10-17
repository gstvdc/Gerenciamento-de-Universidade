package banco;

import java.sql.*;

public class TituloDocenteDAO {

    public void cadastrarTitulo(String titulo) {
        String sql = "INSERT INTO tb_tituloDocente (titulo) VALUES (?)";

        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}