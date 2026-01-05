package hospital.model;

import hospital.model.enums.StatusVisita;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Usuario {
    private StatusVisita statusVisita;
    private List<Consulta> consultas;
    private List<Exame> exames;
    
    // Construtor completo (sem convênio)
    public Paciente(String nome, String cpf, String email, String telefone, String endereco,
                    String login, String senha) {
        super(nome, cpf, email, telefone, endereco, login, senha, TipoUsuario.PACIENTE);
        this.statusVisita = StatusVisita.LIBERADA; // padrão
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
    }
    
    // Construtor vazio
    public Paciente() {
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
    }
    
    // Getters e Setters (sem convênio)
    public StatusVisita getStatusVisita() { return statusVisita; }
    public void setStatusVisita(StatusVisita statusVisita) { 
        this.statusVisita = statusVisita; 
    }
    
    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }
    
    public List<Exame> getExames() { return exames; }
    public void setExames(List<Exame> exames) { this.exames = exames; }
    
    // Métodos específicos do paciente
    public void adicionarConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }
    
    public void adicionarExame(Exame exame) {
        this.exames.add(exame);
    }
    
    public List<Consulta> getConsultasFuturas() {
        // Implementar filtro por data (quando tiver DataHoraUtil)
        return consultas;
    }
    
    public boolean podeReceberVisitas() {
        return statusVisita == StatusVisita.LIBERADA;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Status Visita: " + statusVisita;
    }
}