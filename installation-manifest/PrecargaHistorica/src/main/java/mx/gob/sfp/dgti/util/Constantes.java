package mx.gob.sfp.dgti.util;

/**
 * Clase con las constantes generales para el proyecto
 * @author  programador04
 * @since 07/11/2019
 */
public class Constantes {

	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_CONTENT_APPLICATION_JSON = "application/json; charset=utf-8";
	public static final String TEXT_HTML = "text/html";
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
	public static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "authorization,content-type, dominio";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String PRAGMA = "Pragma";
	public static final String SERVER_PORT = "SERVER_PORT";
	public static final String CONFIG_PORT = "port";
	public static final String URL_PATH = "obtenerDeclaracion";
	public static final String PATH = "/";
	public static final String EMPTY = "";
	public static final String GUION = "-";
	public static final Integer CASADO = 3;
	
	//generales en json declaracion
	public static final String PROP_REGISTRO = "registro";
	public static final String PROP_INCOMPATIBLE = "INCOMPATIBLE";
	public static final Object OBJECT_NULL = null;
	public static final Boolean BOOLEAN_FALSE  = false;
	public static final Boolean BOOLEAN_TRUE  = true;
	public static final String PROP_SECTOR_PUBLICO = "sectorPublico";
	public static final String PROP_DECLARACION  = "declaracion";
	public static final String PROP_ID = "id";
	public static final String PROP_ID_POSICION_VISTA = "idPosicionVista";
	public static final String PROP_TIPO_OPERACION = "tipoOperacion";
	public static final String PROP_REGISTRO_HISTORICO = "registroHistorico";
	public static final String PROP_ACLARACIONES_OBSERVACIONES = "aclaracionesObservaciones";
	public static final String PROP_NINGUNO = "ninguno";
	public static final String PROP_VERIFICAR = "verificar";
	public static final String PROP_FECHA_INGRESO = "fechaIngreso";
	public static final String PROP_FECHA_EGRESO = "fechaEgreso";
	public static final String PROP_NIVEL_ORDEN_GOBIERNO  = "nivelOrdenGobierno";
	public static final String PROP_DOMICILIO  = "domicilio";
	public static final String PROP_UBICACION = "ubicacion";
	public static final String PROP_DOMICILIO_DEPENDIENTE_ECONOMICO = "domicilioDependienteEconomico";
	public static final String ESTRUCTURA_DOMICILIO_NULL  = "{ \"domicilio\": { \"ubicacion\": null, \"domicilioMexico\": { \"calle\": null, \"codigoPostal\": null, \"coloniaLocalidad\": null, \"entidadFederativa\": { \"id\": null, \"valor\": null }, \"municipioAlcaldia\": { \"id\": null, \"valor\": null, \"fk\": null }, \"numeroExterior\": null, \"numeroInterior\": null }, \"domicilioExtranjero\": { \"calle\": null, \"ciudadLocalidad\": null, \"codigoPostal\": null, \"estadoProvincia\": null, \"numeroExterior\": null, \"numeroInterior\": null, \"pais\": { \"id\": null, \"valor\": null } } } }";
	public static final String ESTRUCTURA_LOCALIZACION_VEHICULO_NULL  = "{\"lugarRegistro\": { \"ubicacion\": null, \"localizacionMexico\": { \"entidadFederativa\": { \"id\": null, \"valor\": null } }, \"localizacionExtranjero\": { \"pais\": { \"id\": null, \"valor\": null } } }}";
	
	
	//modulo experiencia laboral
	public static final String PROP_EXPERIENCIAS_LAB = "experienciasLaborales";
	public static final String PROP_EXPERIENCIA_LAB = "experienciaLaboral";
	public static final String PROP_AMBITO_SECTOR_OTRO = "ambitoSectorOtro";
	public static final String PROP_AMBITO_SECTOR = "ambitoSector";
	
	
	//modulo datos curriculares
	public static final String PROP_DATOS_CURRICULARES_DECLARANTE = "datosCurricularesDeclarante";
	public static final String PROP_ESCOLARIDAD = "escolaridad";
	
	//datos pareja
	public static final String PROP_DATOS_PAREJAS = "datosParejas";
	
	//dependientes economicos
	public static final String PROP_DATOS_DEPENDIENTES_ECONOMICOS = "datosDependientesEconomicos";
	
	//inversiones
	public static final String PROP_INVERSIONES_CUENTAS_VALORES = "inversionesCuentasValores";
	public static final String PROP_INVERSION = "inversion";
	
	//adeudos/pasivos
	public static final String PROP_ADEUDOS_PASIVOS = "adeudosPasivos";
	public static final String PROP_ADEUDOS = "adeudos";
	
	//bienes muebles
	public static final String PROP_BIENES_MUEBLES = "bienesMuebles";
	//bienes inmuebles
	public static final String PROP_BIENES_INMUEBLES = "bienesInmuebles";
	//bienes vehiculos
	public static final String PROP_VEHICULOS = "vehiculos";
	public static final String PROP_LUGAR_REGISTRO = "lugarRegistro";
	
	//modulos no precargados
	public static final String PROP_MOD_DATOS_GENERALES = "datosGenerales";
	public static final String PROP_MOD_DOMICILIO_DECLARANTE = "domicilioDeclarante";
	public static final String PROP_MOD_DATOS_EMPLEO_CARGO = "datosEmpleoCargoComision";
	public static final String PROP_MOD_INGRESOS = "ingresos";
	public static final String PROP_MOD_SERVIDOR_PUB_ANIO_PASADO = "actividadAnualAnterior";
	public static final String PROP_MOD_PRESTAMO_COMODATO = "prestamoComodato";
	public static final String PROP_MOD_PARTICIPACION_EN_EMPRESAS = "participaEmpresasSociedadesAsociaciones";
	public static final String PROP_MOD_PARTICIPACION_EN_TOMA_DECISIONES = "participacionTomaDecisiones";
	public static final String PROP_MOD_APOYOS_BENEFICIOS = "apoyos";
	public static final String PROP_MOD_REPRESENTACIONES = "representaciones";
	public static final String PROP_MOD_CLIENTES_PRINCIPALES = "clientesPrincipales";
	public static final String PROP_MOD_BENEFICIOS_PRIVADOS = "beneficiosPrivados";
	public static final String PROP_MOD_FIDEICOMISOS = "fideicomisos";
	
}
