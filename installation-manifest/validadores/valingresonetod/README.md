# Validador para el módulo de ingresos netos del declarante
	
	type IngresosModulosDTO {
		ingresos: IngresosNetosDeclaranteDTO!
		vehiculos: VehiculosDTO!
		muebles: BienesMueblesDTO!
		inmuebles: BienesInmueblesDTO!
	}

	type IngresosNetosDeclaranteDTO {
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
		encabezado: EncabezadoDTO!
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
	