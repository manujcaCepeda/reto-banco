package ec.com.banco.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ReporteCuentaDTO(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        BigDecimal totalDebitos,
        BigDecimal totalCreditos,
        List<ReporteMovimientoDTO> movimientos
) {}
