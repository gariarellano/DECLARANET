import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, first } from 'rxjs/operators';
import { Session } from '../interfaces/session';
import { DatosPersonales } from '../interfaces/datosPersonales';
// import { Idle } from '@ng-idle/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Globals } from '../globals';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private currentUserSubject: BehaviorSubject<Session>;
  public currentUser: Observable<Session>;

  constructor(private http: HttpClient,
              private router: Router,
              private globals: Globals
              // private bnIdle: BnNgIdleService,
              // private idle: Idle
              ) {

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

  renovarToken(): Observable<boolean> {
    const headersObject = new HttpHeaders();
    const Auth = `${sessionStorage.getItem('AuthorizationOmext')}`;
    
    return this.http.post<any>(`${environment.API_RENOVAR}`, JSON.stringify(Auth.replace('Bearer ', '')), { observe: 'response' })
    .pipe(map(data => {
            if (data.status === 200) {
            // const session1: Session = JSON.parse(sessionStorage.getItem('currentUser'));
            // session1.auth = `Bearer ${data.body}`;
            // sessionStorage.setItem('currentUser', JSON.stringify(session1));
            sessionStorage.setItem('AuthorizationOmext', `Bearer ${data.body}`);
            // this.currentUserSubject.next(session1);
            // this.idle.watch();
            }
            return true;
        }, (error)=>{
          return false;
        }));
      }


  logout() {
      // remove user from local storage and set current user to null
      sessionStorage.removeItem('currentUser');
      this.currentUserSubject.next(null);
      sessionStorage.clear();
      localStorage.clear();
      this.globals.clean();
      const data = {
        status: 200,
      };
      return data;
  }
}
