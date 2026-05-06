import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { sha3_512 } from 'js-sha3';

@Injectable({
  providedIn: 'root'
})
export class DeclaracionService {
  httpClient: any;

  constructor(private readonly http: HttpClient) {}


  busquedaServidores(nombre: string, collName: string) {
    return this.http.post<any>(`${environment.API_BASE}/consulta-servidores-publicos/buscarsp?busqueda=${nombre}&collName=${collName}`, { observe: 'response' })
     .pipe(map(catalogo => {
         return catalogo;
     }));
  }

  gabinete(tipo: number) {
    return this.http.post<any>(`${environment.API_BASE}/gabinete/consulta-gabinete?tipoGabinete=${tipo}`, { observe: 'response' })
    // return this.http.post<any>(`${environment.API_BASE}/gabinete/consulta-gabinete?tipoGabinete=0`, { observe: 'response' })
     .pipe(map(catalogo => {
         return catalogo;
     }));
  }

  previewDeclaracion(declaracion: any) {
    //const headersObject = new HttpHeaders();
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };

    return this.http.post<any>(`${environment.API_BASE}/consulta-servidores-publicos/consulta-declaracion`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }
  
  previewDeclaracionInai(dl: string) {

    // tslint:disable-next-line:max-line-length
    // return this.http.get<any>(`${environment.API_BASE}/consulta-servidores-publicos/consulta-declaracion?dI=K%3B%3B%3C.C%40%3B%3B%3B%3B%3D.%3F%3DC%3Flq%3Fnm%3Eq%3Dmqp%3Dp%3B%40Cpnp%40`, { observe: 'response' })
    return this.http.get<any>(`${environment.API_BASE}/consulta-servidores-publicos/consulta-declaracion?dI=${dl}`, { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  historialDeclaracion(idUsrDecnet: number, collName: string) {
    return this.http.post<any>(`${environment.API_BASE}/consulta-servidores-publicos/historico?idUsrDecnet=${idUsrDecnet}&collName=${collName}`, { observe: 'response' })
     .pipe(map(catalogo => {
         return catalogo;
     }));
  }
}
