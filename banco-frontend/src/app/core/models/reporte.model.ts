export interface ReporteMovimiento {
  fecha: string;
  tipo: string;
  valor: number;
  saldo: number;
}

export interface ReporteCuenta {
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  saldoDisponible: number;
  totalDebitos: number;
  totalCreditos: number;
  movimientos: ReporteMovimiento[];
}

export interface ReporteGeneral {
  cliente: string;
  identificacion: string;
  desde: string;
  hasta: string;
  cuentas: ReporteCuenta[];
  pdfBase64: string;
}
