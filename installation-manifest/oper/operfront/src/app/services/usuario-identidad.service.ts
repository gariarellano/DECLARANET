import { Injectable } from '@angular/core';
import { HttpHeaders, HttpRequest, HttpClient } from '@angular/common/http';


import { Usuario, UsuarioEdit } from '../interfaces/usuario';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root'})
export class UsuarioIdentidadService {
    constructor(private http: HttpClient) { }
    edit(usuario: any, id: string) {
      return this.http.put<any>(`${environment.API_REGISTER}/oper/user?curp=${id}`, usuario, { observe: 'response' })
      .pipe(map(registro => {
          console.log(registro);
          return registro;
      }));
  }

    getUsuarioByCurp(curp: string) {
      return this.http.get<any>(`${environment.API_REGISTER}/oper/user?curp=${curp}`, { observe: 'response' })
      .pipe(map(registro => {
          console.log(registro);
          return registro;
      }));
  }

  getServidoresConsultaDeclaracion(valor: string, collname: string, tipoBusqueda: string, usuario: any): any{
    const body = {
      usuario
    };
    return this.http.post<any>(`${environment.API_BASE}/oper/busqueda?${tipoBusqueda}=${valor}&collName=${collname}`, body, { observe: 'response' })
    // return this.http.post<any>(`http://731-review-historial-kau9hg.dkla8s.funcionpublica.gob.mx/api/busqueda?${tipoBusqueda}=${valor}&collName=${collname}`, body, { observe: 'response' })
    // return this.http.post<any>(`https://dnet-oper-operback-staging.dkla8s.funcionpublica.gob.mx/api/busqueda?${tipoBusqueda}=${valor}&collName=${collname}`, body, { observe: 'response' })
    .pipe(map(registro => {
        console.log(registro);
        return registro;
    }));
  }

  recoveryTempPass(curp: string, idUsuario: number) {
    const usuario = {
      curp,
      idUsuario
    };
    return this.http.post<any>(`${environment.API_IDP_REGISTRO}/public/recovery-day-oper`, usuario, { observe: 'response' })
    .pipe(map(registro => {
        console.log(registro);
        return registro;
    }));

}
}
