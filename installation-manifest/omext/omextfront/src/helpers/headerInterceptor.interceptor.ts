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

    if (request.url.includes(`${environment.API_IDP_AUTH}`)) {
      //if (true) {
      const Auth = JSON.parse(sessionStorage.getItem('authIdp'));
      // console.log(Auth);
      // const headers = request.headers
      //     .set( 'Authorization' , Auth);
      const headers = new HttpHeaders({
        'Authorization': Auth.token,
        'TRANSACTION': Auth.transaction,
        'Content-Type': 'application/json',
        'dominio': window.location.protocol + '//' + window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '')
      });
      // if (Auth !== 'null') {
      request = request.clone({ headers });
      // }
      //} else if (request.url.includes(`${environment.API_RENOVAR}`)) {
    } else if (request.url.includes(`${environment.API_RENOVAR}`) || request.url.includes(`${environment.API_BASE}`) || request.url.includes(`${environment.API_NOLOCALIZADOS}`) || request.url.includes(`${environment.API_IDP_REGISTRO}`)) {
      //if (true) 
      // console.log(sessionStorage.getItem('AuthorizationOmext'));
      const Auth = sessionStorage.getItem('Authorization');
      // console.log(Auth);
      // const headers = request.headers
      //     .set( 'Authorization' , Auth);
      const headers = new HttpHeaders({
        'Authorization': Auth,
        'Content-Type': 'application/json',
      });
      // if (Auth !== 'null') {
      request = request.clone({ headers });
      // }
    } else if (request.url.includes(`${environment.API_KONG}/seguridad`)) {
      // } else if (request.url.includes(`https://dgti-ejz-generadortokenpermisos-staging.k8s.sfp.gob.mx/seleccionado`)) {
        const Auth = sessionStorage.getItem('Authorization');
        const headers = new HttpHeaders({
          'Authorization': Auth,
          'Content-Type': 'application/json',
        });
        request = request.clone({ headers });
      } else {
      const Auth = `${sessionStorage.getItem('auth')}`;
      // console.log(Auth);
      // const headers = request.headers
      //     .set( 'Authorization' , Auth);
      const headers = new HttpHeaders({
        'Authorization': Auth,
        'Content-Type': 'application/json'
      });
      if (Auth !== 'null') {
        request = request.clone({ headers });
      }
    }

    return next.handle(request);
  }
}



