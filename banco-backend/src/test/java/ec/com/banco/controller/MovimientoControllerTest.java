package ec.com.banco.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.dto.response.MovimientoResponse;
import ec.com.banco.enums.TipoMovimiento;
import ec.com.banco.exception.GlobalExceptionHandler;
import ec.com.banco.service.MovimientoService;

@WebMvcTest(controllers = MovimientoController.class)
@Import({MovimientoControllerTest.MockConfig.class, GlobalExceptionHandler.class})
class MovimientoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MovimientoService movimientoService;

    private UUID cuentaId;
    private MovimientoRequest requestCredito;
    private MovimientoResponse responseCredito;

    @BeforeEach
    void setup() throws Exception {

        TestContextManager contextManager = new TestContextManager(getClass());
        contextManager.prepareTestInstance(this);

        var ctx = contextManager.getTestContext().getApplicationContext();

        this.mockMvc = ctx.getBean(MockMvc.class);
        this.objectMapper = ctx.getBean(ObjectMapper.class);
        this.movimientoService = ctx.getBean(MovimientoService.class);

        cuentaId = UUID.randomUUID();

        requestCredito = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.DEPOSITO,
                new BigDecimal("150.00")
        );

        responseCredito = new MovimientoResponse(
                UUID.randomUUID(),
                OffsetDateTime.now(),
                TipoMovimiento.DEPOSITO,
                new BigDecimal("150.00"),
                new BigDecimal("150.00"),
                cuentaId
        );
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public MovimientoService movimientoService() {
            return Mockito.mock(MovimientoService.class);
        }
    }

    @Test
    void registrarMovimiento_ok() throws Exception {

        when(movimientoService.registrarMovimiento(any(MovimientoRequest.class)))
                .thenReturn(responseCredito);

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCredito)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.cuentaId").value(cuentaId.toString()));
    }

    @Test
    void registrarMovimiento_saldoInsuficiente() throws Exception {

        MovimientoRequest retiroRequest = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.RETIRO,
                new BigDecimal("500.00")
        );

        when(movimientoService.registrarMovimiento(any(MovimientoRequest.class)))
                .thenThrow(new IllegalArgumentException("Saldo no disponible"));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retiroRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }


    @Test
    void listarMovimientos_ok() throws Exception {

        when(movimientoService.listarMovimientos())
                .thenReturn(List.of(responseCredito));

        mockMvc.perform(get("/api/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].valor").value(150.00));
    }

    @Test
    void listarMovimientosPorCuenta_ok() throws Exception {

        when(movimientoService.listarMovimientosPorCuenta(cuentaId))
                .thenReturn(List.of(responseCredito));

        mockMvc.perform(get("/api/movimientos/cuenta/" + cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cuentaId").value(cuentaId.toString()));
    }

    @Test
    void reporteMovimientos_ok() throws Exception {

        when(movimientoService.reporteMovimientos(
                Mockito.eq(cuentaId),
                Mockito.eq("2024-01-01"),
                Mockito.eq("2024-12-31")))
                .thenReturn(List.of(responseCredito));

        mockMvc.perform(get("/api/movimientos/reporte/" + cuentaId)
                        .param("desde", "2024-01-01")
                        .param("hasta", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoMovimiento").value("DEPOSITO"));
    }
}
