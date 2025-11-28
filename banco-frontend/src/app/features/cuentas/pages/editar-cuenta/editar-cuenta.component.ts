import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Cliente } from '../../../../core/models/cliente.model';
import { CuentaService } from '../../../../core/services/cuenta.service';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ErrorService } from '../../../../core/services/error.service';
import { Cuenta } from '../../../../core/models/cuenta.model';

@Component({
  selector: 'app-editar-cuenta',
  templateUrl: './editar-cuenta.component.html',
  styleUrls: ['./editar-cuenta.component.scss']
})
export class EditarCuentaComponent implements OnInit {

  form!: FormGroup;
  submitting = false;
  loading = false;
  errorMessage = '';
  successMessage = '';
  id!: string;

  clientes: Cliente[] = [];

  tiposCuenta = ['AHORRO', 'CORRIENTE'];

  constructor(
    private fb: FormBuilder,
    private cuentaService: CuentaService,
    private clientesService: ClienteService,
    private errorService: ErrorService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.id = this.route.snapshot.paramMap.get('id')!;

    this.form = this.fb.group({
      clienteId: ['', Validators.required],
      numeroCuenta: ['', Validators.required],
      tipoCuenta: ['', Validators.required],
      saldoInicial: [0, [Validators.required, Validators.min(0)]]
    });

    this.cargarClientes();
    this.cargarCuenta();
  }

  cargarClientes(): void {
    this.clientesService.listar().subscribe({
      next: data => this.clientes = data || [],
      error: err => this.errorMessage = err?.message || 'Error cargando clientes'
    });
  }

  cargarCuenta(): void {
    if (!this.id) {
      this.errorMessage = 'ID inválido';
      return;
    }

    this.loading = true;
    this.cuentaService.obtener(this.id).subscribe({
      next: cuenta => {
        this.form.patchValue(cuenta);
        this.loading = false;
      },
      error: err => {
        this.errorMessage = this.errorService.parse(err?.error || err);
        this.loading = false;
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const payload: Cuenta = { ...this.form.value };

    this.cuentaService.actualizar(this.id, payload).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Cuenta actualizada correctamente';
        setTimeout(() => this.router.navigate(['/cuentas']), 700);
      },
      error: err => {
        this.submitting = false;
        this.errorMessage = this.errorService.parse(err?.error || err);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/cuentas']);
  }
}
