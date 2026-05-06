# Este Dockerfile define un 'multistage build' de dos
# fases para construir y servir un proyecto en Agular
# Basado en: https://malcoded.com/posts/angular-docker

# Primera fase (construir)

# Usar como base la imagen oficial de Node (v8)
FROM node:12 as build
ARG REPLICAS=HOLA
RUN echo ${REPLICAS}
ARG STAGING_ENABLED="NO HAY"
RUN echo ${STAGING_ENABLED}


ARG JS_URL_PROD_INT
ENV JS_URL_PROD_INT ${JS_URL_PROD_INT:-dgti-ejz-ip-front.200.34.175}
ARG JS_URL_PROD_PUB
ENV JS_URL_PROD_PUB ${JS_URL_PROD_PUB:-identidad.apps.funcionpublica.gob.mx}
ARG JS_URL_PROD_PUB_PRES
ENV JS_URL_PROD_PUB_PRES ${JS_URL_PROD_PUB_PRES:-identidad-pres.declaranet.gob.mx}
ARG JS_URL_STAGING_INT
ENV JS_URL_STAGING_INT ${JS_URL_STAGING:-dgti-ejz-ip-front-staging.200.34.175}
ARG JS_URL_STAGING_PUB
ENV JS_URL_STAGING_PUB ${JS_URL_STAGING:-identidad-staging.k8s.sfp.gob.mx}
ARG JS_URL_REVIEW
ENV JS_URL_REVIEW ${JS_URL_REVIEW:-identidad-test.k8s.sfp.gob.mx}
ARG JS_API_PROD
ENV JS_API_PROD ${JS_API_PROD:-servicios.apps.funcionpublica.gob.mx/identidad/login/public/authorize}
ARG JS_API_PROD_PRES
ENV JS_API_PROD_PRES ${JS_API_PROD_PRES:-servicios-pres.declaranet.gob.mx/identidad/login/public/authorize}
ARG JS_API_STAGING
ENV JS_API_STAGING ${JS_API_STAGING:-api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/public/authorize}
ARG JS_API_REVIEW
ENV JS_API_REVIEW ${JS_API_REVIEW:-52-review-testing-wf02bm.k8s.sfp.gob.mx/api/public/authorize}

# Copiar package.json y package-lock.json e instalar dependencias
ARG REPOSITORY_VALUE
ENV REPOSITORY_VALUE ${REPOSITORY_VALUE:-http://sfp-wfds8:8083/repository}
ENV REPOSITORY_FRONT ${REPOSITORY_VALUE}/config-dcla-sfp.html

WORKDIR /app
RUN curl ${REPOSITORY_FRONT} --output ./rutas.json
COPY package*.json ./


RUN npm install

# Copiar el resto del código y construir proyecto
COPY . .
RUN sed -i 's/${JS_URL_PROD_INT}/'"${JS_URL_PROD_INT}"'/g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's/${JS_URL_PROD_PUB}/'"${JS_URL_PROD_PUB}"'/g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's/${JS_URL_PROD_PUB_PRES}/'"${JS_URL_PROD_PUB_PRES}"'/g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's/${JS_URL_STAGING_INT}/'"${JS_URL_STAGING_INT}"'/g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's/${JS_URL_STAGING_PUB}/'"${JS_URL_STAGING_PUB}"'/g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's/${JS_URL_REVIEW}/'"${JS_URL_REVIEW}"'/g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's~${JS_API_PROD}~'"${JS_API_PROD}"'~g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's~${JS_API_PROD_PRES}~'"${JS_API_PROD_PRES}"'~g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's~${JS_API_STAGING}~'"${JS_API_STAGING}"'~g' src/assets/plugin/DnetTool.docker.js
RUN sed -i 's~${JS_API_REVIEW}~'"${JS_API_REVIEW}"'~g' src/assets/plugin/DnetTool.docker.js

RUN sed -i 's/${JS_URL_PROD_INT}/'"${JS_URL_PROD_INT}"'/g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's/${JS_URL_PROD_PUB}/'"${JS_URL_PROD_PUB}"'/g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's/${JS_URL_PROD_PUB_PRES}/'"${JS_URL_PROD_PUB_PRES}"'/g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's/${JS_URL_STAGING_INT}/'"${JS_URL_STAGING_INT}"'/g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's/${JS_URL_STAGING_PUB}/'"${JS_URL_STAGING_PUB}"'/g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's/${JS_URL_REVIEW}/'"${JS_URL_REVIEW}"'/g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's~${JS_API_PROD}~'"${JS_API_PROD}"'~g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's~${JS_API_PROD_PRES}~'"${JS_API_PROD_PRES}"'~g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's~${JS_API_STAGING}~'"${JS_API_STAGING}"'~g' src/assets/plugin/DnetToolForm.docker.js
RUN sed -i 's~${JS_API_REVIEW}~'"${JS_API_REVIEW}"'~g' src/assets/plugin/DnetToolForm.docker.js


RUN npm link javascript-obfuscator
RUN javascript-obfuscator src/assets/plugin/DnetTool.docker.js --output src/assets/plugin/DnetTool.js
RUN javascript-obfuscator src/assets/plugin/DnetToolForm.docker.js --output src/assets/plugin/DnetToolForm.js

RUN npm run build -- --prod --c=production --output-path dist/app


# Segunda fase (servir)

# Usar como base la imagen oficial de Nginx
FROM nginx:1.15-alpine

# Copiar configuración de servidor web
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Copiar distribuible a directorio configurado en nginx
COPY --from=build /app/dist/app /www

EXPOSE 5000
