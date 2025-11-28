import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Movimiento } from '../models/movimiento.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MovimientosService {

  private path = 'movimientos';

  constructor(private api: ApiService) {}

  listar(): Observable<Movimiento[]> {
    return this.api.get<Movimiento[]>(this.path);
  }

  crear(body: Movimiento): Observable<Movimiento> {
    return this.api.post<Movimiento>(this.path, body);
  }
}
