package hospital.service;

import hospital.dao.ConsultaDAO;
import hospital.dao.PacienteDAO;
import hospital.model.Consulta;
import hospital.model.Paciente;
import hospital.model.enums.StatusVisita;
import hospital.util.Logger;

import java.util.List;

public class VisitaService {

    private ConsultaDAO consultaDAO;
    private PacienteDAO pacienteDAO;
    private DocumentoService documentoService;

    public VisitaService() {
        this.consultaDAO = new ConsultaDAO();
        this.pacienteDAO = new PacienteDAO();
        this.documentoService = new DocumentoService();
        Logger.info("VisitaService", "Serviço de visitas inicializado");
    }

    public void registrarVisita(String idConsulta) {
        Logger.info("VisitaService", "Registrando visita para consulta: " + idConsulta);
        Consulta c = consultaDAO.buscarPorId(idConsulta);
        if (c == null) {
            Logger.aviso("VisitaService", "Consulta não encontrada: " + idConsulta);
            return;
        }

        c.setCompareceu(true);
        c.setStatus(hospital.model.enums.StatusConsulta.CONCLUIDA);
        consultaDAO.salvar(c);

        //funcao que atualiza paciente como apto para visita
        Paciente p = pacienteDAO.buscarPorId(c.getPacienteCpf());
        if (p != null) {
            pacienteDAO.atualizarStatusVisita(p.getCpf(), true);
        }
    }

    public void finalizarVisita(String idConsulta, String observacoes) {
        Logger.info("VisitaService", "Finalizando visita: " + idConsulta);
        Consulta c = consultaDAO.buscarPorId(idConsulta);
        if (c == null) return;
        consultaDAO.marcarComoRealizada(idConsulta, observacoes);
    }

    public List<Consulta> listarVisitasPorPaciente(String cpf) {
        return consultaDAO.listarPorPaciente(cpf);
    }
}
