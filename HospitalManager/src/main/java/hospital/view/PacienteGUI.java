package hospital.view;

import hospital.model.Paciente;
import hospital.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PacienteGUI extends JFrame {
    
    private Paciente paciente;
    private JTabbedPane abas;
    
    public PacienteGUI(Paciente paciente) {
        this.paciente = paciente;
        inicializarComponentes();
        Logger.info("PacienteGUI", "Interface do paciente carregada: " + paciente.getNome());
    }
    
    private void inicializarComponentes() {
        // Configurações da janela
        setTitle("Sistema Hospitalar - Paciente: " + paciente.getNome());
        setSize(900, 600);
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
        
        // Aba 1: Minhas Consultas
        JPanel painelConsultas = criarPainelConsultas();
        abas.addTab("Minhas Consultas", new ImageIcon(), painelConsultas, "Agendar e visualizar consultas");
        
        // Aba 2: Histórico
        JPanel painelHistorico = criarPainelHistorico();
        abas.addTab("Histórico", new ImageIcon(), painelHistorico, "Ver histórico médico");
        
        // Aba 3: Documentos
        JPanel painelDocumentos = criarPainelDocumentos();
        abas.addTab("Documentos", new ImageIcon(), painelDocumentos, "Exames, atestados e receitas");
        
        // Aba 4: Visitas
        JPanel painelVisitas = criarPainelVisitas();
        abas.addTab("Visitação", new ImageIcon(), painelVisitas, "Status de visitação");
        
        add(abas, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(52, 152, 219));
        painel.setPreferredSize(new Dimension(900, 80));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Informações do usuário
        JPanel painelInfo = new JPanel(new GridLayout(2, 1));
        painelInfo.setBackground(new Color(52, 152, 219));
        
        JLabel lblNome = new JLabel("Bem-vindo(a), " + paciente.getNome());
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));
        lblNome.setForeground(Color.WHITE);
        
        JLabel lblCPF = new JLabel("CPF: " + paciente.getCpf() + " | Convênio: " + 
            (paciente.getConvenio() != null ? paciente.getConvenio() : "Particular"));
        lblCPF.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCPF.setForeground(Color.WHITE);
        
        painelInfo.add(lblNome);
        painelInfo.add(lblCPF);
        
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
                    PacienteGUI.this,
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
    
    private JPanel criarPainelConsultas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Área de título
        JLabel lblTitulo = new JLabel("Gerenciamento de Consultas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        // Área central com lista de consultas
        JTextArea areaConsultas = new JTextArea();
        areaConsultas.setEditable(false);
        areaConsultas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaConsultas.setText("Carregando consultas...\n\n" +
            "Esta funcionalidade será implementada com:\n" +
            "- Lista de consultas agendadas\n" +
            "- Botão para agendar nova consulta\n" +
            "- Opção de cancelar consultas");
        
        JScrollPane scroll = new JScrollPane(areaConsultas);
        painel.add(scroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAgendar = new JButton("Agendar Consulta");
        btnAgendar.setBackground(new Color(39, 174, 96));
        btnAgendar.setForeground(Color.WHITE);
        btnAgendar.setFocusPainted(false);
        btnAgendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade em desenvolvimento");
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar Consulta");
        btnCancelar.setBackground(new Color(230, 126, 34));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(52, 152, 219));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        
        painelBotoes.add(btnAgendar);
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnAtualizar);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelHistorico() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Histórico de Consultas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JTextArea areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaHistorico.setText("Histórico de consultas realizadas:\n\n" +
            "[Lista de consultas anteriores será exibida aqui]");
        
        JScrollPane scroll = new JScrollPane(areaHistorico);
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelDocumentos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Meus Documentos Médicos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JTextArea areaDocumentos = new JTextArea();
        areaDocumentos.setEditable(false);
        areaDocumentos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDocumentos.setText("Documentos disponíveis:\n\n" +
            "- Exames\n" +
            "- Atestados\n" +
            "- Receitas\n\n" +
            "[Documentos serão listados aqui]");
        
        JScrollPane scroll = new JScrollPane(areaDocumentos);
        painel.add(scroll, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelVisitas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Status de Visitação");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel painelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel lblStatus = new JLabel("Status atual de visitação:");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCentro.add(lblStatus, gbc);
        
        String statusTexto = paciente.isAptoVisita() ? "LIBERADA" : "PROIBIDA";
        Color statusCor = paciente.isAptoVisita() ? new Color(39, 174, 96) : new Color(231, 76, 60);
        
        JLabel lblStatusValor = new JLabel(statusTexto);
        lblStatusValor.setFont(new Font("Arial", Font.BOLD, 24));
        lblStatusValor.setForeground(statusCor);
        gbc.gridy = 1;
        painelCentro.add(lblStatusValor, gbc);
        
        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        areaInfo.setText("\nInformações sobre visitação:\n" +
            "- Horário de visitas: 14h às 18h\n" +
            "- Máximo de 2 visitantes por vez\n" +
            "- Apresentar documento com foto");
        areaInfo.setOpaque(false);
        gbc.gridy = 2;
        painelCentro.add(areaInfo, gbc);
        
        painel.add(painelCentro, BorderLayout.CENTER);
        
        return painel;
    }
}
