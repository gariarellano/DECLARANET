import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import { CommonService } from '../services/common.service';
import { Globals } from '../globals';
import { Observable, Subject } from 'rxjs';
import { map, first } from 'rxjs/operators';
import { IdpService } from '../services/idp.service';
import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService implements CanActivate {
  constructor(private commonService: CommonService,
              private globals: Globals,
              private router: Router,
              private idpService: IdpService,
              private sessionService: SessionService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      let url = route.url[0].path;
      const pageAccess = this.getPage(url);

      

      if (this.globals.tokenInicial) {
        this.globals.setValueSession('usuario', this.globals.tokenInicial.usuario);
        this.globals.setValueSession('profiles', this.globals.tokenInicial.asignaciones.perfiles);
        this.globals.setValueSession('tokenInicial', null);
        this.router.navigate(['profile']);
        return false;
      }

      if (
        this.globals.usuario &&
        this.globals.profile
      ) {
  
        const pageFound = this.globals.profile.permisos.find((permisos) => permisos.pagina === pageAccess);
        if (!pageFound) {this.router.navigate(['home']); }
        return (!!pageFound);
      } else {
        if (sessionStorage.getItem('Authorization')) {
          const subject = new Subject<boolean>();
          
          this.idpService.recuperaToken().subscribe(
            (dataRecuperaToken) => {
              console.log({ dataRecuperaToken });
              sessionStorage.setItem('Authorization', dataRecuperaToken.headers.get('Authorization'));
              this.globals.setValueSession('usuario',  this.globals.pick(dataRecuperaToken.body.perfilBody.usuario, ['curp', 'idUsuario', 'nombre', 'primerApellido', 'segundoApellido', 'rfc', 'homoclave', 'email']));
              this.globals.setValueSession('profile', dataRecuperaToken.body.perfil);
              this.globals.setValueSession('permisos', dataRecuperaToken.body.perfil.permisos);
              const pageFound = this.globals.profile.permisos.find((permisos) => permisos.pagina === pageAccess);
              if (!pageFound) { this.router.navigate(['home']); }
              subject.next(!!pageFound);
  
            },
            (error) => {
              subject.next(false);
              const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
              this.sessionService.logout();
              this.router.navigate([navOut]);
              return false;
            }
          );
          return subject.asObservable();

        } else {
          this.router.navigate(['login']);
          return false;
        }

      }

  }

  getPage(url): number {
    let pageAccess: number;
    switch (url) {
      case 'home':
        pageAccess = 0;
        break;
      case 'usuariosIdentidad':
        pageAccess = 1;
        break;
      case 'consultaDeclaracion':
        pageAccess = 2;
        break;
      case 'usuariosOper':
        pageAccess = 3;
        break;
      default:
        pageAccess = 0;
        break;

    }
    return pageAccess;
  }
}
