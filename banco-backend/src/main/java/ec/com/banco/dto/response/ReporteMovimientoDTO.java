package ec.com.banco.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReporteMovimientoDTO(
		OffsetDateTime fecha,
        String tipo,
        BigDecimal valor,
        BigDecimal saldo
) {}
