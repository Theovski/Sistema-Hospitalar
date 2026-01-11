package hospital.dao;

import hospital.model.HorarioAtendimento;
import hospital.util.FileManager;
import hospital.util.Logger;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HorarioAtendimentoDAO {
    private static final String ARQUIVO = "horarios_atendimento.dat";
    private FileManager fileManager;
    
    public HorarioAtendimentoDAO() {
        this.fileManager = new FileManager();
    }
    
    public void salvar(String medicoCrm, HorarioAtendimento horario) {
        try {
            List<String> linhas = new ArrayList<>();
            
            // Ler horários existentes
            File arquivo = fileManager.getFile(ARQUIVO);
            if (arquivo.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                    String linha;
                    while ((linha = br.readLine()) != null) {
                        String[] partes = linha.split("\\|");
                        // Manter outros médicos ou outros dias
                        if (partes.length >= 5) {
                            String crm = partes[0];
                            String dia = partes[1];
                            if (!crm.equals(medicoCrm) || !dia.equals(horario.getDiaSemana().toString())) {
                                linhas.add(linha);
                            }
                        }
                    }
                }
            }
            
            // Adicionar novo horário
            String novaLinha = medicoCrm + "|" +
                             horario.getDiaSemana().toString() + "|" +
                             horario.getHoraInicio().toString() + "|" +
                             horario.getHoraFim().toString() + "|" +
                             horario.getDuracaoConsulta();
            linhas.add(novaLinha);
            
            // Escrever tudo de volta
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
                for (String linha : linhas) {
                    bw.write(linha);
                    bw.newLine();
                }
            }
            
            Logger.info("HorarioAtendimentoDAO", "Horário salvo para CRM " + medicoCrm);
        } catch (IOException e) {
            Logger.erro("HorarioAtendimentoDAO", "Erro ao salvar horário", e);
        }
    }
    
    public List<HorarioAtendimento> listarPorMedico(String medicoCrm) {
        List<HorarioAtendimento> horarios = new ArrayList<>();
        
        try {
            File arquivo = fileManager.getFile(ARQUIVO);
            if (!arquivo.exists()) {
                return horarios;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] partes = linha.split("\\|");
                    if (partes.length >= 5 && partes[0].equals(medicoCrm)) {
                        HorarioAtendimento h = new HorarioAtendimento();
                        h.setDiaSemana(DayOfWeek.valueOf(partes[1]));
                        h.setHoraInicio(LocalTime.parse(partes[2]));
                        h.setHoraFim(LocalTime.parse(partes[3]));
                        h.setDuracaoConsulta(Integer.parseInt(partes[4]));
                        horarios.add(h);
                    }
                }
            }
        } catch (IOException e) {
            Logger.erro("HorarioAtendimentoDAO", "Erro ao listar horários", e);
        }
        
        return horarios;
    }
    
    public void remover(String medicoCrm, DayOfWeek diaSemana) {
        try {
            File arquivo = fileManager.getFile(ARQUIVO);
            if (!arquivo.exists()) {
                return;
            }
            
            List<String> linhas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] partes = linha.split("\\|");
                    if (partes.length >= 5) {
                        String crm = partes[0];
                        String dia = partes[1];
                        // Manter linhas que não correspondem
                        if (!crm.equals(medicoCrm) || !dia.equals(diaSemana.toString())) {
                            linhas.add(linha);
                        }
                    }
                }
            }
            
            // Reescrever arquivo
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
                for (String linha : linhas) {
                    bw.write(linha);
                    bw.newLine();
                }
            }
            
            Logger.info("HorarioAtendimentoDAO", "Horário removido para CRM " + medicoCrm);
        } catch (IOException e) {
            Logger.erro("HorarioAtendimentoDAO", "Erro ao remover horário", e);
        }
    }
    
    public boolean verificarDisponibilidade(String medicoCrm, DayOfWeek diaSemana, LocalTime horario) {
        List<HorarioAtendimento> horarios = listarPorMedico(medicoCrm);
        
        for (HorarioAtendimento h : horarios) {
            if (h.getDiaSemana().equals(diaSemana)) {
                return !horario.isBefore(h.getHoraInicio()) && 
                       !horario.isAfter(h.getHoraFim());
            }
        }
        
        return false;
    }
}
