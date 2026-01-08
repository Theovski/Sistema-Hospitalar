package hospital.model;

import hospital.model.enums.Especialidade;
import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {
    private String crm;
    private Especialidade especialidade;
    private List<HorarioAtendimento> horariosAtendimento;
    private boolean ativo;
    private List<Consulta> consultasAgendadas;
    
    public Medico(String nome, String cpf, String email, String telefone, String endereco,
                  String login, String senha, String crm, Especialidade especialidade) {
        super(nome, cpf, email, telefone, endereco, login, senha, TipoUsuario.MEDICO);
        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = true;
        this.horariosAtendimento = new ArrayList<>();
        this.consultasAgendadas = new ArrayList<>();
    }
    
    public Medico() {
        this.horariosAtendimento = new ArrayList<>();
        this.consultasAgendadas = new ArrayList<>();
    }
    
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    
    public Especialidade getEspecialidade() { return especialidade; }
    public void setEspecialidade(Especialidade especialidade) { 
        this.especialidade = especialidade; 
    }
    
    public List<HorarioAtendimento> getHorariosAtendimento() { return horariosAtendimento; }
    public void setHorariosAtendimento(List<HorarioAtendimento> horarios) { 
        this.horariosAtendimento = horarios; 
    }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public List<Consulta> getConsultasAgendadas() { return consultasAgendadas; }
    public void setConsultasAgendadas(List<Consulta> consultas) { 
        this.consultasAgendadas = consultas; 
    }
    
    public void adicionarHorario(HorarioAtendimento horario) {
        this.horariosAtendimento.add(horario);
    }
    
    public void adicionarConsulta(Consulta consulta) {
        this.consultasAgendadas.add(consulta);
    }
    
    public void removerHorario(HorarioAtendimento horario) {
        this.horariosAtendimento.remove(horario);
    }
    
    @Override
    public String toString() {
        return super.toString() + ", CRM: " + crm + ", Especialidade: " + especialidade + 
               ", Ativo: " + (ativo ? "Sim" : "Não");
    }
}