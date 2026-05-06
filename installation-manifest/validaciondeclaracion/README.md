# Servicio `ValidarDeclaracion`

## `guardarDeclaracion`

El servicio de `ValidaDeclaracion` utiliza `GraphqlQL`, un ejemplo utilizando variables de `GraphQL` sería el siguiente:

```
mutation guardarDeclaracion($parametros: Parametros!) {
  guardarDeclaracion(parametros: $parametros){
    estado
    modulos{
      modulo
      errores{
        listErrorMensajes{
          errorId
          mensaje
          mensajeAlterno
        }
        nombreCampo
        propiedadValor
      }

    }
    declaracion
    token
  }
}
```
Aquí se encuentran las principales partes de la respuesta:

- `estado`: un Enum que indica el estatus de la respuesta. Puede tener alguno de los siguientes valores:
  - `CORRECTO` : Indica que se pudo guardar correctamente la declaración. Revisar `declaracion` que contiene toda la declaración como fue guardada y un `digitoVerificador`.
  - `ERROR_DIGITO` : Indica que no se pudo validar el dígito verificador.
  - `ERROR_COMUNICACION` : Indica que ocurrió un error de comunicación con los otros validadores. Checar los logs de cada servicio.
  - `ERROR` : Indica que ocurrió otro error.
  - `CON_OBSERVACIONES` : Indica que la información mandada no fue validada por los servicios validadores. En cuyo caso revisar en `modulos` que fue lo que pasó.
  - `MODULOS_INCORRECTOS` : Indica que hay modulos que no deberían de enviarse cuando es el formato simplificado.
  - `ERROR_GUARDADO` : Indica que ocurrió un error en el guardado.

- `modulos` : cuando se presenta el estado de `CON_OBSERVACIONES`, se regresa información en este apartado, indicando con un mensaje en qué módulo, campo y valor ocurrió la observación. No se podrá guardar hasta que ya no existan observaciones.

- `declaracion` : cuando se presenta el estado de `CORRECTO` indicándonos que se guardó la declaración este apartado contiene la información del `JSON` guardado. Dentro del `GraphQL` se ve que la propiedad es `String` pero en realidad es un objeto `JSON` con cualquier cantidad de información. Entonces este objeto contiene dos cosas:
  - `declaracion`: toda la información guardada.
  - `digitoVerificador`: el dígito verificador.
- `token` : este servicio debe tener un filtro de autorización en el que se utilice un token `JWT` por lo que debe ser mandado en este servicio. Consecuentemente se renovará el token recibido enviándolo en este apartado.

Este servicio recibe la declaración completa, realiza validaciones por módulo utilizando servicios individuales para cada uno.

## Configuración

### Servicios de validación de módulos

Es necesario agregar las rutas en las que se encuentran los servicios de validación, para esto se agregan como variables de entorno incluyendo el prefijo `RUTA_` seguido por `NOMBRE_DE_LA_VARIABLE` en el nombre de la variable, en el valor de la variable se agrega el `NOMBRE_MODULO_SERVICIO` separado por `|` y después la `RUTA_ABSOLUTA_SERVICIO`. El formato para las variables de entorno sería el siguiente:

`RUTA_<<NOMBRE_DE_LA_VARIABLE>>=<<NOMBRE_MODULO_SERVICIO>>|<<RUTA_ABSOLUTA_SERVICIO>>`

Por ejemplo:

`RUTA_DATOS_GENERALES=datosGenerales|http://172.29.68.100:5001/`

### Instancias de `RecepcionDeclaracion`

Se pueden definir las instancias por medio de variables de entorno, por defecto la aplicación levantará 2 instancias de cada verticle, esto puede ser modificado con las siguientes variables de entorno definiendo el número de instancias.

Para el verticle encargado de responder los llamados de http utilizar:

`VRTX_INSTANCIAS_HTTP`

Para el verticle que se encarga de llamar otros servicios utilizar

`VRTX_INSTANCIAS_LLAMADOR`

Se puede definir el número general de workers, por defecto se levantan 20, o se puede definir con la variable de entorno:

`VRTX_WKER_POOL`

## GraphiQL

Para visualizar la estructura de la declaración, queries y mutations de GraphQL se puede utilizar la interfaz gráfica de GraphiQL que hay en este servicio, que se encontraría en la ruta:

`/graphiql/`
