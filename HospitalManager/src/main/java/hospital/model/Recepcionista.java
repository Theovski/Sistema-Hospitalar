package hospital.model;

public class Recepcionista extends Usuario {
    private String matricula;
    
    public Recepcionista(String nome, String cpf, String email, String telefone, String endereco,
                         String login, String senha, String matricula) {
        super(nome, cpf, email, telefone, endereco, login, senha, TipoUsuario.RECEPCIONISTA);
        this.matricula = matricula;
    }
    
    public Recepcionista() {}
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    @Override
    public String toString() {
        return super.toString() + ", Matrícula: " + matricula;
    }
}