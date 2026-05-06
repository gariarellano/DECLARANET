### Schema de GraphQL

``` GraphQL
####  II_APOYOS_BENEFICIOS -- apoyos

"II Apoyos, beneficios"
input Apoyos {
	aclaracionesObservaciones: String
	ninguno:Boolean!
	verificar:Boolean
	apoyos:[Apoyo]
}


input Apoyo {
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	beneficiarioPrograma:Catalogo!
	nombrePrograma:String!
	institucionOtorgante:String!
	nivelOrdenGobierno:Catalogo!
	tipoApoyo:Catalogo!
	formaRecepcion:EnumFormaRecepcion
	montoApoyoMensual:MontoMoneda!
	especifiqueApoyo:String
}


enum EnumFormaRecepcion {
	MONETARIO
	ESPECIE
}

```