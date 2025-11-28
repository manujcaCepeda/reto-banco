package ec.com.banco.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(

		@NotBlank(message = "El nombre es obligatorio") 
		String nombre,

		@NotBlank(message = "El género es obligatorio") 
		String genero,

		@Min(value = 0) @Max(value = 110) 
		Integer edad,

		@NotBlank(message = "La identificación es obligatoria") 
		String identificacion,

		String direccion, 
		String telefono,

		@NotBlank(message = "El clienteId es obligatorio") 
		String clienteId,

		@NotBlank(message = "La contraseña es obligatoria") 
		String password) {
}
