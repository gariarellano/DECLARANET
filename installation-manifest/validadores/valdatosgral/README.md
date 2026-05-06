# Estructura para el módulo de datos generales

	input DatosGeneralesDTO {
		datosPersonales: DatosPersonalesDTO!
		correoElectronico: CorreoDTO!
     	telefonoCasa: TelefonoDTO!
     	telefonoCelular: TelefonoDTO!
     	situacionPersonalEstadoCivil: CatalogoDTO!
     	regimenMatrimonial: CatalogoDTO!
     	paisNacimiento: CatalogoDTO!
     	nacionalidad: CatalogoDTO!
     	aclaracionesObservaciones: String
     	encabezado: EncabezadoDTO!
	}
	
	
	type DatosPersonalesDTO {
		nombre: String!
     	primerApellido: String!
     	segundoApellido: String
		curp: String!
     	rfc: String!
    }

	type CorreoDTO {
		institucional: String
     	personalAlterno: String
	}

	type TelefonoDTO {
		numero: String!
		paisCelularPersonal: CatalogoDTO
	}
