package ec.com.banco.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import ec.com.banco.enums.TipoCuenta;

public record CuentaResponse(
        UUID id,
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        Boolean estado,
        UUID clienteId
) {}
