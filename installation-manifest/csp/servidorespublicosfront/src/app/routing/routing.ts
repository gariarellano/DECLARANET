import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BusquedaComponent } from '../busqueda/busqueda.component';
import { GabineteComponent } from '../gabinete/gabinete.component';
import { DeclaracionesComponent } from '../declaraciones/declaraciones.component';
import { ReporteInaiComponent } from '../reporte-inai/reporte-inai.component';
import { MaintenanceComponent } from '../maintenance/maintenance.component';


const appRoutes: Routes = [
    { path: '', component: BusquedaComponent },
    { path: 'busqueda', component: BusquedaComponent },
    { path: 'gabinete', component: GabineteComponent },
    { path: 'declaraciones', component: DeclaracionesComponent },
    { path: 'consultasp.html', component: ReporteInaiComponent },
    { path: 'mantenimiento', component: MaintenanceComponent },
    { path: ':ente/consultasp.html', component: ReporteInaiComponent },
    { path: '**', component: BusquedaComponent }
];

export const appRoutingProviders: any[] = [];
export const routing: ModuleWithProviders<any> = RouterModule.forRoot(appRoutes);