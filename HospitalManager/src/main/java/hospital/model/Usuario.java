package hospital.model;

import hospital.model.enums.TipoUsuario;

public abstract class Usuario extends Pessoa {
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;
    
    public Usuario(String nome, String cpf, String email, String telefone, String endereco,
                   String login, String senha, TipoUsuario tipoUsuario) {
        super(nome, cpf, email, telefone, endereco);
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }
    
    public Usuario() {}
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    
    public boolean autenticar(String senhaDigitada) {
        return this.senha.equals(senhaDigitada);
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Login: " + login + ", Tipo: " + tipoUsuario;
    }
}