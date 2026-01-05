package hospital.model;

public class Recepcionista extends Usuario {
    
    public Recepcionista(String nome, String cpf, String email, String telefone, String endereco,
                         String login, String senha) {

        super(nome, cpf, email, telefone, endereco, login, senha, TipoUsuario.RECEPCIONISTA);
    }
    

    public Recepcionista() {}
    

    @Override
    public String toString() {
        return super.toString(); 
    }
}