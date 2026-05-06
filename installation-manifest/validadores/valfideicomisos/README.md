# Validador del módulo de fideicomisos
	

	type ParticipacionFideicomisoDTO {
		ninguno: boolean!
		fideicomisos: [FideicomisoDTO]
		aclaracionesObservaciones: String
		encabezado: Encabezado!
	}
	
	
	type FideicomisoDTO {
		tipoOperacion: EnumTipoOperacion!
		participante: EnumParticipanteFideicomiso!
		tipoFideicomiso: EnumTipoFideicomiso!
		tipoParticipacion: EnumTipoParticipacionF!
		rfcFideicomiso: String!
		fideicomitente: PersonaDTO!
		fiduciario: PersonaMoralDTO!
		fideicomisario: PersonaDTO!
		sector: CatalogoDTO!
		localizacion: EnumUbicacion!
	}
	
	type PersonaDTO {
		tipoPersona: EnumTipoPersona!
		personaFisica: PersonaFisicaDTO
		personaMoral: PersonaMoralDTO
	}
	
	type PersonaMoralDTO {
		rfc: String!
		nombre: String!
	}
	
	type PersonaFisicaDTO {
		nombre: String!
		primerApellido: String!
		segundoApellido: String
		rfc: String!
	}