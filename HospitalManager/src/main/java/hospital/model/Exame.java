package hospital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Exame {
    private String id;
    private String pacienteCpf;
    private String medicoCrmSolicitante;
    private String tipoExame;
    private LocalDate dataSolicitacao;
    private LocalDateTime dataRealizacao;
    private String resultado;
    private String laboratorio;
    private boolean entregueAoPaciente;
    
    public Exame(String id, String pacienteCpf, String medicoCrmSolicitante, String tipoExame) {
        this.id = id;
        this.pacienteCpf = pacienteCpf;
        this.medicoCrmSolicitante = medicoCrmSolicitante;
        this.tipoExame = tipoExame;
        this.dataSolicitacao = LocalDate.now();
        this.entregueAoPaciente = false;
    }
    
    public Exame() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getPacienteCpf() { return pacienteCpf; }
    public void setPacienteCpf(String pacienteCpf) { this.pacienteCpf = pacienteCpf; }
    
    public String getMedicoCrmSolicitante() { return medicoCrmSolicitante; }
    public void setMedicoCrmSolicitante(String medicoCrm) { this.medicoCrmSolicitante = medicoCrm; }
    
    public String getTipoExame() { return tipoExame; }
    public void setTipoExame(String tipoExame) { this.tipoExame = tipoExame; }
    
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
    
    public LocalDateTime getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDateTime dataRealizacao) { this.dataRealizacao = dataRealizacao; }
    
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    
    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
    
    public boolean isEntregueAoPaciente() { return entregueAoPaciente; }
    public void setEntregueAoPaciente(boolean entregue) { this.entregueAoPaciente = entregue; }
    
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
               "\nPaciente CPF: " + pacienteCpf +
               "\nMédico CRM: " + medicoCrmSolicitante +
               "\nData Solicitação: " + dataSolicitacao +
               "\nResultado: " + (possuiResultado() ? resultado : "Pendente") +
               "\nEntregue: " + (entregueAoPaciente ? "Sim" : "Não");
    }
}