
package hospital.dao;

import hospital.model.Atestado;
import hospital.model.Receita;
import hospital.model.Exame;
import hospital.util.FileManager;
import hospital.util.Logger;

import java.util.ArrayList;
import java.util.List;

// Classe para gerenciar atestados, receitas e exames
public class DocumentoDAO {
    
    private static final String ARQUIVO_ATESTADOS = "src/main/resources/data/atestados.dat";
    private static final String ARQUIVO_RECEITAS = "src/main/resources/data/receitas.dat";
    private static final String ARQUIVO_EXAMES = "src/main/resources/data/exames.dat";
    
    // ============ ATESTADOS ============
    
    public void salvarAtestado(Atestado atestado) {
        String linha = String.join(";",
            atestado.getId(),
            atestado.getPacienteCpf(),
            atestado.getMedicoCrm(),
            atestado.getDataEmissao().toString(),
            atestado.getDiagnostico(),
            atestado.getCid(),
            String.valueOf(atestado.getDiasRepouso())
        );
        FileManager.escreverArquivo(ARQUIVO_ATESTADOS, linha, true);
        Logger.info("DocumentoDAO", "Atestado salvo: " + atestado.getId());
    }
    
    public List<Atestado> listarAtestadosPorPaciente(String pacienteCpf) {
        List<Atestado> atestados = new ArrayList<>();
        List<String> linhas = FileManager.lerArquivo(ARQUIVO_ATESTADOS);
        
        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                if (partes.length >= 7 && partes[1].equals(pacienteCpf)) {
                    Atestado a = new Atestado();
                    a.setId(partes[0]);
                    a.setPacienteCpf(partes[1]);
                    a.setMedicoCrm(partes[2]);
                    a.setDataEmissao(java.time.LocalDate.parse(partes[3]));
                    a.setDiagnostico(partes[4]);
                    a.setCid(partes[5]);
                    a.setDiasRepouso(Integer.parseInt(partes[6]));
                    atestados.add(a);
                }
            } catch (Exception e) {
                Logger.erro("DocumentoDAO", "Erro ao converter atestado: " + linha, e);
            }
        }
        Logger.info("DocumentoDAO", "Encontrados " + atestados.size() + " atestados para CPF: " + pacienteCpf);
        return atestados;
    }
    
    // ============ RECEITAS ============

    public void salvarReceita(Receita receita) {
        String linha = String.join(";",
            receita.getId(),
            receita.getPacienteCpf(),
            receita.getMedicoCrm(),
            receita.getData().toString(),
            receita.getMedicamentosTexto(),
            receita.getInstrucoes()
        );
        FileManager.escreverArquivo(ARQUIVO_RECEITAS, linha, true);
        Logger.info("DocumentoDAO", "Receita salva: " + receita.getId());
    }
    
    public List<Receita> listarReceitasPorPaciente(String pacienteCpf) {
        List<Receita> receitas = new ArrayList<>();
        List<String> linhas = FileManager.lerArquivo(ARQUIVO_RECEITAS);
        
        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                if (partes.length >= 6 && partes[1].equals(pacienteCpf)) {
                    Receita r = new Receita();
                    r.setId(partes[0]);
                    r.setPacienteCpf(partes[1]);
                    r.setMedicoCrm(partes[2]);
                    r.setDataPrescricao(java.time.LocalDate.parse(partes[3]));
                    // partes[4] contém medicamentos (já em string)
                    r.setInstrucoes(partes[5]);
                    receitas.add(r);
                }
            } catch (Exception e) {
                Logger.erro("DocumentoDAO", "Erro ao converter receita: " + linha, e);
            }
        }
        Logger.info("DocumentoDAO", "Encontradas " + receitas.size() + " receitas para CPF: " + pacienteCpf);
        return receitas;
    }
    
    // ============ EXAMES ============

    public void salvarExame(Exame exame) {
        String linha = String.join(";",
            exame.getId(),
            exame.getPacienteCpf(),
            exame.getTipoExame(),
            exame.getDataRealizacao() != null ? exame.getDataRealizacao().toString() : "",
            exame.getDataResultado() != null ? exame.getDataResultado().toString() : "",
            exame.getResultado() != null ? exame.getResultado() : ""
        );
        FileManager.escreverArquivo(ARQUIVO_EXAMES, linha, true);
        Logger.info("DocumentoDAO", "Exame salvo: " + exame.getId());
    }
    
    public List<Exame> listarExamesPorPaciente(String pacienteCpf) {
        List<Exame> exames = new ArrayList<>();
        List<String> linhas = FileManager.lerArquivo(ARQUIVO_EXAMES);
        
        for (String linha : linhas) {
            try {
                String[] partes = linha.split(";");
                if (partes.length >= 6 && partes[1].equals(pacienteCpf)) {
                    Exame e = new Exame();
                    e.setId(partes[0]);
                    e.setPacienteCpf(partes[1]);
                    e.setTipoExame(partes[2]);
                    if (!partes[3].isEmpty()) {
                        e.setDataRealizacao(java.time.LocalDateTime.parse(partes[3]));
                    }
                    e.setResultado(partes[5]);
                    exames.add(e);
                }
            } catch (Exception ex) {
                Logger.erro("DocumentoDAO", "Erro ao converter exame: " + linha, ex);
            }
        }
        Logger.info("DocumentoDAO", "Encontrados " + exames.size() + " exames para CPF: " + pacienteCpf);
        return exames;
    }
}