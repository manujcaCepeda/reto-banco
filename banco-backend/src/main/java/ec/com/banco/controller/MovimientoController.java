package ec.com.banco.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.dto.response.MovimientoResponse;
import ec.com.banco.service.MovimientoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Movimientos", description = "Registro de movimientos débito/crédito")
@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        return ResponseEntity.ok(movimientoService.registrarMovimiento(request));
    }
    
    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listarMovimientos());
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<MovimientoResponse>> listar(@PathVariable UUID cuentaId) {
        return ResponseEntity.ok(movimientoService.listarMovimientosPorCuenta(cuentaId));
    }

    @GetMapping("/reporte/{cuentaId}")
    public ResponseEntity<List<MovimientoResponse>> reporte(
            @PathVariable UUID cuentaId,
            @RequestParam String desde,
            @RequestParam String hasta
    ) {
        return ResponseEntity.ok(movimientoService.reporteMovimientos(cuentaId, desde, hasta));
    }
}
