package ec.com.banco.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import ec.com.banco.enums.TipoCuenta;
import jakarta.validation.constraints.*;

public record CuentaRequest(

        @NotNull(message = "El clienteId es obligatorio")
        UUID clienteId,

        @NotBlank(message = "El número de cuenta es obligatorio")
        String numeroCuenta,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        TipoCuenta tipoCuenta,

        @NotNull(message = "El saldo inicial es obligatorio")
        @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
        BigDecimal saldoInicial
) {}
