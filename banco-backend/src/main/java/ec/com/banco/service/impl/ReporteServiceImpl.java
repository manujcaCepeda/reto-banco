package ec.com.banco.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ec.com.banco.dto.response.ReporteCuentaDTO;
import ec.com.banco.dto.response.ReporteMovimientoDTO;
import ec.com.banco.dto.response.ReporteResponse;
import ec.com.banco.entity.Cliente;
import ec.com.banco.entity.Cuenta;
import ec.com.banco.entity.Movimiento;
import ec.com.banco.enums.TipoMovimiento;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.repository.ClienteRepository;
import ec.com.banco.repository.CuentaRepository;
import ec.com.banco.repository.MovimientoRepository;
import ec.com.banco.service.ReporteService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReporteServiceImpl implements ReporteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final PdfService pdfService;

    public ReporteServiceImpl(
            ClienteRepository clienteRepository,
            CuentaRepository cuentaRepository,
            MovimientoRepository movimientoRepository,
            PdfService pdfService
    ) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.pdfService = pdfService;
    }

    @Override
    public ReporteResponse generarReporte(UUID clienteId, String desde, String hasta) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        LocalDate fDesde = LocalDate.parse(desde);
        LocalDate fHasta = LocalDate.parse(hasta);
        
        OffsetDateTime desdeDate = fDesde.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime hastaDate = fHasta.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        List<ReporteCuentaDTO> cuentasDTO = cuentas.stream().map(cuenta -> {

            // Movimientos por fecha
            List<Movimiento> movimientos =
                    movimientoRepository.findByCuentaIdAndFechaBetweenOrderByFechaAsc(
                            cuenta.getId(), desdeDate, hastaDate);

            // Totales
            BigDecimal totalDebitos = movimientos.stream()
                    .filter(m -> m.getTipoMovimiento() == TipoMovimiento.RETIRO)
                    .map(Movimiento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCreditos = movimientos.stream()
                    .filter(m -> m.getTipoMovimiento() == TipoMovimiento.DEPOSITO)
                    .map(Movimiento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Saldo disponible calculado
            BigDecimal saldoDisponible = cuenta.getSaldoInicial()
                    .add(totalCreditos)
                    .subtract(totalDebitos);

            // Movimientos DTO
            List<ReporteMovimientoDTO> movimientosDTO =
                    movimientos.stream().map(m -> new ReporteMovimientoDTO(
                            m.getFecha(),
                            m.getTipoMovimiento().name(),
                            m.getValor(),
                            m.getSaldo()
                    )).toList();

            return new ReporteCuentaDTO(
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta().name(),
                    cuenta.getSaldoInicial(),
                    saldoDisponible,
                    totalDebitos,
                    totalCreditos,
                    movimientosDTO
            );

        }).toList();

        // PDF en base64
        String pdfBase64 = pdfService.generarPdfEstadoCuenta(
                cliente, cuentasDTO, fDesde, fHasta
        );

        return new ReporteResponse(
                cliente.getNombre(),
                cliente.getIdentificacion(),
                desde,
                hasta,
                cuentasDTO,
                pdfBase64
        );
    }
}

