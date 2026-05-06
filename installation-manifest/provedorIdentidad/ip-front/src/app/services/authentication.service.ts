import { Injectable } from '@angular/core';
import { HttpHeaders, HttpRequest, HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, first } from 'rxjs/operators';

import { User, UserLogin } from '../interfaces/user';
import { environment } from 'src/environments/environment';


@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private currentUserSubject: BehaviorSubject<UserLogin>;
    public currentUser: Observable<UserLogin>;
    private usr: User;
    constructor(private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): UserLogin {
        return this.currentUserSubject.value;
    }

    login(userpar: string, passwordpar: string, token: string, clienteId: string, secretId: string) {

        const headersObject = new HttpHeaders();
        sessionStorage.setItem('Auth', token);
        const data = this.asignaJsonAccess(userpar, passwordpar);
        return this.http.post<any>(`${environment.apiUrl}/private/login`, data, { observe: 'response' })
        .pipe(map(usuario => {
                console.log(usuario);
                const authserv = usuario;
                if (authserv.status === 200 && authserv.body.codigo !== 3 && authserv.body.codigo)  {
                    this.currentUserSubject.next({curp: userpar});
                }
                return usuario;
            }));
    }

    sync(token: string) {

        const headersObject = new HttpHeaders();

        sessionStorage.setItem('Auth', token);

        return this.http.get<any>(`${environment.apiUrl}/private/sync`, { observe: 'response' })
        .pipe(map(usuario => {
                console.log(usuario);
                return usuario;
            }));
    }

    getAuth(c: string, s: string) {
        // const ambiente = ;
        const headersObject = new HttpHeaders();
        // const headers = headersObject.append('dominio', ambiente);
        const headers = new HttpHeaders({'prueba': 'prueba'});
        return this.http.get<any>(`${environment.apiUrl}/public/authorize?cliente_id=${c}&secret_key=${s}&grant_type=authorization_code`, { observe: 'response' })
        .pipe(map(data => {
                return data;
            }));
    }

    createAuthorizationHeader(headers: HttpHeaders, token: string) {
        headers.append('Authorization', token);
    }

    recoveryPassMail(id: string) {
      const headersObject = new HttpHeaders();
      // sessionStorage.setItem('Auth', token);
      return this.http.get<any>(`${environment.apiRegister}/private/get-recovery?curp=${id}`, { observe: 'response' })
      .pipe(map(usuario => {
              console.log(usuario);
              return usuario;
          }));
  }

  recoveryPass(userpar: string, passwordpar: string, token: string) {

    const headersObject = new HttpHeaders();
    sessionStorage.setItem('Auth', token);
    const data = this.asignaJsonAccess(userpar, passwordpar);
    return this.http.post<any>(`${environment.apiRegister}/private/recovery`, data, { observe: 'response' })
    .pipe(map(usuario => {
            console.log(usuario);
            return usuario;
        }));
}

sendActivationMail(id: string) {
  const headersObject = new HttpHeaders();
  return this.http.get<any>(`${environment.apiUrl}/private/get-activate?curp=${id}`, { observe: 'response' })
  .pipe(map(usuario => {
          console.log(usuario);
          return usuario;
      }));
}

getUser(userpar: string, clienteId: string, secretId: string) {
  const headersObject = new HttpHeaders();
  return this.http.get<any>(`${environment.apiRegister}/private/user/${clienteId}?curp=${userpar}&secretkey=${secretId}`, { observe: 'response' })
  .pipe(map(usuario => {
          console.log(usuario);
          this.usr = usuario.body;
          return usuario;
      }));
}


generateOtp(token: string) {

    const headersObject = new HttpHeaders();
    sessionStorage.setItem('Auth', token);
    return this.http.get<any>(`${environment.apiUrl}/private/generate/otp/tele`, { observe: 'response' })
    .pipe(map(otp => {
            console.log(otp);
            return otp;
        }));
}

validateOtp(idUsuario, otp) {

    const headersObject = new HttpHeaders();
    return this.http.get<any>(`${environment.apiUrl}/private/validate/otp/tele/${idUsuario}/${otp}`, { observe: 'response' })
    .pipe(map(otpResponse => {
            console.log(otpResponse);
            return otpResponse;
        }));
}
eliminasincronizacion(token) {

    const headersObject = new HttpHeaders();
    sessionStorage.setItem('Auth', token);
    return this.http.post<any>(`${environment.apiUrl}/private/delete/otp/tele`, {}, { observe: 'response' })
    .pipe(map(otpResponse => {
            console.log(otpResponse);
            return otpResponse;
        }));
}
    private asignaJsonAccess(userpar: string, passwordpar: string) {
        return { 'curp': userpar, 'pwdEnc': passwordpar};
    }


    
logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }
}
