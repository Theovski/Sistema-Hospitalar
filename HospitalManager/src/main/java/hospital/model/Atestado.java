package hospital.model;

import java.time.LocalDate;

public class Atestado {
    private String id;
    private String pacienteCpf;
    private String medicoCrm;
    private LocalDate dataEmissao;
    private int diasAfastamento;
    private String cid;
    private String descricao;
    
<<<<<<< HEAD
    public Atestado(String id, String pacienteCpf, String medicoCrm, int diasAfastamento, String cid) {
=======
    public Atestado(int id, Paciente paciente, Medico medico, int diasAfastamento, String cid) {
>>>>>>> 586504875a4bde42a23314da35225f64cbeac28e
        this.id = id;
        this.pacienteCpf = pacienteCpf;
        this.medicoCrm = medicoCrm;
        this.dataEmissao = LocalDate.now();
        this.diasAfastamento = diasAfastamento;
        this.cid = cid;
    }
    
<<<<<<< HEAD
    public Atestado() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
=======

    public Atestado() {}
    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
>>>>>>> 586504875a4bde42a23314da35225f64cbeac28e
    
    public String getPacienteCpf() { return pacienteCpf; }
    public void setPacienteCpf(String pacienteCpf) { this.pacienteCpf = pacienteCpf; }
    
    public String getMedicoCrm() { return medicoCrm; }
    public void setMedicoCrm(String medicoCrm) { this.medicoCrm = medicoCrm; }
    
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    
    public int getDiasAfastamento() { return diasAfastamento; }
    public void setDiasAfastamento(int diasAfastamento) { this.diasAfastamento = diasAfastamento; }
    
    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDate getDataRetorno() {
        return dataEmissao.plusDays(diasAfastamento);
    }
    
    public String getDiagnostico() { return descricao; }
    public void setDiagnostico(String diagnostico) { this.descricao = diagnostico; }
    
    public int getDiasRepouso() { return diasAfastamento; }
    public void setDiasRepouso(int dias) { this.diasAfastamento = dias; }
    
    @Override
    public String toString() {
        return "Atestado #" + id + 
               "\nPaciente CPF: " + pacienteCpf +
               "\nMédico CRM: " + medicoCrm +
               "\nData: " + dataEmissao +
               "\nDias de afastamento: " + diasAfastamento +
               "\nRetorno: " + getDataRetorno() +
               "\nCID: " + cid +
               "\nDescrição: " + descricao;
    }
}
