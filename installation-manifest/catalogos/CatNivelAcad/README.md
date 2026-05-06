# Servicios de catálogos

## Rutas
El servicio por cada catálogo está dividido en dos schemas de `GraphQL` : uno público y uno privado, y cada uno cuenta con una interfaz de `graphiql` para pruebas. Los paths son:

| Acceso   					|      GraphQL (post)   	 			|  graphiql (get) 			|
|---------------------------|:-----------------------------:|:---------------------:|
| Público					|  /publico/					|  /publico/graphiql 	|
| Privado					|  /privado/					|  /privado/graphiql 	|

Las rutas públicas solo permiten hacer queries, las rutas del privado permiten hacer modificaciones por lo que debería protegerse el acceso de algún modo.


## Campos en catálogos
La funcionalidad básica considera que los catálogos pueden variar en sus propiedades. Las propiedades que siempre deberán estar en un catálogo son:
- `id`
- `valor`

Además de campos de status y de fecha como:
- `activo`
- `fechaRegistro`

Pero hay campos que pueden estar o no en un catálogo de pendiendo de su estructura, estos serían:
- `valorUno`
- `fk`


## Funcionalidad `GraphQL`

### `queries` y `mutations`

Todas las funciones del servicio se llaman por medio de GraphQL. Las funciones se podrían clasificar para este servicio en `queries` y `mutations`. Las `mutations` solo pueden llamarse desde el path `/privado/` ya que modifican datos y este path estará protegido. Los `queries` se pueden llamar desde los 2 paths: `/publico/` y `/privado/` sin problema, los `queries` regularmente son consultas y no se modifica ningún dato.

### `queries`

Existen los siguientes:

- `obtenerCatalogo: [Catalogo]` : Función para obtener todos los registros del catálogo, se mostrarán activos e inactivos.

- `obtenerCatalogoActivo: [Catalogo]`: Función para obtener los registros activos del catálogo. <b>Utiliza cache</b>.

- `obtenerCatalogoActivoFk(fk: Int!): [Catalogo]`: Función para obtener los registros activos del catálogo filtrando obligatoriamente por fk. <b>Utiliza cache</b>.

- `obtenerCatalogoPorId(id: Int!): Catalogo`: Función para obtener un registro del catálogo por el identificador único. Debe de utilizarse para registros que solo tengan una llave. <b>Utiliza cache</b>.

- `obtenerCatalogoPorIdFk(fk: Int!id: Int!): Catalogo`: Función para obtener un registro del catálogo por el identificador único(`id`) y una llave adicional(`fk`). Debe de utilizarse para registros que tengan dos llaves.

- `validarInfoCatalogo(registro: CatalogoValidar!): Boolean`: Función para validar que la información de un registro aparezca en el catálogo. Siempre validará dos campos del `CatalogoValidar`, el `id` y el `valor`. Pero también puede validar dos campos adicionales si son mandados en el `query`: `valorUno` y la `fk`. Función ocupada especialmente por los validadores.

### `mutations`

Existen los siguientes:

- `agregarRegistro(registro: RegistroNuevo!): Catalogo`: Función para agregar un registro nuevo en catálogo, ingresando los campos de `id`, `valor` que son obligatorios y opcionalmente `fk` y `valorUno`. Los campos de `activo` y `fechaRegistro` son determinados en el sistema, quedando siempre activos cuando se crea por primera vez el registro.

- `actualizarRegistro(registro: RegistroModificado!): Catalogo`: Función para actualizar un registro del catálogo. Los campos que pueden modificarse son `valor`, `activo` y `valorUno`. Es necesario enviar como parámetro el `id` cuando es un catálogo de una sola llave. Para catálogos de dos llaves se manda `id` y `fk` para poder indicar correctamente el registro al que se hace referencia.
<b>Importante</b>: Tener cuidado de no mandar solo el `id` cuando se tienen dos llaves, ya que se podrían modificar más de un registro.

## Configuración de la base
Para definir los valores de la base, es necesario establecer los siguientes variables de entorno:

- `CAT_DNET_DATABASE_HOST`
- `CAT_DNET_DATABASE_PORT`
- `CAT_DNET_DATABASE_NAME`
- `CAT_DNET_DATABASE_USERNAME`
- `CAT_DNET_DATABASE_PSW`

Para definir la tabla donde se encuentra el catálogo hay que establecer el valor de la siguiente variable de entorno:

- `CAT_DNET_TABLE`

## Pruebas unitarias
Se ejecutan al contruir el projecto con `maven`. Se prueba que funcionen todos los queries de GraphQL, para estas consultas se hace uso de la base de desarrollo.

Para las mutations no se accede a la base, se hace mock sobre el cliente de la base utilizando Mockito y se genera un resultado genérico.

Todas las pruebas se hacen creando un cliente http y mandándole un `POST` con el query y se comprueba que el valor regresado sea correcto. 