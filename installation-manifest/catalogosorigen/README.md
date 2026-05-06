## ServicioCatalogos en nuevo repositorio.

Este es un servicio que comunica con todos los catálogos y regresa también los enums.


### Funciones  disponibles

Se cuenta con las siguientes funciones:
- `/catalogos` : método `GET` que trae todos los catálogos.
- `/catalogo` : método `GET` que obtiene un solo catálogo. Requiere que se le mande como parámetro el nombre del catálogo: `?cat=CAT_PAIS`.
- `/validar`: método `POST` que valida que la información mandada exista dentro del catálogo. Se manda como JSON el nombre del catálogo y la información a validar.