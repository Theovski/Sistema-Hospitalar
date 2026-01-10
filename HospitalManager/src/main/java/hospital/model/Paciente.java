package hospital.model;

import hospital.model.enums.StatusVisita;
import hospital.model.enums.TipoUsuario;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario {
    private StatusVisita statusVisita;
    private List<Consulta> consultas;
    private List<Exame> exames;
    private String convenio;
    
    public Paciente(String nome, String cpf, String email, String telefone, String endereco,
                    String login, String senha) {
        super(nome, cpf, email, telefone, endereco, login, senha, TipoUsuario.PACIENTE);
        this.statusVisita = StatusVisita.LIBERADA;
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
    }
    
    public Paciente() {
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
    }
    
    public StatusVisita getStatusVisita() { return statusVisita; }
    public void setStatusVisita(StatusVisita statusVisita) { 
        this.statusVisita = statusVisita; 
    }
    
    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }
    
    public List<Exame> getExames() { return exames; }
    public void setExames(List<Exame> exames) { this.exames = exames; }
    
    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }
    
    public boolean isAptoVisita() {
        return statusVisita == StatusVisita.LIBERADA;
    }
    
    public void setAptoVisita(boolean apto) {
        this.statusVisita = apto ? StatusVisita.LIBERADA : StatusVisita.PROIBIDA;
    }
    
    public void adicionarConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }
    
    public void adicionarExame(Exame exame) {
        this.exames.add(exame);
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Status Visita: " + statusVisita + ", Convênio: " + convenio;
    }
}