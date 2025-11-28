package ec.com.banco.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente", schema = "bank")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Persona {

	@Column(name = "cliente_id", unique = true, nullable = false)
    private String clienteId;
	
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean estado = Boolean.TRUE;
}
