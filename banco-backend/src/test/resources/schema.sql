CREATE SCHEMA IF NOT EXISTS bank;

CREATE TABLE IF NOT EXISTS bank.persona (
    id UUID PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    edad INT,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS bank.cliente (
    id UUID PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cliente_persona
        FOREIGN KEY (id) REFERENCES bank.persona(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bank.cuenta (
    id UUID PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo_inicial NUMERIC(19,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id UUID NOT NULL,
    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id) REFERENCES bank.cliente(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bank.movimiento (
    id UUID PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(19,2) NOT NULL,
    saldo NUMERIC(19,2) NOT NULL,
    cuenta_id UUID NOT NULL,
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES bank.cuenta(id) ON DELETE CASCADE
);
