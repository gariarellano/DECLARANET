import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { sha3_512 } from 'js-sha3';

@Injectable({
  providedIn: 'root'
})
export class DeclaracionService {

  constructor(
    private http: HttpClient
  ) { }

  getDomicilio(declaracion: any, usuario: any, justificacion: string) {
    // const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    // const jsonAConsultar = {
    //   declaracion,
    //   digitoVerificador
    // };
    const dataSend = {
      usuario, 
      justificacion
    }

    return this.http.post<any>(`${environment.API_BASE}/oper/obtenerDomicilios?numeroDeclaracion=${declaracion.numeroDeclaracion}&collName=${declaracion.collName}&idUsuario=${declaracion.idUsuario}`, JSON.stringify(dataSend), { observe: 'response' })
    // return this.http.post<any>(`http://731-review-domicilios-e8kf0k.dkla8s.funcionpublica.gob.mx/api/obtenerDomicilios?numeroDeclaracion=${declaracion.numeroDeclaracion}&collName=${declaracion.collName}&idUsuario=${declaracion.idUsuario}`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  previewDeclaracion(declaracion: any, usuario: any, justificacion: string) {
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const jsonAConsultar = {
      declaracion,
      digitoVerificador
    };
    const dataSend = {
      usuario,
      justificacion,
      jsonAConsultar
    }

    return this.http.post<any>(`${environment.API_BASE}/oper/obtenerDeclaracion`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  previewAcuse(declaracion: any, usuario: any, justificacion: string) {
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const jsonAConsultar = {
      declaracion,
      digitoVerificador
    };
    const dataSend = {
      usuario,
      justificacion,
      jsonAConsultar
    }

    return this.http.post<any>(`${environment.API_BASE}/oper/obtenerAcuse`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }


  previewDeclaracionAsync(declaracion: any, usuario: any, justificacion: string) {
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const jsonAConsultar = {
      declaracion,
      digitoVerificador
    };
    const dataSend = {
      usuario,
      justificacion,
      jsonAConsultar
    }

    return this.http.post<any>(`${environment.API_BASE}/oper/obtenerDeclaracion`, JSON.stringify(dataSend), { observe: 'response' })
    .toPromise().then(data => {
        console.log(data);
        return data;
      });
  }

  previewAcuseAsync(declaracion: any, usuario: any, justificacion: string) {
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const jsonAConsultar = {
      declaracion,
      digitoVerificador
    };
    const dataSend = {
      usuario,
      justificacion,
      jsonAConsultar
    };

    return this.http.post<any>(`${environment.API_BASE}/oper/obtenerAcuse`, JSON.stringify(dataSend), { observe: 'response' })
    .toPromise().then(data => {
      console.log(data);
      return data;
    });
  }

}
