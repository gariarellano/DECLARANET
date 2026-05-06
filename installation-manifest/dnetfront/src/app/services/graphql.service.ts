import { Injectable } from '@angular/core';
import { Apollo } from 'apollo-angular';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { GuardaDeclaracion, ConsultaEntes2, GuardaAviso, GuardaNotas } from '../services/graphqlQuery/graphqlQuery';
import { sha3_512 } from 'js-sha3';
import { Recepcion, RecepcionAviso, RecepcionNota } from '../interfaces/recepcion';
// import { GuardaDeclaracionResponse } from '../interfaces/graqhplResponse';


@Injectable({
  providedIn: 'root'
})
export class GraphqlService {

  constructor(private apollo: Apollo) { }


  // guardaDeclaracion(): Observable<GuardaDeclaracionResponse> {
  guardaDeclaracion(recepcion: Recepcion): Observable<any> {
    console.log("recepcion ",recepcion);
    
    const d = {
      idUsuario: recepcion.encabezado.usuario.idUsuario,
      declaracion: recepcion
    };


    const digitoVerificador = sha3_512(JSON.stringify(d));

    const parametros = {
      declaracion: recepcion,
      digitoVerificador
    }
    return this.apollo.mutate({
      mutation: GuardaDeclaracion,
      variables: {
        parametros
      }
    }).pipe(map(result => result));
  }

  guardaNota(recepcionNota: RecepcionNota): Observable<any> {
    const not = {
      idUsuario: recepcionNota.encabezado.usuario.idUsuario,
      declaracion: recepcionNota
    };
    const digitoVerificador = sha3_512(JSON.stringify(not));
    
    const parametros = {
      declaracion: recepcionNota,
      digitoVerificador
    }
    return this.apollo.mutate({
      mutation: GuardaNotas,
      variables: {
        parametros
      }
    }).pipe(map(result => result));
  }
  
  guardaAviso(recepcionAviso: RecepcionAviso): Observable<any> {
    const d = {
      idUsuario: recepcionAviso.encabezado.usuario.idUsuario,
      declaracion: recepcionAviso
    };


    const digitoVerificador = sha3_512(JSON.stringify(d));
    //console.log(digitoVerificador);

    const parametros = {
      declaracion: recepcionAviso,
      digitoVerificador
    }
    return this.apollo.mutate({
      mutation: GuardaAviso,
      variables: {
        parametros
      }
    }).pipe(map(result => result));
  }

  consultaEntes(poder: string, nivelGobierno: string): Observable<any> {
    return this.apollo.use('entes').query<any>({ query: ConsultaEntes2, variables: { poder, nivelGobierno } })
      .pipe(map(result => result.data));
  }


}
