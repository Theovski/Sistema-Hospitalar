package hospital.view;

import hospital.model.Paciente;
import hospital.model.Consulta;
import hospital.model.Medico;
import hospital.model.Receita;
import hospital.model.Atestado;
import hospital.model.Exame;
import hospital.service.AgendamentoService;
import hospital.service.DocumentoService;
import hospital.service.CadastroService;
import hospital.dao.ConsultaDAO;
import hospital.dao.MedicoDAO;
import hospital.dao.DocumentoDAO;
import hospital.dao.PacienteDAO;
import hospital.util.Logger;
import hospital.util.DataHoraUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PacienteGUI extends JFrame {
    
    private Paciente paciente;
    private JTabbedPane abas;
    private AgendamentoService agendamentoService;
    private DocumentoService documentoService;
    private ConsultaDAO consultaDAO;
    private MedicoDAO medicoDAO;
    private DocumentoDAO documentoDAO;
    private CadastroService cadastroService;
    private PacienteDAO pacienteDAO;
    
    // Componentes para atualização dinâmica
    private JTextArea areaConsultas;
    private JTextArea areaHistorico;
    private JTextArea areaDocumentos;
    
    public PacienteGUI(Paciente paciente) {
        this.paciente = paciente;
        this.agendamentoService = new AgendamentoService();
        this.documentoService = new DocumentoService();
        this.consultaDAO = new ConsultaDAO();
        this.medicoDAO = new MedicoDAO();
        this.documentoDAO = new DocumentoDAO();
        this.cadastroService = new CadastroService();
        this.pacienteDAO = new PacienteDAO();
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
        
        // Aba 5: Meus Dados
        JPanel painelMeusDados = criarPainelMeusDados();
        abas.addTab("Meus Dados", new ImageIcon(), painelMeusDados, "Editar dados pessoais");
        
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
        areaConsultas = new JTextArea();
        areaConsultas.setEditable(false);
        areaConsultas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        carregarConsultas();
        
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
                abrirDialogoAgendamento();
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar Consulta");
        btnCancelar.setBackground(new Color(230, 126, 34));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarConsulta();
            }
        });
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(52, 152, 219));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarConsultas();
                JOptionPane.showMessageDialog(painel, "Consultas atualizadas!");
            }
        });
        
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
        
        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        carregarHistorico();
        
        JScrollPane scroll = new JScrollPane(areaHistorico);
        painel.add(scroll, BorderLayout.CENTER);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(52, 152, 219));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarHistorico();
            }
        });
        
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotao.add(btnAtualizar);
        painel.add(painelBotao, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelDocumentos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Meus Documentos Médicos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        areaDocumentos = new JTextArea();
        areaDocumentos.setEditable(false);
        areaDocumentos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        carregarDocumentos();
        
        JScrollPane scroll = new JScrollPane(areaDocumentos);
        painel.add(scroll, BorderLayout.CENTER);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(52, 152, 219));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDocumentos();
            }
        });
        
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotao.add(btnAtualizar);
        painel.add(painelBotao, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelVisitas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Consultar Status de Visitação");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Paciente"));
        
        JLabel lblBusca = new JLabel("CPF do Paciente:");
        lblBusca.setFont(new Font("Arial", Font.PLAIN, 12));
        painelBusca.add(lblBusca);
        
        JTextField txtCpfBusca = new JTextField(15);
        painelBusca.add(txtCpfBusca);
        
        JButton btnBuscar = new JButton("Consultar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        painelBusca.add(btnBuscar);
        
        JButton btnMeuStatus = new JButton("Meu Status");
        btnMeuStatus.setBackground(new Color(155, 89, 182));
        btnMeuStatus.setForeground(Color.WHITE);
        btnMeuStatus.setFocusPainted(false);
        painelBusca.add(btnMeuStatus);
        
        // Área de resultado
        JTextArea areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultado.setText("Digite o CPF de um paciente e clique em 'Consultar' para\n" +
            "verificar se ele está apto a receber visitas.\n\n" +
            "OU\n\n" +
            "Clique em 'Meu Status' para ver seu próprio status.\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "HORÁRIOS DE VISITAÇÃO:\n" +
            "  Segunda a Sexta: 14h às 18h\n" +
            "  Sábados e Domingos: 10h às 12h e 14h às 16h\n\n" +
            "REGRAS:\n" +
            "  • Máximo de 2 visitantes por vez\n" +
            "  • Apresentar documento com foto\n" +
            "  • Respeitar o status de visitação definido pelo médico");
        
        JScrollPane scroll = new JScrollPane(areaResultado);
        
        // Ação do botão Buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpfBusca = txtCpfBusca.getText().trim();
                
                if (cpfBusca.isEmpty()) {
                    JOptionPane.showMessageDialog(painel,
                        "Por favor, digite um CPF para consultar.",
                        "Campo Vazio",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Buscar paciente
                Paciente pacienteBuscado = pacienteDAO.buscarPorId(cpfBusca);
                
                if (pacienteBuscado == null) {
                    areaResultado.setText("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                        "❌ PACIENTE NÃO ENCONTRADO\n\n" +
                        "CPF: " + cpfBusca + "\n\n" +
                        "O CPF informado não está cadastrado no sistema.\n" +
                        "Verifique se digitou corretamente.\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    return;
                }
                
                // Exibir informações
                StringBuilder sb = new StringBuilder();
                sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
                sb.append("📋 INFORMAÇÕES DO PACIENTE\n\n");
                sb.append("Nome: ").append(pacienteBuscado.getNome()).append("\n");
                sb.append("CPF: ").append(pacienteBuscado.getCpf()).append("\n\n");
                
                boolean aptoVisita = pacienteBuscado.isAptoVisita();
                
                if (aptoVisita) {
                    sb.append("✅ STATUS DE VISITAÇÃO: LIBERADA\n\n");
                    sb.append("Este paciente está APTO a receber visitas.\n\n");
                    sb.append("Horários permitidos:\n");
                    sb.append("  • Segunda a Sexta: 14h às 18h\n");
                    sb.append("  • Sábados e Domingos: 10h às 12h e 14h às 16h\n\n");
                    sb.append("Lembre-se:\n");
                    sb.append("  • Máximo de 2 visitantes por vez\n");
                    sb.append("  • Apresentar documento com foto na recepção\n");
                } else {
                    sb.append("🚫 STATUS DE VISITAÇÃO: PROIBIDA\n\n");
                    sb.append("Este paciente NÃO está apto a receber visitas no momento.\n\n");
                    sb.append("A visitação foi restrita por decisão médica.\n");
                    sb.append("Entre em contato com a equipe médica para mais informações.\n");
                }
                
                sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                
                areaResultado.setText(sb.toString());
                areaResultado.setCaretPosition(0);
                
                Logger.info("PacienteGUI", "Consulta de visitação: " + cpfBusca + 
                    " - Status: " + (aptoVisita ? "LIBERADA" : "PROIBIDA"));
            }
        });
        
        // Ação do botão Meu Status
        btnMeuStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCpfBusca.setText(paciente.getCpf());
                btnBuscar.doClick();
            }
        });
        
        // Ação ao pressionar Enter no campo de busca
        txtCpfBusca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnBuscar.doClick();
            }
        });
        
        // Montagem do painel
        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.add(painelBusca, BorderLayout.NORTH);
        painelCentral.add(scroll, BorderLayout.CENTER);
        
        painel.add(painelCentral, BorderLayout.CENTER);
        
        return painel;
    }
    
    // ==================== MÉTODOS DE LÓGICA DE NEGÓCIO ====================
    
    private void carregarConsultas() {
        List<Consulta> consultas = consultaDAO.listarPorPaciente(paciente.getCpf());
        StringBuilder sb = new StringBuilder();
        sb.append("=== MINHAS CONSULTAS AGENDADAS ===\n\n");
        
        boolean temConsultas = false;
        for (Consulta c : consultas) {
            if (c.getDataHora().isAfter(LocalDateTime.now()) && c.estaAgendada()) {
                temConsultas = true;
                Medico medico = medicoDAO.buscarPorId(c.getMedicoCrm());
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Data/Hora: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("Médico: ").append(medico != null ? medico.getNome() : "N/A").append("\n");
                sb.append("Especialidade: ").append(medico != null ? medico.getEspecialidade() : "N/A").append("\n");
                sb.append("Status: ").append(c.getStatus()).append("\n");
                sb.append("\n").append("=".repeat(50)).append("\n\n");
            }
        }
        
        if (!temConsultas) {
            sb.append("Nenhuma consulta agendada.\n");
            sb.append("\nClique em 'Agendar Consulta' para marcar uma nova consulta.");
        }
        
        areaConsultas.setText(sb.toString());
        areaConsultas.setCaretPosition(0);
    }
    
    private void carregarHistorico() {
        List<Consulta> consultas = consultaDAO.listarPorPaciente(paciente.getCpf());
        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTÓRICO DE CONSULTAS ===\n\n");
        
        boolean temHistorico = false;
        for (Consulta c : consultas) {
            if (c.getDataHora().isBefore(LocalDateTime.now()) || !c.estaAgendada()) {
                temHistorico = true;
                Medico medico = medicoDAO.buscarPorId(c.getMedicoCrm());
                sb.append("Data/Hora: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("Médico: ").append(medico != null ? medico.getNome() : "N/A").append("\n");
                sb.append("Status: ").append(c.getStatus()).append("\n");
                if (c.getObservacoes() != null && !c.getObservacoes().isEmpty()) {
                    sb.append("Observações: ").append(c.getObservacoes()).append("\n");
                }
                sb.append("\n").append("=".repeat(50)).append("\n\n");
            }
        }
        
        if (!temHistorico) {
            sb.append("Nenhuma consulta realizada ainda.");
        }
        
        areaHistorico.setText(sb.toString());
        areaHistorico.setCaretPosition(0);
    }
    
    private void carregarDocumentos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MEUS DOCUMENTOS MÉDICOS ===\n\n");
        
        try {
            // Receitas
            List<Receita> receitas = documentoService.listarReceitasPorPaciente(paciente.getCpf());
            sb.append("📋 RECEITAS MÉDICAS (").append(receitas.size()).append("):\n");
            if (receitas.isEmpty()) {
                sb.append("  Nenhuma receita disponível\n");
            } else {
                for (Receita r : receitas) {
                    sb.append("  • ID: ").append(r.getId())
                      .append(" | Data: ").append(r.getData())
                      .append("\n    Orientações: ").append(r.getInstrucoes())
                      .append("\n");
                }
            }
            sb.append("\n");
            
            // Atestados
            List<Atestado> atestados = documentoDAO.listarAtestadosPorPaciente(paciente.getCpf());
            sb.append("📄 ATESTADOS MÉDICOS (").append(atestados.size()).append("):\n");
            if (atestados.isEmpty()) {
                sb.append("  Nenhum atestado disponível\n");
            } else {
                for (Atestado a : atestados) {
                    sb.append("  • ID: ").append(a.getId())
                      .append(" | Data: ").append(a.getDataEmissao())
                      .append("\n    CID: ").append(a.getCid())
                      .append(" | Dias: ").append(a.getDiasRepouso())
                      .append("\n    Diagnóstico: ").append(a.getDiagnostico())
                      .append("\n");
                }
            }
            sb.append("\n");
            
            // Exames
            List<Exame> exames = documentoDAO.listarExamesPorPaciente(paciente.getCpf());
            sb.append("🔬 EXAMES (").append(exames.size()).append("):\n");
            if (exames.isEmpty()) {
                sb.append("  Nenhum exame disponível\n");
            } else {
                for (Exame e : exames) {
                    sb.append("  • ID: ").append(e.getId())
                      .append(" | Tipo: ").append(e.getTipoExame())
                      .append("\n    Data: ").append(e.getDataRealizacao() != null ? e.getDataRealizacao() : "Pendente")
                      .append("\n    Resultado: ").append(e.getResultado() != null ? e.getResultado() : "Em análise")
                      .append("\n");
                }
            }
            
        } catch (Exception e) {
            sb.append("Erro ao carregar documentos: ").append(e.getMessage()).append("\n");
            Logger.erro("PacienteGUI", "Erro ao carregar documentos", e);
        }
        
        sb.append("\n").append("=".repeat(50)).append("\n");
        sb.append("\n💡 Para solicitar novos exames ou atestados, consulte seu médico.");
        
        areaDocumentos.setText(sb.toString());
        areaDocumentos.setCaretPosition(0);
    }
    
    private void abrirDialogoAgendamento() {
        JDialog dialogo = new JDialog(this, "Agendar Consulta", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(10, 10));
        
        JPanel painelCentro = new JPanel(new GridBagLayout());
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Selecionar médico
        gbc.gridx = 0; gbc.gridy = 0;
        painelCentro.add(new JLabel("Selecione o Médico:"), gbc);
        
        List<Medico> medicosAtivos = medicoDAO.listarAtivos();
        JComboBox<String> comboMedicos = new JComboBox<>();
        for (Medico m : medicosAtivos) {
            comboMedicos.addItem(m.getCrm() + " - " + m.getNome() + " (" + m.getEspecialidade() + ")");
        }
        gbc.gridy = 1;
        painelCentro.add(comboMedicos, gbc);
        
        // Data
        gbc.gridy = 2;
        painelCentro.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        JTextField txtData = new JTextField();
        gbc.gridy = 3;
        painelCentro.add(txtData, gbc);
        
        // Hora
        gbc.gridy = 4;
        painelCentro.add(new JLabel("Hora (HH:mm):"), gbc);
        JTextField txtHora = new JTextField();
        gbc.gridy = 5;
        painelCentro.add(txtHora, gbc);
        
        dialogo.add(painelCentro, BorderLayout.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (medicosAtivos.isEmpty()) {
                        JOptionPane.showMessageDialog(dialogo, "Nenhum médico disponível!");
                        return;
                    }
                    
                    String medicoSelecionado = (String) comboMedicos.getSelectedItem();
                    String crm = medicoSelecionado.split(" - ")[0];
                    String dataHoraStr = txtData.getText().trim() + " " + txtHora.getText().trim();
                    
                    LocalDateTime dataHora = DataHoraUtil.parse(dataHoraStr);
                    
                    Consulta consulta = new Consulta(
                        "CONS" + System.currentTimeMillis(),
                        paciente.getCpf(),
                        crm,
                        dataHora
                    );
                    
                    agendamentoService.agendar(consulta);
                    
                    JOptionPane.showMessageDialog(dialogo, 
                        "Consulta agendada com sucesso!\nID: " + consulta.getId(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    
                    dialogo.dispose();
                    carregarConsultas();
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "Erro ao agendar consulta: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCancelar);
        dialogo.add(painelBotoes, BorderLayout.SOUTH);
        
        dialogo.setVisible(true);
    }
    
    private void cancelarConsulta() {
        String idConsulta = JOptionPane.showInputDialog(this, 
            "Digite o ID da consulta que deseja cancelar:",
            "Cancelar Consulta",
            JOptionPane.QUESTION_MESSAGE);
        
        if (idConsulta == null || idConsulta.trim().isEmpty()) {
            return;
        }
        
        try {
            Consulta consulta = consultaDAO.buscarPorId(idConsulta.trim());
            
            if (consulta == null) {
                JOptionPane.showMessageDialog(this, 
                    "Consulta não encontrada!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!consulta.getPacienteCpf().equals(paciente.getCpf())) {
                JOptionPane.showMessageDialog(this, 
                    "Esta consulta não pertence a você!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirma = JOptionPane.showConfirmDialog(this,
                "Deseja realmente cancelar a consulta do dia " + 
                DataHoraUtil.formatar(consulta.getDataHora()) + "?",
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION);
            
            if (confirma == JOptionPane.YES_OPTION) {
                agendamentoService.cancelarConsulta(idConsulta.trim());
                JOptionPane.showMessageDialog(this, 
                    "Consulta cancelada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarConsultas();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cancelar consulta: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel criarPainelMeusDados() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Meus Dados Pessoais");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painel.add(lblTitulo, BorderLayout.NORTH);
        
        // Painel central com formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome (não editável)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 12));
        painelFormulario.add(lblNome, gbc);
        
        gbc.gridx = 1;
        JTextField txtNome = new JTextField(paciente.getNome(), 25);
        txtNome.setEditable(false);
        txtNome.setBackground(new Color(240, 240, 240));
        painelFormulario.add(txtNome, gbc);
        
        // CPF (não editável)
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setFont(new Font("Arial", Font.BOLD, 12));
        painelFormulario.add(lblCPF, gbc);
        
        gbc.gridx = 1;
        JTextField txtCPF = new JTextField(paciente.getCpf(), 25);
        txtCPF.setEditable(false);
        txtCPF.setBackground(new Color(240, 240, 240));
        painelFormulario.add(txtCPF, gbc);
        
        // Email (editável)
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        painelFormulario.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(paciente.getEmail() != null ? paciente.getEmail() : "", 25);
        painelFormulario.add(txtEmail, gbc);
        
        // Telefone (editável)
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("Arial", Font.BOLD, 12));
        painelFormulario.add(lblTelefone, gbc);
        
        gbc.gridx = 1;
        JTextField txtTelefone = new JTextField(paciente.getTelefone() != null ? paciente.getTelefone() : "", 25);
        painelFormulario.add(txtTelefone, gbc);
        
        // Endereço (editável)
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Arial", Font.BOLD, 12));
        painelFormulario.add(lblEndereco, gbc);
        
        gbc.gridx = 1;
        JTextField txtEndereco = new JTextField(paciente.getEndereco() != null ? paciente.getEndereco() : "", 25);
        painelFormulario.add(txtEndereco, gbc);
        
        // Convênio (editável)
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblConvenio = new JLabel("Convênio:");
        lblConvenio.setFont(new Font("Arial", Font.BOLD, 12));
        painelFormulario.add(lblConvenio, gbc);
        
        gbc.gridx = 1;
        JTextField txtConvenio = new JTextField(paciente.getConvenio() != null ? paciente.getConvenio() : "Particular", 25);
        painelFormulario.add(txtConvenio, gbc);
        
        painel.add(painelFormulario, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBackground(new Color(39, 174, 96));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 12));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(180, 35));
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Atualizar dados do paciente
                    paciente.setEmail(txtEmail.getText().trim());
                    paciente.setTelefone(txtTelefone.getText().trim());
                    paciente.setEndereco(txtEndereco.getText().trim());
                    paciente.setConvenio(txtConvenio.getText().trim());
                    
                    // Salvar via CadastroService
                    cadastroService.atualizarUsuario(paciente);
                    
                    // Atualizar também no DAO diretamente
                    pacienteDAO.salvar(paciente);
                    
                    JOptionPane.showMessageDialog(painel,
                        "Dados atualizados com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    Logger.info("PacienteGUI", "Dados do paciente atualizados: " + paciente.getCpf());
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(painel,
                        "Erro ao atualizar dados: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                    Logger.erro("PacienteGUI", "Erro ao atualizar dados", ex);
                }
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(150, 35));
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Restaurar valores originais
                txtEmail.setText(paciente.getEmail() != null ? paciente.getEmail() : "");
                txtTelefone.setText(paciente.getTelefone() != null ? paciente.getTelefone() : "");
                txtEndereco.setText(paciente.getEndereco() != null ? paciente.getEndereco() : "");
                txtConvenio.setText(paciente.getConvenio() != null ? paciente.getConvenio() : "Particular");
                
                JOptionPane.showMessageDialog(painel,
                    "Alterações canceladas.",
                    "Cancelado",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
}
