# Validador del módulo de clientes principales

	type ClientesPrincipalesDTO {
		realizaActividadLucrativa: boolean!
		clientes: [ClienteDTO]
		encabezado: EncabezadoDTO!
		aclaracionesObservaciones: String
	}
	
	
	type ClienteDTO {
		participante: EnumParticipante!
		nombreEmpresaServicio: String!
		rfcEmpresa: String!
		clientePrincipal: PersonaDTO!
		sector: CatalogoDTO!
		montoAproximadoGanancia: MontoMonedaDTO!
		localizacion: LocalizacionDTO!
	} 
	