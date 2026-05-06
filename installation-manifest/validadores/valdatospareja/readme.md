### Schema de GraphQL

``` GraphQL
####  I_DATOS_PAREJA -- datosParejas

"I Datos de pareja"
input DatosParejas {
	aclaracionesObservaciones: String
	ninguno:Boolean!
	verificar:Boolean
	datosParejas:[DatosPareja]
}


input DatosPareja {
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	datosPersonales:DatosPersonales
	relacionConDeclarante:EnumRelacionDeclarante
	ciudadadanoExtranjero:CiudadanoExtranjero
	esDependienteEconomico:Boolean
	habitaDomicilioDeclarante:Boolean
	domicilioDependienteEconomico:DomicilioDependienteEconomico
	ninguno:Boolean
	actividadLaboral:ActividadLaboralDependiente
}

enum EnumRelacionDeclarante {
	CONYUGE
	CONCUBINARIO
	SOCIEDAD_CONVIVENCIA
}
```