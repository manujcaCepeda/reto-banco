package ec.com.banco.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.com.banco.dto.request.ClienteEditarRequest;
import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;
import ec.com.banco.entity.Cliente;
import ec.com.banco.exception.ResourceNotFoundException;
import ec.com.banco.repository.ClienteRepository;
import ec.com.banco.service.ClienteService;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final PasswordEncoder passwordEncoder;

	public ClienteServiceImpl(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

	@Override
	public ClienteResponse crearCliente(ClienteRequest request) {

		if (clienteRepository.existsByIdentificacion(request.identificacion())) {
			throw new IllegalArgumentException("La identificación ya está registrada");
		}

		if (clienteRepository.existsByClienteId(request.clienteId())) {
			throw new IllegalArgumentException("El clienteId ya existe");
		}

		// 1. Crear cliente
		Cliente cliente = new Cliente();

		// Merge de herencia JOINED
		cliente.setNombre(request.nombre());
		cliente.setGenero(request.genero());
		cliente.setEdad(request.edad());
		cliente.setIdentificacion(request.identificacion());
		cliente.setDireccion(request.direccion());
		cliente.setTelefono(request.telefono());

		cliente.setClienteId(request.clienteId());
//		cliente.setPassword(request.password());
		cliente.setPassword(passwordEncoder.encode(request.password()));
		cliente.setEstado(true);

		Cliente saved = clienteRepository.save(cliente);

		return mapToResponse(saved);
	}

	@Override
	public ClienteResponse obtenerCliente(UUID id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

		return mapToResponse(cliente);
	}

	@Override
	public List<ClienteResponse> listarClientes() {
		return clienteRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	@Override
	public ClienteResponse actualizarCliente(UUID id, ClienteEditarRequest request) {

		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

		if (!cliente.getIdentificacion().equals(request.identificacion())
				&& clienteRepository.existsByIdentificacion(request.identificacion())) {
			throw new IllegalArgumentException("La identificación ya está registrada");
		}
			
		cliente.setNombre(request.nombre());
		cliente.setGenero(request.genero());
		cliente.setEdad(request.edad());
		cliente.setIdentificacion(request.identificacion());
		cliente.setDireccion(request.direccion());
		cliente.setTelefono(request.telefono());
		
//		cliente.setClienteId(request.clienteId());
//		cliente.setPassword(request.password());
//		cliente.setPassword(passwordEncoder.encode(request.password()));

		return mapToResponse(clienteRepository.save(cliente));
	}

	@Override
	public void eliminarCliente(UUID id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
		clienteRepository.delete(cliente);
	}

	private ClienteResponse mapToResponse(Cliente c) {
		return new ClienteResponse(c.getId(), c.getNombre(), c.getGenero(), c.getEdad(), c.getIdentificacion(),
				c.getDireccion(), c.getTelefono(), c.getClienteId(), c.getEstado());
	}
}
