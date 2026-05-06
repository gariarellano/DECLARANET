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

        if (request.url.includes(`${environment.apiUrl}`) || request.url.includes(`${environment.apiRegister}`)) {
            const Auth = `${sessionStorage.getItem('Auth')}`;
            // console.log(Auth);
            // const headers = request.headers
            //     .set( 'Authorization' , Auth);
            const headers = new HttpHeaders({
                'Authorization': Auth,
                'Content-Type': 'application/json',
                'dominio': window.location.protocol + '//' +  window.location.hostname + (window.location.port !== '' ? ':' + window.location.port : '')
              });
            request = request.clone({ headers });
        }
        return next.handle(request);
    }
}



