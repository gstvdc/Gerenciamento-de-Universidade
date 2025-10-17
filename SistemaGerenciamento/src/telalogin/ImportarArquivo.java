package telalogin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import banco.CursoDAO;
import banco.DisciplinaDAO;
import banco.FaseDAO;
import banco.ProfessorDAO;

import static telalogin.IconUtils.carregarIcone;

public class ImportarArquivo extends JFrame {
    private File arquivoSelecionado;
    private JLabel labelArquivo;
    private JTable tabelaDados;
    private DefaultTableModel tableModel;

    public ImportarArquivo() {
        configurarTela();
        criarComponentes();
        setVisible(true);
    }

    private void configurarTela() {
        setTitle("IMPORTAÇÃO DE ARQUIVO - ESTRUTURA DE CURSO");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void criarComponentes() {
        JPanel painelTitulo = new JPanel();
        JLabel titulo = new JLabel("Selecione um arquivo com estrutura de curso:");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        ImageIcon iconTitulo = carregarIcone("/icons/import_icon.png", 32, 32);
        if (iconTitulo != null) {
            titulo.setIcon(iconTitulo);
            titulo.setIconTextGap(10);
        }
        
        painelTitulo.add(titulo);
        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnEscolher = new JButton("Escolher arquivo");
        ImageIcon folderIcon = carregarIcone("/icons/folder_icon.png", 20, 20);
        if (folderIcon != null) {
            btnEscolher.setIcon(folderIcon);
            btnEscolher.setIconTextGap(8);
        }
        
        btnEscolher.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEscolher.setBackground(new Color(70, 130, 180));
        btnEscolher.setForeground(Color.WHITE);
        btnEscolher.setFont(new Font("Arial", Font.BOLD, 14));
        btnEscolher.addActionListener(e -> escolherArquivo());

        labelArquivo = new JLabel("Nenhum arquivo selecionado", SwingConstants.CENTER);
        labelArquivo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelArquivo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelArquivo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnImportar = new JButton("Importar");
        ImageIcon importIcon = carregarIcone("/icons/import_icon.png", 20, 20);
        if (importIcon != null) {
            btnImportar.setIcon(importIcon);
            btnImportar.setIconTextGap(8);
        }
        btnImportar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImportar.setBackground(Color.DARK_GRAY);
        btnImportar.setForeground(Color.WHITE);
        btnImportar.setFont(new Font("Arial", Font.BOLD, 14));
        btnImportar.addActionListener(e -> new Thread(this::importarArquivo).start());

        painelSuperior.add(btnEscolher);
        painelSuperior.add(labelArquivo);
        painelSuperior.add(Box.createVerticalStrut(10));
        painelSuperior.add(btnImportar);

        add(painelSuperior, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Curso", "Fase", "Código Disciplina", "Professor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaDados = new JTable(tableModel);
        tabelaDados.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaDados.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabelaDados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estrutura do Curso"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void escolherArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Arquivo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos TXT", "txt"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            arquivoSelecionado = fileChooser.getSelectedFile();
            SwingUtilities.invokeLater(() -> {
                labelArquivo.setText("Selecionado: " + arquivoSelecionado.getName());
                tableModel.setRowCount(0);
            });
        }
    }

    private void importarArquivo() {
        if (arquivoSelecionado == null) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, "Nenhum arquivo selecionado!",
                        "Erro", JOptionPane.ERROR_MESSAGE));
            return;
        }

        CursoDAO cursoDAO = new CursoDAO();
        FaseDAO faseDAO = new FaseDAO();
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        ProfessorDAO professorDAO = new ProfessorDAO();

        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Curso", "Fase", "Código Disciplina", "Professor"});
        });

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoSelecionado))) {
            String linha;
            int cursoId = -1;
            int faseId = -1;
            int disciplinaCodigo = -1;
            String cursoNomeAtual = "";
            String faseNomeAtual = "";

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("9")) continue;

                if (linha.startsWith("0")) {
                    cursoNomeAtual = linha.substring(1, 41).trim();
                    
                    // ===== LINHA CORRIGIDA AQUI =====
                    cursoId = cursoDAO.cadastrarCurso(cursoNomeAtual, 1, 1);
                    // ================================

                    if (cursoId == -1) throw new IOException("Falha ao cadastrar o curso: " + cursoNomeAtual);

                } else if (linha.startsWith("1")) {
                    if (cursoId == -1) continue;
                    faseNomeAtual = linha.substring(1, 9).trim();
                    int qtdDisciplinas = Integer.parseInt(linha.substring(9, 11));
                    faseId = faseDAO.cadastrarFase(faseNomeAtual, cursoId, qtdDisciplinas);
                    if (faseId == -1) throw new IOException("Falha ao cadastrar a fase: " + faseNomeAtual);

                } else if (linha.startsWith("2")) {
                    String[] partes = linha.substring(1).trim().split("\\s+");
                    disciplinaCodigo = Integer.parseInt(partes[0]);

                } else if (linha.startsWith("3")) {
                    if (faseId == -1 || disciplinaCodigo == -1) continue;
                    String nomeProfessor = linha.substring(1, 41).trim();
                    String nomeDisciplina = "Disciplina " + disciplinaCodigo;
                    int disciplinaId = disciplinaDAO.importarDisciplina(nomeDisciplina, disciplinaCodigo, faseId);
                    if (disciplinaId == -1) throw new IOException("Falha ao cadastrar a disciplina: " + disciplinaCodigo);
                    
                    professorDAO.cadastrarProfessor(nomeProfessor, disciplinaId, 1);
                    
                    final String cursoParaTabela = cursoNomeAtual;
                    final String faseParaTabela = faseNomeAtual;
                    final int codigoParaTabela = disciplinaCodigo;
                    final String professorParaTabela = nomeProfessor;
                    SwingUtilities.invokeLater(() -> tableModel.addRow(new Object[]{cursoParaTabela, faseParaTabela, codigoParaTabela, professorParaTabela}));
                    disciplinaCodigo = -1;
                }
            }
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Arquivo importado e dados salvos no banco com sucesso!", "Importação Concluída", JOptionPane.INFORMATION_MESSAGE));

        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erro de Leitura/Gravação no arquivo: " + e.getMessage(), "Erro de Importação", JOptionPane.ERROR_MESSAGE));
            e.printStackTrace();
        } catch (NumberFormatException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erro de formato numérico no arquivo: " + e.getMessage(), "Erro de Importação", JOptionPane.ERROR_MESSAGE));
            e.printStackTrace();
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erro de Banco de Dados durante a importação: " + e.getMessage(), "Erro de Importação", JOptionPane.ERROR_MESSAGE));
            e.printStackTrace();
        }
    }

    private List<String> lerArquivo(File arquivo) throws IOException {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(linha);
                }
            }
        }
        return linhas;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImportarArquivo::new);
    }
}