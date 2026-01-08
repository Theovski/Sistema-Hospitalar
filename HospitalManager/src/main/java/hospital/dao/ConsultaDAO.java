package hospital.dao;

import hospital.model.Consulta;
import hospital.model.enums.StatusConsulta;
import hospital.util.FileManager;
import hospital.util.Logger;
import hospital.util.DataHoraUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO implements ArquivoDAO<Consulta> {
    
    private static final String ARQUIVO = "src/main/resources/data/consultas.dat";
    private List<Consulta> consultas;
    
    public ConsultaDAO() {
        Logger.info("ConsultaDAO", "Inicializando DAO");
        FileManager.criarDiretorioSeNaoExistir("src/main/resources/data");
        this.consultas = carregarDoArquivo();
    }
    
    private Consulta converterLinhaParaConsulta(String linha) {

        try {
            String[] partes = linha.split(";");
            if (partes.length != 7) return null;
            
            Consulta consulta = new Consulta();
            consulta.setId(partes[0]);
            consulta.setPacienteCpf(partes[1]);
            consulta.setMedicoCrm(partes[2]);
            consulta.setDataHora(DataHoraUtil.parse(partes[3]));
            consulta.setStatus(StatusConsulta.valueOf(partes[4]));
            consulta.setObservacoes(partes[5]);
            consulta.setCompareceu(Boolean.parseBoolean(partes[6]));
            
            return consulta;
        } catch (Exception e) {
            Logger.erro("ConsultaDAO", "Erro ao converter linha: " + linha, e);
            return null;
        }
    }
    
    private String converterConsultaParaLinha(Consulta consulta) {
        
        return String.join(";",
            consulta.getId(),
            consulta.getPacienteCpf(),
            consulta.getMedicoCrm(),
            DataHoraUtil.formatar(consulta.getDataHora()),
            consulta.getStatus().name(),
            consulta.getObservacoes(),
            String.valueOf(consulta.isCompareceu())
        );
    }
    
    private List<Consulta> carregarDoArquivo() {
        List<Consulta> lista = new ArrayList<>();
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        
        for (String linha : linhas) {
            Consulta c = converterLinhaParaConsulta(linha);
            if (c != null) lista.add(c);
        }
        
        return lista;
    }
    
    private void salvarNoArquivo() {
        FileManager.limparArquivo(ARQUIVO);
        for (Consulta c : consultas) {
            String linha = converterConsultaParaLinha(c);
            FileManager.escreverArquivo(ARQUIVO, linha, true);
        }
    }
    
    @Override
    public void salvar(Consulta consulta) {
        // Se ID vazio, gera novo ID
        if (consulta.getId() == null || consulta.getId().isEmpty()) {
            consulta.setId("CONS" + System.currentTimeMillis());
        }
        
        remover(consulta.getId());
        consultas.add(consulta);
        salvarNoArquivo();
        Logger.info("ConsultaDAO", "Consulta salva: " + consulta.getId());
    }
    
    @Override
    public List<Consulta> listarTodos() {
        return new ArrayList<>(consultas);
    }
    
    @Override
    public Consulta buscarPorId(String id) {
        for (Consulta c : consultas) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
    
    @Override
    public void remover(String id) {
        consultas.removeIf(c -> c.getId().equals(id));
        salvarNoArquivo();
    }
    
    // ==================== MÉTODOS ESPECÍFICOS PARA CONSULTA ====================
    
    public List<Consulta> listarPorPaciente(String pacienteCpf) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getPacienteCpf().equals(pacienteCpf)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    public List<Consulta> listarPorMedico(String medicoCrm) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getMedicoCrm().equals(medicoCrm)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    public List<Consulta> listarPorData(LocalDateTime data) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getDataHora().toLocalDate().equals(data.toLocalDate())) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    public List<Consulta> listarFuturasPorMedico(String medicoCrm) {
        List<Consulta> resultado = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();
        
        for (Consulta c : consultas) {
            if (c.getMedicoCrm().equals(medicoCrm) && 
                c.getDataHora().isAfter(agora) &&
                c.getStatus() == StatusConsulta.AGENDADA) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    public void marcarComoRealizada(String idConsulta, String observacoes) {
        Consulta consulta = buscarPorId(idConsulta);
        if (consulta != null) {
            consulta.setStatus(StatusConsulta.REALIZADA);
            consulta.setObservacoes(observacoes);
            consulta.setCompareceu(true);
            salvar(consulta);
        }
    }
    
    public void registrarFalta(String idConsulta) {
        Consulta consulta = buscarPorId(idConsulta);
        if (consulta != null) {
            consulta.setStatus(StatusConsulta.FALTA);
            consulta.setCompareceu(false);
            salvar(consulta);
        }
    }
}