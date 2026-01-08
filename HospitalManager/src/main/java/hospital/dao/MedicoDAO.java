
package hospital.dao;

import hospital.model.Medico;
import hospital.model.enums.Especialidade;
import hospital.model.enums.TipoUsuario;
import hospital.util.FileManager;
import hospital.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MedicoDAO implements ArquivoDAO<Medico> {
    
    private static final String ARQUIVO = "src/main/resources/data/medicos.dat";
    private List<Medico> medicos;
    
    public MedicoDAO() {
        Logger.info("MedicoDAO", "Inicializando DAO");
        FileManager.criarDiretorioSeNaoExistir("src/main/resources/data");
        this.medicos = carregarDoArquivo();
    }
    
    private Medico converterLinhaParaMedico(String linha) {

        try {

            String[] partes = linha.split(";");
            if (partes.length != 7) return null;
            
            Medico medico = new Medico();
            medico.setCrm(partes[0]);
            medico.setNome(partes[1]);
            medico.setEmail(partes[2]);
            medico.setTelefone(partes[3]);
            medico.setEspecialidade(Especialidade.valueOf(partes[4]));
            medico.setAtivo(Boolean.parseBoolean(partes[5]));
            medico.setTipo(TipoUsuario.MEDICO);
            medico.setLogin(partes[0]); // CRM como login
            medico.setSenha(partes[6]);
            
            return medico;
        } catch (Exception e) {
            Logger.erro("MedicoDAO", "Erro ao converter linha: " + linha, e);
            return null;
        }
    }
    
    private String converterMedicoParaLinha(Medico medico) {
        return String.join(";",
            medico.getCrm(),
            medico.getNome(),
            medico.getEmail(),
            medico.getTelefone(),
            medico.getEspecialidade().name(),
            String.valueOf(medico.isAtivo()),
            medico.getSenha()
        );
    }
    
    private List<Medico> carregarDoArquivo() {
        List<Medico> lista = new ArrayList<>();
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        
        for (String linha : linhas) {
            Medico m = converterLinhaParaMedico(linha);
            if (m != null) lista.add(m);
        }
        
        return lista;
    }
    
    private void salvarNoArquivo() {
        FileManager.limparArquivo(ARQUIVO);
        for (Medico m : medicos) {
            String linha = converterMedicoParaLinha(m);
            FileManager.escreverArquivo(ARQUIVO, linha, true);
        }
    }
    
    @Override
    public void salvar(Medico medico) {
        remover(medico.getCrm());
        medicos.add(medico);
        salvarNoArquivo();
        Logger.info("MedicoDAO", "Médico salvo: " + medico.getCrm());
    }
    
    @Override
    public List<Medico> listarTodos() {
        return new ArrayList<>(medicos);
    }
    
    @Override
    public Medico buscarPorId(String crm) {
        for (Medico m : medicos) {
            if (m.getCrm().equals(crm)) {
                return m;
            }
        }
        return null;
    }
    
    @Override
    public void remover(String crm) {
        medicos.removeIf(m -> m.getCrm().equals(crm));
        salvarNoArquivo();
    }
    
    // Métodos específicos para Médico
    
    public List<Medico> listarPorEspecialidade(Especialidade especialidade) {
        List<Medico> resultado = new ArrayList<>();
        for (Medico m : medicos) {
            if (m.getEspecialidade() == especialidade && m.isAtivo()) {
                resultado.add(m);
            }
        }
        return resultado;
    }
    
    public List<Medico> listarAtivos() {
        List<Medico> ativos = new ArrayList<>();
        for (Medico m : medicos) {
            if (m.isAtivo()) {
                ativos.add(m);
            }
        }
        return ativos;
    }
}