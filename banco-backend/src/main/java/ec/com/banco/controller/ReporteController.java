package ec.com.banco.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.com.banco.dto.response.ReporteResponse;
import ec.com.banco.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Endpoints para generación de reportes de estado de cuenta")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(
            summary = "Generar reporte de estado de cuenta (JSON)",
            description = "Devuelve información de saldos, movimientos y totales para todas las cuentas del cliente dentro del rango de fechas.",
            parameters = {
                    @Parameter(name = "clienteId", description = "UUID del cliente", required = true),
                    @Parameter(name = "desde", description = "Fecha inicial (YYYY-MM-DD)", required = true),
                    @Parameter(name = "hasta", description = "Fecha final (YYYY-MM-DD)", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte generado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReporteResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Error en parámetros de entrada"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno")
            }
    )
    @GetMapping
    public ResponseEntity<ReporteResponse> generarReporte(
            @RequestParam UUID clienteId,
            @RequestParam String desde,
            @RequestParam String hasta
    ) {
        return ResponseEntity.ok(
                reporteService.generarReporte(clienteId, desde, hasta)
        );
    }
}
