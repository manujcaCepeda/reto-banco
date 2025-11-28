export interface Cuenta {
  id?: string;
  clienteId: string;
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  estado?: boolean;
  clienteNombre?: string;
}
