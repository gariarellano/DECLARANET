# Stage compilaciones
# Se obtiene la referencia de la imagen de docker para maven compilaciones de las librerias
FROM maven:3.6.0-jdk-8 AS build
# Se realiza copeado de archivo de configuracion de repositorio para la imagen a crear
COPY settings.xml /usr/share/maven/ref/
# Se asigna el directorio a trabajar sobre la imagen.
WORKDIR /code
# Se copean los proyectos referenciados para la construcción.
COPY UtilsCatalogo ./UtilsCatalogo
COPY sfp-appiextension ./sfp-appiextension
COPY Utils-Validador-SFP ./Utils-Validador-SFP
COPY configuracionbasica ./configuracionbasica

# Se ejecuta compilaciones con el archivo de configuracion para armar el repositorio.
RUN cd UtilsCatalogo && mvn -s /usr/share/maven/ref/settings.xml clean install -DskipTests;
RUN cd sfp-appiextension && mvn -s /usr/share/maven/ref/settings.xml clean install -DskipTests;
RUN cd Utils-Validador-SFP && mvn -s /usr/share/maven/ref/settings.xml clean install -DskipTests;
RUN cd configuracionbasica && mvn -s /usr/share/maven/ref/settings.xml clean install -DskipTests;

COPY config-dcla-sfp.html ./repository/config-dcla-sfp.html
COPY oracle ./repository/oracle
COPY com ./repository/com

# Se sigue al Stage de exposicion como librerias http
# Se obtiene la referencia de la imagen de docker para el apache tomecat para exponer librerias.
FROM httpd:2.4.46-alpine
#Se realiza el copeado de las librerias generadas en el paso anterior a la imagen de apache tomecat.
COPY --from=build /code/repository /usr/local/apache2/htdocs/repository
# Se exponen las librerias internas en el server tomecat
EXPOSE 80