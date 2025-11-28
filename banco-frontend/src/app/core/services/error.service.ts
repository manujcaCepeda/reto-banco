import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class ErrorService {

  parse(err: any): string {
    if (!err) return 'Error desconocido';

    if (typeof err === 'string') return err;

    if (typeof err === 'object') {
      try {
        return Object.values(err).join(', ');
      } catch {
        return JSON.stringify(err);
      }
    }

    return 'Error inesperado';
  }
}
