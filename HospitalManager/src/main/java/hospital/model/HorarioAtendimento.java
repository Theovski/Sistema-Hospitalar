package hospital.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioAtendimento {
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
<<<<<<< HEAD
    private int duracaoConsulta;
=======
    private int duracaoConsulta; 
>>>>>>> 586504875a4bde42a23314da35225f64cbeac28e
    
    public HorarioAtendimento(DayOfWeek diaSemana, LocalTime horaInicio, 
                              LocalTime horaFim, int duracaoConsulta) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.duracaoConsulta = duracaoConsulta;
    }
    
    public HorarioAtendimento() {}
    
    public DayOfWeek getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DayOfWeek diaSemana) { this.diaSemana = diaSemana; }
    
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
    
    public int getDuracaoConsulta() { return duracaoConsulta; }
    public void setDuracaoConsulta(int duracaoConsulta) { this.duracaoConsulta = duracaoConsulta; }
    
    public boolean estaDisponivel() {
        LocalTime agora = LocalTime.now();
        DayOfWeek hoje = DayOfWeek.from(java.time.LocalDate.now());
        return diaSemana.equals(hoje) &&
               !agora.isBefore(horaInicio) &&
               !agora.isAfter(horaFim);
    }
    
    @Override
    public String toString() {
        return diaSemana + ": " + horaInicio + " às " + horaFim + 
               " (" + duracaoConsulta + "min/consulta)";
    }
}
