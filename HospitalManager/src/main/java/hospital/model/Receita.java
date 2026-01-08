package hospital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receita {
    private String id;
    private String pacienteCpf;
    private String medicoCrm;
    private LocalDate dataPrescricao;
    private List<Medicamento> medicamentos;
    private String orientacoes;
    
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
        
        public String getNome() { return nome; }
        public String getDosagem() { return dosagem; }
        public String getFrequencia() { return frequencia; }
        public int getDuracao() { return duracao; }
        
        @Override
        public String toString() {
            return nome + " - " + dosagem + " - " + frequencia + " - " + duracao + " dias";
        }
    }
    
    public Receita(String id, String pacienteCpf, String medicoCrm) {
        this.id = id;
        this.pacienteCpf = pacienteCpf;
        this.medicoCrm = medicoCrm;
        this.dataPrescricao = LocalDate.now();
        this.medicamentos = new ArrayList<>();
    }
    
    public Receita() {
        this.medicamentos = new ArrayList<>();
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getPacienteCpf() { return pacienteCpf; }
    public void setPacienteCpf(String pacienteCpf) { this.pacienteCpf = pacienteCpf; }
    
    public String getMedicoCrm() { return medicoCrm; }
    public void setMedicoCrm(String medicoCrm) { this.medicoCrm = medicoCrm; }
    
    public LocalDate getDataPrescricao() { return dataPrescricao; }
    public void setDataPrescricao(LocalDate dataPrescricao) { this.dataPrescricao = dataPrescricao; }
    
    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<Medicamento> medicamentos) { this.medicamentos = medicamentos; }
    
    public String getOrientacoes() { return orientacoes; }
    public void setOrientacoes(String orientacoes) { this.orientacoes = orientacoes; }
    
    public void adicionarMedicamento(Medicamento medicamento) {
        this.medicamentos.add(medicamento);
    }
    
    public void removerMedicamento(Medicamento medicamento) {
        this.medicamentos.remove(medicamento);
    }
    
    public String getMedicamentosString() {
        StringBuilder sb = new StringBuilder();
        for (Medicamento m : medicamentos) {
            sb.append(m.toString()).append("; ");
        }
        return sb.toString();
    }
    
    public String getInstrucoes() { return orientacoes; }
    public void setInstrucoes(String instrucoes) { this.orientacoes = instrucoes; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Receita #").append(id)
          .append("\nPaciente CPF: ").append(pacienteCpf)
          .append("\nMédico CRM: ").append(medicoCrm)
          .append("\nData: ").append(dataPrescricao)
          .append("\n\nMedicamentos:\n");
        
        for (Medicamento m : medicamentos) {
            sb.append("- ").append(m).append("\n");
        }
        
        sb.append("\nOrientações: ").append(orientacoes);
        return sb.toString();
    }
}