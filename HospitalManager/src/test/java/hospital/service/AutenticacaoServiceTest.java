package hospital.service;

import hospital.dao.PacienteDAO;
import hospital.dao.MedicoDAO;
import hospital.dao.RecepcionistaDAO;
import hospital.model.Usuario;
import hospital.model.Paciente;
import hospital.model.Medico;
import hospital.model.Recepcionista;
import hospital.model.enums.Especialidade;
import hospital.model.enums.TipoUsuario;
import hospital.model.exceptions.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade para AutenticacaoService
 * Testa as regras de autenticação de usuários
 */
public class AutenticacaoServiceTest {
    
    private AutenticacaoService service;
    private Paciente paciente;
    private Medico medico;
    private Recepcionista recepcionista;
    
    @BeforeEach
    public void setUp() {
        service = new AutenticacaoService();
        
        // Criar dados de teste
        paciente = new Paciente(
            "Pedro Lima",
            "39053344705",
            "pedro@email.com",
            "(11) 97777-6666",
            "Rua D, 321",
            "39053344705",
            "senha123"
        );
        
        medico = new Medico(
            "Dr. Roberto Alves",
            "84434895035",
            "roberto@hospital.com",
            "(11) 3555-4444",
            "Av. Médica, 200",
            "CRM99999",
            "senha456",
            "CRM99999",
            Especialidade.ORTOPEDIA
        );
        medico.setAtivo(true);
        
        recepcionista = new Recepcionista(
            "Carla Mendes",
            "62479367001",
            "carla@hospital.com",
            "(11) 3777-8888",
            "Rua E, 555",
            "MAT12345",
            "senha789",
            "MAT12345"
        );
        
        // Salvar no banco
        new PacienteDAO().salvar(paciente);
        new MedicoDAO().salvar(medico);
        new RecepcionistaDAO().salvar(recepcionista);
    }
    
    @Test
    public void testAutenticarPacienteValido() throws UsuarioNaoEncontradoException {
        Usuario usuario = service.autenticar("39053344705", "senha123");
        
        assertNotNull(usuario);
        assertTrue(usuario instanceof Paciente);
        assertEquals("Pedro Lima", usuario.getNome());
        assertEquals(TipoUsuario.PACIENTE, usuario.getTipoUsuario());
    }
    
    @Test
    public void testAutenticarMedicoValido() throws UsuarioNaoEncontradoException {
        Usuario usuario = service.autenticar("CRM99999", "senha456");
        
        assertNotNull(usuario);
        assertTrue(usuario instanceof Medico);
        assertEquals("Dr. Roberto Alves", usuario.getNome());
        assertEquals(TipoUsuario.MEDICO, usuario.getTipoUsuario());
    }
    
    @Test
    public void testAutenticarRecepcionistaValido() throws UsuarioNaoEncontradoException {
        Usuario usuario = service.autenticar("MAT12345", "senha789");
        
        assertNotNull(usuario);
        assertTrue(usuario instanceof Recepcionista);
        assertEquals("Carla Mendes", usuario.getNome());
        assertEquals(TipoUsuario.RECEPCIONISTA, usuario.getTipoUsuario());
    }
    
    @Test
    public void testAutenticarComSenhaErrada() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.autenticar("39053344705", "senhaerrada");
        });
    }
    
    @Test
    public void testAutenticarComLoginInexistente() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.autenticar("99999999999", "senha123");
        });
    }
    
    @Test
    public void testAutenticarComLoginVazio() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.autenticar("", "senha123");
        });
    }
    
    @Test
    public void testAutenticarComSenhaVazia() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.autenticar("39053344705", "");
        });
    }
    
    @Test
    public void testAutenticarComLoginNulo() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.autenticar(null, "senha123");
        });
    }
    
    @Test
    public void testLoginExiste() {
        assertTrue(service.loginExiste("39053344705"));
        assertTrue(service.loginExiste("CRM99999"));
        assertTrue(service.loginExiste("MAT12345"));
        assertFalse(service.loginExiste("CPF_INEXISTENTE"));
    }
    
    @Test
    public void testMedicoInativo() {
        medico.setAtivo(false);
        new MedicoDAO().salvar(medico);
        
        // Recriar o service para recarregar os dados do arquivo
        service = new AutenticacaoService();
        
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            service.autenticar("CRM99999", "senha456");
        });
    }
}
