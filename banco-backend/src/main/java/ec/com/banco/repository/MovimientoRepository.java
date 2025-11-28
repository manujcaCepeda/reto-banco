package ec.com.banco.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.com.banco.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, UUID> {

	List<Movimiento> findByCuentaIdOrderByFechaAsc(UUID cuentaId);

	List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(UUID cuentaId, OffsetDateTime desde,
			OffsetDateTime hasta);
	
    // Último movimiento por cuenta — para conocer saldo actual
    Optional<Movimiento> findTopByCuenta_IdOrderByFechaDesc(UUID cuentaId);
}
