package mx.gob.sfp.dgti.migracion.helper;
//modulos no precargados
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_DOMICILIO_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_DATOS_EMPLEO_CARGO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_INGRESOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_SERVIDOR_PUB_ANIO_PASADO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_PRESTAMO_COMODATO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_PARTICIPACION_EN_EMPRESAS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_PARTICIPACION_EN_TOMA_DECISIONES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_APOYOS_BENEFICIOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_REPRESENTACIONES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_CLIENTES_PRINCIPALES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_BENEFICIOS_PRIVADOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MOD_FIDEICOMISOS;
//datos curriculares
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_CURRICULARES_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_DEPENDIENTES_ECONOMICOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATOS_PAREJAS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESCOLARIDAD;
import static mx.gob.sfp.dgti.util.Constantes.PROP_EXPERIENCIAS_LAB;
import static mx.gob.sfp.dgti.util.Constantes.PROP_EXPERIENCIA_LAB;
//inversiones
import static mx.gob.sfp.dgti.util.Constantes.PROP_ACLARACIONES_OBSERVACIONES;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ADEUDOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ADEUDOS_PASIVOS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INVERSION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INVERSIONES_CUENTAS_VALORES;
//bienesMuebles
import static mx.gob.sfp.dgti.util.Constantes.PROP_BIENES_MUEBLES;
//bienesInmuebles
import static mx.gob.sfp.dgti.util.Constantes.PROP_BIENES_INMUEBLES;
//vehiculos
import static mx.gob.sfp.dgti.util.Constantes.PROP_VEHICULOS;

//generales
import static mx.gob.sfp.dgti.util.Constantes.PROP_NINGUNO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_REGISTRO;
import static mx.gob.sfp.dgti.util.Constantes.ESTRUCTURA_DOMICILIO_NULL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DOMICILIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_UBICACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DOMICILIO_DEPENDIENTE_ECONOMICO;

