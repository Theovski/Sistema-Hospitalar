package hospital.view;

import hospital.model.Medico;
import hospital.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MedicoGUI extends JFrame {
    
    private Medico medico;
    private JTabbedPane abas;
    
    public MedicoGUI(Medico medico) {
        this.medico = medico;
        inicializarComponentes();
        Logger.info("MedicoGUI", "Interface do médico carregada: " + medico.getNome());
    }
    
    private void inicializarComponentes() {
        // Configurações da janela
        setTitle("Sistema Hospitalar - Médico: Dr(a). " + medico.getNome());
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
        
        // Aba 1: Agenda
        JPanel painelAgenda = criarPainelAgenda();
        abas.addTab("Minha Agenda", new ImageIcon(), painelAgenda, "Consultas agendadas");
        
        // Aba 2: Prontuários
        JPanel painelProntuarios = criarPainelProntuarios();
        abas.addTab("Prontuários", new ImageIcon(), painelProntuarios, "Histórico de pacientes");
        
        // Aba 3: Emitir Documentos
        JPanel painelDocumentos = criarPainelDocumentos();
        abas.addTab("Emitir Documentos", new ImageIcon(), painelDocumentos, "Atestados e receitas");
        
        // Aba 4: Gerenciar Internação
        JPanel painelInternacao = criarPainelInternacao();
        abas.addTab("Internação", new ImageIcon(), painelInternacao, "Controle de visitas");
        
        add(abas, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(26, 188, 156));
        painel.setPreferredSize(new Dimension(950, 80));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Informações do usuário
        JPanel painelInfo = new JPanel(new GridLayout(2, 1));
        painelInfo.setBackground(new Color(26, 188, 156));
        
        JLabel lblNome = new JLabel("Dr(a). " + medico.getNome());
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));
        lblNome.setForeground(Color.WHITE);
        
        JLabel lblInfo = new JLabel("CRM: " + medico.getCrm() + " | Especialidade: " + medico.getEspecialidade());
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(Color.WHITE);
        
        painelInfo.add(lblNome);
        painelInfo.add(lblInfo);
        
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
                    MedicoGUI.this,
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
    
    private JPanel criarPainelAgenda() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Agenda de Consultas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JTextArea areaAgenda = new JTextArea();
        areaAgenda.setEditable(false);
        areaAgenda.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaAgenda.setText("Consultas agendadas para hoje:\n\n" +
            "[Lista de consultas será exibida aqui]\n\n" +
            "Funcionalidades disponíveis:\n" +
            "- Visualizar consultas do dia\n" +
            "- Marcar presença/falta\n" +
            "- Concluir consulta\n" +
            "- Ver detalhes do paciente");
        
        JScrollPane scroll = new JScrollPane(areaAgenda);
        painel.add(scroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnPresenca = new JButton("Marcar Presença");
        btnPresenca.setBackground(new Color(39, 174, 96));
        btnPresenca.setForeground(Color.WHITE);
        btnPresenca.setFocusPainted(false);
        
        JButton btnFalta = new JButton("Marcar Falta");
        btnFalta.setBackground(new Color(231, 76, 60));
        btnFalta.setForeground(Color.WHITE);
        btnFalta.setFocusPainted(false);
        
        JButton btnConcluir = new JButton("Concluir Consulta");
        btnConcluir.setBackground(new Color(52, 152, 219));
        btnConcluir.setForeground(Color.WHITE);
        btnConcluir.setFocusPainted(false);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(149, 165, 166));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        
        painelBotoes.add(btnPresenca);
        painelBotoes.add(btnFalta);
        painelBotoes.add(btnConcluir);
        painelBotoes.add(btnAtualizar);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelProntuarios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Prontuários de Pacientes");
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
        
        JTextArea areaProntuarios = new JTextArea();
        areaProntuarios.setEditable(false);
        areaProntuarios.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaProntuarios.setText("Histórico médico do paciente:\n\n" +
            "[Prontuário será exibido após busca]");
        
        JScrollPane scroll = new JScrollPane(areaProntuarios);
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelBusca, BorderLayout.NORTH);
        painelCentral.add(scroll, BorderLayout.CENTER);
        
        painel.add(painelCentral, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelDocumentos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Emitir Documentos Médicos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel painelCentro = new JPanel(new GridLayout(3, 1, 10, 10));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Botão Atestado
        JButton btnAtestado = new JButton("Emitir Atestado Médico");
        btnAtestado.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtestado.setBackground(new Color(52, 152, 219));
        btnAtestado.setForeground(Color.WHITE);
        btnAtestado.setFocusPainted(false);
        btnAtestado.setPreferredSize(new Dimension(300, 60));
        btnAtestado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade de emitir atestado em desenvolvimento");
            }
        });
        
        // Botão Receita
        JButton btnReceita = new JButton("Emitir Receita Médica");
        btnReceita.setFont(new Font("Arial", Font.BOLD, 14));
        btnReceita.setBackground(new Color(26, 188, 156));
        btnReceita.setForeground(Color.WHITE);
        btnReceita.setFocusPainted(false);
        btnReceita.setPreferredSize(new Dimension(300, 60));
        btnReceita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade de emitir receita em desenvolvimento");
            }
        });
        
        // Botão Solicitar Exame
        JButton btnExame = new JButton("Solicitar Exame");
        btnExame.setFont(new Font("Arial", Font.BOLD, 14));
        btnExame.setBackground(new Color(155, 89, 182));
        btnExame.setForeground(Color.WHITE);
        btnExame.setFocusPainted(false);
        btnExame.setPreferredSize(new Dimension(300, 60));
        btnExame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Funcionalidade de solicitar exame em desenvolvimento");
            }
        });
        
        painelCentro.add(btnAtestado);
        painelCentro.add(btnReceita);
        painelCentro.add(btnExame);
        
        painel.add(painelCentro, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelInternacao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Controle de Internação e Visitas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        JTextArea areaInternacao = new JTextArea();
        areaInternacao.setEditable(false);
        areaInternacao.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaInternacao.setText("Pacientes internados:\n\n" +
            "[Lista de pacientes internados será exibida aqui]\n\n" +
            "Funcionalidades:\n" +
            "- Liberar visitação\n" +
            "- Proibir visitação\n" +
            "- Ver status de internação");
        
        JScrollPane scroll = new JScrollPane(areaInternacao);
        painel.add(scroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnLiberar = new JButton("Liberar Visitas");
        btnLiberar.setBackground(new Color(39, 174, 96));
        btnLiberar.setForeground(Color.WHITE);
        btnLiberar.setFocusPainted(false);
        
        JButton btnProibir = new JButton("Proibir Visitas");
        btnProibir.setBackground(new Color(231, 76, 60));
        btnProibir.setForeground(Color.WHITE);
        btnProibir.setFocusPainted(false);
        
        painelBotoes.add(btnLiberar);
        painelBotoes.add(btnProibir);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
}
