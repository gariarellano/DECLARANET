### Schema de GraphQL

``` GraphQL
####  II_REPRESENTACION -- representaciones

"II Representaciones"
input Representaciones {
	aclaracionesObservaciones: String
	verificar:Boolean
	ninguno:Boolean!
	representaciones:[Representacion]
}

input Representacion {
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	participante:EnumParticipante!
	tipoRepresentacion:EnumTipoRepresentacion!
	representanteRepresentado:Persona
	recibeRemuneracion:Boolean!
	montoMensual:MontoMoneda
	localizacion:Localizacion
	sector:Catalogo!
}

``` 