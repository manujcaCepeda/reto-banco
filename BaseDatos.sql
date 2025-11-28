-- ===========================================================
--   CREACIÓN DE BASE DE DATOS Y EXTENSIONES
-- ===========================================================
CREATE SCHEMA IF NOT EXISTS bank;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===========================================================
--   TABLA PERSONA (CLASE PADRE – JOINED)
-- ===========================================================
CREATE TABLE IF NOT EXISTS bank.persona (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INT,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

-- ===========================================================
--   TABLA CLIENTE (HEREDA DE PERSONA – JOINED)
-- ===========================================================
CREATE TABLE IF NOT EXISTS bank.cliente (
    id UUID PRIMARY KEY,  -- FK hacia persona.id
    cliente_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cliente_persona
        FOREIGN KEY (id) REFERENCES bank.persona(id) ON DELETE CASCADE
);

-- ===========================================================
--   TABLA CUENTA
-- ===========================================================
CREATE TABLE IF NOT EXISTS bank.cuenta (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo_inicial NUMERIC(19,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id UUID NOT NULL,

    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id) REFERENCES bank.cliente(id) ON DELETE CASCADE
);

-- ===========================================================
--   TABLA MOVIMIENTO
-- ===========================================================
CREATE TABLE IF NOT EXISTS bank.movimiento (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fecha TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(19,2) NOT NULL,
    saldo NUMERIC(19,2) NOT NULL,
    cuenta_id UUID NOT NULL,

    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES bank.cuenta(id) ON DELETE CASCADE
);

-- ===========================================================
--   DATOS DE EJEMPLO (SEGÚN EL RETO)
-- ===========================================================

-- =======================
-- PERSONAS / CLIENTES
-- =======================

INSERT INTO bank.persona (id, nombre, genero, edad, identificacion, direccion, telefono)
VALUES 
    (uuid_generate_v4(), 'Jose Lema', 'Masculino', 32, '1711002001', 'Otavalo sn y principal', '098254785'),
    (uuid_generate_v4(), 'Marianela Montalvo', 'Femenino', 28, '1711002002', 'Amazonas y NNUU', '097548965'),
    (uuid_generate_v4(), 'Juan Osorio', 'Masculino', 29, '1711002003', '13 junio y Equinoccial', '098874587');

-- Crear clientes basados en las personas creadas
INSERT INTO bank.cliente (id, cliente_id, password, estado)
SELECT id, 
       identificacion AS cliente_id,
       CASE identificacion 
            WHEN '1711002001' THEN '1234'
            WHEN '1711002002' THEN '5678'
            WHEN '1711002003' THEN '1245'
       END AS password,
       TRUE
FROM bank.persona
WHERE identificacion IN ('1711002001', '1711002002', '1711002003');

-- =======================
-- CUENTAS
-- =======================
INSERT INTO bank.cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '478758', 'AHORRO', 2000, TRUE, id 
FROM bank.cliente WHERE cliente_id = '1711002001';

INSERT INTO bank.cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '225487', 'CORRIENTE', 100, TRUE, id 
FROM bank.cliente WHERE cliente_id = '1711002002';

INSERT INTO bank.cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '495878', 'AHORRO', 0, TRUE, id 
FROM bank.cliente WHERE cliente_id = '1711002003';

INSERT INTO bank.cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '496825', 'AHORRO', 540, TRUE, id 
FROM bank.cliente WHERE cliente_id = '1711002002';

-- Cuenta extra del reto (para Jose Lema)
INSERT INTO bank.cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '585545', 'CORRIENTE', 1000, TRUE, id 
FROM bank.cliente WHERE cliente_id = '1711002001';

-- =======================
-- MOVIMIENTOS
-- =======================

-- Retiro de 575 en cuenta 478758
INSERT INTO bank.movimiento (tipo_movimiento, valor, saldo, cuenta_id)
SELECT 'DEBITO', 575, (2000 - 575), id FROM bank.cuenta WHERE numero_cuenta = '478758';

-- Depósito de 600 en cuenta 225487
INSERT INTO bank.movimiento (tipo_movimiento, valor, saldo, cuenta_id)
SELECT 'CREDITO', 600, (100 + 600), id FROM bank.cuenta WHERE numero_cuenta = '225487';

-- Depósito de 150 en cuenta 495878
INSERT INTO bank.movimiento (tipo_movimiento, valor, saldo, cuenta_id)
SELECT 'CREDITO', 150, (0 + 150), id FROM bank.cuenta WHERE numero_cuenta = '495878';

-- Retiro de 540 en cuenta 496825
INSERT INTO bank.movimiento (tipo_movimiento, valor, saldo, cuenta_id)
SELECT 'DEBITO', 540, (540 - 540), id FROM bank.cuenta WHERE numero_cuenta = '496825';

