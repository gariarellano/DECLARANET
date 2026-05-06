### Schema de GraphQL

``` GraphQL
####  I_BIENES_MUEBLES - bienesMuebles

"I Bienes muebles"
input BienesMuebles {
	aclaracionesObservaciones: String
	ninguno:Boolean!
	verificar:Boolean
	vehiculos:[BienMueble]
}

input BienMueble{
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	tipoBien:Catalogo!
	titular:CatalogoUno!
	descripcionGeneralBien:String!
	terceros:[Persona]!
	transmisores:[Persona]!
	formaAdquisicion:Catalogo!
	formaPago:EnumFormaPago
	valorAdquision:MontoMoneda
	fechaAdquisicion:String!
	lugarRegistro:Domicilio
	motivoBaja:Catalogo
}
```