import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReporteGeneralComponent } from './pages/reporte-general/reporte-general.component';

const routes: Routes = [
  { path: '', component: ReporteGeneralComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportesRoutingModule { }
