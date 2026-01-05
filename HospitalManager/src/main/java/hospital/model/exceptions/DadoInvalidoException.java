package hospital.model.exceptions;

public class DadoInvalidoException extends RuntimeException {
    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }
}