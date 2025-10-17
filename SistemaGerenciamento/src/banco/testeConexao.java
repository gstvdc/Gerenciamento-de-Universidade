package banco;

import java.sql.Connection;

public class testeConexao {
    public static void main(String[] args) {
        Connection conn = conexao.getConexao();

        if (conn != null) {
            System.out.println("✅ Conexão realizada com sucesso!");
        } else {
            System.out.println("❌ Falha na conexão.");
        }
    }
}
