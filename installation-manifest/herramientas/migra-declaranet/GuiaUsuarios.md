GUIA DE MIGRACIÓN DE USUARIOS
=============================

Esta guía tiene como finalidad ayudarlo en la migración de las cuentas de usuario del sistema DeclaraNet
al nuevo servicio de identidad (PostgreSQL)

Se sugiere que estos pasos se sigan en un principio sobre un ambiente de pruebas y una vez que compruebe su correcto funcionamiento, la ejecute sobre
el ambiente de producción.

**1.-** Para esta migración se asume que ya se encuentra creada la base de datos para el servicio de identidad en PostgreSQL.

**2.-** En la base de datos ORACLE ejecutar el siguiente Query:

````
		SELECT 'INSERT INTO public.ip_usuario(	id_usuario, login, pwd_enc, nombre, primer_apellido, segundo_apellido,  curp, rfc, homocve, num_celular, email_alt, email, validado_renapo, fecha_registro, activo) 
		VALUES 
		('||id_usr_decnet||','''|| LOGIN ||''','''|| PWD_ENC ||''','''|| NOMBRE ||''','''|| PRIMER_APELLIDO ||''','''|| SEGUNDO_APELLIDO ||''','''|| CURP ||''','''|| RFC ||''','''|| HOMOCVE ||''','''|| NUM_CELULAR ||''','''|| EMAIL_ALT ||''','''|| EMAIL ||''',0,NOW(),1);' AS INS
		FROM USR_DECNET
		ORDER BY ID_USR_DECNET;
````
		
**3.-** La consulta ejecutada generará las instrucciones "Insert" para la transferencia de las cuentas de usuario al nuevo servicio.

**4.-** En la interfaz de la base de datos PostgreSQL, pegar los "Insert" generados de la consulta y ejecutarlos en la nueva base de datos.

**5.-** Una vez ejecutados los "Insert", deberá actualizar la secuencia que asigna el valor de id_usuario a los nuevos usuarios que se registren, de otra manera el sistema marcará un error, para esto es necesario ejecutar la consulta ````Select max(id_usuario) from id_usuario; ```` y con ese valor como referencia ejecutar el siguiente comando: ````ALTER SEQUENCE public.ip_usuario_id_usuario_seq RESTART WITH <nuevo_valor>; ```` donde <nuevo_valor> debe ser un valor mayor al resultante de la consulta anterior.

**6.-** Con estos pasos ya se podrán hacer las pruebas de login en el nuevo sistema DeclaraNet empleando la CURP y la misma contraseña con la que se ingresaba a la versión anterior del sistema.
