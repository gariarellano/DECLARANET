# Notas de Arquitectura y Migración a HTTPS

Este documento recopila todo el conocimiento adquirido sobre la arquitectura de la plataforma Declaranet y los desafíos encontrados durante el intento de migración para operar bajo un dominio con HTTPS (`declaranet.huetamo.com.mx`).

## 1. Arquitectura de la Plataforma
La plataforma está compuesta por más de 60 microservicios orquestados mediante Docker Compose. 

### Componentes Clave
- **Frontend Principal (Angular):** Sirve la aplicación en el puerto `5000`.
- **Proveedor de Identidad (IDP) Frontend:** Sirve la interfaz de login en el puerto `4000`.
- **DnetTool.js:** Un script inyectado en el frontend que maneja el flujo de redirección hacia el IDP para autenticación.
- **Backend de Identidad (`ip-back`, `ip-registro-back`):** Gestionan la autenticación, generación de JWTs y verificación de usuarios. Puerto interno `5000`, expuesto vía Kong en `5120`.
- **API Gateway (Kong):** Centraliza el acceso a las APIs en el puerto `5120`.
- **Base de Datos (PostgreSQL - `identidad`):** Almacena la configuración de las aplicaciones cliente en la tabla `ip_aplicacion`.

## 2. El Problema Original: Error 500 y "Error de comunicación"
Cuando se intentó acceder a la plataforma a través de HTTPS usando el dominio (via Nginx proxy):
1. El frontend cargaba correctamente.
2. Al intentar iniciar sesión, el script `DnetTool.js` (servido desde una IP HTTP) generaba un error de **Mixed Content** en el navegador, ya que una página HTTPS no puede cargar scripts desde orígenes HTTP inseguros.
3. Esto resultaba en el mensaje "Error de comunicación".

## 3. Intentos de Solución y Descubrimientos

### Intento 1: Cambiar todo a Dominio HTTPS
Se modificaron las variables de entorno (`.env`), la configuración del frontend (`config-dcla-sfp.html`), y la base de datos (`ip_aplicacion`) para usar el dominio. Se implementó un proxy inverso en Nginx para manejar SSL.

**Fallo:** El login fallaba con un `NullPointerException` (HTTP 500) en el backend Java (`AutenticacionHelper.java`).
**Causa:** El backend lee el header HTTP estándar `Origin`. Los navegadores envían este header automáticamente en peticiones XHR cross-origin (ej. de IP puerto 5000 a IP puerto 5120). Sin embargo, al configurar el proxy inverso Nginx bajo el *mismo dominio*, la petición se volvió "same-origin", por lo que el navegador **dejó de enviar el header Origin**. Al llegar al backend, el valor era `null`, provocando la excepción.

### Intento 2: Inyectar el Header Origin en Nginx
Se configuró Nginx para inyectar un header `Origin` por defecto cuando el navegador no lo enviaba.

**Fallo:** El IDP Angular app realiza peticiones POST a rutas internas como `/apibad/api/private/login`. El contenedor de frontend del IDP (`ip-front`) no tenía configurada esta ruta en su propio Nginx interno, resultando en errores HTTP 405 (Method Not Allowed). Se corrigió inyectando la configuración en el contenedor.

### Intento 3: Falla en la Comunicación entre Microservicios (El Bloqueo Final)
Tras resolver los problemas de Nginx y del IDP, el login funcionó (generó el JWT), pero la aplicación fallaba inmediatamente después con el error `badInicio` ("Se ha presentado un problema en el inicio de sesión").

**Causa Raíz:**
1. El microservicio `iniciodeclaracion` intentaba validar los datos del usuario contra el backend de registro (`ip-registro-back`).
2. En esta validación interna, envía el header `origins` con el valor de la variable de entorno `IP_DOMINIO_FRONT` (ej. `198.71.50.217:4000`).
3. El backend de registro (`ip-registro-back`) cruza este valor de origen contra la columna `dominio` de la tabla `ip_aplicacion` en la base de datos.
4. Como habíamos actualizado la base de datos para usar `declaranet.huetamo.com.mx`, pero los microservicios seguían usando la IP en sus variables de entorno, la validación fallaba ("No existe").

**Por qué no simplemente actualizar los microservicios?**
Hay más de 60 microservicios en el `docker-compose.yml`, todos acoplados rígidamente a configuraciones basadas en IPs. Recrear y reconfigurar absolutamente todos los contenedores para usar nombres de dominio y asegurar que la resolución DNS interna de Docker funcione perfectamente es una tarea masiva y propensa a errores.

## 4. La Solución Recomendada para el Futuro

Para habilitar HTTPS sin romper la frágil red de microservicios internos, se debe aplicar un enfoque "quirúrgico":

1. **Base de Datos (`ip_aplicacion`):**
   - Columna `dominio`: **NO TOCAR**. Debe mantenerse como la IP (`198.71.50.217`), ya que los microservicios validan contra este valor.
   - Columna `url`: **SÍ CAMBIAR** a HTTPS. Controla a dónde redirige el IDP después de un login exitoso.

2. **Variables de Entorno (`.env` y Microservicios):**
   - **NO TOCAR**. Toda la comunicación interna debe seguir operando mediante IPs o nombres de contenedores Docker.

3. **Configuración del Frontend (`config-dcla-sfp.html`):**
   - **SÍ CAMBIAR** para que el navegador del usuario apunte a los endpoints HTTPS de Kong y del IDP (evitando Mixed Content).

4. **Nginx (Proxy Inverso):**
   - Debe actuar como un traductor estricto de HTTPS (exterior) a HTTP (interior).
   - Debe **inyectar headers falsificados** para engañar al backend. Específicamente, cuando Nginx recibe una petición al dominio, debe establecer el header `Origin` a `http://198.71.50.217:5000` y el header `dominio` a `http://198.71.50.217:4000`. De esta forma, los backends Java seguirán viendo las IPs que esperan encontrar en la base de datos, aunque el usuario esté navegando por HTTPS.

5. **DnetTool.js:**
   - Requiere un pequeño parche para reconocer el nombre de dominio además de la IP al momento de interceptar la autenticación.

## Conclusión
El sistema fue diseñado con un alto acoplamiento hacia las direcciones IP y los headers HTTP de origen. La migración a dominio HTTPS es posible, pero requiere enmascarar el tráfico en la capa de Nginx para que los microservicios subyacentes sigan operando bajo la ilusión de que el sistema corre exclusivamente sobre IPs. Actualmente, el sistema ha sido revertido a su estado funcional basado en IP para garantizar su operatividad.
