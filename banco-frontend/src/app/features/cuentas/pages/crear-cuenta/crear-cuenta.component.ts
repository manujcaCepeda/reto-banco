import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Cliente } from '../../../../core/models/cliente.model';
import { CuentaService } from '../../../../core/services/cuenta.service';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ErrorService } from '../../../../core/services/error.service';
import { Cuenta } from '../../../../core/models/cuenta.model';

@Component({
  selector: 'app-crear-cuenta',
  templateUrl: './crear-cuenta.component.html',
  styleUrls: ['./crear-cuenta.component.scss']
})
export class CrearCuentaComponent implements OnInit {

  form!: FormGroup;
  submitting = false;
  errorMessage = '';
  successMessage = '';
  
  clientes: Cliente[] = [];

  tiposCuenta = ['AHORRO', 'CORRIENTE'];

  constructor(
    private fb: FormBuilder,
    private cuentaService: CuentaService,
    private clientesService: ClienteService,
    private errorService: ErrorService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      clienteId: ['', Validators.required],
      numeroCuenta: ['', Validators.required],
      tipoCuenta: ['', Validators.required],
      saldoInicial: [0, [Validators.required, Validators.min(0)]],
    });

    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clientesService.listar().subscribe({
      next: data => this.clientes = data || [],
      error: err => this.errorMessage = err?.message || 'Error cargando clientes'
    });
  }

  guardar(): void {
     this.errorMessage = '';
    this.successMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Cuenta = { ...this.form.value };
    this.submitting = true;

    this.cuentaService.crear(payload).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Cuenta creada correctamente';
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
