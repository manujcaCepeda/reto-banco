import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CuentasRoutingModule } from './cuentas-routing.module';
import { ListarCuentasComponent } from './pages/listar-cuentas/listar-cuentas.component';
import { EditarCuentaComponent } from './pages/editar-cuenta/editar-cuenta.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CrearCuentaComponent } from './pages/crear-cuenta/crear-cuenta.component';


@NgModule({
  declarations: [
    ListarCuentasComponent,
    CrearCuentaComponent,
    EditarCuentaComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    CuentasRoutingModule
  ]
})
export class CuentasModule { }
