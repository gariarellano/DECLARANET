### Schema de GraphQL

``` GraphQL
####  I_DATOS_DEPENDIENTE -- datosDependientesEconomicos

"I Datos del Dependiente económico"
input DatosDependienteEconomicos {
	aclaracionesObservaciones: String
	ninguno:Boolean
	verificar:Boolean
	datosDependientesEconomicos:[DatosPareja]
}

input DatosDependienteEconomico {
	id:Int
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion
	datosPersonales:DatosPersonales
	parentescoRelacion:Catalogo
	ciudadadanoExtranjero:CiudadanoExtranjero
	esDependienteEconomico:Boolean
	habitaDomicilioDeclarante:Boolean
	domicilioDependienteEconomico:DomicilioDependienteEconomico
	ninguno:Boolean
	actividadLaboral:ActividadLaboralDependiente
}

```