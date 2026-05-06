import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, map } from "rxjs/operators";
import { Observable, of } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Apollo } from "apollo-angular";
import { HttpLink } from "apollo-angular-link-http";
import { InMemoryCache } from "apollo-cache-inmemory";
import { Globals } from "src/app/globals";
//import { PERMISOS } from 'src/app/Const/const';
import { AUTH } from "src/app/Queries/auth";
import { environment } from "src/environments/environment";
import { data } from "../data";
import Swal from 'sweetalert2';
import { IdpService } from "./idp.service";

@Injectable({
  providedIn: "root",
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

  validateTokenSession(): Observable<boolean> {
    if (
      sessionStorage.getItem("tokenPerfilamiento") &&
      JSON.parse(sessionStorage.getItem("authIdp")).transaction
    ) {
      return this.apollo
        .use("perfilamiento")
        .query({
          query: AUTH.VALIDATE_TOKEN,
          variables: {
            _token: sessionStorage.getItem("tokenPerfilamiento"),
            _idp: JSON.parse(sessionStorage.getItem("authIdp")).transaction,
          },
          fetchPolicy: "no-cache",
        })
        .pipe(
          map((res) => {
            if (res.data["validateToken"].error) {
              console.log("Los datos de la sesión no son válidos.", "error");
              const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
              sessionStorage.clear();
              this.router.navigate(['/' + navOut]);
              return false;
            } else {
              // Validar si el usuario ya tiene asignado un perfil
              if (res.data["validateToken"].profile) {
                this.router.navigate(["home"]);
                return false;
              } else {
                this.setValueSession("user", res.data["validateToken"].user);
                this.setValueSession("curp", res.data["validateToken"].curp);
                this.setValueSession(
                  "profiles",
                  res.data["validateToken"].profiles
                );
                sessionStorage.setItem(
                  "tokenPerfilamiento",
                  res.data["validateToken"].token
                );
                return true;
              }
            }
          }),
          catchError((err) => {
            // Validar el error
            if (err.msj) {
              // Entra cuando el token es inválido o expiró
              this.destroySession(
                "El token no es válido o expiró<br>Intente iniciar sesión de nuevo"
              );
            } else {
              // No se puede conectar al servicio admin-perfiles
              this.destroySession(
                "No es posible validar su identidad<br>Intentelo más tarde"
              );
            }
            this.router.navigate(["login"]);
            return of(false);
          })
        );
    } else {
      this.destroySession("Acceso restringido a la página solicitada");
      this.router.navigate(["login"]);
      return of(false);
    }
  }

  obtenerServidores(filtro: any): Observable<any> {
    console.log({filtroDetalle: filtro});
    return this.apollo
      .use("servidores")
      .query<any>({
        query: AUTH.getServidores,
        fetchPolicy: "no-cache",
        variables: { filtro },
      })
      .pipe(map((result) => result.data));
  }
  descargarCSV(filtro: any): Observable<any> {
    return this.apollo
      .use("servidores")
      .query<any>({
        query: AUTH.descargarReporte,
        fetchPolicy: "no-cache",
        variables: { filtro },
      })
      .pipe(map((result) => result.data));
  }

  buscarServidores(filtro: any): Observable<any> {
    return this.apollo
      .use("servidores")
      .query<any>({
        query: AUTH.buscarServidores,
        fetchPolicy: "no-cache",
        variables: { filtro },
      })
      .pipe(map((result) => result.data));
  }

  obtenerInstituciones(collname, condiciones: any): Observable<any> {
    console.log({filtroInstituciones: condiciones});
    return this.apollo
      .use("servidores")
      .query<any>({
        query: AUTH.getInstituciones,
        fetchPolicy: "no-cache",
        variables: { collname, condiciones },
      })
      .pipe(map((result) => result.data));
  }

  consultaObligacion(comprobante, collname): any {
    return this.http.get(`${environment.API_NOLOCALIZADOS}/consulta-obligacion/${comprobante}/${collname}`, { observe: 'response' })
      .pipe(map(obligado => {

        return obligado;



      }));
  }


  registrarCumplimiento(usuario: any, params: any, motivo: any) {
    const idDNetNoLocalizado = params.idDNetNoLocalizado ? `/${params.idDNetNoLocalizado}` : '';
    const obj = {
      usuario,
      estatus: motivo.estatus,
      comentarios: motivo.comentarios
    }
    return this.http.post<any>(`${environment.API_NOLOCALIZADOS}/registrar-cumplimiento${idDNetNoLocalizado}/${params.idRusp}/${params.instReceptora}`, obj, { observe: 'response' })
      .pipe(map(registro => {
        return registro;
      }));
  }

  obtenerInfoUA(filtro: any): Observable<any> {
    console.log({filtroUA: filtro});
    return this.apollo
      .use("servidores")
      .query<any>({
        query: AUTH.getInfoUA,
        fetchPolicy: "no-cache",
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
    return this.apollo.use("perfilamiento").query({
      query: AUTH.REMOVE_PROFILE_TOKEN,
      variables: {
        _token,
        _idp,
      },
      fetchPolicy: "no-cache",
    });
  }

  setProfileInToken(_profile): Observable<boolean> {
    if (
      sessionStorage.getItem("tokenPerfilamiento") &&
      sessionStorage.getItem("authIdp")
    ) {
      return this.apollo
        .use("perfilamiento")
        .query({
          query: AUTH.SET_PROFILE_TOKEN,
          variables: {
            _token: sessionStorage.getItem("tokenPerfilamiento"),
            _idp: JSON.parse(sessionStorage.getItem("authIdp")).transaction,
            _profile,
          },
          fetchPolicy: "no-cache",
        })
        .pipe(
          map((res) => {
            if (res.data["setProfile"].error) {
              this.destroySession("Los datos de la sesión no son válidos.");
              this.router.navigate(["login"]);
              return false;
            }
            sessionStorage.setItem(
              "tokenPerfilamiento",
              res.data["setProfile"].token
            );
            this.setValueSession("profile", _profile);
            return true;
          }),
          catchError((err) => {
            // Validar el error
            if (err.msj) {
              // Entra cuando el token es inválido o expiró
              this.destroySession(
                "El token no es válido o expiró<br>Intente iniciar sesión de nuevo"
              );
            } else {
              // No se puede conectar al servicio admin-perfiles
              this.destroySession(
                "No es posible validar su identidad<br>Intentelo más tarde"
              );
            }
            this.router.navigate(["login"]);
            return of(false);
          })
        );
    } else {
      this.destroySession("Acceso restringido a la página solicitada");
      this.router.navigate(["login"]);
      return of(false);
    }
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
    if (this.getValueSession("curp")) {
      this.apollo
        .use("perfilamiento")
        .query({
          query: AUTH.LOGOUT,
          variables: {
            _curp: this.getValueSession("curp"),
            _app: "OMEXT",
          },
          fetchPolicy: "no-cache",
        })
        .subscribe(
          (res) => {
            // this.globals.clean();
            const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
            sessionStorage.clear();
            console.log('Sesion destruida');
            this.router.navigate(['/' + navOut]);
          },
          (err) => {
            console.error(err);
          }
        );
    } else {
      const navOut = JSON.parse(sessionStorage.getItem('enteReceptor')).enteContext || '';
      sessionStorage.clear();
      this.router.navigate(['/' + navOut]);
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
    const Toast = Swal.mixin({
      toast: true,
      position: "top",
      showConfirmButton: false,
      timer: 5000,
    });
    Toast.fire({
      icon,
      title,
    });
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

  createUsuario(user: any): Observable<any> {

    return this.apollo.use("perfilamiento").mutate({
      mutation: AUTH.guardarUsuario,
      variables: {
        user
      }
    }).pipe(map(result => result));
  }

  updateUsuario(user: any, curp: string): Observable<any> {

    return this.apollo.use("perfilamiento").mutate({
      mutation: AUTH.updateUser,
      variables: {
        app: environment.API_NAME_PERFILAMIENTO,
        curp,
        user
      }
    }).pipe(map(result => result));
  }
  
  consultaUsuario(curp: string): Observable<any> {

    return this.apollo.use("perfilamiento").mutate({
      mutation: AUTH.getUser,
      variables: {
        app: environment.API_NAME_PERFILAMIENTO,
        curp
      }
    }).pipe(map(result => result));
  }
}
