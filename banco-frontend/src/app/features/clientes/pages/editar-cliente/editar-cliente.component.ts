import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Cliente } from '../../../../core/models/cliente.model';

@Component({
  selector: 'app-editar-cliente',
  templateUrl: './editar-cliente.component.html',
  styleUrls: ['./editar-cliente.component.scss']
})
export class EditarClienteComponent implements OnInit {

  form!: FormGroup;
  loading = false;
  submitting = false;
  errorMessage = '';
  successMessage = '';
  clienteId = '';

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private route: ActivatedRoute,
    public router: Router
  ) {
    // no usamos ngOnInit para inicializar form antes del fetch
  }

  ngOnInit(): void {
    this.clienteId = this.route.snapshot.paramMap.get('id') || '';
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      genero: ['', Validators.required],
      edad: [18, [Validators.required, Validators.min(1), Validators.max(150)]],
      identificacion: ['', [Validators.required, Validators.maxLength(20)]],
      direccion: ['', [Validators.required, Validators.maxLength(200)]],
      telefono: ['', [Validators.required, Validators.maxLength(20)]],
      // NO mostramos/actualizamos password en edición por defecto; si quieres agregar, hazlo con cuidado
    });

    if (this.clienteId) {
      this.loadCliente(this.clienteId);
    } else {
      this.errorMessage = 'ID de cliente inválido';
    }
  }

  loadCliente(id: string): void {
    this.loading = true;
    this.errorMessage = '';
    this.clienteService.obtener(id).subscribe({
      next: (c: Cliente) => {
        this.form.patchValue({
          nombre: c.nombre,
          genero: c.genero,
          edad: c.edad,
          identificacion: c.identificacion,
          direccion: c.direccion,
          telefono: c.telefono
        });
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = err?.message || 'No se pudo cargar cliente';
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
    this.errorMessage = '';
    this.successMessage = '';
    this.clienteService.actualizar(this.clienteId, this.form.value).subscribe({
      next: () => {
        this.submitting = false;
        this.successMessage = 'Cliente actualizado';
        setTimeout(() => this.router.navigate(['/clientes']), 700);
      },
      error: (err: any) => {
        this.submitting = false;
        this.errorMessage = err?.message || 'Error actualizando cliente';
      }
    });
  }
}
