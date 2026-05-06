# Estructura para el módulo de datos del empleo cargo o comisión


	input DatosEmpleoCargoComisionDTO {
		aclaracionesObservaciones : String!
		empleoCargoComision : [EmpleoCargoComisionDTO]! 
	}
	
	type EmpleoCargoComisionDTO {
		idPosicionVista :  String
		registroHistorico : Boolean
		tipoOperacion : EnumTipoOperacion!
		id : String
		ente : EnteReceptorDTO!
		areaAdscripcion : String!
		empleoCargoComision : String!
		nivelJerarquico : CatalogoDTO!
		nivelEmpleoCargoComision : String!
		contratadoPorHonorarios : Boolean!
		remuneracionNeta : MontoMoneda!
		tipoRemuneracion : EnumTipoRemuneracion!
		funcionPrincipal : String!
		fechaEncargo : String!
		telefonoOficina : TelefonoOficinaDTO!
		domicilio : Domicilio!
	}
	
	type EnteReceptorDTO{
		id : String!
		nombre : String!
		nivelOrdenGobierno : NivelGobierno!
		ambitoPublico : EnumAmbitopPublico!
	}
	
	type TelefonoOficinaDTO{
		numero : String
		extension : String
	}
	