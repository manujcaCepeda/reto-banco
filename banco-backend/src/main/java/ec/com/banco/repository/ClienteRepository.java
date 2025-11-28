package ec.com.banco.repository;

import ec.com.banco.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
	
	Optional<Cliente> findByClienteId(String clienteId);
	
	boolean existsByIdentificacion(String identificacion);
	
    boolean existsByClienteId(String clienteId);
	
}
