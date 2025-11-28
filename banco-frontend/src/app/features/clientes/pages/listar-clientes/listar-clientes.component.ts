import { Component, OnInit } from '@angular/core';
import { ClienteService } from '../../../../core/services/cliente.service';
import { Cliente } from '../../../../core/models/cliente.model';
import { Router } from '@angular/router';
import { ErrorService } from '../../../../core/services/error.service';

@Component({
  selector: 'app-listar-clientes',
  templateUrl: './listar-clientes.component.html',
  styleUrls: ['./listar-clientes.component.scss']
})
export class ListarClientesComponent implements OnInit {

  clientes: Cliente[] = [];
  filtered: Cliente[] = [];
  search = '';
  loading = false;
  errorMessage = '';

  constructor(private clienteService: ClienteService, 
    private errorService: ErrorService,
    public router: Router) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.errorMessage = '';
    this.clienteService.listar().subscribe({
      next: data => {
        this.clientes = data || [];
        this.applyFilter();
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = this.errorService.parse(err.error?.message || err); 
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    const q = this.search?.trim().toLowerCase();
    if (!q) {
      this.filtered = [...this.clientes];
      return;
    }
    this.filtered = this.clientes.filter(c =>
      (c.nombre || '').toLowerCase().includes(q) ||
      (c.identificacion || '').toLowerCase().includes(q) ||
      (c.clienteId || '').toLowerCase().includes(q) ||
      (c.telefono || '').toLowerCase().includes(q)
    );
  }

  crear(): void {
    this.router.navigate(['/clientes/nuevo']);
  }

  editar(id?: string): void {
    if (!id) return;
    this.router.navigate(['/clientes/editar', id]);
  }

  eliminar(id?: string): void {
    if (!id) return;
    if (!confirm('¿Eliminar cliente?')) return;
    this.clienteService.eliminar(id).subscribe({
      next: () => this.loadAll(),
      error: (err: any) => this.errorMessage = this.errorService.parse(err?.error || err)
    });
  }
}
