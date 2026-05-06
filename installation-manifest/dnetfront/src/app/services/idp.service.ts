import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IdpService {

  constructor(
    private http: HttpClient
   ) { }
 Authorize() {
   // tslint:disable-next-line:max-line-length
   return this.http.get<any>(`${environment.API_IDP_AUTH}/public/authorize?cliente_id=${environment.ID_CLIENTE}&secret_key=${environment.SECRET_KEY}&grant_type=authorization_code`
   , { observe: 'response' })
   .pipe(map(data => {
       // console.log(registro);
       return data;
   }));
 }

 login(userpar: string, passwordpar: string) {

  const headersObject = new HttpHeaders();
  const data = { curp: userpar, pwdEnc: passwordpar};
  return this.http.post<any>(`${environment.API_IDP_AUTH}/private/login`, data, { observe: 'response' })
  .pipe(map(usuario => {
          console.log(usuario);
          const authserv = usuario;
          return usuario;
      }));
}


}
