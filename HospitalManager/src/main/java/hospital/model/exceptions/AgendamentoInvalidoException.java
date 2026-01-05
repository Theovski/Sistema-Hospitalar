package hospital.model.exceptions;

public class AgendamentoInvalidoException extends RuntimeException {
    public AgendamentoInvalidoException(String mensagem) {
        super(mensagem);
    }
}