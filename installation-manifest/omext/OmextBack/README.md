# Servicio `OmextBack`

Servicio que permite consultar servidores públicos haciendo la relación de cumplimiento de declaraciones patrimoniales por medio de `GraphQL` a una base de datos en `Elastic Search`.

### Variables de entorno
En el servio se configuran algunas varibles de entorno:
- `SERVER_PORT` indica el puerto en el que se levanta el servicio principal (el que recibe las consultas)
- `URL_ELASTIC_SEARCH` indica la URL donde se encuentra `Elastic Search`.

### Test local
Al levantar el servicio se crea un sitio donde se pueden probar las consultas con `graphiql`, para encontrarla:
- `la ruta definida en el SERVER_PORT` + `/graphiql`
