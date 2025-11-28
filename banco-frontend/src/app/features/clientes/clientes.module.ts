import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClientesRoutingModule } from './clientes-routing.module';
import { ListarClientesComponent } from './pages/listar-clientes/listar-clientes.component';
import { CrearClienteComponent } from './pages/crear-cliente/crear-cliente.component';
import { EditarClienteComponent } from './pages/editar-cliente/editar-cliente.component';


@NgModule({
  declarations: [
    ListarClientesComponent,
    CrearClienteComponent,
    EditarClienteComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ClientesRoutingModule
  ]
})
export class ClientesModule { }
