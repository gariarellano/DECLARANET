/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.util.HashMap;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mx.gob.sfp.dgti.dao.interfaces.DAOEnteReceptorInterface;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoPoder;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelGobierno;
import mx.gob.sfp.dgti.service.interfaces.ServiceEnteReceptorInerface;
import static mx.gob.sfp.dgti.util.Constantes.AMBITO_PUB;
import static mx.gob.sfp.dgti.util.Constantes.CONSULTA_ENTES;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.ENTE;
import static mx.gob.sfp.dgti.util.Constantes.ENTIDAD_FED;
import static mx.gob.sfp.dgti.util.Constantes.ID;
import static mx.gob.sfp.dgti.util.Constantes.ID_CONSULTA;
import static mx.gob.sfp.dgti.util.Constantes.ID_ENTIDAD_FEDERATIVA;
import static mx.gob.sfp.dgti.util.Constantes.ID_MUNICIPIO;
import static mx.gob.sfp.dgti.util.Constantes.INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.MUNICIPIO;
import static mx.gob.sfp.dgti.util.Constantes.NIVEL_ORDEN_GOB;
import static mx.gob.sfp.dgti.util.Constantes.NOMBRE_URL_CATALOGOS;
import static mx.gob.sfp.dgti.util.Constantes.URL_ENTES;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValCabeceraDecla;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase que contien la implementaion de los metodos de logica de negocio de
 * entesd receptor de informacion.
 *
 * @author cgarias
 * @since 06/11/2019
 */
public class ServiceEnteReceptor implements ServiceEnteReceptorInerface {

