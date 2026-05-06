import { Injectable } from '@angular/core';

import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CatalogoService {

  constructor(
    private http: HttpClient
   ) { }
 getCatalogosAll() {
   return this.http.get<any>(`${environment.API_BASE}/catalogos/catalogos`, { observe: 'response' })
   .pipe(map(catalogo => {
       // console.log(registro);
       return catalogo;
   }));
 }
 getCatalogoMun() {
   return this.http.get<any>(`${environment.API_BASE}/catalogos/catalogo?cat=CAT_MUNICIPIO_ALCALDIA`, { observe: 'response' })
   .pipe(map(catalogo => {
       return catalogo;
   }));
 }
 getCatalogoEnte() {
   return this.http.get<any>(`${environment.API_BASE}/catalogos/catalogo?cat=CAT_ENTES`, { observe: 'response' })
   .pipe(map(catalogo => {
       return catalogo;
   }));
 }

}
