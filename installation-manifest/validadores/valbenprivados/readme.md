### Schema de GraphQL

``` GraphQL
####  II_BENEFICIOS_PRIVADOS -- beneficiosPrivados

"II BeneficiosPrivados"
input BeneficiosPrivados {
	aclaracionesObservaciones: String
	ninguno:Boolean!
	representaciones:[BeneficioPrivado]
}

input BeneficioPrivado {
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	tipoBeneficio:Catalogo!
	beneficiario:Catalogo!
	otorgante:Persona!
	formaRecepcion:EnumFormaRecepcion!
	especifiqueBeneficio:String
	montoMensualAproximado:MontoMoneda!
	sector:Catalogo
}

```  