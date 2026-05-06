# Estructura para el módulo préstamo o comodato


	input PrestamoComodatoDTO {
		ninguno : Boolean!
		aclaracionesObservaciones : String
		prestamo : [PrestamoDTO]
		encabezado : EncabezadoDTO!
	}
	
	type PrestamoDTO{
		vehiculo : VehiculoDTO
		duenoTitular : Persona
		idPosicionVista :  String
		registroHistorico : Boolean
		tipoOperacion : EnumTipoOperacion!
		id : String
		tipoBien : EnumTipoBien!
		inmueble : InmuebleDTO;
		relacionConTitular : CatalogoDTO
		relacionConTitularOtro : String
		
	}
	
	type InmuebleDTO{
		tipoInmueble : CatalogoDTO!
		domicilio : Domicilio
		tipoInmuebleOtro : String
		
	}
	
	type VehiculoDTO{
		tipoVehiculo:Catalogo!
		tipoVehiculoOtro : String
		marca:String
		modelo:String
		anio:Int
		numeroSerieRegistro:String
		lugarRegistro:Localizacion
		tipoVehiculoOtro : String
	}
	
	enum EnumTipoBienPrestamo{
		INMUEBLE
		VEHICULO
	} 