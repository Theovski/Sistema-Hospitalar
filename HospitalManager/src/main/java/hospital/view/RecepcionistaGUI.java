package hospital.view;

import hospital.model.Recepcionista;
import hospital.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecepcionistaGUI extends JFrame {
    
    private Recepcionista recepcionista;
    private JTabbedPane abas;
    
    public RecepcionistaGUI(Recepcionista recepcionista) {
        this.recepcionista = recepcionista;
        inicializarComponentes();
        Logger.info("RecepcionistaGUI", "Interface da recepcionista carregada: " + recepcionista.getNome());
    }
    
    private void inicializarComponentes() {
        // Configurações da janela
        setTitle("Sistema Hospitalar - Recepcionista: " + recepcionista.getNome());
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Painel do cabeçalho
        JPanel painelTopo = criarPainelTopo();
        add(painelTopo, BorderLayout.NORTH);
        
        // Abas principais
        abas = new JTabbedPane();
        abas.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Aba 1: Cadastro
        JPanel painelCadastro = criarPainelCadastro();
        abas.addTab("Cadastros", new ImageIcon(), painelCadastro, "Cadastrar pacientes e médicos");
        
        // Aba 2: Visitas
        JPanel painelVisitas = criarPainelVisitas();
        abas.addTab("Controle de Visitas", new ImageIcon(), painelVisitas, "Gerenciar visitas");
        
        // Aba 3: Monitoramento
        JPanel painelMonitoramento = criarPainelMonitoramento();
        abas.addTab("Monitoramento", new ImageIcon(), painelMonitoramento, "Faltas e disponibilidade");
        
        // Aba 4: Consultas
        JPanel painelConsultas = criarPainelConsultas();
        abas.addTab("Consultas", new ImageIcon(), painelConsultas, "Visualizar agendamentos");
        
        add(abas, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(142, 68, 173));
        painel.setPreferredSize(new Dimension(950, 80));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Informações do usuário
        JPanel painelInfo = new JPanel(new GridLayout(2, 1));
        painelInfo.setBackground(new Color(142, 68, 173));
        
        JLabel lblNome = new JLabel("Recepcionista: " + recepcionista.getNome());
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));
        lblNome.setForeground(Color.WHITE);
        
        JLabel lblMatricula = new JLabel("Matrícula: " + recepcionista.getMatricula());
        lblMatricula.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMatricula.setForeground(Color.WHITE);
        
        painelInfo.add(lblNome);
        painelInfo.add(lblMatricula);
        
        painel.add(painelInfo, BorderLayout.WEST);
        
        // Botão Sair
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 12));
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcao = JOptionPane.showConfirmDialog(
                    RecepcionistaGUI.this,
                    "Deseja realmente sair?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION
                );
                if (opcao == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginGUI().setVisible(true);
                }
            }
        });
        
        painel.add(btnSair, BorderLayout.EAST);
        
        return painel;
    }
    
    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Gerenciamento de Cadastros");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel painelCentro = new JPanel(new GridLayout(2, 2, 20, 20));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        // Botão Cadastrar Paciente
        JButton btnCadastrarPaciente = new JButton("<html><center>Cadastrar<br>Paciente</center></html>");
        btnCadastrarPaciente.setFont(new Font("Arial", Font.BOLD, 16));
        btnCadastrarPaciente.setBackground(new Color(52, 152, 219));
        btnCadastrarPaciente.setForeground(Color.WHITE);
        btnCadastrarPaciente.setFocusPainted(false);
        btnCadastrarPaciente.setPreferredSize(new Dimension(200, 100));
        btnCadastrarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade de cadastrar paciente em desenvolvimento");
            }
        });
        
        // Botão Cadastrar Médico
        JButton btnCadastrarMedico = new JButton("<html><center>Cadastrar<br>Médico</center></html>");
        btnCadastrarMedico.setFont(new Font("Arial", Font.BOLD, 16));
        btnCadastrarMedico.setBackground(new Color(26, 188, 156));
        btnCadastrarMedico.setForeground(Color.WHITE);
        btnCadastrarMedico.setFocusPainted(false);
        btnCadastrarMedico.setPreferredSize(new Dimension(200, 100));
        btnCadastrarMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade de cadastrar médico em desenvolvimento");
            }
        });
        
        // Botão Listar Pacientes
        JButton btnListarPacientes = new JButton("<html><center>Listar<br>Pacientes</center></html>");
        btnListarPacientes.setFont(new Font("Arial", Font.BOLD, 16));
        btnListarPacientes.setBackground(new Color(155, 89, 182));
        btnListarPacientes.setForeground(Color.WHITE);
        btnListarPacientes.setFocusPainted(false);
        btnListarPacientes.setPreferredSize(new Dimension(200, 100));
        
        // Botão Gerenciar Médicos
        JButton btnGerenciarMedicos = new JButton("<html><center>Gerenciar<br>Médicos</center></html>");
        btnGerenciarMedicos.setFont(new Font("Arial", Font.BOLD, 16));
        btnGerenciarMedicos.setBackground(new Color(230, 126, 34));
        btnGerenciarMedicos.setForeground(Color.WHITE);
        btnGerenciarMedicos.setFocusPainted(false);
        btnGerenciarMedicos.setPreferredSize(new Dimension(200, 100));
        btnGerenciarMedicos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade de ativar/desativar médicos em desenvolvimento");
            }
        });
        
        painelCentro.add(btnCadastrarPaciente);
        painelCentro.add(btnCadastrarMedico);
        painelCentro.add(btnListarPacientes);
        painelCentro.add(btnGerenciarMedicos);
        
        painel.add(painelCentro, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelVisitas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Controle de Visitas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.add(new JLabel("Buscar paciente (CPF):"));
        JTextField txtBusca = new JTextField(15);
        painelBusca.add(txtBusca);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        painelBusca.add(btnBuscar);
        
        JTextArea areaVisitas = new JTextArea();
        areaVisitas.setEditable(false);
        areaVisitas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaVisitas.setText("Status de visitação dos pacientes:\n\n" +
            "[Lista de pacientes internados será exibida aqui]\n\n" +
            "Funcionalidades:\n" +
            "- Visualizar status de visitação\n" +
            "- Registrar entrada/saída de visitantes\n" +
            "- Ver histórico de visitas");
        
        JScrollPane scroll = new JScrollPane(areaVisitas);
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelBusca, BorderLayout.NORTH);
        painelCentral.add(scroll, BorderLayout.CENTER);
        
        painel.add(painelCentral, BorderLayout.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnRegistrarVisita = new JButton("Registrar Visita");
        btnRegistrarVisita.setBackground(new Color(39, 174, 96));
        btnRegistrarVisita.setForeground(Color.WHITE);
        btnRegistrarVisita.setFocusPainted(false);
        
        painelBotoes.add(btnRegistrarVisita);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelMonitoramento() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Monitoramento de Faltas e Disponibilidade");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JTextArea areaMonitoramento = new JTextArea();
        areaMonitoramento.setEditable(false);
        areaMonitoramento.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMonitoramento.setText("Relatório de faltas e disponibilidade:\n\n" +
            "PACIENTES COM FALTAS RECENTES:\n" +
            "[Lista de pacientes com faltas]\n\n" +
            "MÉDICOS DISPONÍVEIS:\n" +
            "[Lista de médicos ativos]\n\n" +
            "AGENDA DO DIA:\n" +
            "[Resumo de consultas agendadas]");
        
        JScrollPane scroll = new JScrollPane(areaMonitoramento);
        painel.add(scroll, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAtualizar = new JButton("Atualizar Dados");
        btnAtualizar.setBackground(new Color(52, 152, 219));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        
        JButton btnRelatorio = new JButton("Gerar Relatório");
        btnRelatorio.setBackground(new Color(230, 126, 34));
        btnRelatorio.setForeground(Color.WHITE);
        btnRelatorio.setFocusPainted(false);
        
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRelatorio);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelConsultas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Consultas Agendadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JTextArea areaConsultas = new JTextArea();
        areaConsultas.setEditable(false);
        areaConsultas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaConsultas.setText("Consultas do dia:\n\n" +
            "[Lista de todas as consultas agendadas]\n\n" +
            "Visualização completa dos agendamentos do hospital.");
        
        JScrollPane scroll = new JScrollPane(areaConsultas);
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
}
