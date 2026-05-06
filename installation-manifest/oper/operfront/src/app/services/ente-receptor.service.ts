import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class EnteReceptorService {
  constructor(
     private http: HttpClient
    ) { }
  access(ente: any) {
    return this.http.get<any>(`${environment.API_BASE}/declaranet/ente-receptor/${ente ? ente : ''}`, { observe: 'response' })
    .pipe(map(registro => {
        // console.log(registro);
        return registro;
    }));
  }
 
}
