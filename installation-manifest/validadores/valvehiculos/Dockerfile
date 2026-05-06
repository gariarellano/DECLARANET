# Este Dockerfile usa "multi-stage builds" para aligerar drásticamente
# el tamaño de la imagen en ejcución, incluyendo Maven sólo para compilar
# Ver https://docs.docker.com/develop/develop-images/multistage-build/)

# Fase de construcción
FROM maven:3.6.0-jdk-8 AS build

# Copiar código fuente y manifiesto pom.xml
WORKDIR /code
COPY src ./src
COPY pom.xml .

ARG REPOSITORY_VALUE
ENV REPOSITORY_VALUE ${REPOSITORY_VALUE:-http://sfp-wfds8:8083/repository}

# Construir paquete
RUN mvn package -DskipTests;

# Fase de ejecución
FROM openjdk:8-alpine

ENV SERVER_PORT 5000
ENV JAR_FILE ValVehiculos-1.0-fat.jar

USER root
RUN echo "America/Mexico_City" > /etc/timezone

COPY --from=build /code/target/$JAR_FILE .

EXPOSE 5000

# Se usa "sh -c" para sortear esta limitante: https://github.com/docker/docker/issues/5509 - variable not expanded
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $JAR_FILE -id ValVehiculos --redirect -output"]

