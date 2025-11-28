import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Cuenta } from '../models/cuenta.model';
import { Observable, map } from 'rxjs';
import { ClienteService } from './cliente.service';

@Injectable({ providedIn: 'root' })
export class CuentaService {

  private path = 'cuentas';

  constructor(
    private api: ApiService,
    private clienteService: ClienteService
  ) {}

  listar(): Observable<Cuenta[]> {
    return this.api.get<Cuenta[]>(this.path).pipe(
      map(cuentas => cuentas.map(c => c))
    );
  }

  obtener(id: string): Observable<Cuenta> {
    return this.api.get<Cuenta>(`${this.path}/${id}`);
  }

  crear(body: Cuenta): Observable<Cuenta> {
    return this.api.post<Cuenta>(this.path, body);
  }

  actualizar(id: string, body: Cuenta): Observable<Cuenta> {
    return this.api.put<Cuenta>(`${this.path}/${id}`, body);
  }

  eliminar(id: string): Observable<void> {
    return this.api.delete<void>(`${this.path}/${id}`);
  }

  listarByCliente(clienteId: string): Observable<Cuenta[]> {
    return this.api.get<Cuenta[]>(`${this.path}?clienteId=${clienteId}`).pipe(
      map(res => {
        if (Array.isArray(res) && res.length >= 0) return res;
        return [];
      })
    );
  }
}
