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

import ec.com.banco.dto.request.ClienteEditarRequest;
import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;
import ec.com.banco.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Operaciones sobre clientes del banco")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
            summary = "Crear un cliente",
            description = "Registra un cliente nuevo en el sistema"
    )
    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.crearCliente(request));
    }

    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.obtenerCliente(id));
    }

    @Operation(summary = "Obtener todos los cliente")
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @Operation(summary = "Actualiza un cliente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteEditarRequest request) {

        return ResponseEntity.ok(clienteService.actualizarCliente(id, request));
    }

    @Operation(summary = "Eliminar cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}

