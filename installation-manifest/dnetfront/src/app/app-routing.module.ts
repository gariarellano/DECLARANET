import { NgModule } from '@angular/core';
import { Routes, RouterModule, ExtraOptions } from '@angular/router';
import { PrincipalComponent } from './components/principal/principal.component';
import { StartComponent } from './components/start/start.component';
import { LoginComponent } from './components/login/login.component';
import { SystemLogInComponent } from './components/system-log-in/system-log-in.component';
import { ErrorComponent } from './components/error/error.component';
import { SignComponent } from './components/sign/sign.component';
import { AvisoComponent } from './components/avisos/avisos.component';
import { NotaAclaraComponent } from './components/nota-aclara/nota-aclara.component';
import { MaintenanceComponent } from './components/maintenance/maintenance.component';

const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled',
  scrollOffset: [0, 64],
};

const routes: Routes = [
  { path: 'declaracion', component: PrincipalComponent },
  { path: 'inicio', component: StartComponent },
  { path: 'login', component: LoginComponent },
  { path: 'system-log-in', component: SystemLogInComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'firmar', component: SignComponent },
  { path: 'aviso', component: AvisoComponent},
  { path: 'nota', component: NotaAclaraComponent},
  { path: 'mantenimiento', component: MaintenanceComponent },
  // { path: '', component: MaintenanceComponent },
  // { path: '**', component: MaintenanceComponent }
  { path: '', component: LoginComponent },
  { path: '**', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, routerOptions)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
