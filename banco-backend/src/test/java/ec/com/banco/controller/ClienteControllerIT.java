package ec.com.banco.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.com.banco.dto.request.ClienteEditarRequest;
import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ClienteControllerIT {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {

        TestContextManager contextManager = new TestContextManager(getClass());
        contextManager.prepareTestInstance(this);

        var ctx = contextManager.getTestContext().getApplicationContext();

        this.mockMvc = ctx.getBean(MockMvc.class);
        this.objectMapper = ctx.getBean(ObjectMapper.class);

    }
    
    @Test
    void listarClientes_deberiaTraerDatosInicialesDeDataSQL() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Jose Lema"))
                .andExpect(jsonPath("$[1].nombre").value("Marianela Montalvo"));
    }
    

    @Test
    void crearCliente_ok() throws Exception {

        ClienteRequest request = new ClienteRequest(
                "Carlos Perez", "M", 30, "0102030405",
                "Av. Siempre Viva", "0999999999",
                "CLI123", "pass1234"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLI123"))
                .andExpect(jsonPath("$.nombre").value("Carlos Perez"));
    }

    @Test
    void obtenerCliente_ok() throws Exception {

        // Crear registro real en H2
        ClienteRequest request = new ClienteRequest(
                "Juan Lopez", "M", 40, "1112223334",
                "Direccion X", "08888888",
                "CLI999", "pass1234"
        );

        var response = objectMapper.readValue(
                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ClienteResponse.class
        );

        mockMvc.perform(get("/api/clientes/" + response.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identificacion").value("1112223334"))
                .andExpect(jsonPath("$.clienteId").value("CLI999"));
    }

    @Test
    void actualizarCliente_ok() throws Exception {

        // Crear cliente real
        ClienteRequest createReq = new ClienteRequest(
                "Pedro", "M", 28, "1231231231",
                "Calle X", "0909090909",
                "CLI555", "pass123"
        );

        var saved = objectMapper.readValue(
                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createReq)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ClienteResponse.class
        );

        // Ahora actualizamos
        ClienteEditarRequest updateReq = new ClienteEditarRequest(
                "Pedro Actualizado", "M", 29, "1231231231",
                "Nueva Calle", "0999999999"
        );

        mockMvc.perform(put("/api/clientes/" + saved.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro Actualizado"))
                .andExpect(jsonPath("$.telefono").value("0999999999"));
    }

    @Test
    void eliminarCliente_ok() throws Exception {

        ClienteRequest req = new ClienteRequest(
                "Maria", "F", 25, "5556667771",
                "Dir", "0900000000",
                "CLI777", "pass"
        );

        var saved = objectMapper.readValue(
                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ClienteResponse.class
        );

        mockMvc.perform(delete("/api/clientes/" + saved.id()))
                .andExpect(status().isNoContent());
    }

}
