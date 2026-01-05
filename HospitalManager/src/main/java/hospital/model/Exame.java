package hospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Exame {

    private int id;
    private Paciente paciente;
    private Medico medicoSolicitante;
    private String tipoExame;
    private LocalDate dataSolicitacao;
    private LocalDateTime dataRealizacao;
    private String resultado;
    private String laboratorio;
    private boolean entregueAoPaciente;
    
    // Construtor
    public Exame(int id, Paciente paciente, Medico medicoSolicitante, String tipoExame) {

        this.id = id;
        this.paciente = paciente;
        this.medicoSolicitante = medicoSolicitante;
        this.tipoExame = tipoExame;
        this.dataSolicitacao = LocalDate.now();
        this.entregueAoPaciente = false;
    }
    
    public Exame() {}
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Medico getMedicoSolicitante() { return medicoSolicitante; }
    public void setMedicoSolicitante(Medico medicoSolicitante) { 
        this.medicoSolicitante = medicoSolicitante; 
    }
    
    public String getTipoExame() { return tipoExame; }
    public void setTipoExame(String tipoExame) { this.tipoExame = tipoExame; }
    
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { 
        this.dataSolicitacao = dataSolicitacao; 
    }
    
    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { 
        this.dataRealizacao = dataRealizacao; 
    }
    
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    
    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
    
    public boolean isEntregueAoPaciente() { return entregueAoPaciente; }
    public void setEntregueAoPaciente(boolean entregue) { 
        this.entregueAoPaciente = entregue; 
    }
    


    public void registrarResultado(String resultado, String laboratorio, LocalDateTime dataRealizacao) {
        this.resultado = resultado;
        this.laboratorio = laboratorio;
        this.dataRealizacao = dataRealizacao;
    }
    
    public void entregarAoPaciente() {
        this.entregueAoPaciente = true;
    }
    
    public boolean possuiResultado() {
        return resultado != null && !resultado.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Exame #" + id + " - " + tipoExame +
               "\nPaciente: " + paciente.getNome() +
               "\nMédico: " + medicoSolicitante.getNome() +
               "\nData Solicitação: " + dataSolicitacao +
               "\nResultado: " + (possuiResultado() ? resultado : "Pendente") +
               "\nEntregue: " + (entregueAoPaciente ? "Sim" : "Não");
    }
}