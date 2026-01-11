package hospital.service;

import hospital.dao.DocumentoDAO;
import hospital.model.Atestado;
import hospital.model.Receita;
import hospital.model.Exame;
import hospital.util.Logger;
import hospital.model.exceptions.DadoInvalidoException;

import java.util.List;

public class DocumentoService {

    private DocumentoDAO documentoDAO;

    public DocumentoService() {
        this.documentoDAO = new DocumentoDAO();
        Logger.info("DocumentoService", "Serviço de documentos inicializado");
    }

    public void criarAtestado(Atestado atestado) {
        if (atestado == null) throw new DadoInvalidoException("Atestado é obrigatório");
        documentoDAO.salvarAtestado(atestado);
        Logger.info("DocumentoService", "Atestado criado: " + atestado.getId());
    }

    public void criarReceita(Receita receita) {
        if (receita == null) throw new DadoInvalidoException("Receita é obrigatória");
        documentoDAO.salvarReceita(receita);
        Logger.info("DocumentoService", "Receita criada: " + receita.getId());
    }

    public void solicitarExame(Exame exame) {
        if (exame == null) throw new DadoInvalidoException("Exame é obrigatório");
        documentoDAO.salvarExame(exame);
        Logger.info("DocumentoService", "Exame solicitado: " + exame.getId());
    }

    public List<Receita> listarReceitasPorPaciente(String cpf) {
        return documentoDAO.listarReceitasPorPaciente(cpf);
    }
    
    public List<Atestado> listarAtestadosPorPaciente(String cpf) {
        return documentoDAO.listarAtestadosPorPaciente(cpf);
    }
    
    public List<Exame> listarExamesPorPaciente(String cpf) {
        return documentoDAO.listarExamesPorPaciente(cpf);
    }
}
