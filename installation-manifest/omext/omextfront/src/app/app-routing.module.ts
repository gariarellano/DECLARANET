import { NgModule } from '@angular/core';
import { Routes, RouterModule, ExtraOptions } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SystemLoginComponent } from './components/system-login/system-login.component';
import { AuthService } from './Authguard/auth.service';
import { PerfilesComponent } from './components/perfiles/perfiles.component';
import { ErrorComponent } from './components/error/error.component';
import { UsuariosOmextComponent } from './components/usuarios-omext/usuarios-omext.component';
const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled',
  scrollOffset: [0, 64],
};

const routes: Routes = [

  {
    path: 'inicio',
    loadChildren: () =>
      import('./modules/home/home.module').then((m) => m.HomeModule),
    canActivate: [AuthService],
  },
  { path: 'login', component: LoginComponent },
  { path: 'system-log-in', component: SystemLoginComponent },
  { path: 'profile', component: PerfilesComponent, data: {showMenu: false} },
  { path: 'usuarios', component: UsuariosOmextComponent, canActivate: [AuthService]},

  /* {
    path: "inicio",
    loadChildren: () =>
      import("./modules/home/home.module").then((m) => m.HomeModule),
  },*/
  { path: 'error', component: ErrorComponent },
  { path: '', component: LoginComponent },
  { path: '**', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, routerOptions)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
