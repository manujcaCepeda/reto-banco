package ec.com.banco.dto.response;

import java.util.List;

public record ReporteResponse(
        String cliente,
        String identificacion,
        String desde,
        String hasta,
        List<ReporteCuentaDTO> cuentas,
        String pdfBase64
) {}
