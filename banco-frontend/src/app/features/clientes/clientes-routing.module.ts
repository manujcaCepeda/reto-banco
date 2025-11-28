import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListarClientesComponent } from './pages/listar-clientes/listar-clientes.component';
import { CrearClienteComponent } from './pages/crear-cliente/crear-cliente.component';
import { EditarClienteComponent } from './pages/editar-cliente/editar-cliente.component';

const routes: Routes = [
  { path: '', component: ListarClientesComponent },
  { path: 'nuevo', component: CrearClienteComponent },
  { path: 'editar/:id', component: EditarClienteComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientesRoutingModule {}
