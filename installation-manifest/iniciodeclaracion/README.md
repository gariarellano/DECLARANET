input Encabezado {
	numeroDeclaracion: String
	tipoDeclaracion:EnumTipoDeclaracion!
	anio: Int!
	fechaEncargo: String
	fechaRegistro: String!
	fechaActualizacion: String
	usuario: Usuario!
	tipoFormato:EnumTipoFormato!
	versionDeclaracion:Int!
	nivelJerarquico : CatalogoUnoFK!
}

input InstitucionReceptora {
	_id: String!
	nombre: String!
	ente: EnteReceptor!
	collName:Int
}

input CatalogoUnoFK {
	id: Int!
	valor: String!
	valorUno:String!
	fk: Int!
}

