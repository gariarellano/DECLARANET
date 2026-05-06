import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
  PRIMARY_OUTLET,
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
      const pageAccess = this.getPage(state);

      

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
              this.globals.setValueSession('entes', dataRecuperaToken.body.perfilBody.entes);
              this.globals.setValueSession('permisos', dataRecuperaToken.body.perfil.permisos);
              const pageFound = this.globals.profile.permisos.find((permisos) => permisos.pagina === pageAccess);
              if (!pageFound) { this.router.navigate(['inicio']); }
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


  getPage(state): number {
    const tree = this.router.parseUrl(state.url);
    const children = tree.root.children[PRIMARY_OUTLET];
    const url = children.segments;
    // let url = route.url;
    let pageAccess: number;
    switch (url[0].path) {
      case 'inicio':
        if (url.length === 1) {
          pageAccess = 0;
        } else {
          switch (url[1].path) {
            case 'usuarios':
              pageAccess = 1;
              break;
            default:
              pageAccess = 0;
              break;
          }
        }
        break;
      default:
        pageAccess = 0;
        break;
    }
    return pageAccess;
  }
}
