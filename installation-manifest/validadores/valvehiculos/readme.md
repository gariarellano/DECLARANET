### Schema de GraphQL 

``` GraphQL
####  I_VEHICULOS - vehiculos

"I Vehiculos"
input Vehiculos {
    verificar:Boolean
	aclaracionesObservaciones: String
	ninguno:Boolean!
	vehiculos:[Vehiculo]
}

input Vehiculo{
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	tipoVehiculo:Catalogo!
	marca:String
	modelo:String
	anio:Int
	numeroSerieRegistro:String
	titular:CatalogoUno!
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