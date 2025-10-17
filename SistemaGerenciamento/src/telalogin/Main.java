package telalogin;

import javax.swing.*;
import java.awt.*;
import static telalogin.IconUtils.carregarIcone;

public class Main extends JFrame {

    public Main() {
        configurarTela();
        criarComponentes();
        setVisible(true);
    }

    private void configurarTela() {
        setTitle("Sistema de Gerenciamento Acadêmico");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void criarComponentes() {
        // título
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel titulo = new JLabel("SISTEMA DE GERENCIAMENTO ACADÊMICO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        ImageIcon iconTitulo = carregarIcone("/icons/system_icon.png", 40, 40);
        if (iconTitulo != null) {
            titulo.setIcon(iconTitulo);
            titulo.setIconTextGap(10);
        }

        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        // Painel dos botões
        JPanel painelBotoes = new JPanel(new GridLayout(5, 1, 10, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(15, 50, 20, 50));

        JButton btnImportacao = criarBotaoMenu("Importação de Arquivos", new Color(46, 139, 87), "/icons/import_icon.png");
        JButton btnCursos = criarBotaoMenu("Cadastro de Cursos", new Color(60, 179, 113), "/icons/course_icon.png");
        JButton btnFases = criarBotaoMenu("Cadastro de Fases", new Color(0, 128, 0), "/icons/phase_icon.png");
        JButton btnDisciplinas = criarBotaoMenu("Cadastro de Disciplinas", new Color(34, 139, 34), "/icons/subject_icon.png");
        JButton btnProfessores = criarBotaoMenu("Cadastro de Professores", new Color(0, 100, 0), "/icons/teacher_icon.png");

        painelBotoes.add(btnImportacao);
        painelBotoes.add(btnCursos);
        painelBotoes.add(btnFases);
        painelBotoes.add(btnDisciplinas);
        painelBotoes.add(btnProfessores);

        // Listeners abrir as janelas
        btnImportacao.addActionListener(e -> new ImportarArquivo().setVisible(true));
        btnCursos.addActionListener(e -> new CadastrarCurso().setVisible(true));
        btnFases.addActionListener(e -> new CadastrarFases().setVisible(true));
        btnDisciplinas.addActionListener(e -> new CadastrarDisciplina().setVisible(true));
        btnProfessores.addActionListener(e -> new CadastrarProfessor().setVisible(true));

        add(painelBotoes, BorderLayout.CENTER);

        // Painel do rodapé
        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        painelRodape.setBackground(new Color(240, 240, 240));

        // Copyright
        JLabel rodape = new JLabel("© 2025 Sistema Acadêmico - Todos os direitos reservados");
        rodape.setFont(new Font("Arial", Font.PLAIN, 12));
        rodape.setBorder(BorderFactory.createEmptyBorder(55, 0, 0, 0));
        painelRodape.add(rodape, BorderLayout.WEST);

        // Logo Unesc
        ImageIcon unescIcon = carregarIcone("/icons/Unesc.png", 100, 100);
        if (unescIcon != null) {
            JLabel logoUnesc = new JLabel(unescIcon);
            painelRodape.add(logoUnesc, BorderLayout.EAST);
        }

        add(painelRodape, BorderLayout.SOUTH);
    }

    private JButton criarBotaoMenu(String texto, Color cor, String caminhoIcone) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(0, 75));
        botao.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        ImageIcon icon = carregarIcone(caminhoIcone, 30, 30);
        if (icon != null) {
            botao.setIcon(icon);
            botao.setHorizontalAlignment(SwingConstants.LEFT);
            botao.setIconTextGap(15);
        }

        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}