package hospital.view;

import hospital.service.AutenticacaoService;
import hospital.model.Usuario;
import hospital.model.Paciente;
import hospital.model.Medico;
import hospital.model.Recepcionista;
import hospital.model.exceptions.UsuarioNaoEncontradoException;
import hospital.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;
    private AutenticacaoService autenticacaoService;
    
    public LoginGUI() {
        this.autenticacaoService = new AutenticacaoService();
        inicializarComponentes();
        Logger.info("LoginGUI", "Tela de login criada");
    }
    
    private void inicializarComponentes() {
        // Configurações da janela
        setTitle("Sistema Hospitalar - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Layout principal
        setLayout(new BorderLayout(10, 10));
        
        // Painel do cabeçalho
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(21, 101, 192));
        painelTopo.setPreferredSize(new Dimension(400, 80));
        
        JLabel lblTitulo = new JLabel("Sistema de Gerenciamento Hospitalar");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        painelTopo.add(lblTitulo);
        
        add(painelTopo, BorderLayout.NORTH);
        
        // Painel central com campos de login
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new GridBagLayout());
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Label e campo de Login
        JLabel lblLogin = new JLabel("Login (CPF/CRM/Matrícula):");
        lblLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelCentro.add(lblLogin, gbc);
        
        txtLogin = new JTextField(20);
        txtLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        painelCentro.add(txtLogin, gbc);
        
        // Label e campo de Senha
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 2;
        painelCentro.add(lblSenha, gbc);
        
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        painelCentro.add(txtSenha, gbc);
        
        add(painelCentro, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEntrar.setBackground(new Color(46, 125, 50));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setPreferredSize(new Dimension(100, 35));
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 12));
        btnSair.setBackground(new Color(198, 40, 40));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setOpaque(true);
        btnSair.setBorderPainted(false);
        btnSair.setPreferredSize(new Dimension(100, 35));
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.info("LoginGUI", "Encerrando aplicação");
                System.exit(0);
            }
        });
        
        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);
        
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Permite pressionar Enter para fazer login
        txtSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }
    
    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        // Validação básica
        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, preencha login e senha.",
                "Campos Vazios",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Tenta autenticar
            Usuario usuario = autenticacaoService.autenticar(login, senha);
            
            Logger.info("LoginGUI", "Login bem-sucedido: " + usuario.getNome());
            
            // Redireciona para a interface apropriada
            abrirInterfaceUsuario(usuario);
            
            // Fecha a tela de login
            dispose();
            
        } catch (UsuarioNaoEncontradoException ex) {
            Logger.aviso("LoginGUI", "Falha no login: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Erro de Autenticação",
                JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtLogin.requestFocus();
        }
    }
    
    private void abrirInterfaceUsuario(Usuario usuario) {
        if (usuario instanceof Paciente) {
            Paciente paciente = (Paciente) usuario;
            Logger.info("LoginGUI", "Abrindo interface de Paciente");
            PacienteGUI gui = new PacienteGUI(paciente);
            gui.setVisible(true);
            
        } else if (usuario instanceof Medico) {
            Medico medico = (Medico) usuario;
            Logger.info("LoginGUI", "Abrindo interface de Médico");
            MedicoGUI gui = new MedicoGUI(medico);
            gui.setVisible(true);
            
        } else if (usuario instanceof Recepcionista) {
            Recepcionista recepcionista = (Recepcionista) usuario;
            Logger.info("LoginGUI", "Abrindo interface de Recepcionista");
            RecepcionistaGUI gui = new RecepcionistaGUI(recepcionista);
            gui.setVisible(true);
        }
    }
}
