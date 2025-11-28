import { Component, OnInit } from '@angular/core';
import { CuentaService } from '../../../../core/services/cuenta.service';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ErrorService } from '../../../../core/services/error.service';
import { Router } from '@angular/router';
import { Cuenta } from '../../../../core/models/cuenta.model';

@Component({
  selector: 'app-listar-cuentas',
  templateUrl: './listar-cuentas.component.html',
  styleUrls: ['./listar-cuentas.component.scss']
})
export class ListarCuentasComponent implements OnInit {

  cuentas: Cuenta[] = [];
  filtered: Cuenta[] = [];
  search = '';

  loading = false;
  errorMessage = '';

  constructor(
    private cuentaService: CuentaService,
    private clienteService: ClienteService,
    private errorService: ErrorService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.errorMessage = '';

    this.cuentaService.listar().subscribe({
      next: data => {
        // obtenemos datos de cada cliente
        data.forEach(cuenta => {
          this.clienteService.obtener(cuenta.clienteId).subscribe({
            next: cli => {
              cuenta.clienteNombre = cli.nombre;
            }
          });
        });

        this.cuentas = data;
        this.applyFilter();
        this.loading = false;
      },
      error: err => {
        this.errorMessage = this.errorService.parse(err?.error || err);
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    const q = this.search.trim().toLowerCase();
    if (!q) {
      this.filtered = [...this.cuentas];
      return;
    }

    this.filtered = this.cuentas.filter(c =>
      (c.numeroCuenta || '').toLowerCase().includes(q) ||
      (c.tipoCuenta || '').toLowerCase().includes(q) ||
      (c.clienteNombre || '').toLowerCase().includes(q)
    );
  }

  crear(): void {
    this.router.navigate(['/cuentas/nuevo']);
  }

  editar(id?: string): void {
    if (!id) return;
    this.router.navigate(['/cuentas/editar', id]);
  }

  eliminar(id?: string): void {
    if (!id) return;
    if (!confirm('¿Eliminar cuenta?')) return;

    this.cuentaService.eliminar(id).subscribe({
      next: () => this.loadAll(),
      error: err => this.errorMessage = this.errorService.parse(err?.error || err)
    });
  }
}
