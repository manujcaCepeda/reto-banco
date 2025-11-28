export interface Movimiento {
  id?: string;
  cuentaId: string;
  tipoMovimiento: 'RETIRO' | 'DEPOSITO';
  valor: number;
  saldo?: number;
  saldoInicial?: number;
  fecha?: string;

  // Campos auxiliares para mostrar en tabla
  numeroCuenta?: string;
  tipoCuenta?: string;
  clienteId?: string;
  estado?: boolean;
}
