# Validador del módulo de participación de empresas


	type ParticipacionEmpresasDTO {
		ninguno: boolean!
		participaciones: [ParticipacionEmpDTO]
		encabezado: EncabezadoDTO!
		aclaracionesObservaciones: String
	}
	
	type ParticipacionEmpDTO { 
		participante: EnumParticipante!
		nombreEmpresaSociedadAsociacion: PersonaMoralDTO!
		porcentajeParticipacion: Integer!
		tipoParticipacion: CatalogoDTO!
		sector: CatalogoDTO!
		recibeRemuneracion: boolean!
		montoMensual: MontoMonedaDTO!
		localizacion: LocalizacionDTO!
	}
	