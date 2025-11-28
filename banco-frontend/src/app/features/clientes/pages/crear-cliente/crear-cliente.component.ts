import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from '../../../../core/services/cliente.service';
import { Router } from '@angular/router';
import { ErrorService } from '../../../../core/services/error.service';
import { Cliente } from '../../../../core/models/cliente.model';

@Component({
  selector: 'app-crear-cliente',
  templateUrl: './crear-cliente.component.html',
  styleUrls: ['./crear-cliente.component.scss']
})
export class CrearClienteComponent {

  form: FormGroup;
  submitting = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private errorService: ErrorService,
    public router: Router
  ) {
    // inicializar formBuilder dentro del constructor (antes de usar)
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      genero: ['', Validators.required],
      edad: [18, [Validators.required, Validators.min(1), Validators.max(150)]],
      identificacion: ['', [Validators.required, Validators.maxLength(20)]],
      clienteId: ['', [Validators.required, Validators.maxLength(20)]],
      direccion: ['', [Validators.required, Validators.maxLength(200)]],
      telefono: ['', [Validators.required, Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      estado: [true]
    });
  }

  guardar(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Cliente = { ...this.form.value };
    this.submitting = true;

    this.clienteService.crear(payload).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Cliente creado correctamente';
        setTimeout(() => this.router.navigate(['/clientes']), 700);
      },
      error: (err: any) => {
        this.submitting = false;
        this.errorMessage = this.errorService.parse(err.error?.message);

        console.log(this.errorMessage)
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/clientes']);
  }
}
