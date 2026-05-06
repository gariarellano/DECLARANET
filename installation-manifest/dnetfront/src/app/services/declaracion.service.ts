import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpHeaders, HttpRequest, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { sha3_512 } from 'js-sha3';
import { Encabezado } from '../interfaces/encabezados';
import { InstitucionReceptora } from '../interfaces/institucionReceptora';
import { Registro } from '../interfaces/registro';
import { Declaracion } from '../interfaces/declaracion';

@Injectable({
  providedIn: 'root'
})
export class DeclaracionService {
  httpClient: any;

  constructor(private http: HttpClient) {

  }

  verificarDeclaracionesInicio(token: string, transact: string, institucionReceptorar: any) {
    const headersObject = new HttpHeaders();
    // sessionStorage.setItem('Auth', token);
    const declaracion = {
      institucionReceptora: institucionReceptorar,
      acces_token: token,
      transaction: transact
    };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion,
      digitoVerificador
    };
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/iniciar`, dataSend, { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }
  consultaAniosAntModificacion(encabezado: Encabezado, institucionReceptora: InstitucionReceptora) {
    const headersObject = new HttpHeaders();
    const declaracion = {
      encabezado,
      institucionReceptora
    };

    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/consultarAniosDeclaModExtemp`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log('aniosModExt', data);
        return data;
      }));
  }
  crearDeclaracion(encabezado: Encabezado, institucionReceptora: InstitucionReceptora, datosPersonales: any) {
    const headersObject = new HttpHeaders();
    const declaracion = {
      encabezado,
      institucionReceptora,
      datosPersonales
    };

    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/crearDeclaracionTemporal`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  crearNota(datosPersonales: any,registro: Registro, institucionReceptora: InstitucionReceptora,) {
    const headersObject = new HttpHeaders();
    const declaracion = {
      datosPersonales,
      registro,
      institucionReceptora,
    };

    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/crear-nota`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  previewDeclaracion(declaracion: any) {
    const headersObject = new HttpHeaders();
    // const declaracion = {
    //   numeroDeclaracion,
    //   idUsuario,
    //   colName
    // };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/api/consulta-declaracion`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  historialDeclaracion(institucionReceptorar: any, idUsuario: number) {
    const headersObject = new HttpHeaders();
    // sessionStorage.setItem('Auth', token);
    const declaracion = {
      institucionReceptora: institucionReceptorar,
      idUsuario

    };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion,
      digitoVerificador
    };
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/historial-declaraciones`, dataSend, { observe: 'response' })
      .pipe(map(data => {
        // console.log('datos del historial', data);
        return data;
      }));
  }


  historialUnaNota(datosPersonales: any,registro: Registro, institucionReceptora: InstitucionReceptora,) {
    const headersObject = new HttpHeaders();
    const declaracion = {
      datosPersonales,
      registro,
      institucionReceptora,
    };

    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/consultar-notas`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }




  viewAcuse(declaracion: any) {
    const headersObject = new HttpHeaders();
    // const declaracion = {
    //   numeroDeclaracion,
    //   idUsuario,
    //   colName
    // };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/api/consulta-acuse`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }

  eliminarDeclaracion(encabezado: Encabezado, institucionReceptora: InstitucionReceptora, datosPersonales: any) {
    const headersObject = new HttpHeaders();
    const declaracion = {
      encabezado,
      institucionReceptora,
      datosPersonales
    };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion
      ,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/eliminarDeclaracionTemporal`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }
  verificarDeclaracion(encabezado: Encabezado, institucionReceptora: InstitucionReceptora, datosPersonales: any) {
    const headersObject = new HttpHeaders();
    const declaracion = {
      encabezado,
      institucionReceptora,
      datosPersonales
    };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/inicioDeclaracion/api/verificar`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }
  iniciaFirmaDeclaracion(declaracionObj: Declaracion, idUsuario: number, numeroDeclaracion: string, collName: number) {
    const headersObject = new HttpHeaders();
    const d = {
      idUsuario,
      declaracion: declaracionObj
    };
    const shaDeclaracion = sha3_512(JSON.stringify(d));
    const declaracion = {
      numeroDeclaracion,
      idUsuario,
      collName,
      shaDeclaracion
    };
    const digitoVerificador = sha3_512(JSON.stringify(declaracion));
    const dataSend = {
      declaracion,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/recepcion-firma/inicia-firma`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }
  validaFirmaDeclaracion(declaracionObj: any) {
    const headersObject = new HttpHeaders();
    const digitoVerificador = sha3_512(JSON.stringify(declaracionObj));
    const dataSend = {
      declaracion: declaracionObj,
      digitoVerificador
    };
    // tslint:disable-next-line: max-line-length
    return this.http.post<any>(`${environment.API_BASE}/recepcion-firma/valida-firmado`, JSON.stringify(dataSend), { observe: 'response' })
      .pipe(map(data => {
        console.log(data);
        return data;
      }));
  }





}
