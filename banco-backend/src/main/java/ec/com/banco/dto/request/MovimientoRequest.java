package ec.com.banco.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import ec.com.banco.enums.TipoMovimiento;
import jakarta.validation.constraints.*;

public record MovimientoRequest(

        @NotNull(message = "La cuenta es obligatoria")
        UUID cuentaId,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @NotNull(message = "El valor del movimiento es obligatorio")
        @DecimalMin(value = "0.01", message = "El valor debe ser mayor a cero")
        BigDecimal valor
) {}
