package ec.com.banco.dto.response;

import java.util.UUID;


public record ClienteResponse(
        
		UUID id,
//		UUID personaId,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        String clienteId,
        Boolean estado
) {}
