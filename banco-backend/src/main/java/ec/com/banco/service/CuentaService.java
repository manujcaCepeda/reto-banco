package ec.com.banco.service;

import java.util.List;
import java.util.UUID;

import ec.com.banco.dto.request.CuentaRequest;
import ec.com.banco.dto.response.CuentaResponse;

public interface CuentaService {

    CuentaResponse crearCuenta(CuentaRequest request);

    CuentaResponse obtenerCuenta(UUID id);

    List<CuentaResponse> listarCuentas();

    List<CuentaResponse> listarCuentasPorCliente(UUID clienteId);

    CuentaResponse actualizarCuenta(UUID id, CuentaRequest request);

    void eliminarCuenta(UUID id);
}

