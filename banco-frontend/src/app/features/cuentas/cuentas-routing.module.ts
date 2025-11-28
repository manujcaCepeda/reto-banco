import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListarCuentasComponent } from './pages/listar-cuentas/listar-cuentas.component';
import { CrearCuentaComponent } from './pages/crear-cuenta/crear-cuenta.component';
import { EditarCuentaComponent } from './pages/editar-cuenta/editar-cuenta.component';

const routes: Routes = [
  { path: '', component: ListarCuentasComponent },
  { path: 'nuevo', component: CrearCuentaComponent },
  { path: 'editar/:id', component: EditarCuentaComponent }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CuentasRoutingModule { }
