package hospital.util;

public class ValidadorCPF {

    public static boolean validar(String cpf) {
        
        if (cpf == null || cpf.length() != 11) return false;
       
        return true; 
    }
}