import java.util.List;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class CrearModulosHelper {

	
	public static JsonObject crearJsonDatosGenerales(JsonObject data) {
		return data;
	}
	
	
	public static JsonObject crearJsonDatosCurriculares(List<JsonObject> data) {
		JsonObject datosCurriculares = new JsonObject(); 
		if(data.isEmpty()) {
			datosCurriculares.putNull(PROP_DATOS_CURRICULARES_DECLARANTE);
		}else {
			datosCurriculares = new JsonObject().put(PROP_DATOS_CURRICULARES_DECLARANTE, new JsonObject());
			datosCurriculares.getJsonObject(PROP_DATOS_CURRICULARES_DECLARANTE).putNull(PROP_ACLARACIONES_OBSERVACIONES);
			datosCurriculares.getJsonObject(PROP_DATOS_CURRICULARES_DECLARANTE).put(PROP_ESCOLARIDAD, new JsonArray());
			for (JsonObject registro : data) {
				datosCurriculares.getJsonObject(PROP_DATOS_CURRICULARES_DECLARANTE).getJsonArray(PROP_ESCOLARIDAD).add(Json.decodeValue(registro.getString(PROP_REGISTRO)));
			}
		}
		
		return datosCurriculares;
	}
	
	public static JsonObject crearJsonExperienciaLaboral(List<JsonObject> data) {
		JsonObject datosExperiencia = new JsonObject().put(PROP_EXPERIENCIAS_LAB, new JsonObject());
		datosExperiencia.getJsonObject(PROP_EXPERIENCIAS_LAB).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosExperiencia.getJsonObject(PROP_EXPERIENCIAS_LAB).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosExperiencia.getJsonObject(PROP_EXPERIENCIAS_LAB).putNull(PROP_EXPERIENCIA_LAB);
		}else {
			datosExperiencia.getJsonObject(PROP_EXPERIENCIAS_LAB).put(PROP_EXPERIENCIA_LAB, new JsonArray());
		}
		for (JsonObject registro : data) {
			datosExperiencia.getJsonObject(PROP_EXPERIENCIAS_LAB).getJsonArray(PROP_EXPERIENCIA_LAB).add(Json.decodeValue(registro.getString(PROP_REGISTRO)));
		}
		return datosExperiencia;
	}
	
	public static JsonObject crearJsonDatosParejas(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_DATOS_PAREJAS, new JsonObject());
		datosParejas.getJsonObject(PROP_DATOS_PAREJAS).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_DATOS_PAREJAS).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_DATOS_PAREJAS).putNull(PROP_DATOS_PAREJAS);
		}else {
			datosParejas.getJsonObject(PROP_DATOS_PAREJAS).put(PROP_DATOS_PAREJAS, new JsonArray());
			JsonObject aux = (JsonObject) Json.decodeValue(data.get(0).getString(PROP_REGISTRO));
			if(aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO) != null && aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO).getJsonObject(PROP_DOMICILIO) != null 
					&& aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO).getJsonObject(PROP_DOMICILIO).getString(PROP_UBICACION) == null) {
				aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO).mergeIn((JsonObject) Json.decodeValue(ESTRUCTURA_DOMICILIO_NULL));
			}
			datosParejas.getJsonObject(PROP_DATOS_PAREJAS).getJsonArray(PROP_DATOS_PAREJAS).add(aux);
		}
		
		
		return datosParejas;
	}
	
	public static JsonObject crearJsonDatosDependientesEconomicos(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_DATOS_DEPENDIENTES_ECONOMICOS, new JsonObject());
		datosParejas.getJsonObject(PROP_DATOS_DEPENDIENTES_ECONOMICOS).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_DATOS_DEPENDIENTES_ECONOMICOS).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_DATOS_DEPENDIENTES_ECONOMICOS).putNull(PROP_DATOS_DEPENDIENTES_ECONOMICOS);
		}else {
			datosParejas.getJsonObject(PROP_DATOS_DEPENDIENTES_ECONOMICOS).put(PROP_DATOS_DEPENDIENTES_ECONOMICOS, new JsonArray());
		}
		
		for (JsonObject registro : data) {
			JsonObject aux = (JsonObject) Json.decodeValue(registro.getString(PROP_REGISTRO));
			if(aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO) != null && aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO).getJsonObject(PROP_DOMICILIO) != null 
					&& aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO).getJsonObject(PROP_DOMICILIO).getString(PROP_UBICACION) == null) {
				aux.getJsonObject(PROP_DOMICILIO_DEPENDIENTE_ECONOMICO).mergeIn((JsonObject) Json.decodeValue(ESTRUCTURA_DOMICILIO_NULL));
			}
			datosParejas.getJsonObject(PROP_DATOS_DEPENDIENTES_ECONOMICOS).getJsonArray(PROP_DATOS_DEPENDIENTES_ECONOMICOS).add(aux);
		}
		return datosParejas;
	}
	
	public static JsonObject crearJsonBienesInmuebles(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_BIENES_INMUEBLES, new JsonObject());
		datosParejas.getJsonObject(PROP_BIENES_INMUEBLES).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_BIENES_INMUEBLES).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_BIENES_INMUEBLES).putNull(PROP_BIENES_INMUEBLES);
		}else {
			datosParejas.getJsonObject(PROP_BIENES_INMUEBLES).put(PROP_BIENES_INMUEBLES, new JsonArray());
		}
		
		for (JsonObject registro : data) {
			JsonObject aux = (JsonObject) Json.decodeValue(registro.getString(PROP_REGISTRO));
			if(aux.getJsonObject(PROP_DOMICILIO) != null && aux.getJsonObject(PROP_DOMICILIO).getString(PROP_UBICACION) == null) {
				aux.mergeIn((JsonObject) Json.decodeValue(ESTRUCTURA_DOMICILIO_NULL));
			}
			datosParejas.getJsonObject(PROP_BIENES_INMUEBLES).getJsonArray(PROP_BIENES_INMUEBLES).add(aux);
		}
		return datosParejas;
	}
	
	public static JsonObject crearJsonVehiculos(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_VEHICULOS, new JsonObject());
		datosParejas.getJsonObject(PROP_VEHICULOS).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_VEHICULOS).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_VEHICULOS).putNull(PROP_VEHICULOS);
		}else {
			datosParejas.getJsonObject(PROP_VEHICULOS).put(PROP_VEHICULOS, new JsonArray());
		}
		
		for (JsonObject registro : data) {
//			JsonObject aux = (JsonObject) Json.decodeValue(registro.getString(PROP_REGISTRO));
//			if(aux.getJsonObject(PROP_LUGAR_REGISTRO) != null && 
//					aux.getJsonObject(PROP_LUGAR_REGISTRO).getString(PROP_UBICACION) != null) {
//				aux.mergeIn((JsonObject) Json.decodeValue(ESTRUCTURA_LOCALIZACION_VEHICULO_NULL));
//			}
			datosParejas.getJsonObject(PROP_VEHICULOS).getJsonArray(PROP_VEHICULOS).add(Json.decodeValue(registro.getString(PROP_REGISTRO)));
		}
		return datosParejas;
	}
	
	public static JsonObject crearJsonAdeudosPasivos(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_ADEUDOS_PASIVOS, new JsonObject());
		datosParejas.getJsonObject(PROP_ADEUDOS_PASIVOS).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_ADEUDOS_PASIVOS).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_ADEUDOS_PASIVOS).putNull(PROP_ADEUDOS);
		}else {
			datosParejas.getJsonObject(PROP_ADEUDOS_PASIVOS).put(PROP_ADEUDOS, new JsonArray());	
		}
		
		for (JsonObject registro : data) {
			datosParejas.getJsonObject(PROP_ADEUDOS_PASIVOS).getJsonArray(PROP_ADEUDOS).add(Json.decodeValue(registro.getString(PROP_REGISTRO)));
		}
		return datosParejas;
	}
	
	
	public static JsonObject crearJsonInversiones(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_INVERSIONES_CUENTAS_VALORES, new JsonObject());
		datosParejas.getJsonObject(PROP_INVERSIONES_CUENTAS_VALORES).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_INVERSIONES_CUENTAS_VALORES).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_INVERSIONES_CUENTAS_VALORES).putNull(PROP_INVERSION);
		}else {
			datosParejas.getJsonObject(PROP_INVERSIONES_CUENTAS_VALORES).put(PROP_INVERSION, new JsonArray());
		}
		
		for (JsonObject registro : data) {
			datosParejas.getJsonObject(PROP_INVERSIONES_CUENTAS_VALORES).getJsonArray(PROP_INVERSION).add(Json.decodeValue(registro.getString(PROP_REGISTRO)));
		}
		return datosParejas;
	}
	
	public static JsonObject crearJsonBienesMuebles(List<JsonObject> data) {
		JsonObject datosParejas = new JsonObject().put(PROP_BIENES_MUEBLES, new JsonObject());
		datosParejas.getJsonObject(PROP_BIENES_MUEBLES).putNull(PROP_ACLARACIONES_OBSERVACIONES);
		datosParejas.getJsonObject(PROP_BIENES_MUEBLES).put(PROP_NINGUNO, data.isEmpty());
		if(data.isEmpty()) {
			datosParejas.getJsonObject(PROP_BIENES_MUEBLES).putNull(PROP_BIENES_MUEBLES);
		}else {
			datosParejas.getJsonObject(PROP_BIENES_MUEBLES).put(PROP_BIENES_MUEBLES, new JsonArray());
		}
		
		for (JsonObject registro : data) {
			datosParejas.getJsonObject(PROP_BIENES_MUEBLES).getJsonArray(PROP_BIENES_MUEBLES).add(Json.decodeValue(registro.getString(PROP_REGISTRO)));
		}
		return datosParejas;
	}
	
	
	
	
	public static JsonObject crearModulosNoPrecargados() {
		JsonObject modulosNoPrecarga = new JsonObject();
		modulosNoPrecarga.putNull(PROP_MOD_DATOS_GENERALES);
		modulosNoPrecarga.putNull(PROP_MOD_DOMICILIO_DECLARANTE);
		modulosNoPrecarga.putNull(PROP_MOD_DATOS_EMPLEO_CARGO);
		modulosNoPrecarga.putNull(PROP_MOD_INGRESOS);
		modulosNoPrecarga.putNull(PROP_MOD_SERVIDOR_PUB_ANIO_PASADO);
		modulosNoPrecarga.putNull(PROP_MOD_PRESTAMO_COMODATO);
		modulosNoPrecarga.putNull(PROP_MOD_PARTICIPACION_EN_EMPRESAS);
		modulosNoPrecarga.putNull(PROP_MOD_PARTICIPACION_EN_TOMA_DECISIONES);
		modulosNoPrecarga.putNull(PROP_MOD_APOYOS_BENEFICIOS);
		modulosNoPrecarga.putNull(PROP_MOD_REPRESENTACIONES);
		modulosNoPrecarga.putNull(PROP_MOD_CLIENTES_PRINCIPALES);
		modulosNoPrecarga.putNull(PROP_MOD_BENEFICIOS_PRIVADOS);
		modulosNoPrecarga.putNull(PROP_MOD_FIDEICOMISOS);
	return modulosNoPrecarga;
	}
	
}
