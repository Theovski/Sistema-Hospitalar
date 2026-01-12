package hospital.model;

import hospital.model.enums.Especialidade;
import hospital.model.enums.StatusConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para a classe Consulta
 */
public class ConsultaTest {
    
    private Consulta consulta;
    private LocalDateTime dataHora;
    
    @BeforeEach
    public void setUp() {
        dataHora = LocalDateTime.of(2026, 1, 15, 14, 30);
        consulta = new Consulta("C001", "98765432100", "CRM12345", dataHora);
    }
    
    @Test
    public void testCriacaoConsulta() {
        assertNotNull(consulta);
        assertEquals("C001", consulta.getId());
        assertEquals("98765432100", consulta.getPacienteCpf());
        assertEquals("CRM12345", consulta.getMedicoCrm());
        assertEquals(dataHora, consulta.getDataHora());
    }
    
    @Test
    public void testStatusInicial() {
        // Status inicial deve ser AGENDADA
        assertEquals(StatusConsulta.AGENDADA, consulta.getStatus());
    }
    
    @Test
    public void testAlterarStatus() {
        consulta.setStatus(StatusConsulta.CONCLUIDA);
        assertEquals(StatusConsulta.CONCLUIDA, consulta.getStatus());
    }
    
    @Test
    public void testCancelarConsulta() {
        consulta.cancelar();
        assertEquals(StatusConsulta.CANCELADA, consulta.getStatus());
    }
    
    @Test
    public void testComparecimentoInicial() {
        assertFalse(consulta.isCompareceu());
    }
    
    @Test
    public void testRegistrarComparecimento() {
        consulta.setCompareceu(true);
        assertTrue(consulta.isCompareceu());
    }
    
    @Test
    public void testObservacoes() {
        assertNull(consulta.getObservacoes());
        
        consulta.setObservacoes("Paciente relatou dor no peito");
        assertEquals("Paciente relatou dor no peito", consulta.getObservacoes());
    }
}
