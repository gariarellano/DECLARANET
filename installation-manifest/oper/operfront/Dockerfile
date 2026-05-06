# Este Dockerfile define un 'multistage build' de dos
# fases para construir y servir un proyecto en Agular
# Basado en: https://malcoded.com/posts/angular-docker

# Primera fase (construir)

# Usar como base la imagen oficial de Node (v8)
FROM node:14 as build

#Asigna los valores del repositorio local para poder asignar las rutas al front dinamicamente.
ARG REPOSITORY_VALUE
ENV REPOSITORY_VALUE ${REPOSITORY_VALUE:-http://172.29.150.23:8083/repository}
ENV REPOSITORY_FRONT ${REPOSITORY_VALUE}/config-dcla-sfp.html

WORKDIR /app
RUN curl ${REPOSITORY_FRONT} --output ./rutas.json

COPY package*.json ./
RUN npm install

# Copiar el resto del código y construir proyecto
COPY . .

RUN npm run build -- --prod --c=production --output-path dist/app --aot

# Usar como base la imagen oficial de Nginx
FROM nginx:1.15-alpine

# Copiar configuración de servidor web
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY ./gzip_nginx.conf /etc/nginx/conf.d/gzip.conf

# Copiar distribuible a directorio configurado en nginx
COPY --from=build /app/dist/app /www

EXPOSE 5000
