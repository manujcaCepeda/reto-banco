import { Component, OnInit } from '@angular/core';
import { Cliente } from '../../../../core/models/cliente.model';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ReporteGeneral } from '../../../../core/models/reporte.model';
import { ReporteService } from '../../../../core/services/reporte.service';
import { ErrorService } from '../../../../core/services/error.service';

@Component({
  selector: 'app-reporte-general',
  templateUrl: './reporte-general.component.html',
  styleUrls: ['./reporte-general.component.scss']
})
export class ReporteGeneralComponent implements OnInit {

  clientes: Cliente[] = [];
  clienteId = '';
  desde = '';
  hasta = '';

  loading = false;
  errorMessage = '';
  reporte?: ReporteGeneral;

  constructor(
    private clienteService: ClienteService,
    private reporteService: ReporteService,
    private errorService: ErrorService
  ) {}

  ngOnInit(): void {
    this.loadClientes();
  }

  private loadClientes(): void {
    this.clienteService.listar().subscribe({
      next: data => this.clientes = data || [],
      error: err => this.errorMessage = this.errorService.parse(err)
    });
  }

  consultar(): void {
    this.errorMessage = '';
    this.reporte = undefined;

    if (!this.clienteId || !this.desde || !this.hasta) {
      this.errorMessage = 'Complete todos los filtros.';
      return;
    }

    this.loading = true;

    this.reporteService.obtener(this.clienteId, this.desde, this.hasta).subscribe({
      next: data => {
        this.reporte = data;
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.errorMessage = this.errorService.parse(err);
      }
    });
  }

  limpiar(): void {
    this.clienteId = '';
    this.desde = '';
    this.hasta = '';
    this.reporte = undefined;
    this.errorMessage = '';
  }

  descargarPDF(): void {
    if (!this.reporte?.pdfBase64) return;

    const link = document.createElement('a');
    link.href = `data:application/pdf;base64,${this.reporte.pdfBase64}`;
    link.download = `reporte_${this.reporte.cliente}.pdf`;
    link.click();
  }
}
