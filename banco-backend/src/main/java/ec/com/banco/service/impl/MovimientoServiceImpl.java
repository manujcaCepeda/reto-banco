package ec.com.banco.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.com.banco.dto.request.MovimientoRequest;
import ec.com.banco.dto.response.MovimientoResponse;
import ec.com.banco.entity.Cuenta;
import ec.com.banco.entity.Movimiento;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.repository.CuentaRepository;
import ec.com.banco.repository.MovimientoRepository;
import ec.com.banco.service.MovimientoService;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository,
                                 CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) {

        UUID cuentaId = request.cuentaId();

        // 1) Bloqueo pesimista: evita condiciones de carrera
        Cuenta cuenta = cuentaRepository.findByIdForUpdate(cuentaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        // 2) Obtener saldo actual: si existe último movimiento, tomar su saldo; si no, usar saldoInicial
        BigDecimal currentSaldo = movimientoRepository.findTopByCuenta_IdOrderByFechaDesc(cuentaId)
                .map(Movimiento::getSaldo)
                .orElse(cuenta.getSaldoInicial() != null ? cuenta.getSaldoInicial() : BigDecimal.ZERO);

        BigDecimal valor = request.valor();

        // 3) Aplicar lógica segun tipo de movimiento
        if (request.tipoMovimiento() == ec.com.banco.enums.TipoMovimiento.RETIRO) {
            if (currentSaldo.compareTo(valor) < 0) {
                throw new IllegalArgumentException("Saldo no disponible");
            }
            currentSaldo = currentSaldo.subtract(valor);
        } else {
            // CREDITO
            currentSaldo = currentSaldo.add(valor);
        }

        // 4) Construir y guardar movimiento con saldo actualizado
        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(OffsetDateTime.now());
        movimiento.setTipoMovimiento(request.tipoMovimiento());
        movimiento.setValor(valor);
        movimiento.setSaldo(currentSaldo);

        Movimiento saved = movimientoRepository.save(movimiento);

        // 5) (Opcional) Si tu diseño guarda un "saldo" en entidad Cuenta, actualízalo aquí.
        // cuenta.setSaldo(currentSaldo); // sólo si existe y has decidido persistir saldo en Cuenta
        // cuentaRepository.save(cuenta);

        return mapToResponse(saved);
    }


    @Override
    public List<MovimientoResponse> listarMovimientos() {
        return movimientoRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }
    
    @Override
    public List<MovimientoResponse> listarMovimientosPorCuenta(UUID cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaAsc(cuentaId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<MovimientoResponse> reporteMovimientos(UUID cuentaId, String desde, String hasta) {

        LocalDate desdeLd = LocalDate.parse(desde);
        LocalDate hastaLd = LocalDate.parse(hasta);

        OffsetDateTime desdeDate = desdeLd.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime hastaDate = hastaLd.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);

        return movimientoRepository
                .findByCuentaIdAndFechaBetweenOrderByFechaAsc(cuentaId, desdeDate, hastaDate)
                .stream().map(this::mapToResponse).toList();
    }

    private MovimientoResponse mapToResponse(Movimiento m) {
        return new MovimientoResponse(
                m.getId(),
                m.getFecha(),
                m.getTipoMovimiento(),
                m.getValor(),
                m.getSaldo(),
                m.getCuenta().getId()
        );
    }
}
