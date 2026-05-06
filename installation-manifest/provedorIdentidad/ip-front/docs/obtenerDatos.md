
# Obtener Datos

Como uso informativo y adicional se pone a disposición la siguiente liga en donde puede obtener datos personales del usuario,

```html
https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/user/{idAplicacion}?curp={curp}&secretkey={secretkey}
```

En donde:

A través del Método GET se le mandan los siguientes parámetros:

+ 	**Curp:**             Curp de la persona que se desea consultar

+ 	**Secretkey:**     Secret Key que se asignó en el registro

Los datos que se comparten son los siguientes:

+ 	Nombre completo 

+ 	Primer apellido

+ 	Segundo apellido

+ 	CURP

+ 	RFC 

+ 	homoclave 

+ 	email

Este paso es opcional para los sistemas que ya estén registrados al Servicio de Identidad Digital y requieran obtener información de las CURP que esten ligadas a su propio sistema.

Ejemplo:

```html
https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/user/1?curp=VARE880714HDFRMD10&secretkey=1234

```

![Ejemplo](https://gitlab.funcionpublica.gob.mx/dgti/ejz/ip-front/raw/docs/docs/img/datos2.PNG)

### Respuesta vacía
Mandará un mensaje de error en los siguientes casos:

1.- Que los datos que se estén intentando obtener de la curp no haya realizado un loggeo exitoso a su aplicativo.

2.- Que la curp no esté registrado en el IP.

3.-Que los parámetros mencionados anteriormente que hacen referencia al registro de tu sistema sean incorrectos.

Esto  regresará un: 

```
status HTTP -> 204 NO CONTENT 
```

## Angular

Para poder implementarlo en Angular lo que se tiene que realizar para obtener los datos mediante la liga es crear un servicio  en donde se define la ruta y se hace la petición.

Como el siguiente ejemplo:


```
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
 providedIn: 'root'
})
export class AutenticacionService {

 url = 'https://dgti-ejz-ip-registro-back-staging.200.34.175.120.nip.io/api/user/';
 usuario: any;

 constructor(private http: HttpClient) { }

 obtenerDatosCurp(curp: string) {
   // tslint:disable-next-line: no-string-literal
   const url = `${this.url}${environment.idAplication}?curp=${curp['curp']}&secretkey=${environment.secretkey}`;
   return this.http.get(url).pipe(map(res => res));
 }
}

```

El siguiente paso es obtener el observable que regresa el metódo obtenerDatosCurp.
En el data ya viene el objeto, pero hay que hacer el subscribe para obtener la información como se muestra a continuación en el método validaDatosEnSistemaIdentidadDigital:

```py

 validaDatosEnSistemaIdentidadDigital(){

    this.service.obtenerDatosCurp(this.forma.value).subscribe(data => {
        console.log(data);
     });
     }
```

Ejemplo en el proyecto de Administración de entes:

![Muestra Datos](https://gitlab.funcionpublica.gob.mx/dgti/ejz/ip-front/raw/docs/docs/img/datos3.PNG)
