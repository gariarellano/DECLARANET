# Este Dockerfile usa "multi-stage builds" para aligerar drásticamente
# el tamaño de la imagen en ejcución, incluyendo Maven sólo para compilar
# Ver https://docs.docker.com/develop/develop-images/multistage-build/)

# Fase de construcción
FROM maven:3.6.0-jdk-8 AS build


# Copiar código fuente y manifiesto pom.xml
WORKDIR /code
COPY src ./src
COPY pom.xml .

# Instalar agente Java de NewRelic
RUN curl -O "http://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip"
RUN unzip newrelic-java.zip

# Construir paquete
RUN mvn package -DskipTests;

# Fase de ejecución
FROM openjdk:8-alpine

ENV SERVER_PORT 5000
ENV JAR_FILE servicio-entes-vertx-1.0-SNAPSHOT-fat.jar

COPY --from=build /code/target/$JAR_FILE .
COPY --from=build /code/newrelic/newrelic.jar /usr/newrelic.jar

EXPOSE 5000

# Se usa "sh -c" para sortear esta limitante: https://github.com/docker/docker/issues/5509 - variable not expanded
ENTRYPOINT ["sh", "-c"]
CMD ["java -javaagent:/usr/newrelic.jar -jar $JAR_FILE"]
