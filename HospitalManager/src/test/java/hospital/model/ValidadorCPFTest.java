package hospital.model;

import hospital.util.ValidadorCPF;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe ValidadorCPF
 * Verifica se a validação de CPF está funcionando corretamente
 */
public class ValidadorCPFTest {
    
    @Test
    public void testCPFValido() {
        // CPFs válidos reais
        assertTrue(ValidadorCPF.validar("11144477735"), "CPF válido sem formatação deveria passar");
    }
    
    @Test
    public void testCPFNulo() {
        assertFalse(ValidadorCPF.validar(null), "CPF nulo deveria ser inválido");
    }
    
    @Test
    public void testCPFVazio() {
        assertFalse(ValidadorCPF.validar(""), "CPF vazio deveria ser inválido");
        assertFalse(ValidadorCPF.validar("   "), "CPF com apenas espaços deveria ser inválido");
    }
    
    @Test
    public void testCPFComTamanhoIncorreto() {
        assertFalse(ValidadorCPF.validar("123"), "CPF com poucos dígitos deveria ser inválido");
        assertFalse(ValidadorCPF.validar("123456789101112"), "CPF com muitos dígitos deveria ser inválido");
    }
    
    @Test
    public void testCPFComCaracteresInvalidos() {
        assertFalse(ValidadorCPF.validar("abc.def.ghi-jk"), "CPF com letras deveria ser inválido");
        assertFalse(ValidadorCPF.validar("111@444#777-35"), "CPF com caracteres especiais inválidos deveria ser inválido");
    }
}
