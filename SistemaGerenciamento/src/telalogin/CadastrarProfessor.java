package telalogin;

import banco.conexao;
import banco.DisciplinaDAO;
import banco.ProfessorDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import static telalogin.IconUtils.carregarIcone;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CadastrarProfessor extends JFrame {
    private Map<String, Integer> disciplinaIdMap = new HashMap<>();
    private Map<String, Integer> tituloIdMap = new HashMap<>();
    private JTextField campoNome;
    private JComboBox<String> comboDisciplina;
    private JComboBox<String> comboTitulo;
    private JTable tabelaProfessores;
    private DefaultTableModel tableModel;
    private int proximoId = 1;
    private DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private ProfessorDAO professorDAO = new ProfessorDAO();

    public CadastrarProfessor() {
        configurarTela();
        criarComponentes();
        setVisible(true);
    }
    
    private void carregarDisciplinasDoBanco() {
        DisciplinaDAO dao = new DisciplinaDAO();
        List<String> disciplinas = dao.listarNomesDisciplinas();
        comboDisciplina.removeAllItems();
        disciplinaIdMap.clear();

        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina encontrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            int id = 1; // Substitua por ID real do banco se possível
            for (String d : disciplinas) {
                comboDisciplina.addItem(d);
                disciplinaIdMap.put(d, id++);
            }
        }
    }
    
    private void carregarTitulosDocentes() {
        comboTitulo.removeAllItems();
        tituloIdMap.clear();

        String[] titulos = {"Especialista", "Mestre", "Doutor", "Pós-Doutor"};
        try (Connection conn = conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, titulo FROM tb_tituloDocente")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                comboTitulo.addItem(titulo);
                tituloIdMap.put(titulo, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback para títulos estáticos se o banco falhar
            for (int i = 0; i < titulos.length; i++) {
                comboTitulo.addItem(titulos[i]);
                tituloIdMap.put(titulos[i], i + 1); // Simulação de ID
            }
        }
    }

    private void configurarTela() {
        setTitle("CADASTRO DE PROFESSORES");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void criarComponentes() {
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel titulo = new JLabel("CADASTRO DE PROFESSORES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        ImageIcon iconTitulo = carregarIcone("/icons/teacher_icon.png", 32, 32);
        if (iconTitulo != null) {
            titulo.setIcon(iconTitulo);
            titulo.setIconTextGap(10);
        }
        
        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 10, 10));

        painelCampos.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("Disciplina:"));
        comboDisciplina = new JComboBox<>();
        carregarDisciplinasDoBanco();
        painelCampos.add(comboDisciplina);

        painelCampos.add(new JLabel("Título Docente:"));
        comboTitulo = new JComboBox<>();
        carregarTitulosDocentes();
        painelCampos.add(comboTitulo);

        painelPrincipal.add(painelCampos);

        JPanel painelBotao = new JPanel();
        painelBotao.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));
        JButton btnAdicionar = new JButton("Adicionar Professor");
        ImageIcon addIcon = carregarIcone("/icons/add_icon.png", 20, 20);
        if (addIcon != null) {
            btnAdicionar.setIcon(addIcon);
            btnAdicionar.setIconTextGap(8);
        }
        btnAdicionar.setBackground(new Color(34, 139, 34));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.addActionListener(e -> adicionarProfessor());
        painelBotao.add(btnAdicionar);

        painelPrincipal.add(painelBotao);

        add(painelPrincipal, BorderLayout.CENTER);

        String[] colunas = {"ID", "Nome", "Disciplina", "Título Docente"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProfessores = new JTable(tableModel);

        tabelaProfessores.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaProfessores.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaProfessores.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaProfessores.getColumnModel().getColumn(3).setPreferredWidth(100);

        tabelaProfessores.setRowHeight(25);
        tabelaProfessores.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaProfessores);
        scrollPane.setPreferredSize(new Dimension(650, 180));

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        painelTabela.add(scrollPane, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.SOUTH);
    }

    private void adicionarProfessor() {
        String nome = campoNome.getText().trim();
        String disciplina = (String) comboDisciplina.getSelectedItem();
        String titulo = (String) comboTitulo.getSelectedItem();

        if (nome.isEmpty() || disciplina == null || titulo == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int disciplinaId = disciplinaIdMap.getOrDefault(disciplina, -1);
        int tituloId = tituloIdMap.getOrDefault(titulo, -1);

        if (disciplinaId == -1 || tituloId == -1) {
            JOptionPane.showMessageDialog(this, "Erro ao mapear IDs!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            professorDAO.cadastrarProfessor(nome, disciplinaId, tituloId);

            // Adicionar à tabela local se for bem-sucedido
            Object[] rowData = {proximoId++, nome, disciplina, titulo};
            tableModel.addRow(rowData);

            campoNome.setText("");
            comboDisciplina.setSelectedIndex(0);
            comboTitulo.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Professor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar professor no banco de dados: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastrarProfessor());
    }
}