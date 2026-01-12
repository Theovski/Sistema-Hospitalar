package hospital.service;

import hospital.dao.ConsultaDAO;
import hospital.dao.MedicoDAO;
import hospital.dao.PacienteDAO;
import hospital.model.Consulta;
import hospital.model.Medico;
import hospital.model.Paciente;
import hospital.model.enums.Especialidade;
import hospital.model.enums.StatusConsulta;
import hospital.model.exceptions.AgendamentoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para AgendamentoService
 * Testa as regras de negócio para agendamento de consultas
 */
public class AgendamentoServiceTest {
    
    private AgendamentoService service;
    private Paciente paciente;
    private Medico medico;
    
    @BeforeEach
    public void setUp() {
        service = new AgendamentoService();
        
        // Criar dados de teste
        paciente = new Paciente(
            "Ana Costa",
            "11144477735",
            "ana@email.com",
            "(11) 98888-7777",
            "Rua C, 789",
            "11144477735",
            "senha123"
        );
        
        medico = new Medico(
            "Dra. Paula Souza",
            "52599927036",
            "paula@hospital.com",
            "(11) 3344-5566",
            "Av. Saúde, 100",
            "CRM54321",
            "senha321",
            "CRM54321",
            Especialidade.CLINICO_GERAL
        );
        medico.setAtivo(true);
        
        // Salvar no banco
        new PacienteDAO().salvar(paciente);
        new MedicoDAO().salvar(medico);
    }
    
    @Test
    public void testAgendarConsultaValida() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(5);
        Consulta consulta = new Consulta("C100", paciente.getCpf(), medico.getCrm(), futuro);
        
        // Não deve lançar exceção
        assertDoesNotThrow(() -> service.agendar(consulta));
    }
    
    @Test
    public void testAgendarConsultaNula() {
        assertThrows(AgendamentoInvalidoException.class, () -> {
            service.agendar(null);
        });
    }
    
    @Test
    public void testAgendarConsultaComDataPassada() {
        LocalDateTime passado = LocalDateTime.now().minusDays(1);
        Consulta consulta = new Consulta("C101", paciente.getCpf(), medico.getCrm(), passado);
        
        assertThrows(AgendamentoInvalidoException.class, () -> {
            service.agendar(consulta);
        });
    }
    
    @Test
    public void testListarConsultasPorPaciente() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(3);
        Consulta consulta = new Consulta("C102", paciente.getCpf(), medico.getCrm(), futuro);
        service.agendar(consulta);
        
        List<Consulta> consultas = service.listarPorPaciente(paciente.getCpf());
        assertNotNull(consultas);
        assertTrue(consultas.size() > 0);
    }
    
    @Test
    public void testListarConsultasPorMedico() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(3);
        Consulta consulta = new Consulta("C103", paciente.getCpf(), medico.getCrm(), futuro);
        service.agendar(consulta);
        
        List<Consulta> consultas = service.listarPorMedico(medico.getCrm());
        assertNotNull(consultas);
        assertTrue(consultas.size() > 0);
    }
    
    @Test
    public void testCancelarConsulta() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(2);
        Consulta consulta = new Consulta("C104", paciente.getCpf(), medico.getCrm(), futuro);
        service.agendar(consulta);
        
        // Cancelar
        assertDoesNotThrow(() -> service.cancelarConsulta(consulta.getId()));
        
        // Verificar status
        ConsultaDAO dao = new ConsultaDAO();
        Consulta cancelada = dao.buscarPorId(consulta.getId());
        assertEquals(StatusConsulta.CANCELADA, cancelada.getStatus());
    }
}
