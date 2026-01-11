package hospital.service;

import hospital.dao.MedicoDAO;
import hospital.dao.PacienteDAO;
import hospital.dao.RecepcionistaDAO;
import hospital.model.Medico;
import hospital.model.Paciente;
import hospital.model.Recepcionista;
import hospital.model.Usuario;
import hospital.model.exceptions.DadoInvalidoException;
import hospital.util.Logger;
import hospital.util.Validador;
import hospital.util.ValidadorCPF;
import hospital.util.ValidadorEmail;

public class CadastroService {

    private PacienteDAO pacienteDAO;
    private MedicoDAO medicoDAO;
    private RecepcionistaDAO recepcionistaDAO;
    private AutenticacaoService autenticacaoService;

    public CadastroService() {
        this.pacienteDAO = new PacienteDAO();
        this.medicoDAO = new MedicoDAO();
        this.recepcionistaDAO = new RecepcionistaDAO();
        this.autenticacaoService = new AutenticacaoService();
        Logger.info("CadastroService", "Serviço de cadastro inicializado");
    }

    public void cadastrarPaciente(Paciente paciente) {
        Logger.info("CadastroService", "Cadastrando paciente: " + (paciente != null ? paciente.getCpf() : "null"));

        if (paciente == null) throw new DadoInvalidoException("Paciente é obrigatório");
        Validador.validarNome(paciente.getNome());
        if (!ValidadorCPF.validar(paciente.getCpf())) throw new DadoInvalidoException("CPF inválido");
        if (!ValidadorEmail.validar(paciente.getEmail())) throw new DadoInvalidoException("Email inválido");

        if (autenticacaoService.loginExiste(paciente.getCpf())) {
            throw new DadoInvalidoException("Login já existe: " + paciente.getCpf());
        }

        paciente.setLogin(paciente.getCpf());
        pacienteDAO.salvar(paciente);
        Logger.info("CadastroService", "Paciente cadastrado: " + paciente.getCpf());
    }

    public void cadastrarMedico(Medico medico) {
        Logger.info("CadastroService", "Cadastrando médico: " + (medico != null ? medico.getCrm() : "null"));

        if (medico == null) throw new DadoInvalidoException("Médico é obrigatório");
        Validador.validarNome(medico.getNome());
        if (!ValidadorEmail.validar(medico.getEmail())) throw new DadoInvalidoException("Email inválido");

        if (autenticacaoService.loginExiste(medico.getCrm())) {
            throw new DadoInvalidoException("Login já existe: " + medico.getCrm());
        }

        medico.setLogin(medico.getCrm());
        medicoDAO.salvar(medico);
        Logger.info("CadastroService", "Médico cadastrado: " + medico.getCrm());
    }

    public void cadastrarRecepcionista(Recepcionista recepcionista) {
        Logger.info("CadastroService", "Cadastrando recepcionista: " + (recepcionista != null ? recepcionista.getMatricula() : "null"));

        if (recepcionista == null) throw new DadoInvalidoException("Recepcionista é obrigatório");
        Validador.validarNome(recepcionista.getNome());
        if (!ValidadorEmail.validar(recepcionista.getEmail())) throw new DadoInvalidoException("Email inválido");

        if (autenticacaoService.loginExiste(recepcionista.getMatricula())) {
            throw new DadoInvalidoException("Login já existe: " + recepcionista.getMatricula());
        }

        recepcionista.setLogin(recepcionista.getMatricula());
        recepcionistaDAO.salvar(recepcionista);
        Logger.info("CadastroService", "Recepcionista cadastrado: " + recepcionista.getMatricula());
    }

    public void atualizarUsuario(Usuario usuario) {
        Logger.info("CadastroService", "Atualizando usuário: " + (usuario != null ? usuario.getLogin() : "null"));
        if (usuario == null) throw new DadoInvalidoException("Usuário é obrigatório");

        if (usuario instanceof Paciente) {
            pacienteDAO.salvar((Paciente) usuario);
        } else if (usuario instanceof Medico) {
            medicoDAO.salvar((Medico) usuario);
        } else if (usuario instanceof Recepcionista) {
            recepcionistaDAO.salvar((Recepcionista) usuario);
        } else {
            throw new DadoInvalidoException("Tipo de usuário desconhecido");
        }
    }
}
