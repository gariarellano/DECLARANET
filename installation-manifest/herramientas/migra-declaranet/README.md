Proyecto MIGRA DECLARANET
=========================

Esta rutina tiene como finalidad ayudarlo en el proceso de vincular la información del sistema DeclaraNet en su versión actual con la Nueva versión del sistema.

El codigo que se proporciona para facilitar la migración se encuentra desarrollado en Java, para su creación se empleó NetBeans, por lo que se recomienda usar dicha herramienta para su ejecución, sin embargo no es exclusiva ya que puede emplearse la que mejor considere.

Se sugiere que estos pasos se sigan en un principio sobre un ambiente de pruebas y una vez que compruebe su correcto funcionamiento, la ejecute sobre el ambiente de producción.

Para vincular la información de la base de datos ORACLE de DeclaraNet con la nueva base de datos, es importante realizar una migración de los acuses almacenados en la tabla "RECEPCION_WEB", para lo cual es importante seguir los siguientes pasos.

**1.-** Para el proceso de migración se asume que ya se encuentran creadas y pobladas las bases de datos de la nueva versión del sistema declaranet.

**2.-** En la base de datos ORACLE, ejecutar los siguientes script para crear las funciones **fn_etiquetajsonStr** y **fn_etiquetajsonNum**

````
create or replace function fn_etiquetajsonStr 
		(
		etiqueta in varchar2 
		, valor in varchar2 
		, coma in number
		) return varchar2 as 
		begin
		return '"'||TRIM(etiqueta)||'": '||(case when upper(valor) = 'NULL' then 'null' else '"'||TRIM(valor)||'"' end)||(case when coma=1 then ',' end);
		end fn_etiquetajsonStr; 

create or replace function fn_etiquetajsonNum 
		(
		etiqueta in varchar2 
		, valor in varchar2 
		, coma in number
		) return varchar2 as 
		begin
		return '"'||TRIM(etiqueta)||'": '||TRIM(NVL(valor,'null'))||(case when coma=1 then ',' end);
		end fn_etiquetajsonNum;
````

**3.-** En el archivo MigraRecepcionWeb.java cambiar los valores de las variables ahí señaladas por las correspondientes.
	
**Nota:** Es importante mencionar que los ID pueden variar entre ambientes de bases de datos (producción, desarrollo, calidad), por lo que debe revisar
	los valores correctos al momento de definirlo en las variables antes mencionadas.

**4.-** Una vez definidos los valores, ya podrá ejecutar el proyecto, con lo que se realizará una transferencia de los acuses de recibo de la tabla recepcion_web a
la nueva base de datos en MongoDb.

**5.-** Es importante mencionar que la base de datos ORACLE deberá quedar activa ya que las declaraciones registradas en dicha base de datos ahi seguirán resguardadas
y el nuevo sistema las consultará para mostrarlas al usuario sin necesidad de hacer una migración total.
