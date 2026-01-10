package hospital.service;

import hospital.dao.PacienteDAO;
import hospital.dao.MedicoDAO;
import hospital.dao.RecepcionistaDAO;
import hospital.model.Usuario;
import hospital.model.Paciente;
import hospital.model.Medico;
import hospital.model.Recepcionista;
import hospital.model.exceptions.UsuarioNaoEncontradoException;
import hospital.util.Logger;

public class AutenticacaoService {
    
    private PacienteDAO pacienteDAO;
    private MedicoDAO medicoDAO;
    private RecepcionistaDAO recepcionistaDAO;
    
    public AutenticacaoService() {
        this.pacienteDAO = new PacienteDAO();
        this.medicoDAO = new MedicoDAO();
        this.recepcionistaDAO = new RecepcionistaDAO();
        Logger.info("AutenticacaoService", "Serviço de autenticação inicializado");
    }
    
    public Usuario autenticar(String login, String senha) throws UsuarioNaoEncontradoException {
        Logger.info("AutenticacaoService", "Tentativa de login: " + login);
        
        // Validação de entrada
        if (login == null || login.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            Logger.aviso("AutenticacaoService", "Login ou senha vazios");
            throw new UsuarioNaoEncontradoException("Login e senha são obrigatórios");
        }
        
        // Tenta autenticar como Paciente (login = CPF)
        Paciente paciente = pacienteDAO.buscarPorId(login);
        if (paciente != null && paciente.getSenha().equals(senha)) {
            Logger.info("AutenticacaoService", "Paciente autenticado: " + login);
            return paciente;
        }
        
        // Tenta autenticar como Médico (login = CRM)
        Medico medico = medicoDAO.buscarPorId(login);
        if (medico != null && medico.getSenha().equals(senha)) {
            if (!medico.isAtivo()) {
                Logger.aviso("AutenticacaoService", "Médico inativo tentou fazer login: " + login);
                throw new UsuarioNaoEncontradoException("Médico desativado. Contate a administração.");
            }
            Logger.info("AutenticacaoService", "Médico autenticado: " + login);
            return medico;
        }
        
        // Tenta autenticar como Recepcionista (login = Matrícula)
        Recepcionista recepcionista = recepcionistaDAO.buscarPorMatricula(login);
        if (recepcionista != null && recepcionista.getSenha().equals(senha)) {
            Logger.info("AutenticacaoService", "Recepcionista autenticado: " + login);
            return recepcionista;
        }
        
        // Nenhum usuário encontrado
        Logger.aviso("AutenticacaoService", "Falha na autenticação: " + login);
        throw new UsuarioNaoEncontradoException("Login ou senha inválidos");
    }
    
    public boolean loginExiste(String login) {
        return pacienteDAO.buscarPorId(login) != null ||
               medicoDAO.buscarPorId(login) != null ||
               recepcionistaDAO.buscarPorMatricula(login) != null;
    }
}
