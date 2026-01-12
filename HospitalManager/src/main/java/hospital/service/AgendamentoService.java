package hospital.service;

import hospital.dao.ConsultaDAO;
import hospital.dao.MedicoDAO;
import hospital.dao.PacienteDAO;
import hospital.dao.HorarioAtendimentoDAO;
import hospital.model.Consulta;
import hospital.model.Medico;
import hospital.model.Paciente;
import hospital.model.HorarioAtendimento;
import hospital.model.exceptions.AgendamentoInvalidoException;
import hospital.util.DataHoraUtil;
import hospital.util.Logger;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class AgendamentoService {

    private ConsultaDAO consultaDAO;
    private PacienteDAO pacienteDAO;
    private MedicoDAO medicoDAO;
    private HorarioAtendimentoDAO horarioDAO;

    public AgendamentoService() {
        this.consultaDAO = new ConsultaDAO();
        this.pacienteDAO = new PacienteDAO();
        this.medicoDAO = new MedicoDAO();
        this.horarioDAO = new HorarioAtendimentoDAO();
        Logger.info("AgendamentoService", "Serviço de agendamento inicializado");
    }

    public void agendar(Consulta consulta) {
        Logger.info("AgendamentoService", "Agendando consulta: " + (consulta != null ? consulta.toString() : "null"));

        if (consulta == null) throw new AgendamentoInvalidoException("Consulta nula");

        
        Paciente paciente = pacienteDAO.buscarPorId(consulta.getPacienteCpf());
        if (paciente == null) throw new AgendamentoInvalidoException("Paciente não encontrado: " + consulta.getPacienteCpf());

        Medico medico = medicoDAO.buscarPorId(consulta.getMedicoCrm());
        if (medico == null) throw new AgendamentoInvalidoException("Médico não encontrado: " + consulta.getMedicoCrm());
        if (!medico.isAtivo()) throw new AgendamentoInvalidoException("Médico inativo: " + medico.getCrm());

        
        LocalDateTime agora = LocalDateTime.now();
        if (consulta.getDataHora() == null || consulta.getDataHora().isBefore(agora)) {
            throw new AgendamentoInvalidoException("Data/hora inválida");
        }
        
        // Validar se o horário está dentro dos slots cadastrados pelo médico
        validarHorarioDisponivel(medico.getCrm(), consulta.getDataHora());

        
        List<Consulta> futuras = consultaDAO.listarFuturasPorMedico(medico.getCrm());
        for (Consulta c : futuras) {
            if (c.getDataHora() != null && c.getDataHora().isEqual(consulta.getDataHora())) {
                throw new AgendamentoInvalidoException("Conflito de horário para o médico");
            }
        }

    
        consultaDAO.salvar(consulta);
        Logger.info("AgendamentoService", "Consulta agendada: " + consulta.getId());
    }

    public void cancelarConsulta(String id) {
        Logger.info("AgendamentoService", "Cancelando consulta: " + id);
        Consulta c = consultaDAO.buscarPorId(id);
        if (c == null) {
            Logger.aviso("AgendamentoService", "Consulta não encontrada: " + id);
            return;
        }
        c.cancelar();
        consultaDAO.salvar(c);
    }

    public void reagendar(String id, LocalDateTime novaDataHora) {
        Logger.info("AgendamentoService", "Reagendando consulta: " + id + " -> " + novaDataHora);
        Consulta c = consultaDAO.buscarPorId(id);
        if (c == null) throw new AgendamentoInvalidoException("Consulta não encontrada: " + id);
        c.setDataHora(novaDataHora);
        agendar(c); 
    }

    public List<Consulta> listarPorMedico(String crm) {
        return consultaDAO.listarPorMedico(crm);
    }

    public List<Consulta> listarPorPaciente(String cpf) {
        return consultaDAO.listarPorPaciente(cpf);
    }
    
    private void validarHorarioDisponivel(String medicoCrm, LocalDateTime dataHora) {
        List<HorarioAtendimento> horarios = horarioDAO.listarPorMedico(medicoCrm);
        
        if (horarios.isEmpty()) {
            // Se não há horários cadastrados, permite qualquer horário
            Logger.aviso("AgendamentoService", "Médico sem horários cadastrados - permitindo agendamento");
            return;
        }
        
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime horario = dataHora.toLocalTime();
        
        // Verificar se existe horário para esse dia
        for (HorarioAtendimento h : horarios) {
            if (h.getDiaSemana().equals(diaSemana)) {
                // Verificar se está dentro do intervalo
                if (!horario.isBefore(h.getHoraInicio()) && !horario.isAfter(h.getHoraFim())) {
                    // Está dentro do horário de atendimento
                    return;
                }
            }
        }
        
        throw new AgendamentoInvalidoException(
            "Horário fora dos slots de atendimento do médico. " +
            "Verifique os horários disponíveis na aba 'Meus Horários'."
        );
    }
}
