
package hospital.dao;

import hospital.model.Atestado;
import hospital.model.Receita;
import hospital.model.Exame;
import hospital.util.FileManager;
import hospital.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO único para todos os tipos de documentos médicos
 */
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
            String[] partes = linha.split(";");
            if (partes.length >= 2 && partes[1].equals(pacienteCpf)) {

                Atestado a = new Atestado();
                a.setId(partes[0]);
                a.setPacienteCpf(partes[1]);
                a.setMedicoCrm(partes[2]);
                atestados.add(a);
            }
        }
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
    }
    
    public List<Receita> listarReceitasPorPaciente(String pacienteCpf) {
        return new ArrayList<>();
    }
    
    // ============ EXAMES ============

    public void salvarExame(Exame exame) {

        String linha = String.join(";",
            exame.getId(),
            exame.getPacienteCpf(),
            exame.getTipoExame(),
            exame.getDataRealizacao().toString(),
            exame.getDataResultado() != null ? exame.getDataResultado().toString() : "",
            exame.getResultado()
        );
        FileManager.escreverArquivo(ARQUIVO_EXAMES, linha, true);
    }
    
}