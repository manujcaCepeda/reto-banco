import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListarMovimientosComponent } from './pages/listar-movimientos/listar-movimientos.component';
import { RegistrarMovimientoComponent } from './pages/registrar-movimiento/registrar-movimiento.component';

const routes: Routes = [
  { path: '', component: ListarMovimientosComponent },
  { path: 'nuevo', component: RegistrarMovimientoComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MovimientosRoutingModule { }