    private static final Logger logger = Logger.getLogger(ServiceEnteReceptor.class.getName());
    private static final String URL_SERVICE_ENTES = System.getenv(URL_ENTES) != null ? System.getenv(URL_ENTES) : EMPTY;
    private static final String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS) != null ? System.getenv(NOMBRE_URL_CATALOGOS) : EMPTY;
    private static final String FRONT_INFO = "frontInfo";
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
    private static final String FIELD_INSTITUCION_RECEPTORA = "institucionReceptora";
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
    private final DAOEnteReceptorInterface daoEnteReceptorInterface;
    private static final Map<String, JsonObject> entesReceptoraes = new HashMap<>();
    private final WebClient webClient;
     
    public ServiceEnteReceptor(Vertx vertx, WebClient webClient) {
        daoEnteReceptorInterface = DAOEnteReceptorInterface.createProxy(vertx, DAOEnteReceptorInterface.SERVICE_ADDRESS);
        this.webClient = webClient;
    }

    @Override
    public Future<JsonObject> consultaEnteReceptorDeclaracion(String path) {
        Future<JsonObject> fu;
        if (entesReceptoraes.containsKey(path)){
            fu = Future.future(r->{
                r.handle(Future.succeededFuture(entesReceptoraes.get(path)));
            });
        }else{
            fu = Future.future(f->{
                daoEnteReceptorInterface.consultaEnteReceptor(path, resultHandler -> {
                    if (resultHandler.succeeded()) {
                        JsonObject objetoResp = new JsonObject();
                        JsonObject enteReceptor = resultHandler.result();

                        objetoResp.put(FRONT_INFO, new JsonObject().put(FIELD_NOMBRE, enteReceptor.getString(FIELD_NOMBRE)).put("parametrosEspecificos", enteReceptor.getValue("parametrosEspecificos"))
                                .put(FIELD_PREFIJO, enteReceptor.getString(FIELD_PREFIJO))
                                        .put(FIELD_IMG_B64, enteReceptor.getString(FIELD_IMG_B64)));
                        enteReceptor.remove(FIELD_IMG_B64);
                        enteReceptor.remove(FIELD_PREFIJO);
                        enteReceptor.remove("parametrosEspecificos");

                        objetoResp.put(FIELD_INSTITUCION_RECEPTORA, enteReceptor);
                        
                        entesReceptoraes.put(path, objetoResp);
                        f.handle(Future.succeededFuture(objetoResp));
                    } else {
                        f.fail(resultHandler.cause());
                    }
                });
            });
        }
        return fu;
    }
    
    @Override
    public Future<JsonObject> consultaEnteReceptorporEnteId(String idEnte){
        Future<JsonObject> f = Future.future();
        daoEnteReceptorInterface.consultaEnteReceptorporEnteId(idEnte, resultHandler -> {
            if (resultHandler.succeeded()) {
                f.handle(Future.succeededFuture(resultHandler.result()));
            } else {
                f.fail(resultHandler.cause());
            }
        });
        return f;
    }
    
    @Override
    public Future<JsonObject> consultaEnteReceptorDeclaracionPorCollName(Integer collName) {

        Future<JsonObject> f = Future.future();
        daoEnteReceptorInterface.consultaEnteReceptorPorCollName(collName, resultHandler -> {
            if (resultHandler.succeeded()) {
                JsonObject enteReceptor = resultHandler.result();
                JsonObject objetoResp = new JsonObject().put(FRONT_INFO, new JsonObject().put(FIELD_IMG_INST_B64, enteReceptor.getString(FIELD_IMG_INST_B64))
                        .put(FIELD_IMG_OFI_B64, enteReceptor.getString(FIELD_IMG_OFI_B64))
                        .put(FIELD_INSTITUCION_RECEPTORA, enteReceptor.getJsonObject(FIELD_ENTE))
                        .put(FIELD_PREFIJO, enteReceptor.getString(FIELD_PREFIJO)));
                f.handle(Future.succeededFuture(objetoResp));
            } else {
                f.fail(resultHandler.cause());
            }
        });
        return f;
    }

    @Override
    public Future<JsonObject> agregaEnteReceptorDeclaracion(JsonObject enteReceptor) {
        Future<JsonObject> f = Future.future();
        try {
            this.realizaValidaciones(enteReceptor, resultHandler -> {
                if (resultHandler.succeeded()) {
                    if (resultHandler.result().getErrores().isEmpty()) {
                        daoEnteReceptorInterface.agregaEnteReceptorDeclaracion(enteReceptor, resultGuarado -> {
                            if (resultGuarado.succeeded()) {
                                f.handle(resultGuarado);
                            } else {
                                f.fail(resultGuarado.cause());
                            }
                        });
                    } else {
                        f.handle(Future.succeededFuture(new JsonObject(Json.encode(resultHandler.result()))));
                    }
                } else {
                    f.fail(resultHandler.cause());
                }
            });
        } catch (Exception e) {
            logger.severe(String.format("%s", e));
        }
        return f;

    }

    private ServiceEnteReceptor realizaValidaciones(JsonObject jsonObject,
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

    @Override
    public Future<ModuloDTO> validarBody(JsonObject json) {
        EnteReceptorDTO enteReceptor = new EnteReceptorDTO(json.getJsonObject(INSTITUCION_RECEPTORA).getJsonObject(ENTE));
        Future<ModuloDTO> future = Future.future();
        ModuloValidarDTO moduloAValidar = new ModuloValidarDTO(INSTITUCION_RECEPTORA);
        ModuloDTO modulo = new ModuloDTO(INSTITUCION_RECEPTORA);

        ValCabeceraDecla.creaModuloValidacionEnteInstitucion(moduloAValidar, enteReceptor);

        new ExectValidations(webClient, URL_CATALOGOS).ejecutarValidacionesRx(moduloAValidar, modulo
        ).doOnComplete(() -> {
            logger.info("doOnComplete(()");
            future.complete(modulo);
        }).doOnError(e -> {
            logger.severe(String.format("=== doOnError() %s", e));
            future.fail(e.toString());
        }).subscribe();

        return future;
    }

    @Override
    public Future<JsonObject> consultarServicioEnte(JsonObject json) {
        StringBuilder filtros = new StringBuilder();
        JsonObject nivelOrdenGob = json.getJsonObject(INSTITUCION_RECEPTORA).getJsonObject(ENTE).getJsonObject(NIVEL_ORDEN_GOB);
        String poder = json.getJsonObject(INSTITUCION_RECEPTORA).getJsonObject(ENTE).getString(AMBITO_PUB);
        String nivelOG = nivelOrdenGob.getString(NIVEL_ORDEN_GOB);

        if (poder != null && !EMPTY.equals(poder) && nivelOG != null && !EMPTY.equals(nivelOG)) {

            if (EnumAmbitoPoder.ORGANISMOS_AUTONOMOS.name().equals(poder)) {
                filtros.append(String.format(ID_CONSULTA, json.getJsonObject(INSTITUCION_RECEPTORA).getJsonObject(ENTE).getString(ID)));
            }

            if (EnumNivelGobierno.ESTATAL.name().equals(nivelOG)) {
                filtros.append(ID_ENTIDAD_FEDERATIVA)
                        .append(nivelOrdenGob.getJsonObject(ENTIDAD_FED).getInteger(ID));

            } else if (EnumNivelGobierno.MUNICIPAL.name().equals(nivelOG)) {
                filtros.append(ID_ENTIDAD_FEDERATIVA)
                        .append(nivelOrdenGob.getJsonObject(ENTIDAD_FED).getInteger(ID))
                        .append(ID_MUNICIPIO)
                        .append(nivelOrdenGob.getJsonObject(ENTIDAD_FED).getJsonObject(MUNICIPIO).getInteger(ID));
            }
        }

        
        Future<JsonObject> future = Future.future();
        logger.info(String.format(CONSULTA_ENTES, poder, nivelOG, filtros));

        webClient.postAbs(URL_SERVICE_ENTES).sendJson(new JsonObject(String.format(CONSULTA_ENTES, poder, nivelOG, filtros)), ar -> {
            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                future.complete(ar.result().bodyAsJsonObject());
            } else {
                logger.severe(String.format("status code response de entes: %d %n segun consullta hubo error: %s", ar.result().statusCode(), ar));
                future.fail(ar.cause());
            }
        });
        return future;
    }
    
    

    @Override
    public Future<JsonArray> consultarTodos(String parametro) {
        Future<JsonArray> f = Future.future();
        daoEnteReceptorInterface.consultarTodos(resultHandler -> {
            if (resultHandler.succeeded()) {
                f.handle(Future.succeededFuture(new JsonArray(resultHandler.result())));
            } else {
                f.fail(resultHandler.cause());
            }
        });
        return f;
    }
}
