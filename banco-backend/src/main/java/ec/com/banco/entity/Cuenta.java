package ec.com.banco.entity;

import java.math.BigDecimal;
import java.util.UUID;

import ec.com.banco.enums.TipoCuenta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cuenta", schema = "bank")
@Getter
@Setter
@NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 50)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false)
    private Boolean estado = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
