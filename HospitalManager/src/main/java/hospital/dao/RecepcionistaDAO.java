package hospital.dao;

import hospital.model.Recepcionista;
import hospital.model.enums.TipoUsuario;
import hospital.util.FileManager;
import hospital.util.Logger;
import hospital.util.ValidadorEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementa persistência em arquivo conforme requisito do trabalho.
 * Formato do arquivo: MATRICULA;NOME;EMAIL;TELEFONE;SENHA
 */

public class RecepcionistaDAO {
    
    private static final String ARQUIVO = "src/main/resources/data/recepcionistas.dat";
    private List<Recepcionista> recepcionistas;
    
   
    public RecepcionistaDAO() {
        Logger.info("RecepcionistaDAO", "Inicializando DAO");
        FileManager.criarDiretorioSeNaoExistir("src/main/resources/data");
        this.recepcionistas = carregarDoArquivo();
        Logger.info("RecepcionistaDAO", "Carregados " + recepcionistas.size() + " recepcionistas");
    }
    
    // ==================== CONVERSÃO ARQUIVO - OBJETO ====================
    
    
    private Recepcionista converterLinhaParaRecepcionista(String linha) {
        try {
            String[] partes = linha.split(";");
            if (partes.length != 5) {
                Logger.aviso("RecepcionistaDAO", "Linha inválida (campos incorretos): " + linha);
                return null;
            }
            
            Recepcionista recepcionista = new Recepcionista();
            recepcionista.setMatricula(partes[0]);
            recepcionista.setNome(partes[1]);
            recepcionista.setEmail(partes[2]);
            recepcionista.setTelefone(partes[3]);
            recepcionista.setSenha(partes[4]);
            recepcionista.setLogin(partes[0]); // Matrícula como login
            
            return recepcionista;
            
        } catch (Exception e) {
            Logger.erro("RecepcionistaDAO", "Erro ao converter linha: " + linha, e);
            return null;
        }
    }
    
    /**
     * Converte objeto Recepcionista para linha do arquivo
     */

    private String converterRecepcionistaParaLinha(Recepcionista recepcionista) {
        return String.join(";",
            recepcionista.getMatricula(),
            recepcionista.getNome(),
            recepcionista.getEmail(),
            recepcionista.getTelefone(),
            recepcionista.getSenha()
        );
    }
    
    /**
     * Carrega todos os recepcionistas do arquivo
     */

    private List<Recepcionista> carregarDoArquivo() {
        List<Recepcionista> lista = new ArrayList<>();
        
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        
        for (String linha : linhas) {
            Recepcionista r = converterLinhaParaRecepcionista(linha);
            if (r != null) {
                lista.add(r);
            }
        }
        
        return lista;
    }
    
    /**
     * Salva todos os recepcionistas no arquivo
     */

    private void salvarNoArquivo() {
        Logger.info("RecepcionistaDAO", "Salvando " + recepcionistas.size() + " recepcionistas no arquivo");
        
        FileManager.limparArquivo(ARQUIVO);
        
        for (Recepcionista r : recepcionistas) {
            String linha = converterRecepcionistaParaLinha(r);
            FileManager.escreverArquivo(ARQUIVO, linha, true);
        }
    }
    
    // ==================== MÉTODOS PÚBLICOS PRINCIPAIS ====================
    
    /**
     * Salva ou atualiza um recepcionista
     */
    public void salvar(Recepcionista recepcionista) {
        Logger.info("RecepcionistaDAO", "Salvando recepcionista: " + recepcionista.getMatricula());
        
       
        if (recepcionista.getMatricula() == null || recepcionista.getMatricula().trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula não pode ser vazia");
        }
        
        if (recepcionista.getNome() == null || recepcionista.getNome().trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }
        
        if (!ValidadorEmail.validar(recepcionista.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        // Remove se já existir
        remover(recepcionista.getMatricula());
        
        // Adiciona novo
        recepcionistas.add(recepcionista);
        
        // Persiste no arquivo
        salvarNoArquivo();
        
        Logger.info("RecepcionistaDAO", "Recepcionista salvo com sucesso: " + recepcionista.getMatricula());
    }
    
    /**
     * Retorna todos os recepcionistas
     */
    public List<Recepcionista> listarTodos() {
        return new ArrayList<>(recepcionistas);
    }
    
    /**
     * Busca recepcionista pela matrícula
     */
    public Recepcionista buscarPorMatricula(String matricula) {
        for (Recepcionista r : recepcionistas) {
            if (r.getMatricula().equals(matricula)) {
                return r;
            }
        }
        return null;
    }
    
    /**
     * Remove recepcionista pela matrícula
     */
    public void remover(String matricula) {
        boolean removido = recepcionistas.removeIf(r -> r.getMatricula().equals(matricula));
        
        if (removido) {
            salvarNoArquivo();
            Logger.info("RecepcionistaDAO", "Recepcionista removido: " + matricula);
        } else {
            Logger.aviso("RecepcionistaDAO", "Recepcionista não encontrado para remoção: " + matricula);
        }
    }
    
    // ==================== MÉTODOS DE NEGÓCIO ====================
    
    /**
     * Autentica recepcionista por matrícula e senha
     */

    public Recepcionista autenticar(String matricula, String senha) {
        Recepcionista recepcionista = buscarPorMatricula(matricula);
        
        if (recepcionista != null && recepcionista.getSenha().equals(senha)) {
            Logger.info("RecepcionistaDAO", "Autenticação bem-sucedida: " + matricula);
            return recepcionista;
        }
        
        Logger.aviso("RecepcionistaDAO", "Autenticação falhou: " + matricula);
        return null;
    }
    
  
    public boolean existeRecepcionistas() {
        return !recepcionistas.isEmpty();
    }
    
    /**
     * Atualiza dados de um recepcionista (mantém a mesma matrícula)
     */

    public void atualizar(Recepcionista recepcionistaAtualizado) {
        String matricula = recepcionistaAtualizado.getMatricula();
        Recepcionista existente = buscarPorMatricula(matricula);
        
        if (existente != null) {

            if (recepcionistaAtualizado.getSenha() == null || recepcionistaAtualizado.getSenha().isEmpty()) {
                recepcionistaAtualizado.setSenha(existente.getSenha());
            }
            
            remover(matricula);
            recepcionistas.add(recepcionistaAtualizado);
            salvarNoArquivo();
            
            Logger.info("RecepcionistaDAO", "Recepcionista atualizado: " + matricula);
        } 
        
        else {
            throw new IllegalArgumentException("Recepcionista não encontrado: " + matricula);
        }
    }
}