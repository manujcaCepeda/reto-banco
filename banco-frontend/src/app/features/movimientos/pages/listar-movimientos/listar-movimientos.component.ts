import { Component, OnInit } from '@angular/core';
import { MovimientosService } from '../../../../core/services/movimiento.service';
import { CuentaService } from '../../../../core/services/cuenta.service';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ErrorService } from '../../../../core/services/error.service';
import { Router } from '@angular/router';
import { Movimiento } from '../../../../core/models/movimiento.model';
import { Cuenta } from '../../../../core/models/cuenta.model';
import { Cliente } from '../../../../core/models/cliente.model';

@Component({
  selector: 'app-listar-movimientos',
  templateUrl: './listar-movimientos.component.html',
  styleUrls: ['./listar-movimientos.component.scss']
})
export class ListarMovimientosComponent implements OnInit {

  // Datos
  movimientos: Movimiento[] = [];
  filtered: Movimiento[] = [];

  // Autocomplete clientes
  clientes: Cliente[] = [];
  clienteSearch = '';
  clientesMatches: Cliente[] = [];
  clienteSeleccionado?: Cliente;

  // Cuentas del cliente seleccionado
  cuentasCliente: Cuenta[] = [];

  // UI
  loading = false;
  errorMessage = '';
  search = '';

  constructor(
    private movimientosService: MovimientosService,
    private cuentaService: CuentaService,
    private clienteService: ClienteService,
    private errorService: ErrorService,
    public router: Router
  ) { }

  ngOnInit(): void {
    this.loadClientes();
    this.loadAllData();
  }

  loadAllData(): void {
    this.loading = true;
    this.errorMessage = '';

    this.cuentaService.listar().subscribe({
      next: cuentas => {
        const cuentaMap = new Map(cuentas.map(c => [c.id!, c]));

        this.movimientosService.listar().subscribe({
          next: movs => {

            // Enriquecer movimientos con datos de cuenta
            const completos = movs.map(m => {
              const c = cuentaMap.get(m.cuentaId);
              return {
                ...m,
                clienteId: c?.clienteId,
                numeroCuenta: c?.numeroCuenta,
                tipoCuenta: c?.tipoCuenta,
                saldoInicial: c?.saldoInicial,
                estado: c?.estado
              };
            });

            this.movimientos = completos;
            this.filtered = completos;
            this.loading = false;
          },
          error: err => {
            this.loading = false;
            this.errorMessage = this.errorService.parse(err);
          }
        });

      },
      error: err => {
        this.loading = false;
        this.errorMessage = this.errorService.parse(err);
      }
    });
  }


  loadClientes(): void {
    this.clienteService.listar().subscribe({
      next: data => {
        this.clientes = data || [];
        this.clientesMatches = this.clientes.slice(0, 5);
      },
      error: err => {
        this.errorMessage = this.errorService.parse(err?.error || err);
      }
    });
  }


  onClienteInput(value: string): void {
    this.clienteSearch = value || '';
    const q = this.clienteSearch.trim().toLowerCase();
    if (!q) {
      this.clientesMatches = [];
      return;
    }

    this.clientesMatches = this.clientes
      .filter(c => (c.nombre || '').toLowerCase().includes(q) || (c.identificacion || '').toLowerCase().includes(q))
      .slice(0, 8);
  }


  seleccionarCliente(c: Cliente): void {
    this.clienteSeleccionado = c;
    this.clienteSearch = c.nombre;
    this.clientesMatches = [];
    this.filtered = this.movimientos.filter(m => m.clienteId === c.id);
  }


  applyFilter(): void {
    const q = this.search.trim().toLowerCase();
    if (!q) { this.filtered = [...this.movimientos]; return; }
    this.filtered = this.movimientos.filter(m =>
      (m.numeroCuenta || '').toLowerCase().includes(q) ||
      (m.tipoMovimiento || '').toLowerCase().includes(q) ||
      (m.valor + '').includes(q) ||
      (m.saldo + '').includes(q)
    );
  }

  registrar(): void {
    this.router.navigate(['/movimientos/nuevo']);
  }

  clearClienteSelection(): void {
    this.clienteSeleccionado = undefined;
    this.clienteSearch = '';
    this.cuentasCliente = [];

    // Mostrar todos los movimientos nuevamente
    this.filtered = [...this.movimientos];
  }
}
