import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'clientes',
    loadChildren: () =>
      import('./features/clientes/clientes.module').then(m => m.ClientesModule)
  },
  {
    path: 'cuentas',
    loadChildren: () =>
      import('./features/cuentas/cuentas.module').then(m => m.CuentasModule)
  },
  {
    path: 'movimientos',
    loadChildren: () =>
      import('./features/movimientos/movimientos.module').then(m => m.MovimientosModule)
  },
  {
    path: 'reportes',
    loadChildren: () =>
      import('./features/reportes/reportes.module').then(m => m.ReportesModule)
  },
  { path: '', redirectTo: '/clientes', pathMatch: 'full' },
  { path: '**', redirectTo: '/clientes' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
