package hospital.dao;

import java.util.List;

/**
 * Interface para operações CRUD em arquivos
 */
public interface ArquivoDAO<T> {
    
    void salvar(T entidade);
    
    List<T> listarTodos();
    
    T buscarPorId(String id);
    
    void remover(String id);
}
