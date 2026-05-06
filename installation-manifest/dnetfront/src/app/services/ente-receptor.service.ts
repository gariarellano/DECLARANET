import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpHeaders, HttpRequest, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EnteReceptorService {
  constructor(
     private http: HttpClient
    ) { }
  access(ente: any) {
    return this.http.get<any>(`${environment.API_BASE}/ente-receptor/${ente ? ente : ''}`, { observe: 'response' })
    .pipe(map(registro => {
        // console.log(registro);
        return registro;
    }));
  }


  getInstituciones(ente: any) {
    const dataSend = {
      institucionReceptora : ente
    }
    return this.http.post<any>(`${environment.API_BASE}/consultar-ente`, JSON.stringify(dataSend), { observe: 'response' })
    .pipe(map(registro => {
        // console.log(registro);
        return registro;
    }));
  }

}
