import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Apollo } from 'apollo-angular';
import { HttpLink } from 'apollo-angular-link-http';
// import { InMemoryCache } from "apollo-cache-inmemory";
import { Globals } from 'src/app/globals';
//import { PERMISOS } from 'src/app/Const/const';
import { AUTH } from 'src/app/Queries/auth';
import { environment } from 'src/environments/environment';
import { IdpService } from './idp.service';
// import { data } from "../data";

@Injectable({
  providedIn: 'root',
})
export class CommonService {
  constructor(
    private http: HttpClient,
    private globals: Globals,
    //private globals: Globals,
    private apollo: Apollo,
    private httpLink: HttpLink,
    private router: Router,
    private idpService: IdpService,
  ) {
    // const perfiles = {
    //    idEnte: AUTH.VALIDATE_TOKEN.institutions.inst.ur,
    //   // perfil: data.validateToken.inst.institutions.profile
    // }
    // sessionStorage.setItem('perfiles', JSON.stringify(perfiles));
  }



  obtenerServidores(filtro: any): Observable<any> {
    return this.apollo
      .use('servidores')
      .query<any>({
        query: AUTH.getServidores,
        fetchPolicy: 'no-cache',
        variables: { filtro },
      })
      .pipe(map((result) => result.data));
  }

  obtenerInstituciones(collName: string, condiciones: any): Observable<any> {
    return this.apollo
      .use('servidores')
      .query<any>({
        query: AUTH.getInstituciones,
        fetchPolicy: 'no-cache',
        variables: { collName, condiciones },
      })
      .pipe(map((result) => result.data));
  }

  obtenerInfoUA(filtro: any): Observable<any> {
    return this.apollo
      .use('servidores')
      .query<any>({
        query: AUTH.getInfoUA,
        fetchPolicy: 'no-cache',
        variables: { filtro },
      })
      .pipe(map((result) => result.data));
  }

  

  allowAccess(_page) {
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

      const pageFound = this.globals.profile.permisos.find((permisos) => permisos.pagina === _page);
      // if (pageFound) {
      //   return of(true);
      // } else {
      //   return of(false);
      // }
      return true;
        } else {
      if (sessionStorage.getItem('Authorization')) {
        const r = this.idpService.recuperaToken().subscribe(
          (dataRecuperaToken) => {
            console.log({ dataRecuperaToken });
            sessionStorage.setItem('Authorization', dataRecuperaToken.headers.get('Authorization'));
            this.globals.setValueSession('usuario', this.globals.tokenInicial.usuario);
            this.globals.setValueSession('profile', dataRecuperaToken.body.perfil);
            this.globals.setValueSession('permisos', dataRecuperaToken.body.perfil.permisos);
            return true;

          },
          (error) => {
            
          }
        );
        console.log(r);
      }
    }



  }



  removeSessionProfile(_token, _idp) {
    // this.globals.removeProfile()
    return this.apollo.use('perfilamiento').query({
      query: AUTH.REMOVE_PROFILE_TOKEN,
      variables: {
        _token,
        _idp,
      },
      fetchPolicy: 'no-cache',
    });
  }


  // Crear menú
  createMenu() {
    // this.globals.menu = [];
    // for (const i of this.getValueSession("pages")) {
    //   this.globals.menu.push(PERMISOS.ALL_PAGES[i]);
    // }
  }

  getValueSession(_property) {
    return this.globals[_property];
  }

  setValueSession(_property, _value) {
    this.globals[_property] = _value;
  }

  // Eliminar datos de la sesión
  deleteSession() {
    this.globals.clean();
  }

  // Eliminar perfil de los datos de sesión
  deleteProfile() {
    this.globals.profile = undefined;
  }

  // Remover el perfil del token
  removeProfile(_token) {
    /* return this.http
      .post
      environment.API_BASE,
      {},
      {
        headers: {
          authorization: _token,
        }
      }
    
      ();*/
  }

  destroySession(_text) {
    if (this.getValueSession('curp')) {
      this.apollo
        .use('perfilamiento')
        .query({
          query: AUTH.LOGOUT,
          variables: {
            _curp: this.getValueSession('curp'),
            _app: 'OPER',
          },
          fetchPolicy: 'no-cache',
        })
        .subscribe(
          (res) => {
            // this.globals.clean();
            this.deleteSession();
            sessionStorage.clear();
            this.router.navigate(['login']);
          },
          (err) => {
            console.error(err);
          }
        );
    } else {
      this.router.navigate(['login']);
    }
  }

  auth(_token, _transaction, _instancia) {
    return this.apollo.use('perfilamiento').query({
      query: AUTH.GET_TOKEN,
      variables: {
        _token,
        _transaction,
        _instancia,
        _app: environment.API_NAME_PERFILAMIENTO
      },
      fetchPolicy: 'no-cache',
    });
  }

  // Cerrar alerta
  closeAlert() {
    //  Swal.close();
  }

  // Lanzar toast al usuario
  launchToast(title, icon) {
    /*const Toast = Swal.mixin({
      toast: true,
      position: "top",
      showConfirmButton: false,
      timer: 3000,
    });*/
    /* Toast.fire({
      icon,
      title,
    });*/
  }

  // Mostrar loading
  showLoading(title, text) {
    /*Swal.fire({
      title,
      text,
      allowOutsideClick: false,
      onBeforeOpen: () => {
//        Swal.showLoading();
      },
    });*/
  }
}
