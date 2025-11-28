package ec.com.banco.service;

import java.util.UUID;

import ec.com.banco.dto.response.ReporteResponse;

public interface ReporteService {

	ReporteResponse generarReporte(UUID clienteId, String desde, String hasta);
	
}
