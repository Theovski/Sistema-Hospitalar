package hospital.view;

import hospital.model.Medico;
import hospital.model.Consulta;
import hospital.model.Paciente;
import hospital.model.Atestado;
import hospital.model.Receita;
import hospital.service.AgendamentoService;
import hospital.service.DocumentoService;
import hospital.dao.ConsultaDAO;
import hospital.dao.PacienteDAO;
import hospital.dao.HorarioAtendimentoDAO;
import hospital.model.HorarioAtendimento;
import hospital.util.Logger;
import hospital.util.DataHoraUtil;
import java.time.DayOfWeek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MedicoGUI extends JFrame {
    
    private Medico medico;
    private JTabbedPane abas;
    private AgendamentoService agendamentoService;
    private DocumentoService documentoService;
    private ConsultaDAO consultaDAO;
    private PacienteDAO pacienteDAO;
    private HorarioAtendimentoDAO horarioDAO;
    
    // Componentes para atualização dinâmica
    private JTextArea areaAgenda;
    private JTextArea areaProntuarios;
    private JTextArea areaHorarios;
    
    public MedicoGUI(Medico medico) {
        this.medico = medico;
        this.agendamentoService = new AgendamentoService();
        this.documentoService = new DocumentoService();
        this.consultaDAO = new ConsultaDAO();
        this.pacienteDAO = new PacienteDAO();
        this.horarioDAO = new HorarioAtendimentoDAO();
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
        
        // Aba 5: Horários de Atendimento
        JPanel painelHorarios = criarPainelHorarios();
        abas.addTab("Meus Horários", new ImageIcon(), painelHorarios, "Gerenciar horários e slots");
        
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
        
        areaAgenda = new JTextArea();
        areaAgenda.setEditable(false);
        areaAgenda.setFont(new Font("Monospaced", Font.PLAIN, 12));
        carregarAgenda();
        
        JScrollPane scroll = new JScrollPane(areaAgenda);
        painel.add(scroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnPresenca = new JButton("Marcar Presença");
        btnPresenca.setBackground(new Color(39, 174, 96));
        btnPresenca.setForeground(Color.WHITE);
        btnPresenca.setFocusPainted(false);
        btnPresenca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarPresenca();
            }
        });
        
        JButton btnFalta = new JButton("Marcar Falta");
        btnFalta.setBackground(new Color(231, 76, 60));
        btnFalta.setForeground(Color.WHITE);
        btnFalta.setFocusPainted(false);
        btnFalta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarFalta();
            }
        });
        
        JButton btnConcluir = new JButton("Concluir Consulta");
        btnConcluir.setBackground(new Color(52, 152, 219));
        btnConcluir.setForeground(Color.WHITE);
        btnConcluir.setFocusPainted(false);
        btnConcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concluirConsulta();
            }
        });
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(149, 165, 166));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarAgenda();
            }
        });
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
        
        areaProntuarios = new JTextArea();
        areaProntuarios.setEditable(false);
        areaProntuarios.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaProntuarios.setText("Digite o CPF do paciente e clique em Buscar.\n\n" +
            "Será exibido:\n" +
            "- Dados pessoais\n" +
            "- Histórico de consultas\n" +
            "- Documentos médicos");
        
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = txtBusca.getText().trim();
                if (!cpf.isEmpty()) {
                    buscarProntuario(cpf);
                }
            }
        });
        
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
                emitirAtestado();
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
                emitirReceita();
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
                solicitarExame();
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
        btnLiberar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlarVisita(true);
            }
        });
        
        JButton btnProibir = new JButton("Proibir Visitas");
        btnProibir.setBackground(new Color(231, 76, 60));
        btnProibir.setForeground(Color.WHITE);
        btnProibir.setFocusPainted(false);
        btnProibir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlarVisita(false);
            }
        });
        
        painelBotoes.add(btnLiberar);
        painelBotoes.add(btnProibir);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    // ==================== MÉTODOS DE LÓGICA DE NEGÓCIO ====================
    
    private void carregarAgenda() {
        // Detectar e marcar faltas automaticamente
        detectarFaltasAutomaticas();
        
        List<Consulta> consultas = consultaDAO.listarFuturasPorMedico(medico.getCrm());
        StringBuilder sb = new StringBuilder();
        sb.append("=== MINHA AGENDA - ").append(LocalDate.now().toString()).append(" ===\n\n");
        
        // Verificar se há notificações
        int faltasHoje = contarFaltasHoje();
        int novasConsultas = contarNovasConsultas();
        
        if (faltasHoje > 0 || novasConsultas > 0) {
            sb.append("NOTIFICAÇÕES:\n");
            if (faltasHoje > 0) {
                sb.append(faltasHoje).append(" falta(s) registrada(s) hoje\n");
            }
            if (novasConsultas > 0) {
                sb.append(novasConsultas).append(" nova(s) consulta(s) agendada(s) hoje\n");
            }
            sb.append("\n").append("=".repeat(50)).append("\n\n");
        }
        
        if (consultas.isEmpty()) {
            sb.append("Nenhuma consulta agendada.\n");
        } else {
            for (Consulta c : consultas) {
                Paciente p = pacienteDAO.buscarPorId(c.getPacienteCpf());
                sb.append("ID: ").append(c.getId()).append("\n");
                sb.append("Paciente: ").append(p != null ? p.getNome() : "N/A").append("\n");
                sb.append("CPF: ").append(c.getPacienteCpf()).append("\n");
                sb.append("Data/Hora: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("Status: ").append(c.getStatus()).append("\n");
                sb.append("\n").append("=".repeat(50)).append("\n\n");
            }
        }
        
        areaAgenda.setText(sb.toString());
        areaAgenda.setCaretPosition(0);
    }
    
    private void marcarPresenca() {
        String id = JOptionPane.showInputDialog(this, "Digite o ID da consulta:");
        if (id == null || id.trim().isEmpty()) return;
        
        try {
            Consulta c = consultaDAO.buscarPorId(id.trim());
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Consulta não encontrada!");
                return;
            }
            
            c.setCompareceu(true);
            consultaDAO.salvar(c);
            
            JOptionPane.showMessageDialog(this, "Presença marcada!");
            carregarAgenda();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    private void marcarFalta() {
        String id = JOptionPane.showInputDialog(this, "Digite o ID da consulta:");
        if (id == null || id.trim().isEmpty()) return;
        
        try {
            consultaDAO.registrarFalta(id.trim());
            JOptionPane.showMessageDialog(this, "Falta registrada!");
            carregarAgenda();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    private void concluirConsulta() {
        String id = JOptionPane.showInputDialog(this, "Digite o ID da consulta:");
        if (id == null || id.trim().isEmpty()) return;
        
        String obs = JOptionPane.showInputDialog(this, "Observações da consulta:");
        
        try {
            consultaDAO.marcarComoRealizada(id.trim(), obs != null ? obs : "");
            JOptionPane.showMessageDialog(this, "Consulta concluída!");
            carregarAgenda();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    private void buscarProntuario(String cpf) {
        Paciente p = pacienteDAO.buscarPorId(cpf);
        
        if (p == null) {
            areaProntuarios.setText("Paciente não encontrado!");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== PRONTUÁRIO DO PACIENTE ===\n\n");
        sb.append("Nome: ").append(p.getNome()).append("\n");
        sb.append("CPF: ").append(p.getCpf()).append("\n");
        sb.append("Email: ").append(p.getEmail()).append("\n");
        sb.append("Telefone: ").append(p.getTelefone()).append("\n");
        sb.append("Convênio: ").append(p.getConvenio() != null ? p.getConvenio() : "Particular").append("\n");
        sb.append("Status Visitação: ").append(p.getStatusVisita()).append("\n");
        sb.append("\n").append("=".repeat(50)).append("\n\n");
        
        // Histórico de Consultas
        sb.append("HISTÓRICO DE CONSULTAS:\n\n");
        List<Consulta> consultas = consultaDAO.listarPorPaciente(cpf);
        
        if (consultas.isEmpty()) {
            sb.append("  Nenhuma consulta registrada.\n");
        } else {
            for (Consulta c : consultas) {
                sb.append("  Data: ").append(DataHoraUtil.formatar(c.getDataHora())).append("\n");
                sb.append("  Status: ").append(c.getStatus()).append("\n");
                if (c.getObservacoes() != null && !c.getObservacoes().isEmpty()) {
                    sb.append("  Obs: ").append(c.getObservacoes()).append("\n");
                }
                sb.append("\n");
            }
        }
        
        sb.append("=".repeat(50)).append("\n\n");
        
        // Exames
        try {
            List<hospital.model.Exame> exames = documentoService.listarExamesPorPaciente(cpf);
            sb.append("EXAMES (").append(exames.size()).append("):\n\n");
            if (exames.isEmpty()) {
                sb.append("  Nenhum exame solicitado.\n");
            } else {
                for (hospital.model.Exame e : exames) {
                    sb.append("  ID: ").append(e.getId()).append("\n");
                    sb.append("  Tipo: ").append(e.getTipoExame()).append("\n");
                    sb.append("  Data: ").append(e.getDataRealizacao() != null ? e.getDataRealizacao() : "Pendente").append("\n");
                    sb.append("  Resultado: ").append(e.getResultado() != null ? e.getResultado() : "Em análise").append("\n\n");
                }
            }
        } catch (Exception e) {
            sb.append("EXAMES: Erro ao carregar\n\n");
        }
        
        sb.append("=".repeat(50)).append("\n\n");
        
        // Atestados
        try {
            List<hospital.model.Atestado> atestados = documentoService.listarAtestadosPorPaciente(cpf);
            sb.append("ATESTADOS (").append(atestados.size()).append("):\n\n");
            if (atestados.isEmpty()) {
                sb.append("  Nenhum atestado emitido.\n");
            } else {
                for (hospital.model.Atestado a : atestados) {
                    sb.append("  ID: ").append(a.getId()).append("\n");
                    sb.append("  Data: ").append(a.getDataEmissao()).append("\n");
                    sb.append("  CID: ").append(a.getCid()).append("\n");
                    sb.append("  Dias: ").append(a.getDiasRepouso()).append("\n\n");
                }
            }
        } catch (Exception e) {
            sb.append("ATESTADOS: Erro ao carregar\n\n");
        }
        
        sb.append("=".repeat(50)).append("\n\n");
        
        // Receitas
        try {
            List<hospital.model.Receita> receitas = documentoService.listarReceitasPorPaciente(cpf);
            sb.append("RECEITAS (").append(receitas.size()).append("):\n\n");
            if (receitas.isEmpty()) {
                sb.append("  Nenhuma receita emitida.\n");
            } else {
                for (hospital.model.Receita r : receitas) {
                    sb.append("  ID: ").append(r.getId()).append("\n");
                    sb.append("  Data: ").append(r.getData()).append("\n");
                    sb.append("  Orientações: ").append(r.getInstrucoes()).append("\n\n");
                }
            }
        } catch (Exception e) {
            sb.append("RECEITAS: Erro ao carregar\n\n");
        }
        
        areaProntuarios.setText(sb.toString());
        areaProntuarios.setCaretPosition(0);
    }
    
    private void emitirAtestado() {
        String cpf = JOptionPane.showInputDialog(this, "CPF do paciente:");
        if (cpf == null || cpf.trim().isEmpty()) return;
        
        Paciente p = pacienteDAO.buscarPorId(cpf.trim());
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado!");
            return;
        }
        
        String dias = JOptionPane.showInputDialog(this, "Dias de afastamento:");
        String motivo = JOptionPane.showInputDialog(this, "Motivo do atestado:");
        
        if (dias == null || motivo == null) return;
        
        try {
            Atestado atestado = new Atestado();
            atestado.setId("ATST" + System.currentTimeMillis());
            atestado.setPacienteCpf(cpf.trim());
            atestado.setMedicoCrm(medico.getCrm());
            atestado.setDataEmissao(LocalDate.now());
            atestado.setDiasAfastamento(Integer.parseInt(dias));
            atestado.setDescricao(motivo);
            
            documentoService.criarAtestado(atestado);
            
            JOptionPane.showMessageDialog(this, 
                "Atestado emitido com sucesso!\nID: " + atestado.getId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    private void emitirReceita() {
        String cpf = JOptionPane.showInputDialog(this, "CPF do paciente:");
        if (cpf == null || cpf.trim().isEmpty()) return;
        
        Paciente p = pacienteDAO.buscarPorId(cpf.trim());
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado!");
            return;
        }
        
        String medicamentos = JOptionPane.showInputDialog(this, "Medicamentos prescritos:");
        String instrucoes = JOptionPane.showInputDialog(this, "Instruções de uso:");
        
        if (medicamentos == null) return;
        
        try {
            Receita receita = new Receita();
            receita.setId("REC" + System.currentTimeMillis());
            receita.setPacienteCpf(cpf.trim());
            receita.setMedicoCrm(medico.getCrm());
            receita.setOrientacoes(instrucoes != null ? instrucoes : "");
            
            // Adicionar medicamentos (simplificado)
            if (medicamentos != null && !medicamentos.isEmpty()) {
                String[] meds = medicamentos.split(",");
                for (String med : meds) {
                    Receita.Medicamento medicamento = new Receita.Medicamento(
                        med.trim(), "conforme prescrito", "conforme prescrito", 7
                    );
                    receita.adicionarMedicamento(medicamento);
                }
            }
            
            documentoService.criarReceita(receita);
            
            JOptionPane.showMessageDialog(this, 
                "Receita emitida com sucesso!\nID: " + receita.getId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    private void controlarVisita(boolean liberar) {
        String cpf = JOptionPane.showInputDialog(this, "CPF do paciente:");
        if (cpf == null || cpf.trim().isEmpty()) return;
        
        try {
            Paciente p = pacienteDAO.buscarPorId(cpf.trim());
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Paciente não encontrado!");
                return;
            }
            
            pacienteDAO.atualizarStatusVisita(cpf.trim(), liberar);
            
            String msg = liberar ? "Visitas LIBERADAS" : "Visitas PROIBIDAS";
            JOptionPane.showMessageDialog(this, 
                msg + " para o paciente " + p.getNome());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    private void solicitarExame() {
        JDialog dialogo = new JDialog(this, "Solicitar Exame", true);
        dialogo.setSize(450, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(10, 10));
        
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // CPF do Paciente
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(new JLabel("CPF do Paciente:"), gbc);
        gbc.gridy = 1;
        JTextField txtCPF = new JTextField(20);
        painelCampos.add(txtCPF, gbc);
        
        // Tipo de Exame
        gbc.gridy = 2;
        painelCampos.add(new JLabel("Tipo de Exame:"), gbc);
        gbc.gridy = 3;
        String[] tiposExame = {"Hemograma", "Raio-X", "Tomografia", "Ressonância", "Ultrassom", "Urina", "Fezes", "Outro"};
        JComboBox<String> comboTipo = new JComboBox<>(tiposExame);
        comboTipo.setEditable(true);
        painelCampos.add(comboTipo, gbc);
        
        // Observações
        gbc.gridy = 4;
        painelCampos.add(new JLabel("Observações/Instruções:"), gbc);
        gbc.gridy = 5;
        JTextArea txtObs = new JTextArea(4, 20);
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObs);
        painelCampos.add(scrollObs, gbc);
        
        dialogo.add(painelCampos, BorderLayout.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnSolicitar = new JButton("Solicitar");
        btnSolicitar.setBackground(new Color(39, 174, 96));
        btnSolicitar.setForeground(Color.WHITE);
        btnSolicitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = txtCPF.getText().trim();
                if (cpf.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Digite o CPF do paciente!");
                    return;
                }
                
                Paciente p = pacienteDAO.buscarPorId(cpf);
                if (p == null) {
                    JOptionPane.showMessageDialog(dialogo, "Paciente não encontrado!");
                    return;
                }
                
                try {
                    hospital.model.Exame exame = new hospital.model.Exame();
                    exame.setId("EXM" + System.currentTimeMillis());
                    exame.setPacienteCpf(cpf);
                    exame.setMedicoCrmSolicitante(medico.getCrm());
                    exame.setTipoExame((String) comboTipo.getSelectedItem());
                    exame.setDataSolicitacao(java.time.LocalDate.now());
                    exame.setResultado(txtObs.getText().trim());
                    
                    documentoService.solicitarExame(exame);
                    
                    JOptionPane.showMessageDialog(dialogo,
                        "Exame solicitado com sucesso!\nID: " + exame.getId() +
                        "\nPaciente: " + p.getNome(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    
                    dialogo.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo,
                        "Erro ao solicitar exame: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        painelBotoes.add(btnSolicitar);
        painelBotoes.add(btnCancelar);
        dialogo.add(painelBotoes, BorderLayout.SOUTH);
        
        dialogo.setVisible(true);
    }
    
    // ==================== DETECÇÃO AUTOMÁTICA DE FALTAS ====================
    
    private void detectarFaltasAutomaticas() {
        try {
            List<Consulta> todasConsultas = consultaDAO.listarPorMedico(medico.getCrm());
            LocalDateTime agora = LocalDateTime.now();
            int faltasDetectadas = 0;
            
            for (Consulta c : todasConsultas) {
                // Se a consulta está agendada e já passou da hora
                if (c.getStatus() == hospital.model.enums.StatusConsulta.AGENDADA && 
                    c.getDataHora().isBefore(agora)) {
                    
                    // Marcar como falta automaticamente
                    c.registrarFalta();
                    consultaDAO.salvar(c);
                    faltasDetectadas++;
                    
                    Logger.info("MedicoGUI", "Falta detectada automaticamente: " + c.getId());
                }
            }
            
            if (faltasDetectadas > 0) {
                Logger.info("MedicoGUI", "Total de faltas detectadas: " + faltasDetectadas);
            }
        } catch (Exception e) {
            Logger.erro("MedicoGUI", "Erro ao detectar faltas", e);
        }
    }
    
    private int contarFaltasHoje() {
        try {
            List<Consulta> consultas = consultaDAO.listarPorMedico(medico.getCrm());
            LocalDate hoje = LocalDate.now();
            int faltas = 0;
            
            for (Consulta c : consultas) {
                if (c.getStatus() == hospital.model.enums.StatusConsulta.FALTOU &&
                    c.getDataHora().toLocalDate().equals(hoje)) {
                    faltas++;
                }
            }
            
            return faltas;
        } catch (Exception e) {
            return 0;
        }
    }
    
    private int contarNovasConsultas() {
        try {
            List<Consulta> consultas = consultaDAO.listarFuturasPorMedico(medico.getCrm());
            LocalDate hoje = LocalDate.now();
            int novas = 0;
            
            for (Consulta c : consultas) {
                // Considerar "nova" se foi criada hoje
                if (c.getDataHora().toLocalDate().isAfter(hoje) ||
                    c.getDataHora().toLocalDate().equals(hoje)) {
                    novas++;
                }
            }
            
            return novas;
        } catch (Exception e) {
            return 0;
        }
    }
    
    // ==================== PAINEL DE HORÁRIOS ====================
    
    private JPanel criarPainelHorarios() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Área de texto para exibir horários
        areaHorarios = new JTextArea();
        areaHorarios.setEditable(false);
        areaHorarios.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaHorarios);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnAdicionar = new JButton("Adicionar Horário");
        btnAdicionar.addActionListener(e -> adicionarHorario());
        
        JButton btnRemover = new JButton("Remover Horário");
        btnRemover.addActionListener(e -> removerHorario());
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarHorarios());
        
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAtualizar);
        
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Carregar horários iniciais
        carregarHorarios();
        
        return painel;
    }
    
    private void carregarHorarios() {
        List<HorarioAtendimento> horarios = horarioDAO.listarPorMedico(medico.getCrm());
        StringBuilder sb = new StringBuilder();
        sb.append("=== MEUS HORÁRIOS DE ATENDIMENTO ===\\n\\n");
        
        if (horarios.isEmpty()) {
            sb.append("Nenhum horário cadastrado.\\n");
            sb.append("Use o botão 'Adicionar Horário' para definir seus horários de atendimento.\\n");
        } else {
            // Ordenar por dia da semana
            horarios.sort((h1, h2) -> h1.getDiaSemana().compareTo(h2.getDiaSemana()));
            
            for (HorarioAtendimento h : horarios) {
                sb.append(traduzirDia(h.getDiaSemana())).append("\\n");
                sb.append("   Horário: ").append(h.getHoraInicio()).append(" às ").append(h.getHoraFim()).append("\\n");
                sb.append("   Duração: ").append(h.getDuracaoConsulta()).append(" minutos por consulta\\n");
                sb.append("   Slots disponíveis: ").append(calcularSlots(h)).append("\\n");
                sb.append("\\n").append("=".repeat(50)).append("\\n\\n");
            }
        }
        
        areaHorarios.setText(sb.toString());
        areaHorarios.setCaretPosition(0);
    }
    
    private int calcularSlots(HorarioAtendimento h) {
        long minutos = java.time.Duration.between(h.getHoraInicio(), h.getHoraFim()).toMinutes();
        return (int) (minutos / h.getDuracaoConsulta());
    }
    
    private String traduzirDia(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "Segunda-feira";
            case TUESDAY: return "Terça-feira";
            case WEDNESDAY: return "Quarta-feira";
            case THURSDAY: return "Quinta-feira";
            case FRIDAY: return "Sexta-feira";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return dia.toString();
        }
    }
    
    private void adicionarHorario() {
        JDialog dialogo = new JDialog(this, "Adicionar Horário", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Dia da semana
        gbc.gridx = 0; gbc.gridy = 0;
        dialogo.add(new JLabel("Dia da Semana:"), gbc);
        
        gbc.gridx = 1;
        String[] dias = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", 
                        "Sexta-feira", "Sábado", "Domingo"};
        JComboBox<String> comboDia = new JComboBox<>(dias);
        dialogo.add(comboDia, gbc);
        
        // Hora início
        gbc.gridx = 0; gbc.gridy = 1;
        dialogo.add(new JLabel("Hora Início:"), gbc);
        
        gbc.gridx = 1;
        JPanel painelInicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JSpinner spinnerHoraInicio = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        JSpinner spinnerMinInicio = new JSpinner(new SpinnerNumberModel(0, 0, 59, 15));
        painelInicio.add(spinnerHoraInicio);
        painelInicio.add(new JLabel(":"));
        painelInicio.add(spinnerMinInicio);
        dialogo.add(painelInicio, gbc);
        
        // Hora fim
        gbc.gridx = 0; gbc.gridy = 2;
        dialogo.add(new JLabel("Hora Fim:"), gbc);
        
        gbc.gridx = 1;
        JPanel painelFim = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JSpinner spinnerHoraFim = new JSpinner(new SpinnerNumberModel(18, 0, 23, 1));
        JSpinner spinnerMinFim = new JSpinner(new SpinnerNumberModel(0, 0, 59, 15));
        painelFim.add(spinnerHoraFim);
        painelFim.add(new JLabel(":"));
        painelFim.add(spinnerMinFim);
        dialogo.add(painelFim, gbc);
        
        // Duração
        gbc.gridx = 0; gbc.gridy = 3;
        dialogo.add(new JLabel("Duração (min):"), gbc);
        
        gbc.gridx = 1;
        JSpinner spinnerDuracao = new JSpinner(new SpinnerNumberModel(30, 15, 120, 15));
        dialogo.add(spinnerDuracao, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                DayOfWeek dia = converterDia(comboDia.getSelectedIndex());
                
                int horaIn = (Integer) spinnerHoraInicio.getValue();
                int minIn = (Integer) spinnerMinInicio.getValue();
                java.time.LocalTime horaInicio = java.time.LocalTime.of(horaIn, minIn);
                
                int horaFi = (Integer) spinnerHoraFim.getValue();
                int minFi = (Integer) spinnerMinFim.getValue();
                java.time.LocalTime horaFim = java.time.LocalTime.of(horaFi, minFi);
                
                int duracao = (Integer) spinnerDuracao.getValue();
                
                // Validações
                if (horaFim.isBefore(horaInicio) || horaFim.equals(horaInicio)) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "Hora de término deve ser posterior ao início!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                HorarioAtendimento horario = new HorarioAtendimento(dia, horaInicio, horaFim, duracao);
                horarioDAO.salvar(medico.getCrm(), horario);
                
                JOptionPane.showMessageDialog(dialogo, "Horário cadastrado com sucesso!");
                dialogo.dispose();
                carregarHorarios();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, 
                    "Erro ao salvar horário: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        dialogo.add(painelBotoes, gbc);
        
        dialogo.setVisible(true);
    }
    
    private void removerHorario() {
        String[] dias = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", 
                        "Sexta-feira", "Sábado", "Domingo"};
        String diaStr = (String) JOptionPane.showInputDialog(
            this,
            "Selecione o dia para remover:",
            "Remover Horário",
            JOptionPane.QUESTION_MESSAGE,
            null,
            dias,
            dias[0]
        );
        
        if (diaStr != null) {
            int index = java.util.Arrays.asList(dias).indexOf(diaStr);
            DayOfWeek dia = converterDia(index);
            
            int confirma = JOptionPane.showConfirmDialog(
                this,
                "Confirma a remoção do horário de " + diaStr + "?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirma == JOptionPane.YES_OPTION) {
                horarioDAO.remover(medico.getCrm(), dia);
                JOptionPane.showMessageDialog(this, "Horário removido com sucesso!");
                carregarHorarios();
            }
        }
    }
    
    private DayOfWeek converterDia(int index) {
        switch (index) {
            case 0: return DayOfWeek.MONDAY;
            case 1: return DayOfWeek.TUESDAY;
            case 2: return DayOfWeek.WEDNESDAY;
            case 3: return DayOfWeek.THURSDAY;
            case 4: return DayOfWeek.FRIDAY;
            case 5: return DayOfWeek.SATURDAY;
            case 6: return DayOfWeek.SUNDAY;
            default: return DayOfWeek.MONDAY;
        }
    }
}
