import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MovimientosService } from '../../../../core/services/movimiento.service';
import { CuentaService } from '../../../../core/services/cuenta.service';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ErrorService } from '../../../../core/services/error.service';
import { Router } from '@angular/router';
import { Movimiento } from '../../../../core/models/movimiento.model';
import { Cuenta } from '../../../../core/models/cuenta.model';
import { Cliente } from '../../../../core/models/cliente.model';

@Component({
  selector: 'app-registrar-movimiento',
  templateUrl: './registrar-movimiento.component.html',
  styleUrls: ['./registrar-movimiento.component.scss']
})
export class RegistrarMovimientoComponent implements OnInit {

  form!: FormGroup;
  submitting = false;
  errorMessage = '';
  successMessage = '';

  // Autocomplete clientes
  clientes: Cliente[] = [];
  clienteSearch = '';
  clientesMatches: Cliente[] = [];
  clienteSeleccionado?: Cliente;

  // Cuentas filtradas
  cuentasCliente: Cuenta[] = [];

  constructor(
    private fb: FormBuilder,
    private movimientosService: MovimientosService,
    private cuentaService: CuentaService,
    private clienteService: ClienteService,
    private errorService: ErrorService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      cuentaId: ['', Validators.required],
      tipoMovimiento: ['', Validators.required],
      valor: [null, [Validators.required, Validators.min(1)]]
    });

    this.loadClientes();
  }

  loadClientes(): void {
    this.clienteService.listar().subscribe({
      next: data => this.clientes = data || [],
      error: err => this.errorMessage = this.errorService.parse(err?.error || err)
    });
  }

  onClienteInput(value: string): void {
    this.clienteSearch = value || '';
    const q = this.clienteSearch.trim().toLowerCase();
    if (!q) { this.clientesMatches = []; return; }
    this.clientesMatches = this.clientes
      .filter(c => (c.nombre || '').toLowerCase().includes(q) || (c.identificacion || '').toLowerCase().includes(q))
      .slice(0, 8);
  }

  seleccionarCliente(c: Cliente): void {
    this.clienteSeleccionado = c;
    this.clienteSearch = c.nombre;
    this.clientesMatches = [];
    this.loadCuentasCliente(c.id!);
    this.form.patchValue({ cuentaId: '' });
  }

  clearClienteSelection(): void {
    this.clienteSeleccionado = undefined;
    this.clienteSearch = '';
    this.cuentasCliente = [];
    this.form.patchValue({ cuentaId: '' });
  }

  private loadCuentasCliente(clienteId: string): void {
    this.cuentaService.listar().subscribe({
      next: cuentas => {
        // Filtrar SOLO las cuentas del cliente seleccionado
        this.cuentasCliente = (cuentas || []).filter(c => c.clienteId === clienteId);
      },
      error: err => {
        this.errorMessage = this.errorService.parse(err);
      }
    });
  }


  guardar(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    const payload: Movimiento = { ...this.form.value };
    this.submitting = true;

    this.movimientosService.crear(payload).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Movimiento registrado correctamente';
        setTimeout(() => this.router.navigate(['/movimientos']), 700);
      },
      error: err => {
        this.submitting = false;
        this.errorMessage = this.errorService.parse(err?.error || err);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/movimientos']);
  }
}
