package ec.com.banco.service;

import java.util.List;
import java.util.UUID;

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.dto.response.MovimientoResponse;

public interface MovimientoService {

    MovimientoResponse registrarMovimiento(MovimientoRequest request);

    List<MovimientoResponse> listarMovimientos();
    
    List<MovimientoResponse> listarMovimientosPorCuenta(UUID cuentaId);

    List<MovimientoResponse> reporteMovimientos(UUID cuentaId, String fechaDesde, String fechaHasta);
}
