import { Injectable } from '@angular/core';
import { HttpHeaders, HttpRequest, HttpClient } from '@angular/common/http';


import { Usuario } from '../interfaces/usuario';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
    constructor(private http: HttpClient) { }



    register(usuario: Usuario) {
        return this.http.post<any>(`${environment.apiRegister}/private/user`, usuario, { observe: 'response' })
        .pipe(map(registro => {
            console.log(registro);
            return registro;
        }));
    }

    edit(usuario: User, status: string, id: string) {
      return this.http.put<any>(`${environment.apiRegister}/private/user?estatus=${status}&curp=${id}`, usuario, { observe: 'response' })
      .pipe(map(registro => {
          console.log(registro);
          return registro;
      }));
  }


    validateDuplicate(curp: string, rfc: string) {
      return this.http.get<any>(`${environment.apiRegister}/private/search?curp=${curp}&rfc=${rfc}`, { observe: 'response' })
      .pipe(map(registro => {
          console.log(registro);
          return registro;
      }));
    }

    validateDuplicateCurp(curp: string) {
      return this.http.get<any>(`${environment.apiRegister}/private/search?curp=${curp}`, { observe: 'response' })
      .pipe(map(registro => {
          console.log(registro);
          return registro;
      }));
  }

    activateCode(idConfirm: string, id: string) {

      const headersObject = new HttpHeaders();

      // sessionStorage.setItem('Auth', token);

      return this.http.get<any>(`${environment.apiUrl}/private/activate/${idConfirm}?curp=${id}`, { observe: 'response' })
      .pipe(map(usuario => {
              console.log(usuario);
              return usuario;
          }));
  }
  activateLink(token: string) {

    const headersObject = new HttpHeaders();

    sessionStorage.setItem('Auth', token);

    return this.http.get<any>(`${environment.apiUrl}/private/activate`, { observe: 'response' })
    .pipe(map(usuario => {
            console.log(usuario);
            return usuario;
        }));
}



}
