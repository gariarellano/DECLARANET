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
      if (!(request.url.includes(`${environment.API_ENTES}`) || request.url.includes(`${environment.API_CONSULTAR_ENTES}`) )) 
      {
        if (request.url.includes(`${environment.API_IDP_AUTH}`)) {
        //if (true) {
            const Auth = `${sessionStorage.getItem('authIdp')}`;
            // console.log(Auth);
            // const headers = request.headers
            //     .set( 'Authorization' , Auth);
            const headers = new HttpHeaders({
              'Authorization': Auth,
              'Content-Type': 'application/json',
              'dominio': window.location.protocol + '//' +  window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '')
            });
            // if (Auth !== 'null') {
            request = request.clone({ headers });
            // }
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
      }
        return next.handle(request);
    }
}



