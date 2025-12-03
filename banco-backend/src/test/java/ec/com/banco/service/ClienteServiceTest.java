package ec.com.banco.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import ec.com.banco.dto.request.ClienteEditarRequest;
import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;
import ec.com.banco.entity.Cliente;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.repository.ClienteRepository;
import ec.com.banco.service.impl.ClienteServiceImpl;

class ClienteServiceTest {

    private ClienteServiceImpl service;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new ClienteServiceImpl(clienteRepository, passwordEncoder);
    }

    @Test
    void crearCliente_identificacionDuplicada_lanzaExcepcion() {
        ClienteRequest req = new ClienteRequest("Juan", "M", 30, "1234", "Dir", "0999", "CLI1", "1234");

        when(clienteRepository.existsByIdentificacion("1234")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> service.crearCliente(req));

        assertEquals("La identificación ya está registrada", ex.getMessage());
    }

    @Test
    void crearCliente_clienteIdDuplicado_lanzaExcepcion() {
        ClienteRequest req = new ClienteRequest("Juan", "M", 30, "1234", "Dir", "0999", "CLI1", "1234");

        when(clienteRepository.existsByIdentificacion("1234")).thenReturn(false);
        when(clienteRepository.existsByClienteId("CLI1")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> service.crearCliente(req));

        assertEquals("El clienteId ya existe", ex.getMessage());
    }

    @Test
    void crearCliente_ok_guardaCorrectamente() {
        ClienteRequest req = new ClienteRequest("Juan", "M", 30, "1234", "Dir", "0999", "CLI1", "1234");

        when(clienteRepository.existsByIdentificacion("1234")).thenReturn(false);
        when(clienteRepository.existsByClienteId("CLI1")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("ENC_1234");

        Cliente saved = new Cliente();
        saved.setId(UUID.randomUUID());
        saved.setNombre("Juan");
        saved.setClienteId("CLI1");
        saved.setPassword("ENC_1234");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(saved);

        ClienteResponse res = service.crearCliente(req);

        assertEquals("Juan", res.nombre());
        assertEquals("CLI1", res.clienteId());
        assertNotNull(res.id());
    }

    @Test
    void obtenerCliente_noExiste_lanzaExcepcion() {
        UUID id = UUID.randomUUID();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.obtenerCliente(id));
    }

    @Test
    void obtenerCliente_ok() {
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Juan");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteResponse res = service.obtenerCliente(id);

        assertEquals("Juan", res.nombre());
        assertEquals(id, res.id());
    }

    @Test
    void actualizarCliente_identificacionDuplicada_lanzaExcepcion() {
        UUID id = UUID.randomUUID();

        Cliente existente = new Cliente();
        existente.setId(id);
        existente.setIdentificacion("1111");

        ClienteEditarRequest req = new ClienteEditarRequest(
                "Pedro", "M", 30, "2222", "Dir", "0999"
        );

        when(clienteRepository.findById(id)).thenReturn(Optional.of(existente));
        when(clienteRepository.existsByIdentificacion("2222")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarCliente(id, req));
    }

    @Test
    void actualizarCliente_ok() {
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setIdentificacion("1111");

        ClienteEditarRequest req = new ClienteEditarRequest(
                "Pedro", "M", 35, "2222", "NewDir", "0888"
        );

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponse res = service.actualizarCliente(id, req);

        assertEquals("Pedro", res.nombre());
        assertEquals("2222", res.identificacion());
    }

    @Test
    void eliminarCliente_noExiste_lanzaExcepcion() {
        UUID id = UUID.randomUUID();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.eliminarCliente(id));
    }

    @Test
    void eliminarCliente_ok() {
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        assertDoesNotThrow(() -> service.eliminarCliente(id));
        verify(clienteRepository).delete(cliente);
    }
}
