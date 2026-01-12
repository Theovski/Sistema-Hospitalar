
package hospital.dao;

import hospital.model.Paciente;
import hospital.model.Endereco;
import hospital.model.enums.TipoUsuario;
import hospital.util.FileManager;
import hospital.util.ValidadorCPF;
import hospital.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements ArquivoDAO<Paciente> {
    
    private static final String ARQUIVO = "src/main/resources/data/pacientes.dat";
    private List<Paciente> pacientes; // Cache em memória
    
    public PacienteDAO() {
        Logger.info("PacienteDAO", "Inicializando DAO");
        FileManager.criarDiretorioSeNaoExistir("src/main/resources/data");
        this.pacientes = carregarDoArquivo();
        Logger.info("PacienteDAO", "Carregados " + pacientes.size() + " pacientes");
    }
    
    // ==================== MÉTODOS PRIVADOS (CONVERSÃO) ====================
    
    // Converte linha do arquivo para objeto
    private Paciente converterLinhaParaPaciente(String linha) {
        try {
            String[] partes = linha.split(";");
            if (partes.length != 7) {
                Logger.aviso("PacienteDAO", "Linha inválida (campos incorretos): " + linha);
                return null;
            }
            
            // Valida CPF antes de criar o objeto
            if (!ValidadorCPF.validar(partes[0])) {
                Logger.aviso("PacienteDAO", "CPF inválido na linha: " + partes[0]);
                return null;
            }
            
            Paciente paciente = new Paciente();
            paciente.setCpf(partes[0]);
            paciente.setNome(partes[1]);
            paciente.setEmail(partes[2]);
            paciente.setTelefone(partes[3]);
            paciente.setConvenio(partes[4]);
            paciente.setAptoVisita(Boolean.parseBoolean(partes[5]));
            paciente.setLogin(partes[0]); // CPF como login
            paciente.setSenha(partes[6]); // Senha (em texto por enquanto)
            paciente.setTipoUsuario(hospital.model.enums.TipoUsuario.PACIENTE);
            
            return paciente;
            
        } catch (Exception e) {
            Logger.erro("PacienteDAO", "Erro ao converter linha: " + linha, e);
            return null;
        }
    }
    
    /**
     * Converte objeto Paciente para linha do arquivo
     */
    private String converterPacienteParaLinha(Paciente paciente) {
        return String.join(";",
            paciente.getCpf(),
            paciente.getNome(),
            paciente.getEmail(),
            paciente.getTelefone(),
            paciente.getConvenio(),
            String.valueOf(paciente.isAptoVisita()),
            paciente.getSenha()
        );
    }
    
    /**
     * Carrega todos os pacientes do arquivo para memória
     */
    private List<Paciente> carregarDoArquivo() {
        List<Paciente> lista = new ArrayList<>();
        
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        Logger.info("PacienteDAO", "Lendo " + linhas.size() + " linhas do arquivo");
        
        for (String linha : linhas) {
            Paciente p = converterLinhaParaPaciente(linha);
            if (p != null) {
                lista.add(p);
            }
        }
        
        return lista;
    }
    
    /**
     * Salva lista atual de pacientes no arquivo
     */
    private void salvarNoArquivo() {
        Logger.info("PacienteDAO", "Salvando " + pacientes.size() + " pacientes no arquivo");
        
        FileManager.limparArquivo(ARQUIVO);
        
        for (Paciente p : pacientes) {
            String linha = converterPacienteParaLinha(p);
            FileManager.escreverArquivo(ARQUIVO, linha, true);
        }
        
        Logger.info("PacienteDAO", "Arquivo salvo com sucesso");
    }
    
    // ==================== MÉTODOS PÚBLICOS (INTERFACE) ====================
    
    @Override
    public void salvar(Paciente paciente) {
        Logger.info("PacienteDAO", "Salvando paciente CPF: " + paciente.getCpf());
        
        // Validações
        if (!ValidadorCPF.validar(paciente.getCpf())) {
            Logger.erro("PacienteDAO", "CPF inválido para paciente: " + paciente.getCpf());
            throw new IllegalArgumentException("CPF inválido: " + paciente.getCpf());
        }
        
        // Remove paciente existente  e adiciona novo
        remover(paciente.getCpf());
        pacientes.add(paciente);
        
        salvarNoArquivo();
        Logger.info("PacienteDAO", "Paciente salvo com sucesso: " + paciente.getCpf());
    }
    
    @Override
    public List<Paciente> listarTodos() {
        Logger.info("PacienteDAO", "Listando todos os pacientes");
        return new ArrayList<>(pacientes); // Retorna cópia para não modificar lista interna
    }
    
    @Override
    public Paciente buscarPorId(String cpf) {
        Logger.info("PacienteDAO", "Buscando paciente CPF: " + cpf);
        
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                Logger.info("PacienteDAO", "Paciente encontrado: " + cpf);
                return p;
            }
        }
        
        Logger.aviso("PacienteDAO", "Paciente não encontrado: " + cpf);
        return null;
    }
    
    @Override
    public void remover(String cpf) {
        Logger.info("PacienteDAO", "Removendo paciente CPF: " + cpf);
        
        boolean removido = pacientes.removeIf(p -> p.getCpf().equals(cpf));
        
        if (removido) {
            salvarNoArquivo();
            Logger.info("PacienteDAO", "Paciente removido: " + cpf);
        } else {
            Logger.aviso("PacienteDAO", "Paciente não encontrado para remoção: " + cpf);
        }
    }
    
    // ==================== MÉTODOS ESPECÍFICOS PARA PACIENTE ====================
    
    /**
     * Busca pacientes por nome (parcial)
     */

    public List<Paciente> buscarPorNome(String nomeParcial) {
        Logger.info("PacienteDAO", "Buscando pacientes com nome contendo: " + nomeParcial);
        
        List<Paciente> resultado = new ArrayList<>();
        String nomeBusca = nomeParcial.toLowerCase();
        
        for (Paciente p : pacientes) {
            if (p.getNome().toLowerCase().contains(nomeBusca)) {
                resultado.add(p);
            }
        }
        
        Logger.info("PacienteDAO", "Encontrados " + resultado.size() + " pacientes");
        return resultado;
    }
    
    /**
     * Atualiza status de visita de um paciente
     */
    public void atualizarStatusVisita(String cpf, boolean aptoVisita) {
        Logger.info("PacienteDAO", "Atualizando status visita para CPF: " + cpf + " = " + aptoVisita);
        
        Paciente paciente = buscarPorId(cpf);
        if (paciente != null) {
            paciente.setAptoVisita(aptoVisita);
            salvar(paciente); // Reusa método salvar para persistir
        } else {
            Logger.aviso("PacienteDAO", "Paciente não encontrado para atualizar visita: " + cpf);
        }
    }
}