import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReporteGeneral } from '../models/reporte.model';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  private baseUrl = 'http://localhost:8080/api/reportes';

  constructor(private http: HttpClient) {}

  obtener(clienteId: string, desde: string, hasta: string): Observable<ReporteGeneral> {
    const params = new HttpParams()
      .set('clienteId', clienteId)
      .set('desde', desde)
      .set('hasta', hasta);

    return this.http.get<ReporteGeneral>(this.baseUrl, { params });
  }
}
