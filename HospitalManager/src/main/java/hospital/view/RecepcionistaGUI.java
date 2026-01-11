package hospital.view;

import hospital.model.Recepcionista;
import hospital.model.Paciente;
import hospital.model.Medico;
import hospital.model.Consulta;
import hospital.model.enums.Especialidade;
import hospital.service.CadastroService;
import hospital.dao.PacienteDAO;
import hospital.dao.MedicoDAO;
import hospital.dao.ConsultaDAO;
import hospital.util.Logger;
import hospital.util.DataHoraUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class RecepcionistaGUI extends JFrame {
    
    private Recepcionista recepcionista;
    private JTabbedPane abas;
    private CadastroService cadastroService;
    private PacienteDAO pacienteDAO;
    private MedicoDAO medicoDAO;
    private ConsultaDAO consultaDAO;
    private JTextArea areaVisitas;
    
    public RecepcionistaGUI(Recepcionista recepcionista) {
        this.recepcionista = recepcionista;
        this.cadastroService = new CadastroService();
        this.pacienteDAO = new PacienteDAO();
        this.medicoDAO = new MedicoDAO();
        this.consultaDAO = new ConsultaDAO();
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
        btnCadastrarPaciente.setBackground(new Color(25, 118, 210));
        btnCadastrarPaciente.setForeground(Color.WHITE);
        btnCadastrarPaciente.setFocusPainted(false);
        btnCadastrarPaciente.setPreferredSize(new Dimension(200, 100));
        btnCadastrarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarPaciente();
            }
        });
        
        // Botão Cadastrar Médico
        JButton btnCadastrarMedico = new JButton("<html><center>Cadastrar<br>Médico</center></html>");
        btnCadastrarMedico.setFont(new Font("Arial", Font.BOLD, 16));
        btnCadastrarMedico.setBackground(new Color(0, 150, 136));
        btnCadastrarMedico.setForeground(Color.WHITE);
        btnCadastrarMedico.setFocusPainted(false);
        btnCadastrarMedico.setPreferredSize(new Dimension(200, 100));
        btnCadastrarMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarMedico();
            }
        });
        
        // Botão Listar Pacientes
        JButton btnListarPacientes = new JButton("<html><center>Listar<br>Pacientes</center></html>");
        btnListarPacientes.setFont(new Font("Arial", Font.BOLD, 16));
        btnListarPacientes.setBackground(new Color(123, 31, 162));
        btnListarPacientes.setForeground(Color.WHITE);
        btnListarPacientes.setFocusPainted(false);
        btnListarPacientes.setPreferredSize(new Dimension(200, 100));
        btnListarPacientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarPacientes();
            }
        });
        
        // Botão Gerenciar Médicos
        JButton btnGerenciarMedicos = new JButton("<html><center>Gerenciar<br>Médicos</center></html>");
        btnGerenciarMedicos.setFont(new Font("Arial", Font.BOLD, 16));
        btnGerenciarMedicos.setBackground(new Color(230, 81, 0));
        btnGerenciarMedicos.setForeground(Color.WHITE);
        btnGerenciarMedicos.setFocusPainted(false);
        btnGerenciarMedicos.setPreferredSize(new Dimension(200, 100));
        btnGerenciarMedicos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerenciarMedicos();
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
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = txtBusca.getText().trim();
                if (!cpf.isEmpty()) {
                    Paciente p = pacienteDAO.buscarPorId(cpf);
                    if (p != null) {
                        areaVisitas.setText("=== PACIENTE ===\n\n" +
                            "Nome: " + p.getNome() + "\n" +
                            "CPF: " + p.getCpf() + "\n" +
                            "Status de Visita: " + p.getStatusVisita() + "\n\n" +
                            "Use os botões abaixo para controlar visitas.");
                    } else {
                        areaVisitas.setText("Paciente não encontrado!");
                    }
                }
            }
        });
        painelBusca.add(btnBuscar);
        
        areaVisitas = new JTextArea();
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
        btnRegistrarVisita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = JOptionPane.showInputDialog(painel, "CPF do paciente:");
                if (cpf != null && !cpf.trim().isEmpty()) {
                    Paciente p = pacienteDAO.buscarPorId(cpf.trim());
                    if (p != null && p.isAptoVisita()) {
                        JOptionPane.showMessageDialog(painel, "Visita registrada para " + p.getNome());
                    } else if (p != null) {
                        JOptionPane.showMessageDialog(painel, "Visita PROIBIDA para este paciente!");
                    } else {
                        JOptionPane.showMessageDialog(painel, "Paciente não encontrado!");
                    }
                }
            }
        });
        
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
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarMonitoramento(areaMonitoramento);
            }
        });
        
        JButton btnRelatorio = new JButton("Gerar Relatório");
        btnRelatorio.setBackground(new Color(230, 126, 34));
        btnRelatorio.setForeground(Color.WHITE);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painel, "Relatório gerado!\n(Dados exibidos na tela)");
            }
        });
        
        atualizarMonitoramento(areaMonitoramento);
        
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRelatorio);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelConsultas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel superior com título e filtros
        JPanel painelTopo = new JPanel(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("Consultas Agendadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelTopo.add(lblTitulo, BorderLayout.NORTH);
        
        // Painel de filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelFiltros.setBorder(BorderFactory.createTitledBorder("Filtrar por Período"));
        
        painelFiltros.add(new JLabel("Data Início:"));
        JSpinner spinnerDataInicio = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spinnerDataInicio, "dd/MM/yyyy");
        spinnerDataInicio.setEditor(editorInicio);
        painelFiltros.add(spinnerDataInicio);
        
        painelFiltros.add(new JLabel("Data Fim:"));
        JSpinner spinnerDataFim = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFim = new JSpinner.DateEditor(spinnerDataFim, "dd/MM/yyyy");
        spinnerDataFim.setEditor(editorFim);
        painelFiltros.add(spinnerDataFim);
        
        painelFiltros.add(new JLabel("Médico:"));
        List<Medico> medicos = medicoDAO.listarTodos();
        String[] nomesMedicos = new String[medicos.size() + 1];
        nomesMedicos[0] = "Todos";
        for (int i = 0; i < medicos.size(); i++) {
            nomesMedicos[i + 1] = medicos.get(i).getNome() + " (" + medicos.get(i).getCrm() + ")";
        }
        JComboBox<String> comboMedico = new JComboBox<>(nomesMedicos);
        painelFiltros.add(comboMedico);
        
        painelTopo.add(painelFiltros, BorderLayout.CENTER);
        painel.add(painelTopo, BorderLayout.NORTH);
        
        // Área de texto para exibir consultas
        JTextArea areaConsultas = new JTextArea();
        areaConsultas.setEditable(false);
        areaConsultas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scroll = new JScrollPane(areaConsultas);
        painel.add(scroll, BorderLayout.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnFiltrar = new JButton("[Filtrar]");
        btnFiltrar.setBackground(new Color(46, 204, 113));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(e -> {
            java.util.Date dataInicio = (java.util.Date) spinnerDataInicio.getValue();
            java.util.Date dataFim = (java.util.Date) spinnerDataFim.getValue();
            String medicoSelecionado = (String) comboMedico.getSelectedItem();
            String crm = null;
            
            if (medicoSelecionado != null && !medicoSelecionado.equals("Todos")) {
                // Extrair CRM do texto "Nome (CRM)"
                int inicio = medicoSelecionado.lastIndexOf("(");
                int fim = medicoSelecionado.lastIndexOf(")");
                if (inicio >= 0 && fim > inicio) {
                    crm = medicoSelecionado.substring(inicio + 1, fim);
                }
            }
            
            filtrarConsultas(areaConsultas, dataInicio, dataFim, crm);
        });
        
        JButton btnLimpar = new JButton("[Limpar Filtros]");
        btnLimpar.setBackground(new Color(52, 152, 219));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.addActionListener(e -> {
            comboMedico.setSelectedIndex(0);
            spinnerDataInicio.setValue(new java.util.Date());
            spinnerDataFim.setValue(new java.util.Date());
            carregarTodasConsultas(areaConsultas);
        });
        
        painelBotoes.add(btnFiltrar);
        painelBotoes.add(btnLimpar);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Carregar todas as consultas inicialmente
        carregarTodasConsultas(areaConsultas);
        
        return painel;
    }
    
    private void carregarTodasConsultas(JTextArea area) {
        List<Consulta> todasConsultas = consultaDAO.listarTodos();
        StringBuilder sb = new StringBuilder();
        sb.append("=== TODAS AS CONSULTAS AGENDADAS ===\n\n");
        
        for (Consulta c : todasConsultas) {
            if (c.estaAgendada()) {
                Paciente p = pacienteDAO.buscarPorId(c.getPacienteCpf());
                Medico m = medicoDAO.buscarPorId(c.getMedicoCrm());
                
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Paciente: ").append(p != null ? p.getNome() : "N/A").append("\n");
                sb.append("Médico: ").append(m != null ? m.getNome() : "N/A").append("\n");
                sb.append("Data/Hora: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("\n").append("=".repeat(50)).append("\n\n");
            }
        }
        
        if (sb.toString().equals("=== TODAS AS CONSULTAS AGENDADAS ===\n\n")) {
            sb.append("Nenhuma consulta agendada no momento.");
        }
        
        area.setText(sb.toString());
        area.setCaretPosition(0);
    }
    
    private void filtrarConsultas(JTextArea area, java.util.Date dataInicio, java.util.Date dataFim, String crm) {
        List<Consulta> todasConsultas = consultaDAO.listarTodos();
        StringBuilder sb = new StringBuilder();
        sb.append("=== CONSULTAS FILTRADAS ===\n");
        sb.append("Período: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dataInicio));
        sb.append(" a ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dataFim)).append("\n");
        if (crm != null) {
            sb.append("Médico CRM: ").append(crm).append("\n");
        }
        sb.append("\n");
        
        LocalDateTime inicio = LocalDateTime.ofInstant(dataInicio.toInstant(), java.time.ZoneId.systemDefault());
        LocalDateTime fim = LocalDateTime.ofInstant(dataFim.toInstant(), java.time.ZoneId.systemDefault()).plusDays(1);
        
        int count = 0;
        for (Consulta c : todasConsultas) {
            if (c.getDataHora() != null && 
                !c.getDataHora().isBefore(inicio) && 
                c.getDataHora().isBefore(fim)) {
                
                // Filtrar por médico se especificado
                if (crm != null && !c.getMedicoCrm().equals(crm)) {
                    continue;
                }
                
                Paciente p = pacienteDAO.buscarPorId(c.getPacienteCpf());
                Medico m = medicoDAO.buscarPorId(c.getMedicoCrm());
                
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Paciente: ").append(p != null ? p.getNome() : "N/A").append("\n");
                sb.append("Médico: ").append(m != null ? m.getNome() : "N/A").append("\n");
                sb.append("Data/Hora: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("Status: ").append(c.getStatus()).append("\n");
                sb.append("\n").append("=".repeat(50)).append("\n\n");
                count++;
            }
        }
        
        if (count == 0) {
            sb.append("Nenhuma consulta encontrada no período especificado.");
        } else {
            sb.append("\nTotal: ").append(count).append(" consulta(s) encontrada(s).");
        }
        
        area.setText(sb.toString());
        area.setCaretPosition(0);
    }
    
    // ==================== MÉTODOS DE LÓGICA DE NEGÓCIO ====================
    
    private void cadastrarPaciente() {
        JDialog dialogo = new JDialog(this, "Cadastrar Paciente", true);
        dialogo.setSize(450, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtNome = new JTextField(20);
        JTextField txtCPF = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtTelefone = new JTextField(20);
        JTextField txtConvenio = new JTextField(20);
        JPasswordField txtSenha = new JPasswordField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridy = 1;
        painelCampos.add(txtNome, gbc);
        
        gbc.gridy = 2;
        painelCampos.add(new JLabel("CPF:"), gbc);
        gbc.gridy = 3;
        painelCampos.add(txtCPF, gbc);
        
        gbc.gridy = 4;
        painelCampos.add(new JLabel("Email:"), gbc);
        gbc.gridy = 5;
        painelCampos.add(txtEmail, gbc);
        
        gbc.gridy = 6;
        painelCampos.add(new JLabel("Telefone:"), gbc);
        gbc.gridy = 7;
        painelCampos.add(txtTelefone, gbc);
        
        gbc.gridy = 8;
        painelCampos.add(new JLabel("Convênio:"), gbc);
        gbc.gridy = 9;
        painelCampos.add(txtConvenio, gbc);
        
        gbc.gridy = 10;
        painelCampos.add(new JLabel("Senha:"), gbc);
        gbc.gridy = 11;
        painelCampos.add(txtSenha, gbc);
        
        dialogo.add(painelCampos, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(39, 174, 96));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Paciente p = new Paciente(
                        txtNome.getText().trim(),
                        txtCPF.getText().trim(),
                        txtEmail.getText().trim(),
                        txtTelefone.getText().trim(),
                        "",
                        txtCPF.getText().trim(),
                        new String(txtSenha.getPassword())
                    );
                    p.setConvenio(txtConvenio.getText().trim());
                    
                    cadastroService.cadastrarPaciente(p);
                    
                    JOptionPane.showMessageDialog(dialogo, 
                        "Paciente cadastrado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        dialogo.add(painelBotoes, BorderLayout.SOUTH);
        
        dialogo.setVisible(true);
    }
    
    private void cadastrarMedico() {
        JDialog dialogo = new JDialog(this, "Cadastrar Médico", true);
        dialogo.setSize(450, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtNome = new JTextField(20);
        JTextField txtCRM = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtTelefone = new JTextField(20);
        JComboBox<Especialidade> comboEspecialidade = new JComboBox<>(Especialidade.values());
        JPasswordField txtSenha = new JPasswordField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridy = 1;
        painelCampos.add(txtNome, gbc);
        
        gbc.gridy = 2;
        painelCampos.add(new JLabel("CRM:"), gbc);
        gbc.gridy = 3;
        painelCampos.add(txtCRM, gbc);
        
        gbc.gridy = 4;
        painelCampos.add(new JLabel("Email:"), gbc);
        gbc.gridy = 5;
        painelCampos.add(txtEmail, gbc);
        
        gbc.gridy = 6;
        painelCampos.add(new JLabel("Telefone:"), gbc);
        gbc.gridy = 7;
        painelCampos.add(txtTelefone, gbc);
        
        gbc.gridy = 8;
        painelCampos.add(new JLabel("Especialidade:"), gbc);
        gbc.gridy = 9;
        painelCampos.add(comboEspecialidade, gbc);
        
        gbc.gridy = 10;
        painelCampos.add(new JLabel("Senha:"), gbc);
        gbc.gridy = 11;
        painelCampos.add(txtSenha, gbc);
        
        dialogo.add(painelCampos, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(39, 174, 96));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Medico m = new Medico(
                        txtNome.getText().trim(),
                        "",
                        txtEmail.getText().trim(),
                        txtTelefone.getText().trim(),
                        "",
                        txtCRM.getText().trim(),
                        new String(txtSenha.getPassword()),
                        txtCRM.getText().trim(),
                        (Especialidade) comboEspecialidade.getSelectedItem()
                    );
                    
                    cadastroService.cadastrarMedico(m);
                    
                    JOptionPane.showMessageDialog(dialogo, 
                        "Médico cadastrado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        dialogo.add(painelBotoes, BorderLayout.SOUTH);
        
        dialogo.setVisible(true);
    }
    
    private void listarPacientes() {
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE PACIENTES CADASTRADOS ===\n\n");
        
        for (Paciente p : pacientes) {
            sb.append("Nome: ").append(p.getNome()).append("\n");
            sb.append("CPF: ").append(p.getCpf()).append("\n");
            sb.append("Email: ").append(p.getEmail()).append("\n");
            sb.append("Telefone: ").append(p.getTelefone()).append("\n");
            sb.append("\n").append("=".repeat(50)).append("\n\n");
        }
        
        if (pacientes.isEmpty()) {
            sb.append("Nenhum paciente cadastrado.");
        }
        
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setCaretPosition(0);
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scroll, "Lista de Pacientes", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerenciarMedicos() {
        List<Medico> medicos = medicoDAO.listarTodos();
        
        String[] opcoes = new String[medicos.size()];
        for (int i = 0; i < medicos.size(); i++) {
            Medico m = medicos.get(i);
            opcoes[i] = m.getCrm() + " - " + m.getNome() + " (" + (m.isAtivo() ? "ATIVO" : "INATIVO") + ")";
        }
        
        String escolha = (String) JOptionPane.showInputDialog(
            this,
            "Selecione um médico para ativar/desativar:",
            "Gerenciar Médicos",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes.length > 0 ? opcoes[0] : null
        );
        
        if (escolha != null) {
            String crm = escolha.split(" - ")[0];
            Medico m = medicoDAO.buscarPorId(crm);
            
            if (m != null) {
                m.setAtivo(!m.isAtivo());
                medicoDAO.salvar(m);
                
                String status = m.isAtivo() ? "ATIVADO" : "DESATIVADO";
                JOptionPane.showMessageDialog(this, 
                    "Médico " + status + " com sucesso!\n" + m.getNome());
            }
        }
    }
    
    private void atualizarMonitoramento(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE MONITORAMENTO ===\n\n");
        
        // Contar consultas por status
        List<Consulta> todas = consultaDAO.listarTodos();
        int agendadas = 0, concluidas = 0, canceladas = 0, faltas = 0;
        List<Consulta> faltasRecentes = new java.util.ArrayList<>();
        LocalDateTime seteDiasAtras = LocalDateTime.now().minusDays(7);
        
        for (Consulta c : todas) {
            switch (c.getStatus()) {
                case AGENDADA: agendadas++; break;
                case CONCLUIDA: concluidas++; break;
                case CANCELADA: canceladas++; break;
                case FALTOU: 
                    faltas++;
                    if (c.getDataHora() != null && c.getDataHora().isAfter(seteDiasAtras)) {
                        faltasRecentes.add(c);
                    }
                    break;
            }
        }
        
        // ALERTAS DE FALTAS
        if (!faltasRecentes.isEmpty()) {
            sb.append("[!!!] ALERTAS DE FALTAS RECENTES [!!!]\n");
            sb.append("=".repeat(50)).append("\n");
            sb.append(faltasRecentes.size()).append(" falta(s) registrada(s) nos últimos 7 dias:\n\n");
            
            for (Consulta c : faltasRecentes) {
                Paciente p = pacienteDAO.buscarPorId(c.getPacienteCpf());
                Medico m = medicoDAO.buscarPorId(c.getMedicoCrm());
                
                sb.append("  [!] Paciente: ").append(p != null ? p.getNome() : "N/A");
                sb.append(" (CPF: ").append(c.getPacienteCpf()).append(")\n");
                sb.append("     Médico: ").append(m != null ? m.getNome() : "N/A").append("\n");
                sb.append("     Data: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("     ID Consulta: ").append(c.getId()).append("\n\n");
            }
            sb.append("=".repeat(50)).append("\n\n");
        }
        
        sb.append("CONSULTAS:\n");
        sb.append("  Agendadas: ").append(agendadas).append("\n");
        sb.append("  Concluídas: ").append(concluidas).append("\n");
        sb.append("  Canceladas: ").append(canceladas).append("\n");
        sb.append("  Faltas (total): ").append(faltas);
        if (faltas > 0) {
            sb.append(" \u26a0\ufe0f");
        }
        sb.append("\n\n");
        
        // Estatísticas de faltas por paciente
        if (faltas > 0) {
            sb.append("PACIENTES COM MAIS FALTAS:\n");
            java.util.Map<String, Integer> faltasPorPaciente = new java.util.HashMap<>();
            
            for (Consulta c : todas) {
                if (c.getStatus() == hospital.model.enums.StatusConsulta.FALTOU) {
                    faltasPorPaciente.put(c.getPacienteCpf(), 
                        faltasPorPaciente.getOrDefault(c.getPacienteCpf(), 0) + 1);
                }
            }
            
            faltasPorPaciente.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> {
                    Paciente p = pacienteDAO.buscarPorId(entry.getKey());
                    sb.append("  - ").append(p != null ? p.getNome() : "N/A");
                    sb.append(": ").append(entry.getValue()).append(" falta(s)\n");
                });
            sb.append("\n");
        }
        
        // Médicos ativos
        List<Medico> medicosAtivos = medicoDAO.listarAtivos();
        sb.append("MÉDICOS ATIVOS: ").append(medicosAtivos.size()).append("\n");
        for (Medico m : medicosAtivos) {
            sb.append("  - ").append(m.getNome()).append(" (").append(m.getEspecialidade()).append(")\n");
        }
        sb.append("\n");
        
        // Total de pacientes
        int totalPacientes = pacienteDAO.listarTodos().size();
        sb.append("TOTAL DE PACIENTES: ").append(totalPacientes).append("\n");
        
        // Taxa de comparecimento
        if (concluidas + faltas > 0) {
            double taxa = (concluidas * 100.0) / (concluidas + faltas);
            sb.append("\nTAXA DE COMPARECIMENTO: ").append(String.format("%.1f%%", taxa));
            if (taxa < 80) {
                sb.append(" [!] ATENÇÃO: Taxa baixa!");
            }
            sb.append("\n");
        }
        
        area.setText(sb.toString());
        area.setCaretPosition(0);
    }
}
