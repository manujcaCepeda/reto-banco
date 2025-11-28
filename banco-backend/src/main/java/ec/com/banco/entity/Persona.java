package ec.com.banco.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persona", schema = "bank")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20)
    private String genero;

    private Integer edad;

    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;
}
