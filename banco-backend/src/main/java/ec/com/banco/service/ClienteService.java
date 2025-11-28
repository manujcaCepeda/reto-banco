package ec.com.banco.service;

import java.util.List;
import java.util.UUID;

import ec.com.banco.dto.request.ClienteEditarRequest;
import ec.com.banco.dto.request.ClienteRequest;
import ec.com.banco.dto.response.ClienteResponse;

public interface ClienteService {

	ClienteResponse crearCliente(ClienteRequest request);

	ClienteResponse obtenerCliente(UUID id);

	List<ClienteResponse> listarClientes();

	ClienteResponse actualizarCliente(UUID id, ClienteEditarRequest request);

	void eliminarCliente(UUID id);
}
