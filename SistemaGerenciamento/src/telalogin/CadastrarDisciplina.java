package telalogin;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import static telalogin.IconUtils.carregarIcone;
import banco.DisciplinaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import banco.conexao;

public class CadastrarDisciplina extends JFrame {
    private JComboBox<String> comboCurso;
    private JComboBox<String> comboFase;
    private JTextField campoCodigo;
    private JTextField campoNome;
    private JComboBox<String> comboDiaSemana;
    private JTable tabelaDisciplinas;
    private DefaultTableModel tableModel;
    private int proximoId = 1;

    public CadastrarDisciplina() {
        configurarTela();
        criarComponentes();
        setVisible(true);
    }

    private void configurarTela() {
        setTitle("CADASTRO DE DISCIPLINAS");
        setSize(700, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void criarComponentes() {
        // título com ícone
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel titulo = new JLabel("CADASTRO DE DISCIPLINAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        ImageIcon iconTitulo = carregarIcone("/icons/subject_icon.png", 32, 32);
        if (iconTitulo != null) {
            titulo.setIcon(iconTitulo);
            titulo.setIconTextGap(10);
        }
        
        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        // formulário principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // curso e fase
        JPanel painelCursoFase = new JPanel(new GridLayout(2, 2, 10, 10));

        painelCursoFase.add(new JLabel("Curso:"));
        comboCurso = new JComboBox<>(new String[] {"Informática", "Administração", "Enfermagem"});
        painelCursoFase.add(comboCurso);

        painelCursoFase.add(new JLabel("Fase:"));
        comboFase = new JComboBox<>(new String[]{"Fase 1 (5 disciplinas)", "Fase 2 (5 disciplinas)", "Fase 3 (5 disciplinas)"});
        painelCursoFase.add(comboFase);

        painelPrincipal.add(painelCursoFase);

        // campos da disciplina
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        
        painelCampos.add(new JLabel("Código:"));
        campoCodigo = new JTextField();
        painelCampos.add(campoCodigo);

        painelCampos.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("Dia da Semana:"));
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        comboDiaSemana = new JComboBox<>(dias);
        painelCampos.add(comboDiaSemana);

        painelPrincipal.add(painelCampos);

        // botão
        JPanel painelBotao = new JPanel();
        painelBotao.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));
        JButton btnAdicionar = new JButton("Adicionar Disciplina");
        ImageIcon addIcon = carregarIcone("/icons/add_icon.png", 20, 20);
        if (addIcon != null) {
            btnAdicionar.setIcon(addIcon);
            btnAdicionar.setIconTextGap(8);
        }
        btnAdicionar.setBackground(new Color(70, 130, 180));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.addActionListener(e -> adicionarDisciplina());
        painelBotao.add(btnAdicionar);

        painelPrincipal.add(painelBotao);

        add(painelPrincipal, BorderLayout.CENTER);

        // Disciplinas com ID
        String[] colunas = {"ID", "Fase", "Código", "Nome", "Dia da Semana"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaDisciplinas = new JTable(tableModel);

        tabelaDisciplinas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaDisciplinas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelaDisciplinas.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabelaDisciplinas.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabelaDisciplinas.getColumnModel().getColumn(4).setPreferredWidth(100);

        tabelaDisciplinas.setRowHeight(25);
        tabelaDisciplinas.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaDisciplinas);
        scrollPane.setPreferredSize(new Dimension(650, 180));

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        painelTabela.add(scrollPane, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.SOUTH);
    }

    private void adicionarDisciplina() {
        String curso = (String) comboCurso.getSelectedItem();
        String fase = (String) comboFase.getSelectedItem();
        String codigo = campoCodigo.getText().trim();
        String nome = campoNome.getText().trim();
        String diaSemana = (String) comboDiaSemana.getSelectedItem();

        if (codigo.isEmpty() || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Converter código para inteiro
        int codigoDisciplina;
        try {
            codigoDisciplina = Integer.parseInt(codigo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "O código deve ser um número inteiro!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar se o código já existe
        if (codigoExiste(codigoDisciplina)) {
            JOptionPane.showMessageDialog(this, "Código já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o faseId
        int faseId = obterFaseId(fase, curso);
        if (faseId == -1) {
            JOptionPane.showMessageDialog(this, "Fase ou curso não encontrado no banco!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter o dataId
        DisciplinaDAO dao = new DisciplinaDAO();
        int dataId = dao.buscarDataIdPorDia(diaSemana);
        if (dataId == -1) {
            JOptionPane.showMessageDialog(this, "Dia da semana não encontrado no banco!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Persistir no banco
        try {
            dao.cadastrarDisciplina(nome, faseId, dataId, codigoDisciplina);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Adicionar à tabela local
        Object[] rowData = {proximoId++, fase, codigo, nome, diaSemana};
        tableModel.addRow(rowData);

        // Limpar os campos
        campoCodigo.setText("");
        campoNome.setText("");
        comboDiaSemana.setSelectedIndex(0);

        JOptionPane.showMessageDialog(this, "Disciplina cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean codigoExiste(int codigo) {
        String sql = "SELECT COUNT(*) FROM tb_disciplinas WHERE codigo_disciplina = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int obterFaseId(String nomeFase, String nomeCurso) {
        String sql = "SELECT f.id FROM tb_fases f JOIN tb_cursos c ON f.fase_id = c.id WHERE f.nome_fase = ? AND c.nome_curso = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeFase.split(" ")[0]);
            stmt.setString(2, nomeCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao consultar fase: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastrarDisciplina());
    }
}