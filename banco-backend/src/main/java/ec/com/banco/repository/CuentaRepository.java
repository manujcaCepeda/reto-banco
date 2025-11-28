package ec.com.banco.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ec.com.banco.entity.Cuenta;
import jakarta.persistence.LockModeType;

public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {
	
	boolean existsByNumeroCuenta(String numeroCuenta);
	
	Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
	
	List<Cuenta> findByClienteId(UUID clienteId);
	
	// Lectura de cuenta con bloqueo pesimista para operaciones que actualizan saldo
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cuenta c where c.id = :id")
    Optional<Cuenta> findByIdForUpdate(@Param("id") UUID id);
}
