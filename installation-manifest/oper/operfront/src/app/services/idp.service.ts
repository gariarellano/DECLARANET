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
  // return this.http.get<any>(`http://ejzpriv-dnet-catalogosorigen-staging.k8s.funcionpublica.gob.mx/catalogos`, { observe: 'response' })
  //  return this.http.get<any>(`http://172.29.100.19:5000/catalogos`, { observe: 'response' })
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
getDataUser(usuario: any) {
  return this.http.get(`${environment.API_IDP_REGISTRO}/private/user/${usuario.user}?curp=${usuario.curp}&secretkey=${usuario.secretKey}`, { observe: 'response' })
    .pipe(map(res => {
      return res;
    }));
}

token(){
  return this.http.get<any>(`${environment.API_IDP_AUTH}/public/validate-response?secret_key=${environment.SECRET_KEY}&cliente_id=${environment.ID_CLIENTE}`,{ observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}
iniciarToken(userToken: any){
  // return this.http.post<any>(`${environment.API_BASE}/oper/iniciar`, userToken, { observe: 'response'})
  return this.http.post<any>(`${environment.API_INICIAR}`, userToken, { observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}
seleccionarPerfil(perfil: any) {
  // return this.http.post<any>(`${environment.API_BASE}/oper/iniciar`, userToken, { observe: 'response'})
  return this.http.post<any>(`${environment.API_BASE}/seguridad/seleccionado`, perfil, { observe: 'response'})
  // return this.http.post<any>(`https://dgti-ejz-generadortokenpermisos-staging.k8s.sfp.gob.mx/seleccionado`, perfil, { observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}
getUserbyId(usuario: any, collName: number) {
  return this.http.get<any>(`${environment.API_BASE}/seguridad/asignacion/datos/${collName}/${usuario.idUsuario}/${usuario.curp}`, { observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}
createUser(body: any, collName: number, curp: string){
  // return this.http.post<any>(`${environment.API_BASE}/oper/iniciar`, userToken, { observe: 'response'})
  return this.http.put<any>(`${environment.API_BASE}/seguridad/asignacion/${collName}/${environment.ID_CLIENTE}/${curp}`, body, { observe: 'response'})
  // body = {};
  // return this.http.put<any>(`https://451-review-login-py9mn2.k8s.sfp.gob.mx/asignacion/${collName}/${environment.ID_CLIENTE}/${curp}`, body, { observe: 'response'})
  // return this.http.put<any>(`https://451-review-login-py9mn2.k8s.sfp.gob.mx/asignacion/${collName}/${environment.ID_CLIENTE}/${curp}`, JSON.stringify(body), { observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}
updateUser(body: any, collName: number){
  // return this.http.post<any>(`${environment.API_BASE}/oper/iniciar`, userToken, { observe: 'response'})
  return this.http.patch<any>(`${environment.API_BASE}/seguridad/asignacion/${collName}/${environment.ID_CLIENTE}/x`, body, { observe: 'response'})
  // body = {};
  // return this.http.patch<any>(`https://451-review-login-py9mn2.k8s.sfp.gob.mx/asignacion/${collName}/${environment.ID_CLIENTE}/x`, body, { observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}

recuperaToken(){
  // return this.http.post<any>(`${environment.API_BASE}/oper/iniciar`, userToken, { observe: 'response'})
  return this.http.get<any>(`${environment.API_BASE}/seguridad/seleccionado`, { observe: 'response'})
  // return this.http.get<any>(`https://dgti-ejz-generadortokenpermisos-staging.k8s.sfp.gob.mx/seleccionado`, { observe: 'response'})
  .pipe(map(catalogo => {
    return catalogo;
}));
}


}
