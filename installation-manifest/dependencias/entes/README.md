# Servicio Entes [![pipeline status](https://gitlab.funcionpublica.gob.mx/dgti/servicio-entes-vertx.got/badges/master/pipeline.svg)](https://gitlab.funcionpublica.gob.mx/dgti/servicio-entes-vertx.got/commits/master)
Proyecto con servicios para administrar el catálogo de entes y consultarlos.

## Para levantar el proyecto
```shell
docker build -t entes-vertx .
docker run -p 5000:5000 entes-vertx
# Recibe peticiones en http://localhost:5000
```

## Para levantar el proyecto en JRE preexistente
```shell
$ mvn package
$ java -jar target/servicio-entes-vertx-1.0-SNAPSHOT-fat.jar -conf src/main/conf/my-application-conf.json
# Recibe peticiones en http://localhost:5000
```

## Graphql
Además de que las funciones en el microservicio se encuentran en REST, se añaden esas mismas funciones en Graphql. Se agrega un cliente con el que se puede ver el schema y hacer pruebas. El cliente se puede encontrar en :

```
../api/graphql/demo/demo.html
```