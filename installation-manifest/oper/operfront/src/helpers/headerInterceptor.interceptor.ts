import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpHeaders,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';


@Injectable()
export class HeaderInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!request.url.includes(`${environment.API_BASE}/declaranet/`)) {
      if (request.url.includes(`${environment.API_IDP_AUTH}`)) {
        const Auth = JSON.parse(sessionStorage.getItem('authIdp'));
        const headers = new HttpHeaders({
          'Authorization': Auth.token,
          'TRANSACTION': Auth.transaction,
          'Content-Type': 'application/json',
          'dominio': window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '')
        });
        request = request.clone({ headers });
      } 
      // else if (request.url.includes(`${environment.API_REGISTER}/oper/user`) && request.method === 'PUT') {
      //   const Auth = sessionStorage.getItem('authUE');
      //   const headers = new HttpHeaders({
      //     'Authorization': Auth,
      //     'Content-Type': 'application/json'
      //   });
      //   request = request.clone({ headers });
      // } 
      else if (request.url.includes(`${environment.API_RENOVAR}`)) {
        const Auth = sessionStorage.getItem('AuthorizationOper');
        const headers = new HttpHeaders({
          'Authorization': Auth,
          'Content-Type': 'application/json',
        });
        request = request.clone({ headers });
      } else if (request.url.includes(`${environment.API_BASE}/seguridad`)) {
      // } else if (request.url.includes(`https://dgti-ejz-generadortokenpermisos-staging.k8s.sfp.gob.mx/seleccionado`)) {
        const Auth = sessionStorage.getItem('Authorization');
        const headers = new HttpHeaders({
          'Authorization': Auth,
          'Content-Type': 'application/json',
        });
        request = request.clone({ headers });
      } else {
        const Auth = `${sessionStorage.getItem('Authorization')}`;
        const headers = new HttpHeaders({
          'Authorization': Auth,
          'Content-Type': 'application/json',
          // 'dominio': window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '')
        });
        if (Auth !== 'null') {
          request = request.clone({ headers });
        }
      }
    }
    return next.handle(request);
  }
}



