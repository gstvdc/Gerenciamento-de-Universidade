package telalogin;

import banco.CursoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static telalogin.IconUtils.carregarIcone;

public class CadastrarCurso extends JFrame {
    private JTextField campoNomeCurso;
    private JSpinner spinnerQtdFases;
    private JSpinner spinnerPeriodoTotal;
    private JTable tabelaCursos;
    private DefaultTableModel tableModel;

    public CadastrarCurso() {
        configurarTela();
        criarComponentes();
        atualizarTabela(); // Carrega os dados iniciais do banco
        setVisible(true);
    }

    private void configurarTela() {
        setTitle("CADASTRO DE CURSOS");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void criarComponentes() {
        // --- O restante do seu método criarComponentes() permanece o mesmo ---
        // título com ícone
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel titulo = new JLabel("CADASTRO DE CURSOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        // ícone
        ImageIcon iconTitulo = carregarIcone("/icons/course_icon.png", 32, 32);
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

        painelCampos.add(new JLabel("Nome do Curso:"));
        campoNomeCurso = new JTextField();
        painelCampos.add(campoNomeCurso);

        painelCampos.add(new JLabel("Quantidade de Fases:"));
        spinnerQtdFases = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        painelCampos.add(spinnerQtdFases);

        painelCampos.add(new JLabel("Período Total (anos):"));
        spinnerPeriodoTotal = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        painelCampos.add(spinnerPeriodoTotal);

        painelPrincipal.add(painelCampos);

        // botão
        JPanel painelBotao = new JPanel();
        painelBotao.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));
        JButton botaoAdicionar = new JButton("Cadastrar Curso");
        ImageIcon addIcon = carregarIcone("/icons/add_icon.png", 20, 20);
        if (addIcon != null) {
            botaoAdicionar.setIcon(addIcon);
            botaoAdicionar.setIconTextGap(8);
        }
        botaoAdicionar.setBackground(new Color(0, 102, 204));
        botaoAdicionar.setForeground(Color.WHITE);
        botaoAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoAdicionar.addActionListener(e -> cadastrarCurso());
        painelBotao.add(botaoAdicionar);

        painelPrincipal.add(painelBotao);

        add(painelPrincipal, BorderLayout.CENTER);

        // cursos
        String[] colunas = {"ID", "Nome do Curso", "Data Cadastro", "Fases", "Anos"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCursos = new JTable(tableModel);

        tabelaCursos.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tabelaCursos.getColumnModel().getColumn(1).setPreferredWidth(250);  // Nome
        tabelaCursos.getColumnModel().getColumn(2).setPreferredWidth(100);  // Data
        tabelaCursos.getColumnModel().getColumn(3).setPreferredWidth(50);   // Fases
        tabelaCursos.getColumnModel().getColumn(4).setPreferredWidth(50);   // Anos

        tabelaCursos.setRowHeight(25);
        tabelaCursos.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaCursos);
        scrollPane.setPreferredSize(new Dimension(650, 180));

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        painelTabela.add(scrollPane, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.SOUTH);
    }

    private void atualizarTabela() {
        CursoDAO dao = new CursoDAO();
        DefaultTableModel model = dao.listarCursos();
        tabelaCursos.setModel(model);
    }
    
    // MÉTODO CORRIGIDO COM TRY-CATCH
    private void cadastrarCurso() {
        String nomeCurso = campoNomeCurso.getText().trim();
        int qtdFases = (Integer) spinnerQtdFases.getValue();
        int periodoTotal = (Integer) spinnerPeriodoTotal.getValue();

        if (nomeCurso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do curso!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        CursoDAO dao = new CursoDAO();
        try {
            dao.cadastrarCurso(nomeCurso, qtdFases, periodoTotal);
            
            // Apenas executa se o cadastro no banco for bem-sucedido
            atualizarTabela();
            campoNomeCurso.setText("");
            spinnerQtdFases.setValue(1);
            spinnerPeriodoTotal.setValue(1);
            JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            // Se ocorrer um erro no banco, mostra a mensagem
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar curso no banco de dados: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastrarCurso());
    }
}