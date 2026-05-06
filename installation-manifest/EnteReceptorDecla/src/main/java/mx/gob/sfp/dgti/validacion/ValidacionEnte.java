package mx.gob.sfp.dgti.validacion;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValCabeceraDecla;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

public class ValidacionEnte {

    private static final String FIELD_PREFIJO = "prefijo";
    private static final String FIELD_URL_CONS_DECLA = "urlConsDecla";
    private static final String FIELD_URL_PRECARGA = "urlPrecarga";
    private static final String FIELD_NOMBRE = "nombre";
    private static final String FIELD_IMG_B64 = "imagenB64";
    private static final String FIELD_IMG_INST_B64 = "imagenInstitucionalB64";
    private static final String FIELD_IMG_OFI_B64 = "imagenOficialB64";
    private static final String FIELD_FECHA_INCORPORACION = "fechaIncorporacion";
    private static final String FIELD_ACTIVO = "activo";
    private static final String FIELD_PATH = "path";
    private static final String FIELD_DATOS_CONTACTO = "datosContacto";
    private static final String FIELD_FECHA_FIRMA_COMVENIO = "fechaFirmaConvenio";
    private static final String FIELD_ENTE = "ente";
    private static final String FIELD_NOMBRE_COMPLETO = "nombreCompleto";
    private static final String FIELD_PUESTO = "puesto";
    private static final String FIELD_TELEFONO_OFICINA = "telefonoOficina";
    private static final String FIELD_TELEFONO_CELULAR = "telefonoCelular";
    private static final String FIELD_CORREO_ELECTRONICO = "correoElectronico";
    private static final String FIEL_TELEFONO_CELULAR_TELEFONO_OFICINA = "telefonoCelular-telefonoOficina";
    private static final String FIELD_COLLECTION_NAME = "collName";
    private static final String PREFIJOS_PERMITIDOS = "El o La o Los o Ella o Se";
    private static final Map<String, String> PREFIJOS = Stream.of(new String[][] {
    	  { "El", "El" }, { "La", "La" }, { "Los", "Los" }, { "Ella", "Ella" },{ "Se", "Se" }})
    		.collect(Collectors.toMap(data -> data[0], data -> data[1]));
    
   
    /**
	 * CONSTANTE URL CATALGOOS
	 */
	private static final String URL_CATALOGOS = "URL_CATALOGOS";
	
    /**
	 * URL para catalogos
	 */
	private static final String URL_CATALOGOS_ENV = System.getenv(URL_CATALOGOS);
	
	
	/**
	 * WebCLient
	 */
	private WebClient client;
	
	public ValidacionEnte(Vertx vertx) {
		client = WebClient.create(vertx);
	}
	
	public  ValidacionEnte realizaValidaciones(JsonObject jsonObject,
            Handler<AsyncResult<ModuloDTO>> resultHandler) {
        ModuloDTO modulo = new ModuloDTO("ALTA NUEVO ENTE RECEPTOR");
        new ExectValidations().ejecutaValidaciones(creaModuloValidar(jsonObject), modulo);
        resultHandler.handle(Future.succeededFuture(modulo));
        return this;
    }
	

