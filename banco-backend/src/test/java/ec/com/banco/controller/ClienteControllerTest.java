package ec.com.banco.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;
import ec.com.banco.exception.GlobalExceptionHandler;
import ec.com.banco.service.ClienteService;
import ec.com.banco.service.CuentaService;

@WebMvcTest(controllers = ClienteController.class)
@Import({ClienteControllerTest.MockConfig.class, GlobalExceptionHandler.class})
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Configuración explícita del mock, sin @MockBean
     */
    @TestConfiguration
    static class MockConfig {

        @Bean
        public ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }
        
        @Bean
        CuentaService cuentaService() {
            return Mockito.mock(CuentaService.class);
        }
    }

    @Test
    void crearCliente_ok() throws Exception {

        ClienteRequest request = new ClienteRequest(
                "Carlos Perez", "M", 30, "0102030405",
                "Av. Siempre Viva 123", "0999999999",
                "CLI123", "pass1234"
        );

        ClienteResponse response = new ClienteResponse(
                UUID.randomUUID(), request.nombre(), request.genero(),
                request.edad(), request.identificacion(), request.direccion(),
                request.telefono(), request.clienteId(), true
        );

        Mockito.when(clienteService.crearCliente(Mockito.any(ClienteRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLI123"))
                .andExpect(jsonPath("$.nombre").value("Carlos Perez"));
    }
}
