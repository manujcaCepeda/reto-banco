package ec.com.banco.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.com.banco.dto.request.CuentaRequest;
import ec.com.banco.dto.response.CuentaResponse;
import ec.com.banco.entity.Cliente;
import ec.com.banco.entity.Cuenta;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.repository.ClienteRepository;
import ec.com.banco.repository.CuentaRepository;
import ec.com.banco.service.CuentaService;

@Service
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public CuentaResponse crearCuenta(CuentaRequest request) {

        // Validación de cliente existente
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        // Validar número de cuenta único
        if (cuentaRepository.existsByNumeroCuenta(request.numeroCuenta())) {
            throw new IllegalArgumentException("El número de cuenta ya está registrado");
        }

        Cuenta cuenta = new Cuenta();

        cuenta.setCliente(cliente);
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setEstado(true);

        Cuenta saved = cuentaRepository.save(cuenta);

        return mapToResponse(saved);
    }

    @Override
    public CuentaResponse obtenerCuenta(UUID id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        return mapToResponse(cuenta);
    }

    @Override
    public List<CuentaResponse> listarCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CuentaResponse> listarCuentasPorCliente(UUID clienteId) {
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CuentaResponse actualizarCuenta(UUID id, CuentaRequest request) {

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        // Validar que no duplique número de cuenta con otro cliente
        if (!cuenta.getNumeroCuenta().equals(request.numeroCuenta()) &&
            cuentaRepository.existsByNumeroCuenta(request.numeroCuenta())) {
            throw new IllegalArgumentException("El número de cuenta ya está registrado");
        }

        // Validación del cliente
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setCliente(cliente);

        return mapToResponse(cuentaRepository.save(cuenta));
    }

    @Override
    public void eliminarCuenta(UUID id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        cuentaRepository.delete(cuenta);
    }

    private CuentaResponse mapToResponse(Cuenta c) {
        return new CuentaResponse(
                c.getId(),
                c.getNumeroCuenta(),
                c.getTipoCuenta(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente().getId()
        );
    }
}
