export interface Cliente {
  id?: string;
  nombre: string;
  genero: 'M' | 'F' | 'O' | string;
  edad: number;
  identificacion: string;
  direccion: string;
  telefono: string;
  clienteId?: string;
  password?: string;
  estado?: boolean;
}