import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MovimientosRoutingModule } from './movimientos-routing.module';
import { ListarMovimientosComponent } from './pages/listar-movimientos/listar-movimientos.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrarMovimientoComponent } from './pages/registrar-movimiento/registrar-movimiento.component';


@NgModule({
  declarations: [
    ListarMovimientosComponent,
    RegistrarMovimientoComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MovimientosRoutingModule
  ]
})
export class MovimientosModule { }
