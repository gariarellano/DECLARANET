# Validador para el módulo de inversiones y cuentas bancarias
	
	
	type InversionCuentaVal {
		ninguno: boolean!
		inversion: [Inversion]
		aclaracionesObservaciones: String
		encabezado: Encabezado
	}
	
	type Inversion {
		tipoInversion: CatalogoDTO!
		subTipoInversion: CatalogoFkDTO! 
		titular: CatalogoUno!
		terceros: [Persona]
		numeroCuentaContrato: String!
		localizacionInversion: LocalizacionInversion!
		saldo: MontoMonedaDTO!
	}


	type CatalogoDTO {
		id: Integer!
		valor: String!
	}
	
	type CatalogoFkDTO {
		id: Integer!
		valor: String!
		fk: Integer!
	}
	
	type LocalizacionInversion {
		pais: CatalogoDTO!
		institucionRazonSocial: PersonaMoralDTO!
	}
	