import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Cliente } from '../models/cliente.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private path = 'clientes';
  constructor(private api: ApiService) { }

  listar(): Observable<Cliente[]> {
    return this.api.get<Cliente[]>(this.path);
  }

  obtener(id: string) {
    return this.api.get<Cliente>(`${this.path}/${id}`);
  }

  crear(body: Cliente) {
    return this.api.post<Cliente>(this.path, body);
  }

  actualizar(id: string, body: Cliente) {
    return this.api.put<Cliente>(`${this.path}/${id}`, body);
  }

  eliminar(id: string) {
    return this.api.delete<void>(`${this.path}/${id}`);
  }

}
