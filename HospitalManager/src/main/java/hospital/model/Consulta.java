package hospital.model;

import hospital.model.enums.StatusConsulta;
import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String observacoes;
    private boolean pacienteCompareceu;
    

    public Consulta(int id, Paciente paciente, Medico medico, LocalDateTime dataHora) {

        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.status = StatusConsulta.AGENDADA;
        this.pacienteCompareceu = false;
    }
    
    public Consulta() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    
    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }

    public boolean isPacienteCompareceu() { return pacienteCompareceu; }
    public void setPacienteCompareceu(boolean compareceu) { 
        this.pacienteCompareceu = compareceu; 
    }
    

    public void cancelar() {
        this.status = StatusConsulta.CANCELADA;
    }
    
    public void concluir(String observacoes) {
        this.status = StatusConsulta.CONCLUIDA;
        this.observacoes = observacoes;
    }
    
    public void registrarFalta() {
        this.status = StatusConsulta.FALTOU;
        this.pacienteCompareceu = false;
    }
    
    public boolean estaAgendada() {
        return status == StatusConsulta.AGENDADA;
    }
    
    public boolean estaConcluida() {
        return status == StatusConsulta.CONCLUIDA;
    }
    
    @Override
    public String toString() {
        return "Consulta #" + id + " - " + dataHora + 
               "\nPaciente: " + paciente.getNome() +
               "\nMédico: " + medico.getNome() + 
               "\nStatus: " + status +
               "\nCompareceu: " + (pacienteCompareceu ? "Sim" : "Não");
    }
}