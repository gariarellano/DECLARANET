### Schema de GraphQL

``` GraphQL
####  I_BIENES_INMUEBLES - bienesInmuebles

"I Bienes inmuebles"
input BienesInmuebles {
	aclaracionesObservaciones: String
	ninguno:Boolean!
	verificar:Boolean
	bienesInmuebles:[BienInmueble]
}

input BienInmueble {
	id:Int!
	idPosicionVista:String!
	verificar:Boolean
	tipoOperacion:EnumTipoOperacion!
	tipoInmueble:Catalogo!
	titular:CatalogoUno!
	porcentajePropiedad:Int!
	superficieTerrenoM2:Int!
	superficieConstruccionM2:Int!
	terceros:[Persona]!
	transmisores:[Persona]!
	formaAdquisicion:Catalogo!
	formaPago:EnumFormaPago
	valorAdquision:MontoMoneda
	fechaAdquisicion:String!
	datoIdentificacion:String
	valorConformeA:String
	domicilio:Domicilio
	motivoBaja:Catalogo
}
 
```