# Este Dockerfile usa "multi-stage builds" para aligerar drásticamente
# el tamaño de la imagen en ejcución, incluyendo Maven sólo para compilar
# Ver https://docs.docker.com/develop/develop-images/multistage-build/)

# Fase de construcción
FROM maven:3.6.0-jdk-8 AS build

# 
WORKDIR /code
COPY src ./src
COPY pom.xml .

ARG REPOSITORY_VALUE
ENV REPOSITORY_VALUE ${REPOSITORY_VALUE:-http://sfp-wfds8:8083/repository}

# Construir paquete
RUN mvn package;

# Fase de ejecución
FROM openjdk:8-alpine

ENV SERVER_PORT 5000
ENV JAR_FILE ConsultaDeclaracion-1.0-fat.jar

COPY --from=build /code/target/$JAR_FILE .

EXPOSE 5000

ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $JAR_FILE -id ConsultaDeclaracion --redirect -output"]
