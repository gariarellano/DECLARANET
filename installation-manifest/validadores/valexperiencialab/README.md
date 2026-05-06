# Validador del módulo de experiencia laboral


	input ExperienciasLaboralesDTO {
     	aclaracionesObservaciones: String
     	ninguno: Boolean!
     	experienciaLaboral: [ExperienciaLaboralDTO]
     	encabezado: EncabezadoDTO!
	}
	
	input ExperienciaLaboralDTO {
		actividadLaboral: ActividadLaboralDTO
		id: Integer
		tipoOperacion: EnumTipoOperacion
	}
	
	input ActividadLaboralDTO {
		ambitoSector: CatalogoDTO!
		sectorPublico: SectorPublicoDTO
		sectorPrivadoOtro: SectorPrivadoDTO
		fechaIngreso: String!
		fechaEgreso: String!
		ubicacion: EnumUbicacion!
	}
	
	input SectorPublicoDTO {
		nivelOrdenGobierno: CatalogoDTO!
		ambitoPublico: CatalogoDTO!
		nombreEntePublico: String!
		areaAdscripcion: String!
		empleoCargoComision: String!
		funcionPrincipal: String!
	}
	
	input SectorPrivadoDTO {
		nombreEmpresaSociedadAsociacion: String!
		rfc: String!
		area: String!
		empleoCargo: String!
		sector: CatalogoDTO!
	}

