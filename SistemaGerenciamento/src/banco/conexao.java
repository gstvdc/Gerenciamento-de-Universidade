package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexao {

	private static final String user = "root";
	private static final String password = "12345";
	private static final String database = "PROJETO_SISTEMA_ESCOLAR";
	private static final String host = "localhost:3306";

	private static final String url = "jdbc:mysql://" + host + "/" + database;
	private static Connection conexao;

	public static Connection getConexao() {
		try {
			conexao = DriverManager.getConnection(url, user, password);
			return conexao;
		} catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco.");
            e.printStackTrace();
        }
		return null;
	}

	public static void closeConnection() {
		if (conexao != null) {
			try {
				conexao.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
