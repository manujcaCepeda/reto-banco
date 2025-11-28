package ec.com.banco.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import ec.com.banco.enums.TipoMovimiento;

public record MovimientoResponse(
        UUID id,
        OffsetDateTime fecha,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        UUID cuentaId
) {}
