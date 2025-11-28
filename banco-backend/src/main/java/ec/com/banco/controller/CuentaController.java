package ec.com.banco.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.com.banco.dto.request.CuentaRequest;
import ec.com.banco.dto.response.CuentaResponse;
import ec.com.banco.service.CuentaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cuentas", description = "Gestión de cuentas bancarias")
@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CuentaRequest request) {
        return ResponseEntity.ok(cuentaService.crearCuenta(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(cuentaService.obtenerCuenta(id));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listar() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaResponse>> listarPorCliente(@PathVariable UUID clienteId) {
        return ResponseEntity.ok(cuentaService.listarCuentasPorCliente(clienteId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CuentaRequest request) {

        return ResponseEntity.ok(cuentaService.actualizarCuenta(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        cuentaService.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
