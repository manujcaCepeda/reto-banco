package ec.com.banco.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.enums.TipoMovimiento;
import ec.com.banco.exception.GlobalExceptionHandler;
import ec.com.banco.service.CuentaService;
import ec.com.banco.service.MovimientoService;

@WebMvcTest(controllers = MovimientoController.class)
@Import({MovimientoControllerTest.MockConfig.class, GlobalExceptionHandler.class})
class MovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public MovimientoService movimientoService() {
            return Mockito.mock(MovimientoService.class);
        }
        
        @Bean
        CuentaService cuentaService() {
            return Mockito.mock(CuentaService.class);
        }
    }

    @Test
    void registrarMovimiento_saldoInsuficiente() throws Exception {

        MovimientoRequest request = new MovimientoRequest(
                UUID.randomUUID(),
                TipoMovimiento.RETIRO,
                BigDecimal.valueOf(500)
        );

        Mockito.when(movimientoService.registrarMovimiento(Mockito.any()))
                .thenThrow(new IllegalArgumentException("Saldo no disponible"));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }
}
