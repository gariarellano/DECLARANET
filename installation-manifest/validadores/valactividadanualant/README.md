# Validador del módulo de actividad anual anterior del servidor público

	input ActividadAnualAnteriorDTO {
     	aclaracionesObservaciones: String
     	servidorPublicoAnioAnterior: Boolean!
     	actividadAnual: ActividadAnualDTO
     	encabezado: EncabezadoDTO!
	}
	
	type ActividadAnualDTO {
		fechaInicio: String!
		fechaConclusion: String!
		enajenacionBienes: [EnajenacionBienDTO]!
		tipoRemuneracion: EnumTipoRemuneracion!
		remuneracionNetaCargoPublico: MontoMonedaDTO!
		otrosIngresosTotal: MontoMonedaDTO!
		actividadIndustrialComercialEmpresarial: [ActividadIndustrialCEDTO]!
		actividadFinanciera: [ActividadFinancieraDTO]!
		serviciosProfesionales: [ServiciosProfesionalesDTO]!
		otrosIngresos: [OtrosIngresosDTO]!
		ingresoNetoDeclarante: RemuneracionDTO!
		ingresoNetoParejaDependiente: RemuneracionDTO!
		totalIngresosNetos: RemuneracionDTO!
	}
	
	type EnajenacionBienDTO {
		tipoBien: EnumTipoBien!
		remuneracion: MontoMonedaDTO!
	}
	
	type MontoMonedaDTO {
		moneda: CatalogoDTO!
		monto: Integer!
	}
	
	type ActividadIndustrialCEDTO {
		nombreRazonSocial: String!
		tipoNegocio: String!
		remuneracion: MontoMonedaDTO!
	}
	
	type ActividadFinancieraDTO {
		tipoInstrumento: CatalogoDTO!
		remuneracion: MontoMonedaDTO!
	}
	
	type ServiciosProfesionalesDTO {
		tipoServicio: String!
		remuneracion: MontoMonedaDTO!
	}
	
	type OtrosIngresosDTO {
		tipoIngreso:String!
		remuneracion: MontoMonedaDTO!
	}
	
	type RemuneracionDTO {
		remuneracion: MontoMonedaDTO!
	}
	