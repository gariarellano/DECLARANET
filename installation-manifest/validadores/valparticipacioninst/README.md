# Validador del módulo de participación en instituciones


	type ParticipacionInstitucionesDTO {
		ninguno: boolean!
		participaciones: [ParticipacionDTO]
		aclaracionesObservaciones: String
		encabezado: EncabezadoDTO!
	}
	
	
	type ParticipacionDTO {
		participante: EnumParticipante!
		tipoInstitucion: CatalogoDTO!
		institucion: PersonaMoralDTO!
		puestoRol: String!
		fechaInicioParticipacion: String!
		recibeRemuneracion: boolean!
		montoMensual: MontoMonedaDTO!
		localizacion: LocalizacionDTO!
	} 
	