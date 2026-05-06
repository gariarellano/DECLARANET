import { NgModule } from '@angular/core';
import { Routes, RouterModule, ExtraOptions } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SystemLoginComponent } from './components/system-login/system-login.component';
import { AuthService } from './Authguard/auth.service';
import { ConsultaComponent } from './components/consulta/consulta.component';
import { HomeComponent } from './components/home/home.component';
import { PerfilesComponent } from './components/perfiles/perfiles.component';
import { UsuariosIdentidadComponent } from './components/usuarios-identidad/usuarios-identidad.component';
import { UsuariosOperComponent } from './components/usuarios-oper/usuarios-oper.component';

const routerOptions: ExtraOptions = {
    scrollPositionRestoration: 'enabled',
    anchorScrolling: 'enabled',
    scrollOffset: [0, 64],
    relativeLinkResolution: 'legacy'
};

const routes: Routes = [
  { path: '', component: LoginComponent, data: {showMenu: false} },
  { path: 'login', component: LoginComponent, data: {showMenu: false} },
  { path: 'usuariosIdentidad', component: UsuariosIdentidadComponent, canActivate: [AuthService] },
  { path: 'usuariosOper', component: UsuariosOperComponent, canActivate: [AuthService] },
  { path: 'system-login', component: SystemLoginComponent, data: {showMenu: false} },
  { path: 'consultaDeclaracion', component: ConsultaComponent, canActivate: [AuthService]},
  { path: 'home', component: HomeComponent, canActivate: [AuthService] },
  { path: 'profile', component: PerfilesComponent, data: {showMenu: false} },
  { path: '**', component: LoginComponent, data: {showMenu: false} }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
