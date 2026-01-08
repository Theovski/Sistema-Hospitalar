package hospital.util;

public class Validador {
    
    public static void validarNome(String nome) {

        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }
    }
    
    public static void validarTelefone(String telefone) {

        if (telefone == null || telefone.replaceAll("[^0-9]", "").length() < 10) {
            
            throw new IllegalArgumentException("Telefone inválido");
        }
    }
    
    public static void validarCampoObrigatorio(String campo, String nomeCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " é obrigatório");
        }
    }
}
