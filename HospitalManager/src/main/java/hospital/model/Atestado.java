package hospital.model;

import java.time.LocalDate;

public class Atestado {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDate dataEmissao;
    private int diasAfastamento;
    private String cid; // Código Internacional de Doenças (Descobri que isso existe, acho que é melhor que sla colocar string doença)
    
    public Atestado(int id, Paciente paciente, Medico medico, int diasAfastamento, String cid) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataEmissao = LocalDate.now();
        this.diasAfastamento = diasAfastamento;
        this.cid = cid;
    }
    

    public Atestado() {}
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    
    public int getDiasAfastamento() { return diasAfastamento; }
    public void setDiasAfastamento(int diasAfastamento) { 
        this.diasAfastamento = diasAfastamento; 
    }
    
    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }
 
    
    public LocalDate getDataRetorno() {
        return dataEmissao.plusDays(diasAfastamento);
    }
    
    @Override
    public String toString() {
        return "Atestado #" + id + 
               "\nPaciente: " + paciente.getNome() +
               "\nMédico: " + medico.getNome() +
               "\nData: " + dataEmissao +
               "\nDias de afastamento: " + diasAfastamento +
               "\nRetorno: " + getDataRetorno() +
               "\nCID: " + cid +
               "\nDescrição: " + descricao;
    }
}
