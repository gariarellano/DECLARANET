# Generador de Tokens

## Funcionalidad

Este servicio tiene la funcionalidad de generar, actualizar y validar tokens`JWT`.

### Generar token `/generarToken`

Para generar un token se envía por medio de `POST` la información necesaria que es la siguiente:

- `usuario`: un identificador del usuario dentro del `JWT`, este campo es un <b>String</b> y es <b>obligatorio</b>.

- `iss`: Identifica el proveedor de identidad que emitió el `JWT`, este campo es un <b>String</b> y es <b>obligatorio</b>.

- `expMinutos` : número que indica el tiempo de expiración del `JWT`, debe de ser mayor a 0, se mantendrá como información dentro del payload del JWT con el nombre de `tiempo`, este campo es <b>int</b> y es <b>obligatorio</b>.

- `data` : información adicional añadida en el `JWT`, se puede agregar cualquier cosa dentro de esta variable y <b>no es obligatorio</b>.

Un ejemplo de JSON valido sería:
``` json
{
	"usuario":"pavel.martinez",
	"expMinutos":2,
	"iss":"declaranet",
	"data": {
		"cualquierCosa":5,
		"adentroDeCualquierCosa": {
			"otros":3
		}
	}
}
```

Que nos devolverá una respuesta en `String` como la siguente:

``` json
"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJkYXRhIjp7ImN1YWxxdWllckNvc2EiOjUsImFkZW50cm9EZUN1YWxxdWllckNvc2EiOnsib3Ryb3MiOjN9fSwidXN1YXJpbyI6InBhdmVsLm1hcnRpbmV6IiwidGllbXBvIjoyLCJpYXQiOjE1NzQ4ODE2ODUsImV4cCI6MTU3NDg4MTgwNSwiaXNzIjoiZGVjbGFyYW5ldCJ9.Jsa3cz0Ox4wubdW7ab_hb67QN6IUv6TlcWARaNix9stArSNySYF2QPvA6lgC6Z9Rfc1U4p7WakbJYJzoMWvf7-B8ARj4EusDI115RR3hKDzlEbU2qlgxKEM1SusxdKoOe9qhSRZF03csK9wzdp_DIWJG2jv9Rh11uVcyV0ZLjlgjq2B0E43aZ6fCVm8fyBnT9UFhSrTqFhsa1AIs6KG4Y_hWngi4jesSu7RLHhWM2TbQlhtE5FzUgEnrQn8rlxDbtMK5FIc_2j-H6djaIVQK_S6E6OLRWzPDdob_XG4KzWlzzKUKj_YGE8hWTDh2t0e1aYRNJHSwZJB4DObxD5SWjw"
```

### Renovar token `/renovarToken`

Función POST para renovar un token simplemente hay que mandar como `String` un token que no haya expirado que haya sido generado por el servivio de `generarToken`, al renovar el JWT se añadirá el tiempo indicado en `tiempo` y los mismos datos contenidos en el token recibido. Por lo que podemos enviar el JWT un String como el siguiente:

``` json
"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJkYXRhIjp7ImN1YWxxdWllckNvc2EiOjUsImFkZW50cm9EZUN1YWxxdWllckNvc2EiOnsib3Ryb3MiOjN9fSwidXN1YXJpbyI6InBhdmVsLm1hcnRpbmV6IiwidGllbXBvIjoyLCJpYXQiOjE1NzQ4ODE2ODUsImV4cCI6MTU3NDg4MTgwNSwiaXNzIjoiZGVjbGFyYW5ldCJ9.Jsa3cz0Ox4wubdW7ab_hb67QN6IUv6TlcWARaNix9stArSNySYF2QPvA6lgC6Z9Rfc1U4p7WakbJYJzoMWvf7-B8ARj4EusDI115RR3hKDzlEbU2qlgxKEM1SusxdKoOe9qhSRZF03csK9wzdp_DIWJG2jv9Rh11uVcyV0ZLjlgjq2B0E43aZ6fCVm8fyBnT9UFhSrTqFhsa1AIs6KG4Y_hWngi4jesSu7RLHhWM2TbQlhtE5FzUgEnrQn8rlxDbtMK5FIc_2j-H6djaIVQK_S6E6OLRWzPDdob_XG4KzWlzzKUKj_YGE8hWTDh2t0e1aYRNJHSwZJB4DObxD5SWjw"
```

Y obtendremos de vuelta un nuevo `JWT` renovado  como <b>String</b>:

``` json
"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJkYXRhIjp7ImN1YWxxdWllckNvc2EiOjUsImFkZW50cm9EZUN1YWxxdWllckNvc2EiOnsib3Ryb3MiOjN9fSwidXN1YXJpbyI6InBhdmVsLm1hcnRpbmV6IiwidGllbXBvIjoyLCJpYXQiOjE1NzQ4ODE2ODUsImV4cCI6MTU3NDg4MTg5MiwiaXNzIjoiZGVjbGFyYW5ldCJ9.knVqdvvIbJ8C9QS6LG05tw_FjPPbJsMhB4mmavow4iPbQB4OTC_OlqYT8hmOTJdba_sjnP1vjAvUHWcOswTHACfvFj4-Oqqb0rkwhrTrx8D6z0WQzdv9WVH0Jl49oLfalxvXc9sZNQvhUS2wFteyebDOICRQIAQgPEUzFYPYod69nQveFVxLf4KOZRs4pfivwN9gIkfRW293V5I5gkY8jz9NXXeBD6RLKZpwHP8nBmqxgvrrkrchwTWrQPEM1gz44lwct8q5oIuFnet7PKlWP2H_baP2seKiketbL_A6yuXw0nrRifNWKEBqt0Zv_gJqqeYkURz_dFwytVNRzwB23w"
```
## Configuración

### Configuración de este servicio (el generador de `JWT`)

Para que este servicio funcione correctamente es necesario agregar como variables de entorno la llave pública `LLAVE_PUBLICA` y la llave privada `LLAVE_PRIVADA`.

El  algoritmo para las llaves es: `RS256` y las llaves pueden ser generadas con varios métodos. Se recomienda que las llaves se agreguen sin saltos de línea para evitar tener que escaparlas.

<b>Importante</b>: Estas llaves deben de estar protegidas lo más posible, especialmente la llave privada que es la que puede firmar tokens.

### Integración con `Kong`

Si se planea utilizar este servicio generador de tokens con el `API Gateway` de `Kong`, es necesario:

- 1 - Crear un `consumer` de `Kong` y agregarle en `credentials` un `JWT`.

- 2 - Al crear el `JWT` en `credentials`  configurar la `rsa_public_key` con la misma llave pública definida en este servicio (generador de tokens) y el `key` con el `iss` (el que quedo dentro de nuestro token), seleccionar el algoritmo: `RS256` y guardar.

- 3 - Crear un `plugin` de `Kong` para nuestros servicio, en la configuración, para `key claim name` dejar `iss` y si queremos que `Kong` también verifique los tiempos de expiración es necesario agregar en el apartado de `claims to verify` el valor: `exp`, de lo contrario solo validará que el token sea correcto pero nunca validará si ya expiró, por lo que el token se vuelve eterno.

<b>Importante</b>: Se debe de configurar un `plugin` por cada servicio que pase por `Kong` para que se realice la verificación de tokens.

La forma en la que se puede enviar un token válido en una solicitud que pasa por `Kong` son:

- Un parametro query string
- Una cookie
- En el Authorization header

Más información sobre `Kong` + `JWT` en: https://docs.konghq.com/hub/kong-inc/jwt/ .
