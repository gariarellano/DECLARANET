import { Injectable } from '@angular/core';
import { HttpHeaders, HttpRequest, HttpClient } from '@angular/common/http';


import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root'})
export class UsuarioOmextService {
    constructor(private http: HttpClient) { }

    getUsuarioByField(field: string, valor: string) {
      return this.http.get<any>(`${environment.API_IDP_REGISTRO}/public/search/like-curp-rfc-name/${environment.ID_CLIENTE}/${environment.SECRET_KEY}/${valor}?${field}=x`, { observe: 'response' })
      .pipe(map(registro => {
          console.log(registro);
          return registro;
      }));

  }
  getPerfiles(collName: string) {
    // tslint:disable-next-line:max-line-length
    return this.http.post<any>(`${environment.API_KONG}/seguridad/perfil/${collName}/${environment.ID_CLIENTE}/ADMINISTRADOR`, {}, { observe: 'response' })
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


}
