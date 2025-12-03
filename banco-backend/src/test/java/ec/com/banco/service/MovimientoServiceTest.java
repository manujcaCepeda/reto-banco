package ec.com.banco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.dto.response.MovimientoResponse;
import ec.com.banco.entity.Cuenta;
import ec.com.banco.entity.Movimiento;
import ec.com.banco.enums.TipoMovimiento;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.repository.CuentaRepository;
import ec.com.banco.repository.MovimientoRepository;
import ec.com.banco.service.impl.MovimientoServiceImpl;

class MovimientoServiceTest {

    private MovimientoServiceImpl service;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    private UUID cuentaId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new MovimientoServiceImpl(movimientoRepository, cuentaRepository);
        cuentaId = UUID.randomUUID();
    }

    @Test
    void registrarMovimiento_cuentaNoExiste_lanzaExcepcion() {

        MovimientoRequest req = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.DEPOSITO,
                new BigDecimal("100")
        );

        when(cuentaRepository.findByIdForUpdate(cuentaId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.registrarMovimiento(req));
    }

    @Test
    void registrarMovimiento_retiroSaldoInsuficiente_lanzaExcepcion() {

        Cuenta cuenta = new Cuenta();
        cuenta.setId(cuentaId);
        cuenta.setSaldoInicial(new BigDecimal("50"));

        MovimientoRequest req = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.RETIRO,
                new BigDecimal("100")
        );

        when(cuentaRepository.findByIdForUpdate(cuentaId))
                .thenReturn(Optional.of(cuenta));

        // No existen movimientos anteriores
        when(movimientoRepository.findTopByCuenta_IdOrderByFechaDesc(cuentaId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.registrarMovimiento(req));
    }

    @Test
    void registrarMovimiento_deposito_ok() {

        Cuenta cuenta = new Cuenta();
        cuenta.setId(cuentaId);
        cuenta.setSaldoInicial(new BigDecimal("200"));

        MovimientoRequest req = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.DEPOSITO,
                new BigDecimal("50")
        );

        when(cuentaRepository.findByIdForUpdate(cuentaId))
                .thenReturn(Optional.of(cuenta));

        when(movimientoRepository.findTopByCuenta_IdOrderByFechaDesc(cuentaId))
                .thenReturn(Optional.empty());

        Movimiento saved = new Movimiento();
        saved.setId(UUID.randomUUID());
        saved.setCuenta(cuenta);
        saved.setValor(new BigDecimal("50"));
        saved.setSaldo(new BigDecimal("250"));
        saved.setTipoMovimiento(TipoMovimiento.DEPOSITO);

        when(movimientoRepository.save(any(Movimiento.class)))
                .thenReturn(saved);

        MovimientoResponse response = service.registrarMovimiento(req);

        assertNotNull(response);
        assertEquals(new BigDecimal("250"), response.saldo());
        assertEquals(TipoMovimiento.DEPOSITO, response.tipoMovimiento());
    }

    @Test
    void registrarMovimiento_retiro_ok_conMovimientosPrevios() {

        Cuenta cuenta = new Cuenta();
        cuenta.setId(cuentaId);

        Movimiento lastMov = new Movimiento();
        lastMov.setSaldo(new BigDecimal("300"));

        MovimientoRequest req = new MovimientoRequest(
                cuentaId,
                TipoMovimiento.RETIRO,
                new BigDecimal("100")
        );

        when(cuentaRepository.findByIdForUpdate(cuentaId))
                .thenReturn(Optional.of(cuenta));

        when(movimientoRepository.findTopByCuenta_IdOrderByFechaDesc(cuentaId))
                .thenReturn(Optional.of(lastMov));

        Movimiento saved = new Movimiento();
        saved.setId(UUID.randomUUID());
        saved.setSaldo(new BigDecimal("200"));
        saved.setCuenta(cuenta);
        saved.setTipoMovimiento(TipoMovimiento.RETIRO);

        when(movimientoRepository.save(any(Movimiento.class)))
                .thenReturn(saved);

        MovimientoResponse res = service.registrarMovimiento(req);

        assertEquals(new BigDecimal("200"), res.saldo());
        assertEquals(TipoMovimiento.RETIRO, res.tipoMovimiento());
    }

    @Test
    void listarMovimientos_ok() {

        Movimiento m = new Movimiento();
        m.setId(UUID.randomUUID());
        m.setSaldo(new BigDecimal("100"));
        Cuenta cuenta = new Cuenta();
        cuenta.setId(cuentaId);
        m.setCuenta(cuenta);

        when(movimientoRepository.findAll())
                .thenReturn(List.of(m));

        List<MovimientoResponse> lista = service.listarMovimientos();

        assertEquals(1, lista.size());
        assertEquals(new BigDecimal("100"), lista.get(0).saldo());
    }

    @Test
    void listarMovimientosPorCuenta_ok() {

        Movimiento m = new Movimiento();
        m.setCuenta(new Cuenta());
        m.setSaldo(new BigDecimal("200"));

        when(movimientoRepository.findByCuentaIdOrderByFechaAsc(cuentaId))
                .thenReturn(List.of(m));

        List<MovimientoResponse> res = service.listarMovimientosPorCuenta(cuentaId);

        assertEquals(1, res.size());
    }

}
