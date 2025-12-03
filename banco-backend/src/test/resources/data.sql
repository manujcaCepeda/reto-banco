INSERT INTO bank.persona (id, nombre, genero, edad, identificacion, direccion, telefono)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'Jose Lema', 'Masculino', 32, '1711002001', 'Otavalo sn y principal', '098254785'),
    ('22222222-2222-2222-2222-222222222222', 'Marianela Montalvo', 'Femenino', 28, '1711002002', 'Amazonas y NNUU', '097548965');

INSERT INTO bank.cliente (id, cliente_id, password, estado)
VALUES
    ('11111111-1111-1111-1111-111111111111', '1711002001', '1234', TRUE),
    ('22222222-2222-2222-2222-222222222222', '1711002002', '5678', TRUE);

    
-- Cuenta inicial con saldo 1000
INSERT INTO bank.cuenta (id, numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '999001', 'AHORRO', 1000.00, TRUE, '11111111-1111-1111-1111-111111111111');

-- Movimiento inicial (opcional)
INSERT INTO bank.movimiento (id, fecha, tipo_movimiento, valor, saldo, cuenta_id)
VALUES
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', NOW(), 'DEPOSITO', 200.00, 1200.00, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb');