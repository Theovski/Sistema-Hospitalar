package hospital.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Prontuario {

    private Paciente paciente;
    private List<Consulta> historicoConsultas;
    private List<Exame> historicoExames;
    private List<Atestado> atestados;
    private List<Receita> receitas;
    private String alergias;

    

    public Prontuario(Paciente paciente) {

        this.paciente = paciente;
        this.historicoConsultas = new ArrayList<>();
        this.historicoExames = new ArrayList<>();
        this.atestados = new ArrayList<>();
        this.receitas = new ArrayList<>();
        this.alergias = "";
    }
    

    public Prontuario() {
        this.historicoConsultas = new ArrayList<>();
        this.historicoExames = new ArrayList<>();
        this.atestados = new ArrayList<>();
        this.receitas = new ArrayList<>();
    }
    


    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public List<Consulta> getHistoricoConsultas() { return historicoConsultas; }
    public void setHistoricoConsultas(List<Consulta> historico) { 
        this.historicoConsultas = historico; 
    }
    
    public List<Exame> getHistoricoExames() { return historicoExames; }
    public void setHistoricoExames(List<Exame> exames) { 
        this.historicoExames = exames; 
    }
    
    public List<Atestado> getAtestados() { return atestados; }
    public void setAtestados(List<Atestado> atestados) { 
        this.atestados = atestados; 
    }
    
    public List<Receita> getReceitas() { return receitas; }
    public void setReceitas(List<Receita> receitas) { 
        this.receitas = receitas; 
    }
    
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    

    public void adicionarConsulta(Consulta consulta) {
        this.historicoConsultas.add(consulta);
    }
    
    public void adicionarExame(Exame exame) {
        this.historicoExames.add(exame);
    }
    
    public void adicionarAtestado(Atestado atestado) {
        this.atestados.add(atestado);
    }
    
    public void adicionarReceita(Receita receita) {
        this.receitas.add(receita);
    }
    
    public Consulta getUltimaConsulta() {
        if (historicoConsultas.isEmpty()) return null;
        return historicoConsultas.get(historicoConsultas.size() - 1);
    }
    
    public List<Consulta> getConsultasPorMedico(Medico medico) {
        List<Consulta> consultasMedico = new ArrayList<>();
        for (Consulta c : historicoConsultas) {
            if (c.getMedico().equals(medico)) {
                consultasMedico.add(c);
            }
        }
        return consultasMedico;
    }
    
    public void imprimirResumo() {
        System.out.println("=== PRONTUÁRIO MÉDICO ===");
        System.out.println("Paciente: " + paciente.getNome());
        System.out.println("CPF: " + paciente.getCpf());
        System.out.println("\nAlergias: " + alergias);
        System.out.println("\nTotal de consultas: " + historicoConsultas.size());
        System.out.println("Total de exames: " + historicoExames.size());
        System.out.println("Total de atestados: " + atestados.size());
        System.out.println("Total de receitas: " + receitas.size());
    }
}
