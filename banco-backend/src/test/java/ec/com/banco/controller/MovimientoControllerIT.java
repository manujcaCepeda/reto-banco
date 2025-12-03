package ec.com.banco.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.enums.TipoMovimiento;
import ec.com.banco.repository.CuentaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovimientoControllerIT {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CuentaRepository cuentaRepository;

    @BeforeEach
    void setup() throws Exception {

        TestContextManager ctxMgr = new TestContextManager(getClass());
        ctxMgr.prepareTestInstance(this);

        var ctx = ctxMgr.getTestContext().getApplicationContext();

        this.mockMvc = ctx.getBean(MockMvc.class);
        this.objectMapper = ctx.getBean(ObjectMapper.class);
        this.cuentaRepository = ctx.getBean(CuentaRepository.class);
    }

    @Test
    void listarMovimientos_deberiaTraerDatosIniciales() throws Exception {
        mockMvc.perform(get("/api/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoMovimiento").exists());
    }


    @Test
    void registrarMovimiento_deposito_ok() throws Exception {

        UUID cuentaId = cuentaRepository.findAll().get(0).getId();

        MovimientoRequest req = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.DEPOSITO,
                BigDecimal.valueOf(200)
        );

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(200));
    }

    @Test
    void registrarMovimiento_retiro_sinFondos_lanzaError() throws Exception {

        UUID cuentaId = cuentaRepository.findAll().get(0).getId();

        MovimientoRequest req = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.RETIRO,
                BigDecimal.valueOf(999999)
        );

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void listarMovimientosPorCuenta_ok() throws Exception {

        UUID cuentaId = cuentaRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/movimientos/cuenta/" + cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cuentaId").value(cuentaId.toString()));
    }

}
