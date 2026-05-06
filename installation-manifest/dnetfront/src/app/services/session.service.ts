import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, first } from 'rxjs/operators';
import { Session } from '../interfaces/session';
import { DatosPersonales } from '../interfaces/datosPersonales';
import { Idle } from '@ng-idle/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private currentUserSubject: BehaviorSubject<Session>;
  public currentUser: Observable<Session>;

  constructor(private http: HttpClient,
    private router: Router,
    // private bnIdle: BnNgIdleService,
    private idle: Idle) {

    this.currentUserSubject = new BehaviorSubject<Session>(JSON.parse(sessionStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): Session {
    return this.currentUserSubject.value;
  }

  login(token: string, datosPersonales: DatosPersonales) {
    // store user details and jwt token in local storage to keep user logged in between page refreshes
    const session1: Session = {
      auth: token,
      datosPersonales
    };
    sessionStorage.setItem('currentUser', JSON.stringify(session1));
    sessionStorage.setItem('auth', session1.auth);
    this.currentUserSubject.next(session1);
  }

  renovarToken() {
    const headersObject = new HttpHeaders();
    const Auth = `${sessionStorage.getItem('auth')}`;
    // tslint:disable-next-line: max-line-length
    // return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/crearDeclaracionTemporal`, JSON.stringify(dataSend), { observe: 'response' })
    // return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/crearDeclaracionTemporal`, JSON.stringify(dataSend), { observe: 'response' })
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_RENOVAR}`, JSON.stringify(Auth.replace('Bearer ', '')), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        if (data.status === 200) {
          const session1: Session = JSON.parse(sessionStorage.getItem('currentUser'));
          session1.auth = `Bearer ${data.body}`;
          sessionStorage.setItem('currentUser', JSON.stringify(session1));
          sessionStorage.setItem('auth', session1.auth);
          this.currentUserSubject.next(session1);
          this.idle.watch();
        }
        return data;
      }));
  }


  logout() {
    // remove user from local storage and set current user to null
    sessionStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    sessionStorage.clear();
    localStorage.clear();
    const data = {
      status: 200,
    };
    return data;
  }

  async getApiArgs(arg: string) {
    const obj = JSON.parse(arg);
    if (obj.API_BASE) {
      return obj;
    } else {
      return this.http.get<any>(`${obj}`, { observe: 'response' })
        .pipe(map(data => {
          console.log(data);
          if (data.status === 200) {

          }
          return data;
        }));
    }
  }

}
