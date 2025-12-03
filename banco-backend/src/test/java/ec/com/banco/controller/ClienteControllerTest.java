package ec.com.banco.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.com.banco.dto.request.ClienteEditarRequest;
import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;
import ec.com.banco.exception.GlobalExceptionHandler;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.service.ClienteService;

@WebMvcTest(controllers = ClienteController.class)
@Import({ClienteControllerTest.MockConfig.class, GlobalExceptionHandler.class})
class ClienteControllerTest {

    private MockMvc mockMvc;
    
    private ClienteService clienteService;
    
    private ObjectMapper objectMapper;

    private ClienteRequest request;
    
    private ClienteResponse response;
    
    /**
     * Inicializa el contexto de Spring sin usar @Autowired
     */
    @BeforeEach
    void setup() throws Exception {
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);

        this.mockMvc = testContextManager.getTestContext().getApplicationContext().getBean(MockMvc.class);
        this.clienteService = testContextManager.getTestContext().getApplicationContext().getBean(ClienteService.class);
        this.objectMapper = testContextManager.getTestContext().getApplicationContext().getBean(ObjectMapper.class);
        
        request = new ClienteRequest(
                "Carlos Perez", "M", 30, "0102030405",
                "Av. Siempre Viva 123", "0999999999",
                "CLI123", "pass1234"
        );

        response = new ClienteResponse(
                UUID.randomUUID(), request.nombre(), request.genero(),
                request.edad(), request.identificacion(), request.direccion(),
                request.telefono(), request.clienteId(), true
        );
    }

    /**
     * Configura mocks explícitos (mejor que usar @MockBean)
     */
    @TestConfiguration
    static class MockConfig {

        @Bean
        public ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }
        
    }

    @Test
    void crearCliente_ok() throws Exception {

        when(clienteService.crearCliente(any(ClienteRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLI123"))
                .andExpect(jsonPath("$.nombre").value("Carlos Perez"));
    }
    
    @Test
    void crearCliente_identificacionDuplicada() throws Exception {
    	when(clienteService.crearCliente(any(ClienteRequest.class)))
                .thenThrow(new IllegalArgumentException("La identificación ya está registrada"));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La identificación ya está registrada"));
    }
    
    @Test
    void obtenerCliente_ok() throws Exception {
    	when(clienteService.obtenerCliente(any(UUID.class))).thenReturn(response);

        mockMvc.perform(get("/api/clientes/" + response.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos Perez"));
    }
    
    @Test
    void obtenerCliente_notFound() throws Exception {
        when(clienteService.obtenerCliente(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado"));

        mockMvc.perform(get("/api/clientes/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"));
    }
    
    @Test
    void listarClientes_ok() throws Exception {
        when(clienteService.listarClientes()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value("CLI123"));
    }
    
    @Test
    void actualizarCliente_ok() throws Exception {
        UUID id = UUID.randomUUID();

		ClienteEditarRequest request = new ClienteEditarRequest("Juan Actualizado", "M", 35, "0102030405",
				"Nueva direccion", "0999999999");

		ClienteResponse response = new ClienteResponse(id, request.nombre(), request.genero(), request.edad(),
				request.identificacion(), request.direccion(), request.telefono(), "CLI123", true);

        Mockito.when(clienteService.actualizarCliente(Mockito.eq(id), Mockito.any(ClienteEditarRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/clientes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"))
                .andExpect(jsonPath("$.identificacion").value("0102030405"))
                .andExpect(jsonPath("$.telefono").value("0999999999"));
    }
    
    @Test
    void eliminarCliente_ok() throws Exception {

        mockMvc.perform(delete("/api/clientes/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}
