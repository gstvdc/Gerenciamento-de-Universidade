package telalogin;

import banco.FaseDAO;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import banco.CursoDAO;
import java.sql.SQLException;
import static telalogin.IconUtils.carregarIcone;

public class CadastrarFases extends JFrame {
    private JComboBox<String> comboCurso;
    private JSpinner spinnerNumeroFase;
    private JSpinner spinnerQtdDisciplinas;
    private JTable tabelaFases;
    private DefaultTableModel tableModel;
    private int proximoId = 1;

    public CadastrarFases() {
        configurarTela();
        criarComponentes();
        setVisible(true);
    }

    private void configurarTela() {
        setTitle("CADASTRO DE FASES DE CURSOS");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }
    
    private void carregarCursosDoBanco() {
        // ... seu método carregarCursosDoBanco() permanece o mesmo ...
        CursoDAO cursoDAO = new CursoDAO();
        java.util.List<String> cursos = cursoDAO.listarNomesCursos();

        if (cursos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum curso encontrado no banco de dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            for (String curso : cursos) {
                comboCurso.addItem(curso);
            }
        }
    }

    private int buscarQuantidadeFasesMaxima(String cursoNome) {
        // ... seu método buscarQuantidadeFasesMaxima() permanece o mesmo ...
        int maxFases = 0;
        CursoDAO cursoDAO = new CursoDAO();
        int cursoId = cursoDAO.buscarCursoIdPorNome(cursoNome);
        if (cursoId != -1) {
            String sql = "SELECT quantidade_fases FROM tb_cursos WHERE id = ?";
            try (java.sql.Connection conn = banco.conexao.getConexao();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, cursoId);
                java.sql.ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    maxFases = rs.getInt("quantidade_fases");
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao consultar a quantidade máxima de fases.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        return maxFases;
    }

    private int buscarMaiorNumeroFase(String cursoNome) {
        // ... seu método buscarMaiorNumeroFase() permanece o mesmo ...
        int maxFase = 0;
        CursoDAO cursoDAO = new CursoDAO();
        int cursoId = cursoDAO.buscarCursoIdPorNome(cursoNome);
        if (cursoId != -1) {
            String sql = "SELECT MAX(CAST(SUBSTRING(nome_fase, 6) AS UNSIGNED)) AS max_fase FROM tb_fases WHERE curso_id = ?";
            try (java.sql.Connection conn = banco.conexao.getConexao();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, cursoId);
                java.sql.ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    maxFase = rs.getInt("max_fase");
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao consultar o número máximo de fases.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        return maxFase + 1; // Próximo número disponível
    }

    private void criarComponentes() {
        // --- O restante do seu método criarComponentes() permanece o mesmo ---
        // título com ícone
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel titulo = new JLabel("CADASTRO DE FASES DE CURSOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        // ícone
        ImageIcon iconTitulo = carregarIcone("/icons/phase_icon.png", 32, 32);
        if (iconTitulo != null) {
            titulo.setIcon(iconTitulo);
            titulo.setIconTextGap(10);
        }
        
        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        // principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 10, 10));

        painelCampos.add(new JLabel("Curso:"));
        comboCurso = new JComboBox<>();
        carregarCursosDoBanco();
        comboCurso.addActionListener(e -> {
            String cursoSelecionado = (String) comboCurso.getSelectedItem();
            if (cursoSelecionado != null) {
                int maxFases = buscarQuantidadeFasesMaxima(cursoSelecionado);
                int maxFaseCadastrada = buscarMaiorNumeroFase(cursoSelecionado) - 1;
                int proximoFase = Math.min(maxFaseCadastrada + 1, maxFases);
                if (maxFases > 0) {
                    spinnerNumeroFase.setModel(new SpinnerNumberModel(proximoFase, 1, maxFases, 1));
                } else {
                    spinnerNumeroFase.setModel(new SpinnerNumberModel(1, 1, 10, 1)); // Fallback
                }
            }
        });
        painelCampos.add(comboCurso);

        painelCampos.add(new JLabel("Número da Fase:"));
        spinnerNumeroFase = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // Valor inicial padrão
        painelCampos.add(spinnerNumeroFase);

        painelCampos.add(new JLabel("Qtd. Disciplinas:"));
        spinnerQtdDisciplinas = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        painelCampos.add(spinnerQtdDisciplinas);

        painelPrincipal.add(painelCampos);

        // Painel do botão
        JPanel painelBotao = new JPanel();
        painelBotao.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));
        JButton btnAdicionar = new JButton("Adicionar Fase");
        ImageIcon addIcon = carregarIcone("/icons/add_icon.png", 20, 20);
        if (addIcon != null) {
            btnAdicionar.setIcon(addIcon);
            btnAdicionar.setIconTextGap(8);
        }
        btnAdicionar.setBackground(new Color(70, 130, 180));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.addActionListener(e -> adicionarFase());
        painelBotao.add(btnAdicionar);

        painelPrincipal.add(painelBotao);

        add(painelPrincipal, BorderLayout.CENTER);

        // Tabela
        String[] colunas = {"ID", "Curso", "Número da Fase", "Qtd Disciplinas"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFases = new JTable(tableModel);

        tabelaFases.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaFases.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaFases.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaFases.getColumnModel().getColumn(3).setPreferredWidth(120);

        tabelaFases.setRowHeight(25);
        tabelaFases.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaFases);
        scrollPane.setPreferredSize(new Dimension(650, 180));

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        painelTabela.add(scrollPane, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.SOUTH);
    }
    
    // MÉTODO CORRIGIDO COM TRY-CATCH
    private void adicionarFase() {
        String cursoNome = (String) comboCurso.getSelectedItem();

        if (cursoNome == null || cursoNome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um curso!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numeroFase = (Integer) spinnerNumeroFase.getValue();
        int qtdDisciplinas = (Integer) spinnerQtdDisciplinas.getValue();

        CursoDAO cursoDAO = new CursoDAO();
        int cursoId = cursoDAO.buscarCursoIdPorNome(cursoNome);

        if (cursoId == -1) {
            JOptionPane.showMessageDialog(this, "Erro ao encontrar o ID do curso!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FaseDAO faseDAO = new FaseDAO();
        try {
            faseDAO.cadastrarFase("Fase " + numeroFase, cursoId, qtdDisciplinas);

            // Apenas executa se o cadastro no banco for bem-sucedido
            Object[] rowData = {proximoId++, cursoNome, numeroFase, qtdDisciplinas};
            tableModel.addRow(rowData);
            spinnerNumeroFase.setValue(numeroFase + 1); // Sugere a próxima fase
            JOptionPane.showMessageDialog(this, "Fase cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar fase no banco de dados: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastrarFases());
    }
}