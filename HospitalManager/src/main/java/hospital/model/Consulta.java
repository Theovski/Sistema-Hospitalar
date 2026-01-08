package hospital.model;

import hospital.model.enums.StatusConsulta;
import java.time.LocalDateTime;

public class Consulta {
    private String id;
    private String pacienteCpf;
    private String medicoCrm;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String observacoes;
    private boolean compareceu;
    
    public Consulta(String id, String pacienteCpf, String medicoCrm, LocalDateTime dataHora) {
        this.id = id;
        this.pacienteCpf = pacienteCpf;
        this.medicoCrm = medicoCrm;
        this.dataHora = dataHora;
        this.status = StatusConsulta.AGENDADA;
        this.compareceu = false;
    }
    
    public Consulta() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getPacienteCpf() { return pacienteCpf; }
    public void setPacienteCpf(String pacienteCpf) { this.pacienteCpf = pacienteCpf; }
    
    public String getMedicoCrm() { return medicoCrm; }
    public void setMedicoCrm(String medicoCrm) { this.medicoCrm = medicoCrm; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    
    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public boolean isCompareceu() { return compareceu; }
    public void setCompareceu(boolean compareceu) { this.compareceu = compareceu; }
    
    public void cancelar() {
        this.status = StatusConsulta.CANCELADA;
    }
    
    public void concluir(String observacoes) {
        this.status = StatusConsulta.CONCLUIDA;
        this.observacoes = observacoes;
    }
    
    public void registrarFalta() {
        this.status = StatusConsulta.FALTOU;
        this.compareceu = false;
    }
    
    public boolean estaAgendada() {
        return status == StatusConsulta.AGENDADA;
    }
    
    @Override
    public String toString() {
        return "Consulta #" + id + " - " + dataHora + 
               "\nPaciente CPF: " + pacienteCpf +
               "\nMédico CRM: " + medicoCrm + 
               "\nStatus: " + status +
               "\nCompareceu: " + (compareceu ? "Sim" : "Não");
    }
}