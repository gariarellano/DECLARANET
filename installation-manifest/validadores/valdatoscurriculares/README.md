# Estructura para el módulo de datos curriculares

	input DatosCurricularesDTO {
		aclaracionesObservaciones: String
		escolaridad: [DatoCurricularDTO]
		encabezado: EncabezadoDTO!
	}


	type DatoCurricularDTO {
		tipoOperacion: EnumTipoOperacion!		
		nivel: CatalogoDTO!
	    institucionEducativa: String! 
	    carreraAreaConocimiento: String!
	    estatus: EnumEstatusEscolaridad!
	    documentoObtenido: DocumentoObtenidoDTO!  
	    ubicacion: EnumUbicacion!
	    id: Integer
	    idPosicionVista: String
	}

	type DocumentoObtenidoDTO {
		tipo: EnumTipoDocumento!
		fechaObtencion: String!
	}
	
	type EncabezadoDTO {
		numeroDeclaracion: Integer
		tipoDeclaracion: EnumTipoDeclaracion!
		anio: Integer
		fechaEncargo: String
	}  