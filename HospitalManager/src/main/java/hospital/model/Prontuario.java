package hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Prontuario {
    private String pacienteCpf;
    private List<Consulta> historicoConsultas;
    private List<Exame> historicoExames;
    private List<Atestado> atestados;
    private List<Receita> receitas;
    private String alergias;
    private String medicamentosUsoContinuo;
    private String historicoFamiliar;
    
    public Prontuario(String pacienteCpf) {
        this.pacienteCpf = pacienteCpf;
        this.historicoConsultas = new ArrayList<>();
        this.historicoExames = new ArrayList<>();
        this.atestados = new ArrayList<>();
        this.receitas = new ArrayList<>();
        this.alergias = "";
        this.medicamentosUsoContinuo = "";
        this.historicoFamiliar = "";
    }
    
    public Prontuario() {
        this.historicoConsultas = new ArrayList<>();
        this.historicoExames = new ArrayList<>();
        this.atestados = new ArrayList<>();
        this.receitas = new ArrayList<>();
    }
    
    public String getPacienteCpf() { return pacienteCpf; }
    public void setPacienteCpf(String pacienteCpf) { this.pacienteCpf = pacienteCpf; }
    
    public List<Consulta> getHistoricoConsultas() { return historicoConsultas; }
    public void setHistoricoConsultas(List<Consulta> historico) { this.historicoConsultas = historico; }
    
    public List<Exame> getHistoricoExames() { return historicoExames; }
    public void setHistoricoExames(List<Exame> exames) { this.historicoExames = exames; }
    
    public List<Atestado> getAtestados() { return atestados; }
    public void setAtestados(List<Atestado> atestados) { this.atestados = atestados; }
    
    public List<Receita> getReceitas() { return receitas; }
    public void setReceitas(List<Receita> receitas) { this.receitas = receitas; }
    
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    
    public String getMedicamentosUsoContinuo() { return medicamentosUsoContinuo; }
    public void setMedicamentosUsoContinuo(String medicamentos) { this.medicamentosUsoContinuo = medicamentos; }
    
    public String getHistoricoFamiliar() { return historicoFamiliar; }
    public void setHistoricoFamiliar(String historico) { this.historicoFamiliar = historico; }
    
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
    
    public void imprimirResumo() {
        System.out.println("=== PRONTUÁRIO MÉDICO ===");
        System.out.println("Paciente CPF: " + pacienteCpf);
        System.out.println("\nAlergias: " + alergias);
        System.out.println("Medicamentos em uso: " + medicamentosUsoContinuo);
        System.out.println("Histórico familiar: " + historicoFamiliar);
        System.out.println("\nTotal de consultas: " + historicoConsultas.size());
        System.out.println("Total de exames: " + historicoExames.size());
        System.out.println("Total de atestados: " + atestados.size());
        System.out.println("Total de receitas: " + receitas.size());
    }
}