    private ModuloValidarDTO creaModuloValidar(JsonObject jsonObject) {
        ModuloValidarDTO moduloValida = new ModuloValidarDTO("ALTA NUEVO ENTE RECEPTOR");
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_NOMBRE, jsonObject.getString(FIELD_NOMBRE)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(0)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(0)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_MAY_MIN, 5, 200));
        moduloValida.getListPropsTovalidate().add(
                new PropiedadesValidarDTO(FIELD_FECHA_INCORPORACION, jsonObject.getString(FIELD_FECHA_INCORPORACION)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(1)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_ACTIVO, jsonObject.getBoolean(FIELD_ACTIVO)));
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_PATH, jsonObject.getString(FIELD_PATH)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(3)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.CADENA_TEXTO_MINUSCULAS_GIONES));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(3)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYORQUE_MENORQUE, 1, 15));
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_IMG_B64, jsonObject.getString(FIELD_IMG_B64)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(4)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 50));
        moduloValida.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_FECHA_FIRMA_COMVENIO,
                jsonObject.getString(FIELD_FECHA_FIRMA_COMVENIO)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(5)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
        moduloValida.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_FECHA_FIRMA_COMVENIO,
                jsonObject.getString(FIELD_FECHA_FIRMA_COMVENIO)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(6)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_COLLECTION_NAME, jsonObject.getInteger(FIELD_COLLECTION_NAME)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(7)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYORQUE_MENORQUE, 99, 1000000));
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_IMG_INST_B64, jsonObject.getString(FIELD_IMG_INST_B64)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(8)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 50));
        moduloValida.getListPropsTovalidate()
                .add(new PropiedadesValidarDTO(FIELD_IMG_OFI_B64, jsonObject.getString(FIELD_IMG_OFI_B64)));
        ((PropiedadesValidarDTO) moduloValida.getListPropsTovalidate().get(9)).getListValToExec()
                .add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 50));
        moduloValida.getListPropsTovalidate().add(PropBase
                .crearModuloDebeSerNoNulo(jsonObject.getJsonObject(FIELD_DATOS_CONTACTO), FIELD_DATOS_CONTACTO));

        if (jsonObject.getJsonObject(FIELD_DATOS_CONTACTO) != null) {
            JsonObject datosContactos = jsonObject.getJsonObject(FIELD_DATOS_CONTACTO);
            ModuloValidarDTO moduloValidaHijo = new ModuloValidarDTO(FIELD_DATOS_CONTACTO);
            moduloValidaHijo.getListPropsTovalidate().add(
                    new PropiedadesValidarDTO(FIELD_NOMBRE_COMPLETO, datosContactos.getString(FIELD_NOMBRE_COMPLETO)));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(0)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 5));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(0)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_MAY_MIN));
            moduloValidaHijo.getListPropsTovalidate()
                    .add(new PropiedadesValidarDTO(FIELD_PUESTO, datosContactos.getString(FIELD_PUESTO)));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(1)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 5));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(1)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_MAY_MIN));
            moduloValidaHijo.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_TELEFONO_OFICINA,
                    datosContactos.getString(FIELD_TELEFONO_OFICINA), false));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(2)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.TELEFONO_CASA));
            moduloValidaHijo.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_TELEFONO_CELULAR,
                    datosContactos.getString(FIELD_TELEFONO_CELULAR), false));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(3)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.CELULAR_NACIONAL));
            moduloValidaHijo.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_CORREO_ELECTRONICO,
                    datosContactos.getString(FIELD_CORREO_ELECTRONICO), false));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(4)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.CORREO));
            moduloValidaHijo.getListPropsTovalidate()
                    .add(new PropiedadesValidarDTO(FIEL_TELEFONO_CELULAR_TELEFONO_OFICINA, ""));
            ((PropiedadesValidarDTO) moduloValidaHijo.getListPropsTovalidate().get(5)).getListValToExec()
                    .add(new ParametrosValicacionDTO(EnumValidacion.AL_MENOS_UN_OBJETO_NO_DEBE_SER_NULL,
                                    new Object[]{datosContactos.getString(FIELD_TELEFONO_CELULAR),
                                        datosContactos.getString(FIELD_TELEFONO_OFICINA)},
                                    new String[]{FIELD_TELEFONO_CELULAR, FIELD_TELEFONO_OFICINA}));
            moduloValida.getListModuloshijos().add(moduloValidaHijo);
        }

        moduloValida.getListPropsTovalidate()
                .add(PropBase.crearModuloDebeSerNoNulo(jsonObject.getJsonObject(FIELD_ENTE), FIELD_ENTE));
        if (jsonObject.getJsonObject(FIELD_ENTE) != null) {
            EnteReceptorDTO enteReceptor = Json.decodeValue(jsonObject.getJsonObject(FIELD_ENTE).toString(),
                    EnteReceptorDTO.class);
            ValCabeceraDecla.creaModuloValidacionEnteInstitucion(moduloValida, enteReceptor);
        }
        moduloValida.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_PREFIJO, jsonObject.getString(FIELD_PREFIJO)));
        if(!(jsonObject.getString(FIELD_PREFIJO) != null && PREFIJOS.containsValue(jsonObject.getString(FIELD_PREFIJO)))) {
        	moduloValida.getListPropsTovalidate().get(moduloValida.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, PREFIJOS_PERMITIDOS ));;
        }
        
        moduloValida.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_URL_CONS_DECLA,jsonObject.getString(FIELD_URL_CONS_DECLA), true));
        if(jsonObject.getString(FIELD_URL_CONS_DECLA) != null) {
        	moduloValida.getListPropsTovalidate().get(moduloValida.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_CONTIENE_URL));;
        }
        
        moduloValida.getListPropsTovalidate().add(new PropiedadesValidarDTO(FIELD_URL_PRECARGA,jsonObject.getString(FIELD_URL_PRECARGA), true));
        if(jsonObject.getString(FIELD_URL_PRECARGA) != null) {
        	moduloValida.getListPropsTovalidate().get(moduloValida.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_CONTIENE_URL));;
        }
        
        return moduloValida;
    }
}
