### Schema de GraphQL

``` GraphQL
####  I_ADEUDOS_PASIVOS -- adeudosPasivos


"I Adeudos pasivos"
input AdeudosPasivos {
	aclaracionesObservaciones: String
	ninguno:Boolean!
	verificar:Boolean
	adeudos:[Adeudo]
}


input Adeudo {
	id:Int!
	verificar:Boolean
	idPosicionVista:String!
	tipoOperacion:EnumTipoOperacion!
	titular:CatalogoUno!
	tipoAdeudo:Catalogo!
	numeroCuentaContrato:String!
	fechaAdquisicion:String!
	montoOriginal:MontoMoneda!
	saldoInsoluto:Int!
	terceros:[Persona]
	otorganteCredito:Persona
	paisAdeudo:Catalogo
}

```