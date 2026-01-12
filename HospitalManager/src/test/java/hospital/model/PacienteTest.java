package hospital.model;

import hospital.model.enums.StatusVisita;
import hospital.model.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe Paciente
 * Verifica se os métodos do paciente estão funcionando corretamente
 */
public class PacienteTest {
    
    private Paciente paciente;
    
    @BeforeEach
    public void setUp() {
        // Executa antes de cada teste - cria um paciente novo
        paciente = new Paciente(
            "João Silva",
            "12345678910",
            "joao@email.com",
            "(11) 98765-4321",
            "Rua A, 123",
            "12345678910",
            "senha123"
        );
    }
    
    @Test
    public void testCriacaoPaciente() {
        assertNotNull(paciente, "Paciente não deveria ser nulo");
        assertEquals("João Silva", paciente.getNome());
        assertEquals("12345678910", paciente.getCpf());
        assertEquals("joao@email.com", paciente.getEmail());
        assertEquals(TipoUsuario.PACIENTE, paciente.getTipoUsuario());
    }
    
    @Test
    public void testStatusVisitaInicial() {
        // Por padrão, visitação deve estar liberada
        assertEquals(StatusVisita.LIBERADA, paciente.getStatusVisita());
        assertTrue(paciente.isAptoVisita());
    }
    
    @Test
    public void testProibirVisita() {
        paciente.setStatusVisita(StatusVisita.PROIBIDA);
        assertEquals(StatusVisita.PROIBIDA, paciente.getStatusVisita());
        assertFalse(paciente.isAptoVisita());
    }
    
    @Test
    public void testLiberarVisita() {
        paciente.setStatusVisita(StatusVisita.PROIBIDA);
        paciente.setStatusVisita(StatusVisita.LIBERADA);
        assertTrue(paciente.isAptoVisita());
    }
    
    @Test
    public void testSetAptoVisita() {
        paciente.setAptoVisita(false);
        assertFalse(paciente.isAptoVisita());
        assertEquals(StatusVisita.PROIBIDA, paciente.getStatusVisita());
        
        paciente.setAptoVisita(true);
        assertTrue(paciente.isAptoVisita());
        assertEquals(StatusVisita.LIBERADA, paciente.getStatusVisita());
    }
    
    @Test
    public void testConvenio() {
        assertNull(paciente.getConvenio(), "Convênio inicial deveria ser nulo");
        
        paciente.setConvenio("Unimed");
        assertEquals("Unimed", paciente.getConvenio());
    }
    
    @Test
    public void testListaConsultasInicial() {
        assertNotNull(paciente.getConsultas());
        assertEquals(0, paciente.getConsultas().size(), "Lista de consultas deveria estar vazia inicialmente");
    }
    
    @Test
    public void testListaExamesInicial() {
        assertNotNull(paciente.getExames());
        assertEquals(0, paciente.getExames().size(), "Lista de exames deveria estar vazia inicialmente");
    }
}
