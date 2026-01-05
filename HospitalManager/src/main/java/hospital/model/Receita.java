package hospital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receita {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDate dataPrescricao;
    private List<Medicamento> medicamentos;
    
    public static class Medicamento {

        private String nome;
        private String dosagem;
        private String frequencia;
        private int duracao; 
        
        public Medicamento(String nome, String dosagem, String frequencia, int duracao) {
            this.nome = nome;
            this.dosagem = dosagem;
            this.frequencia = frequencia;
            this.duracao = duracao;
        }
        
        // Getters
        public String getNome() { return nome; }
        public String getDosagem() { return dosagem; }
        public String getFrequencia() { return frequencia; }
        public int getDuracao() { return duracao; }
        
        @Override
        public String toString() {
            return nome + " - " + dosagem + " - " + frequencia + " - " + duracao + " dias";
        }
    }
    
    public Receita(int id, Paciente paciente, Medico medico) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataPrescricao = LocalDate.now();
        this.medicamentos = new ArrayList<>();
    }
    
    public Receita() {
        this.medicamentos = new ArrayList<>();
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    
    public LocalDate getDataPrescricao() { return dataPrescricao; }
    public void setDataPrescricao(LocalDate dataPrescricao) { 
        this.dataPrescricao = dataPrescricao; 
    }
    
    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<Medicamento> medicamentos) { 
        this.medicamentos = medicamentos; 
    }
    
    public String getOrientacoes() { return orientacoes; }
    public void setOrientacoes(String orientacoes) { this.orientacoes = orientacoes; }
    


    public void adicionarMedicamento(Medicamento medicamento) {
        this.medicamentos.add(medicamento);
    }
    
    public void removerMedicamento(Medicamento medicamento) {
        this.medicamentos.remove(medicamento);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Receita #").append(id)
          .append("\nPaciente: ").append(paciente.getNome())
          .append("\nMédico: ").append(medico.getNome())
          .append("\nData: ").append(dataPrescricao)
          .append("\n\nMedicamentos:\n");
        
        for (Medicamento m : medicamentos) {
            sb.append("- ").append(m).append("\n");
        }
        
        sb.append("\nOrientacoes: ").append(orientacoes);
        return sb.toString();
    }
}