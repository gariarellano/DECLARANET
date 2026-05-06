/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "EnteReceptorDecla" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.dao.interfaces.DAOConsultDeclaInterface;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.service.interfaces.Historial;
import mx.gob.sfp.dgti.service.interfaces.ServiceConsultaDeclaInerface;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_COLLECTION_NAME;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_NUMERO_DECLARACION_PRECARGA;
import static mx.gob.sfp.dgti.util.Constantes.NUM_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PATHERN_DIGITOS;
import static mx.gob.sfp.dgti.util.Constantes.URL_PRECARGA_HISTORICO;
import static mx.gob.sfp.dgti.util.Constantes.FIELD_USUARIO;
import mx.gob.sfp.dgti.util.IRecorreDecla;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 * Clase que contien la implementaion de los metodos de logica de negocio de
 * entesd receptor de informacion.
 *
 * @author cgarias
 * @since 06/11/2019
 * @edited by programador04 23/12/2019
 */
public class ServiceConsultaDecla implements ServiceConsultaDeclaInerface {

    private static final Logger logger = Logger.getLogger(ServiceConsultaDecla.class.getName());
    private final DAOConsultDeclaInterface daoConsultDeclaInterface;
    private final WebClient webClient;
    
    private static final String FIELD_DECLARACION = "declaracion";
    private static final String FIELD_DATOS_GENERALES = "datosGenerales";

    private static final String FIELD_INSTITUCION_RECEPTORA = "institucionReceptora";
    private static final String DATOS_EMPLEO_CARGO_COMISION = "datosEmpleoCargoComision"; 
    private static final String ACTIVIDAD_ANUAL_ANTERIOR = "actividadAnualAnterior"; 
    private static final String INGRESOS = "ingresos";   
    private static final String FIELD_ENCABEZADO = "encabezado";
    private static final String NUMERO_DECLARACION_PRECARGA = "numeroDeclaracionPrecarga";
    private static final String FIELD_RECEPCION_WEB = "recepcionWeb";
    private static final String FIED_ACUSE = "acuse";
    private static final String FIED_FECHA_RECEPCION = "fechaRecepcion";
    
    private static final String FIELD_QUERY = "query";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_FIRMADA = "firmada"; 
    
    //RUSP QUERY
    private static final String URL_RUSP = "URL_RUSP";
    private static final String URL_DATOS_RUSP = System.getenv(URL_RUSP) != null ? System.getenv(URL_RUSP) : "https://precarga-rusp.apps.funcionpublica.gob.mx/api/graphql/";
    private static final String QUERY_RUSP_A = "query{metodo:altas(%s) {array:spDTO {idSp:id_sp ramo ur idMovimiento:id_alta idEnte:id_ente ente:nombre_ente areaAdscripcion:area_adscripcion empleoCargoComision:nombre_empleo nivelEmpleoCargoComision:nivel_empleo nivelJerarquico:id_nivel_equivalencia contratadoPorHonorarios:contratado_por_honorarios fechaEncargo:fecha_toma_posesion_puesto}}}";
    private static final String QUERY_RUSP_B = "query{metodo:bajas(%s) { array:spDTO { idSp:id_sp ramo ur idMovimiento:id_baja idEnte:id_ente ente:nombre_ente areaAdscripcion:area_adscripcion empleoCargoComision:nombre_empleo nivelEmpleoCargoComision:nivel_empleo nivelJerarquico:id_nivel_equivalencia contratadoPorHonorarios:contratado_por_honorarios fechaEncargo:fecha_baja}}}";
    private static final String QUERY_RUSP_M = "query{metodo:activosMayo(%s) {array:spDTO {idSp:id_sp ramo ur idMovimiento:id_activo_mayo idEnte:id_ente ente:nombre_ente areaAdscripcion:area_adscripcion empleoCargoComision:nombre_empleo nivelEmpleoCargoComision:nivel_empleo nivelJerarquico:id_nivel_equivalencia contratadoPorHonorarios:contratado_por_honorarios fechaEncargo:fecha_toma_posesion_puesto}}}";
    
    
    //ENTES BY ID
    private static final String URL_ENTE = "URL_ENTE";
    private static final String URL_DATOS_ENTE = System.getenv(URL_ENTE) != null ? System.getenv(URL_ENTE): "https://dgti-ejz-entes.200.34.175.120.nip.io/";
    private static final String QUERY_ENTE = "query{ente:obtenerEntes(filtro:{%s}){id nombre:enteDesc nivelOrdenGobierno:nivelGobierno{nivelOrdenGobierno:nivelGobierno entidadFederativa {id:idEntidadFederativa valor:entidadFederativaDesc municipio{id:idMunicipio valor:municipioDesc}}} ambitoPublico: poder}}";
    
    private static final String FIELD_ID_ENTE = "idEnte";
    private static final String FIELD_RAMO = "ramo";
    private static final String FIELD_ID_DOS_PUNTOS = "id : ";
    private static final String FIELD_RAMO_DOS_PUNTOS = "ramo: ";
    private static final String FIELD_ENTE = "ente";
    //Etiquetas del Json de datosEmpleoCargoComisión.
    private static final String FIELD_TIPO_OPERACION = "tipoOperacion";
    private static final String FIELD_ID = "id";
    private static final String FIELD_ID_MOVIMIENTO = "idMovimiento";
    private static final String FIELD_NIVEL_JERARQUICO = "nivelJerarquico";
    private static final String FIELD_REMUNERACION_NETA = "remuneracionNeta";
    private static final String FIELD_TIPO_REMUNERACION = "tipoRemuneracion";
    private static final String FIELD_FUNCION_PRINCIPAL = "funcionPrincipal";
    private static final String FIELD_TELEFONO_OFICINA = "telefonoOficina";
    private static final String FIELD_DOMICILIO = "domicilio";
    private static final String FIELD_ID_POSICION_VISTA = "idPosicionVista";
    private static final String FIELD_REGISTRO_HISTORICO = "registroHistorico";
    private static final String FIELD_AREA_ADSCRIPCION = "areaAdscripcion";
    private static final String FIELD_EMPLEO_CARGO_COMISION = "empleoCargoComision";
    private static final String FIELD_NIVEL_EMPLEO_CARGO_COMISION = "nivelEmpleoCargoComision";
    private static final String FIELD_CONTRATADO_POR_HONORARIOS = "contratadoPorHonorarios";
    private static final String FIELD_FECHA_ENCARGO = "fechaEncargo";
    private static final String FIELD_FECHA_ENCARGO_RUSP = "fechaEncargoRusp";
    private static final String FIELD_VERIFICAR = "verificar";
    private static final String FIELD_TIPO_DECLARACION = "tipoDeclaracion";
    private static final String FIELD_ANIO = "anio";
    private static final String HONORARIOS = "HONORARIOS";
    private static final String FIELD_ID_SP = "idSp";
    private final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final String EXPERIENCUAS_LABORALES = "experienciasLaborales";
    private static final String NINGUNO = "ninguno";
    private static final String EXPERIENCIA_LABORAL = "experienciaLaboral";
    private static final String DATOS_DEPENDIENTES_ECONOMICOS = "datosDependientesEconomicos";
    private static final String BIENES_INMUEBLES = "bienesInmuebles";
    private static final String VEHICULOS = "vehiculos";
    private static final String BIENES_MUEBLES = "bienesMuebles";
    private static final String INVERSIONES_CUENTAS_VALORES = "inversionesCuentasValores";
    private static final String INVERSION = "inversion";
    private static final String ADEUDOS_PASIVOS = "adeudosPasivos";
    private static final String ADEUDOS = "adeudos";
    private static final String PRESTAMO_COMATO = "prestamoComodato";
    private static final String PRESTAMO = "prestamo";
    private static final String PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION = "participaEmpresasSociedadesAsociaciones";
    private static final String PARTICIPACIONES = "participaciones";
    private static final String PARTICIPACION_TOMA_DECISIONES = "participacionTomaDecisiones";
    private static final String APOYOS = "apoyos";
    private static final String REPRESENTACIONES = "representaciones";
    private static final String CLIENTES_PRINCIPALES = "clientesPrincipales";
    private static final String CLIENTES = "clientes";
    private static final String BENEFICIOS_PRIVADOS = "beneficiosPrivados";
    private static final String BENEFICIOS = "beneficios";
    private static final String FIDEICOMISOS = "fideicomisos";
    private static final String DATOS_CURRICULARES = "datosCurricularesDeclarante";
    private static final String ESCOLARIDAD = "escolaridad";
    private static final String REALIZA_ACTIVIDAD_LUCRATIVA = "realizaActividadLucrativa";
    
    public ServiceConsultaDecla(Vertx vertx, WebClient webClient) {
        daoConsultDeclaInterface = DAOConsultDeclaInterface.init(vertx);
        this.webClient = webClient;
    }

    @Override
    public Future<JsonObject> consultaDeclaracion(JsonObject parametrosDeclaracion) {
        Future<JsonObject> f = Future.future();
        if (parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION) != null
                && !parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION).isEmpty()
                && parametrosDeclaracion.getInteger(FIELD_ID_USUARIO) != null
                && parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME) != null) {
            daoConsultDeclaInterface.consultaDeclaracion(parametrosDeclaracion, true, resultHandler -> {
                if (resultHandler.succeeded()) {
                    f.handle(Future.succeededFuture(resultHandler.result()));
                } else {
                    f.fail(resultHandler.cause());
                }
            });
        } else {
            f.fail("Parametros incorrectos");
        }
        return f;
    }

    @Override
    public Future<JsonObject> consultaDeclaracionSi(JsonObject parametrosDeclaracion) {
        Future<JsonObject> fresult = Future.future(resultado ->{
            
            if (parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION) != null
                    && !parametrosDeclaracion.getString(FIELD_NUMERO_DECLARACION).isEmpty()
                    && parametrosDeclaracion.getInteger(FIELD_ID_USUARIO) != null
                    && parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME) != null) {
                
                if (parametrosDeclaracion.containsKey("html")){
                    daoConsultDeclaInterface.consultaDeclaracionHtml(parametrosDeclaracion, localizada->{
                        if (localizada.succeeded()){
                            if (localizada.result() != null){
                                resultado.handle(localizada);
                            }else{
                                this.bucarDeclaracion(parametrosDeclaracion, resultado);
                            }
                        }else{
                            resultado.handle(Future.failedFuture(localizada.cause()));
                        }
                    });
                }else{
                    this.bucarDeclaracion(parametrosDeclaracion, resultado);
                }
            } else {
                resultado.fail("Parametros incorrectos");
            }
        });
        return fresult;
    }
    
    private void bucarDeclaracion(JsonObject parametrosDeclaracion, Handler resultado){
        Future<JsonObject> enteReceptor = Future.future();
        Future<JsonObject> declaracion = Future.future();
        Future<JsonObject> recepcionWeb = Future.future();

        daoConsultDeclaInterface.consultaRecepcionWeb(parametrosDeclaracion, firmada -> {
            if (firmada.succeeded()) {
                daoConsultDeclaInterface.consultaDeclaracion(parametrosDeclaracion, firmada.result() == null, resultHandler -> {
                    declaracion.handle(resultHandler);
                });
                daoConsultDeclaInterface.consultaEnteReceptor(parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME), firmada.result() == null,
                        resultHandler -> {
                            enteReceptor.handle(resultHandler);
                        });
            } else {
                declaracion.fail(firmada.cause());
            }
            recepcionWeb.handle(firmada);
        });

        CompositeFuture.all(enteReceptor, declaracion, recepcionWeb).setHandler(termino -> {
            if (termino.succeeded()) {
                JsonObject jsonImagenes = new JsonObject()
                        .put(FIELD_DECLARACION, declaracion.result())
                        .put(FIELD_INSTITUCION_RECEPTORA, enteReceptor.result());
                if (recepcionWeb.result() == null) {
                    jsonImagenes.putNull(FIED_ACUSE);
                } else {
                    jsonImagenes.put(FIED_ACUSE, new JsonObject().put(FIED_FECHA_RECEPCION, recepcionWeb.result().getJsonObject(FIELD_DECLARACION).getString(FIED_FECHA_RECEPCION)));
                }

                resultado.handle(Future.succeededFuture(jsonImagenes));
            } else {
                resultado.handle(Future.failedFuture(termino.cause()));
            }
        });
    }

    @Override
    public Future<JsonObject> consultarFirmados(JsonObject parametrosDeclaracion) {
        Future<JsonObject> futureResult = Future.future();
        JsonObject resultado = new JsonObject();
        daoConsultDeclaInterface.consultaRecepcionWeb(parametrosDeclaracion, firmada -> {
            if (firmada.succeeded()) {
                Future<JsonObject> enteReceptor = Future.future();
                Future<JsonObject> textoFirmante = Future.future();
                
                resultado.put(FIELD_RECEPCION_WEB, firmada.result());
                JsonObject parametros = 
                        new JsonObject()
                                .put("collName",parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME))
                                .put("idFirmante", firmada.result().getJsonObject("firmaAcuse").getString("idFirmante") );
                
                daoConsultDeclaInterface.consultaEnteReceptor(parametrosDeclaracion.getInteger(FIELD_COLLECTION_NAME),
                        firmada.result() == null,
                        resultHandler -> {
                            if (resultHandler.succeeded()) {
                                enteReceptor.handle(resultHandler);
                            } else {
                                logger.severe("ERROR al ejecutar consulta de entes");
                                enteReceptor.fail(resultHandler.cause());
                            }
                        }
                );
                if (firmada.result()!= null){
                    daoConsultDeclaInterface.consultaFirmante(parametros, resultHandler ->{
                         if (resultHandler.succeeded()) {
                                    textoFirmante.handle(resultHandler);
                                } else {
                                    logger.severe("ERROR al ejecutar consulta de firmante");
                                    textoFirmante.fail(resultHandler.cause());
                                }
                         }
                    );
                }else{
                    //Se asigna susedido sin consultar porque aun no es una declaración firmada
                    textoFirmante.handle(Future.succeededFuture());
                }
                        
                CompositeFuture.all(enteReceptor, textoFirmante).setHandler(termino -> {
                    if (termino.succeeded()){
                        resultado.put(FIELD_INSTITUCION_RECEPTORA,enteReceptor.result());
                        if (firmada.result()!= null && textoFirmante.result().containsKey("texto"))
                            resultado.getJsonObject(FIELD_RECEPCION_WEB).getJsonObject("firmaAcuse").put("texto", textoFirmante.result().getString("texto"));
                        
                        futureResult.handle(Future.succeededFuture(resultado));
                    }else{
                        futureResult.fail(termino.cause());
                    }
                });
            } else {
                logger.severe("ERROR AL EJECUTAR EL QUERY DE RECEPCION WEB.");
                futureResult.fail(firmada.cause());
            }
        });
        return futureResult;
    }

    @Override
    public Future<JsonObject> consultaDeclaracionFirmada(JsonObject parametrosDeclaracion) {
        Future<JsonObject> f = Future.future();
        daoConsultDeclaInterface.consultaDeclaracion(parametrosDeclaracion, false, resultHandler -> {
            if (resultHandler.succeeded()) {
                f.handle(Future.succeededFuture(resultHandler.result()));
            } else {
                f.fail(resultHandler.cause());
            }
        });
        return f;
    }
    
    @Override
    public Future<JsonObject> consultaDeclaracionFirmadaParaPrecarga(JsonObject header, HashMap<Integer, JsonObject> catNivelPresupuestal) {
        Future<JsonObject> declaracionPrecarga = Future.future();
        
        Future<JsonObject> declaracionOriginal = Future.future();
        Future<JsonObject> datosRuspFuture = Future.future();
        String numeroDeclaracion =  header.getString(FIELD_NUMERO_DECLARACION_PRECARGA);
        if (isNumber(numeroDeclaracion)){
            if (!"0000000000".equals(numeroDeclaracion)){//es una declaración sin historial
                this.consultaDeclaracionOracle(Integer.parseInt(numeroDeclaracion), header).setHandler(declaOra->{
                    if (declaOra.succeeded()){
                        declaracionOriginal.handle(Future.succeededFuture(declaOra.result()));
                    }else{
                        logger.info(String.format("Sucedio un error al consultar declaracion de oracle: %s", declaOra.cause()));
                        declaracionOriginal.handle(Future.failedFuture(declaOra.cause()));
                    }
                });
            }else{
                declaracionOriginal.handle(Future.succeededFuture(new JsonObject()));//es una declaración sin historial
            }
        }else{
            if(header.getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO) != null
                    && header.getInteger(FIELD_COLLECTION_NAME) != null) {
                JsonObject parametrosDeclaracion= 
                        new JsonObject()
                                .put(FIELD_COLLECTION_NAME, header.getInteger(FIELD_COLLECTION_NAME))
                                .put(FIELD_NUMERO_DECLARACION,numeroDeclaracion);
                daoConsultDeclaInterface.consultaDeclaracion(parametrosDeclaracion, false, resultHandler -> {
                    if (resultHandler.succeeded()){
                        declaracionOriginal.handle(
                                Future.succeededFuture(
                                        this.tratamientoDeclaParaPrecarga(resultHandler.result(), header)
                                )
                        );
                    }else{
                        logger.warning(String.format("Sucedio un error en precarga de declaracion, por lo que se devolvera un objeto bacio, para no afetar flujo %s", resultHandler.cause()));
                        declaracionOriginal.handle(Future.succeededFuture(new JsonObject()));//Se retorna objeto vacio en caso de que ocurra un error en la consulta de las precargas.
                    }
                });
            }else{
                logger.warning(String.format("Parametos incompletos para la declaracion de mongo \n numeroDecla: %s, IDUSUARIO: %s COLLNAME: %s", header.getString(FIELD_NUMERO_DECLARACION_PRECARGA), header.getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO), header.getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_USUARIO), header.getInteger(FIELD_COLLECTION_NAME)));
                declaracionOriginal.handle(Future.failedFuture("parametros Incorrectos"));
            }
        }
        
        String query = this.getQuery(header);
        if (!query.isEmpty()){
            this.consultaDatosRusp(query).setHandler(respuestaRusp ->{
                    if (respuestaRusp.succeeded() && respuestaRusp.result() != null && !respuestaRusp.result().isEmpty()){
                        this.actualizaEntesEnDatosRusp(respuestaRusp.result())
                                .setHandler(puestosConEntsComple->{
                                    if (puestosConEntsComple.succeeded()){
                                        JsonObject datosRusp = new JsonObject();
                                        datosRusp.put(FIELD_ID_SP, respuestaRusp.result().getJsonObject(0).getInteger(FIELD_ID_SP));
                                        datosRusp.putNull("aclaracionesObservaciones");
                                        this.generaDatosEmpleoCargoComision(datosRusp, puestosConEntsComple.result(), catNivelPresupuestal);
                                        datosRuspFuture.handle(Future.succeededFuture(datosRusp));
                                    }else{
                                        logger.info(String.format("ERROR al contestar los entes de los puestos de RUSP. %s", puestosConEntsComple.cause()));
                                        datosRuspFuture.handle(Future.succeededFuture(new JsonObject()));
                                    }
                                });
                    }else{
                        if (!respuestaRusp.succeeded()){
                            logger.info(String.format("--->>>ERROR al consultar activos de Rusp  %s", respuestaRusp.cause()));
                            datosRuspFuture.handle(Future.failedFuture("ERROR al consultar activos de Rusp"));
                        }
                        if (respuestaRusp.result()==null || respuestaRusp.result().isEmpty()){
                            logger.log(Level.INFO, String.format("RUSP no tiene datos del Declarante. %s  {\"query\": \"%s\" }", header.getJsonObject(FIELD_USUARIO).getString("curp"), query));
                            datosRuspFuture.handle(Future.succeededFuture(new JsonObject()));
                        }
                    }
                }
            );
        }else{
            logger.info("Por error en parseo o tipo declaracion no permitida.");
            datosRuspFuture.handle(Future.succeededFuture(new JsonObject()));
        }

        CompositeFuture.all(declaracionOriginal, datosRuspFuture).setHandler(termino -> {
            if (termino.succeeded()) {
                JsonObject declaracionOrigJson = declaracionOriginal.result();
                JsonObject datosEmpleoCargoComision= datosRuspFuture.result();
                if (!declaracionOrigJson.isEmpty()){
                    declaracionOrigJson.put(NUMERO_DECLARACION_PRECARGA, header.getString(FIELD_NUMERO_DECLARACION_PRECARGA));
                    if (!datosEmpleoCargoComision.isEmpty()){
                        declaracionOrigJson.getJsonObject(FIELD_DECLARACION).putNull(DATOS_EMPLEO_CARGO_COMISION);
                    }else{
                        if (declaracionOrigJson.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_EMPLEO_CARGO_COMISION) != null &&
                                declaracionOrigJson.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION) != null &&
                                !declaracionOrigJson.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION).isEmpty()){
                            for (int x=0; x < declaracionOrigJson.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_EMPLEO_CARGO_COMISION).getJsonArray(FIELD_EMPLEO_CARGO_COMISION).size(); x++) {
                                    declaracionOrigJson
                                            .getJsonObject(FIELD_DECLARACION)
                                            .getJsonObject(DATOS_EMPLEO_CARGO_COMISION)
                                            .getJsonArray(FIELD_EMPLEO_CARGO_COMISION).getJsonObject(x).put(FIELD_REGISTRO_HISTORICO, Boolean.FALSE);

                            }
                        }
                    }
                }else{
                    declaracionOrigJson.put(FIELD_DECLARACION, new JsonObject());
                    declaracionOrigJson.putNull(NUMERO_DECLARACION_PRECARGA);
                }
                
                if (!datosEmpleoCargoComision.isEmpty()){
                    declaracionOrigJson.put(FIELD_ID_SP, datosEmpleoCargoComision.getInteger(FIELD_ID_SP));
                    datosEmpleoCargoComision.remove(FIELD_ID_SP);                    
                    declaracionOrigJson.getJsonObject(FIELD_DECLARACION).put(DATOS_EMPLEO_CARGO_COMISION, datosEmpleoCargoComision);
                    if ((EnumTipoDeclaracion.INICIO.name().equals(header.getString(FIELD_TIPO_DECLARACION)) || 
                            EnumTipoDeclaracion.CONCLUSION.name().equals(header.getString(FIELD_TIPO_DECLARACION))) &&
                            datosEmpleoCargoComision.getJsonArray(FIELD_EMPLEO_CARGO_COMISION).size() == 1
                            ){
                        declaracionOrigJson.put(FIELD_FECHA_ENCARGO_RUSP,datosEmpleoCargoComision.getJsonArray(FIELD_EMPLEO_CARGO_COMISION).getJsonObject(0).getString(FIELD_FECHA_ENCARGO));
                    }
                }
                declaracionPrecarga.handle(Future.succeededFuture(declaracionOrigJson));
            }else{
                if(!declaracionOriginal.succeeded())
                    logger.info("El objeto declaracionOriginal sufrio un error por lo que se debuelbe  precarga empty");
                if(!datosRuspFuture.succeeded())
                    logger.info("El objeto datosRuspFuture sufrio un error por lo que se debuelbe  precarga empty");
                
                declaracionPrecarga.handle(Future.succeededFuture(new JsonObject()));
            }
        });
        return declaracionPrecarga;
    }
    
    /**
     * Método para obtener historial de declaraciones presentadas de un 
     * Usuario en declaranet.
     * 
     * @param parametros Objeto que contiene los filtros para la consulta del historial.
     * @return Arreglo de datos que contiene el historico de la información de historial.
     */
    @Override
    public Future<JsonArray> consultaHistorialUsurio(JsonObject parametros) {
        Future<JsonArray> historialOrdenado= Future.future();
        daoConsultDeclaInterface.consultaHistorialUsuario(parametros, resultHand ->{
            if(resultHand.succeeded()){
                if (!resultHand.result().isEmpty()){
                    List<JsonObject> historialList = resultHand.result();
                    asignaFechasEncargoModificaciones(historialList);
                    historialOrdenado.handle(Future.succeededFuture(ordenaHistorico(historialList)));
                }else{
                    historialOrdenado.handle(Future.succeededFuture(new JsonArray()));
                }
            }else{
                historialOrdenado.fail(historialOrdenado.cause());
            }                
        });
        return historialOrdenado;
    }
    
    /**
     * Método privado de la clase que se utiliza al realizar una 
     * consulta del metodo de consultaHistorialUsurio.
     * 
     * @param historialList Historial de declaraciones de un usuario.
     */
    private void asignaFechasEncargoModificaciones(List<JsonObject> historialList){
        for (JsonObject index : historialList){
            if (EnumTipoDeclaracion.MODIFICACION.name().equals(index.getJsonObject(FIELD_DECLARACION).getString(FIELD_TIPO_DECLARACION))){    
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, index.getJsonObject(FIELD_DECLARACION).getInteger(FIELD_ANIO));
                cal.set(Calendar.MONTH, 4);//Mayo
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
                index.getJsonObject(FIELD_DECLARACION).put(FIELD_FECHA_ENCARGO, FechaUtil.getFechaFormatoISO8601(cal.getTime()));
            }
        }
    }
    /**
     * Método privado de la clase que se utiliza al realizar una 
     * consulta del metodo de asignaFechasEncargoModificaciones, para obtener historial.
     * 
     * @param historialList Historial de declaraciones de un usuario.
     */
    private JsonArray ordenaHistorico(List<JsonObject> historialList){
        List<Historial> listaHistorial= new ArrayList<>();
        historialList.stream().forEach((index) -> {
            try {
                listaHistorial.add(
                        new Historial(
                                index.getJsonObject("declarante").getInteger("idUsrDecnet"),
                                index.getJsonObject(FIELD_DECLARACION).getString(FIELD_NUMERO_DECLARACION),
                                index.getJsonObject(FIELD_DECLARACION).getString(FIED_FECHA_RECEPCION).substring(0,10),
                                index.getJsonObject(FIELD_INSTITUCION_RECEPTORA).getInteger(FIELD_COLLECTION_NAME),
                                index.getJsonObject(FIELD_DECLARACION).getString(FIELD_TIPO_DECLARACION),
                                index.getJsonObject(FIELD_DECLARACION).getString(FIELD_FECHA_ENCARGO) != null && !index.getJsonObject(FIELD_DECLARACION).getString(FIELD_FECHA_ENCARGO).isEmpty() ?
                                    index.getJsonObject(FIELD_DECLARACION).getString(FIELD_FECHA_ENCARGO).substring(0,10): index.getJsonObject(FIELD_DECLARACION).getString(FIED_FECHA_RECEPCION).substring(0,10),
                                index.getJsonObject(FIELD_DECLARACION).getInteger(FIELD_ANIO),
                                sDateFormat.parse(index.getJsonObject(FIELD_DECLARACION).getString(FIELD_FECHA_ENCARGO) != null && !index.getJsonObject(FIELD_DECLARACION).getString(FIELD_FECHA_ENCARGO).isEmpty() ?
                                    index.getJsonObject(FIELD_DECLARACION).getString(FIELD_FECHA_ENCARGO).substring(0,10): index.getJsonObject(FIELD_DECLARACION).getString(FIED_FECHA_RECEPCION).substring(0,10))
                        )
                );
            } catch (ParseException ex) {
                Logger.getLogger(ServiceConsultaDecla.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Collections.sort(listaHistorial, Collections.reverseOrder());
        return new JsonArray(listaHistorial);
    }
    
    /**
     * @param cadena
     * @return
     */
    public boolean isNumber(String cadena) {
        return PATHERN_DIGITOS.matcher(cadena).matches();
    }
    
    /**
     * Método que realiza la consulta de la declaracion de Oracle para la precarga
     * de informacion del servidor público.
     * 
     * @param numeroDeclaracion Numero de la declaracion de Oracle
     * @return Objeto Json Futuro que contiene la declaracion a precargar.
     */
    private Future<JsonObject> consultaDeclaracionOracle(Integer numeroDeclaracion, JsonObject header){
        Future<JsonObject> declaracion = Future.future();
        webClient.postAbs(System.getenv(URL_PRECARGA_HISTORICO))
                .timeout(7000)
                .sendJsonObject(new JsonObject().put(NUM_DECLARACION, numeroDeclaracion), ar -> {
            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                JsonObject declaOracle = ar.result().bodyAsJsonObject();
                IRecorreDecla.evaluateDataOracle(declaOracle, declaOracle.fieldNames().iterator(), getFechaEncargo(header));
                declaracion.handle(Future.succeededFuture(declaOracle));
            } else {
                if (ar.succeeded())
                    logger.warning(String.format("Error al obtener la declaraci\u00f3n de ORACLE por status: %d , se debuelve objeto bacio. ", ar.result().statusCode()));
                            
                logger.info(String.format("Error al obtener la declaraci\u00f3n %d de ORACLE, se debuelve objeto bacio. : %s", numeroDeclaracion, ar.cause().getMessage()));
                declaracion.handle(Future.succeededFuture(new JsonObject()));
            }
        });
        return declaracion;
    }
    
    /**
     * Método que realiza la consulta de la información de RUSP
     * 
     * @param queryRusp Query que obtendra la información de rusp para la precarga de la información.
     * 
     * @return Respuesta futura de la informacion de RUSP.
     */
    private Future<JsonArray> consultaDatosRusp(String queryRusp){
        Future<JsonArray> activosRusp = Future.future();
        webClient.postAbs(URL_DATOS_RUSP)
                .timeout(20000)
                .sendJsonObject(new JsonObject().put(FIELD_QUERY, queryRusp), respuestaRusp -> {
                    if (respuestaRusp.succeeded() && respuestaRusp.result().statusCode()== HttpResponseStatus.OK.code() ) {
                        activosRusp.handle(Future.succeededFuture(new JsonObject(respuestaRusp.result().bodyAsBuffer() ).getJsonObject(FIELD_DATA).getJsonObject("metodo").getJsonArray("array")));
                    }else{
                        logger.severe(String.format("Error al obtener la informacion RUSP. %s", respuestaRusp));
                        activosRusp.handle(Future.succeededFuture(new JsonArray()));
                    }
                });
        return activosRusp;
    }
    
    /**
     * Método que debuelbe el query para la ejecucion en RUSP.
     * y debuelba informacion.
     * 
     * @param tipoDecla tip
     * @param encabezado
     * @return 
     */
    private String getQuery(JsonObject encabezado){
        String query = "";
        try {
            StringBuilder parametros =
                    new StringBuilder()
                            .append(encabezado.getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_SP) != null ?
                                    "id_sp :"+encabezado.getJsonObject(FIELD_USUARIO).getInteger(FIELD_ID_SP) :
                                    "curp :\""+encabezado.getJsonObject(FIELD_USUARIO).getString("curp")+"\"");
            
            switch (EnumTipoDeclaracion.valueOf(encabezado.getString(FIELD_TIPO_DECLARACION))){
                case INICIO:
                    completaParametrosConFechas(parametros, encabezado.getString("fechaEncargo"));
                    query = String.format(QUERY_RUSP_A, parametros.toString());
                    break;
                case CONCLUSION:
                    completaParametrosConFechas(parametros, encabezado.getString("fechaEncargo"));
                    query = String.format(QUERY_RUSP_B, parametros.toString());
                    break;
                case MODIFICACION:
                    parametros.append(" anio:").append(encabezado.getInteger(FIELD_ANIO));
                    query = String.format(QUERY_RUSP_M, parametros.toString());
                    break;
                default:
                    logger.info(String.format("No existe query especificado para este tipoDeclaración %s", encabezado.getString(FIELD_TIPO_DECLARACION)));
                    break;
            }
        } catch (ParseException ex) {
            logger.severe(String.format("Error al intentar parcear la fecha de encargo. a formato yyy-MM-dd por valor: %s , SEVERE:%s", encabezado.getString("fechaEncargo"),ex));
        }
        return query;
    }
    
    /**
     * Método que realiza el llenado de las fechas  para rango de qunce dias antes
     * y quince dias despues,para la consulta de rusp inicio y conclusión.
     * 
     * @param parametros String que concatena la informacion de los parametros,
     * @param fechaEncargo String qu econtiene la fecha de dencargo yyyy-MM-dd
     * @throws ParseException en caso de ocurrir una excecion de parseo.
     */
    private void completaParametrosConFechas(StringBuilder parametros, String fechaEncargo) throws ParseException{
        Calendar fEncargo = Calendar.getInstance();
        fEncargo.setTime(sDateFormat.parse(fechaEncargo));
        fEncargo.add(Calendar.DAY_OF_MONTH, -15);
        parametros.append(" fecha_inicio: \"").append(sDateFormat.format(fEncargo.getTime())).append("\"");
        fEncargo.add(Calendar.MONTH, 1);
        parametros.append(" fecha_fin: \"").append(sDateFormat.format(fEncargo.getTime())).append("\"");
    }
    
    private Future<JsonObject> consultaDatosEnte(String queryEntes){
        Future<JsonObject> ente = Future.future();
        webClient.postAbs(URL_DATOS_ENTE)
                .timeout(5000)
                .sendJsonObject(new JsonObject().put(FIELD_QUERY, queryEntes), ar -> {
            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                ente.handle(Future.succeededFuture(ar.result().bodyAsJsonObject()));
            } else {
                logger.severe(String.format("Error al obtener la informacion de ENTES. : %s", ar.toString()));
                ente.handle(Future.failedFuture(ar.cause()));
            }
        });
        return ente;
    }
    
    /**
     * Método que realiza la consulta de lois entes por puesto , 
     * para actualizar los pustos con sus entres correspondientes.
     * 
     * @param arrayPuestos Arraglo que contiene los datos de los puestos.
     * @return Se graga el arreglo9s de los puestos con los entes que le corresponden.
     */
    private Future<JsonArray> actualizaEntesEnDatosRusp(JsonArray arrayPuestos){
        Future<JsonArray> datosRuspConEntes = Future.future();
        List<Future> entesPuestos= new ArrayList<>();
        for (Object index : arrayPuestos){
            Future<JsonObject> enteConsultar = Future.future();
            String queryEnte = String.format(QUERY_ENTE, 
                            ((JsonObject) index).getString(FIELD_ID_ENTE) != null ? 
                                    (FIELD_ID_DOS_PUNTOS+"\""+((JsonObject) index).getString(FIELD_ID_ENTE)+"\""):
                                    (FIELD_RAMO_DOS_PUNTOS+((JsonObject) index).getInteger(FIELD_RAMO)+" unidadResponsable:\""+((JsonObject) index).getString("ur")+"\""));
            consultaDatosEnte(queryEnte).setHandler(ente -> {
                if (ente.succeeded()){
                    if (!ente.result().getJsonObject(FIELD_DATA).getJsonArray(FIELD_ENTE).isEmpty()){
                        ((JsonObject) index).put(FIELD_ENTE, ente.result().getJsonObject(FIELD_DATA).getJsonArray(FIELD_ENTE).getJsonObject(0));
                        enteConsultar.handle(ente);
                    }else{
                        logger.warning(String.format("---->>>>> EL ENTE CONSULTA NO EXISTE %s PUESTO %s", queryEnte, ((JsonObject) index).toString()));
                        enteConsultar.fail("No se encontro ente proporcionado.");
                    }
                }else{
                    logger.warning(String.format("no se pudo recuperar el ente para los puestos. %s", ente.cause()));
                    enteConsultar.handle(ente);
                }
            });
            entesPuestos.add(enteConsultar); 
        }
        CompositeFuture.all(entesPuestos).setHandler(termino -> {
            if (termino.succeeded()){
                datosRuspConEntes.handle(Future.succeededFuture(arrayPuestos));    
            }else{
                logger.warning(String.format("ERROR en la recuperacion de los entes %s", termino.cause()));
                datosRuspConEntes.handle(Future.succeededFuture(new JsonArray()));
            }
        });
        return datosRuspConEntes;
    }
    
    /**
     * Método que realiza el llenado de datos del empleo cargo comisión.
     * 
     * @param datosRusp Arreglo de datos RUSP.
     * @return Objeto del modulo de Datos del empleo cargo comision.
     */
    private void generaDatosEmpleoCargoComision(JsonObject datosRusp, JsonArray puestos, HashMap<Integer, JsonObject> catNivelPresupuestal){
        if (!datosRusp.isEmpty()){
            JsonArray emplCargCom = new JsonArray();
            int x=1;
            for(Object index : puestos){
                JsonObject objEmpCarCom = new JsonObject();
                objEmpCarCom.putNull(FIELD_TIPO_OPERACION);
                objEmpCarCom.putNull(FIELD_ID);
                objEmpCarCom.putNull(FIELD_NIVEL_JERARQUICO);
                objEmpCarCom.putNull(FIELD_REMUNERACION_NETA);
                objEmpCarCom.putNull(FIELD_TIPO_REMUNERACION);
                objEmpCarCom.putNull(FIELD_FUNCION_PRINCIPAL);
                objEmpCarCom.putNull(FIELD_TELEFONO_OFICINA);
                objEmpCarCom.putNull(FIELD_DOMICILIO);

                objEmpCarCom.put(FIELD_ID_POSICION_VISTA, String.valueOf(x++));
                objEmpCarCom.put(FIELD_REGISTRO_HISTORICO, false);
                objEmpCarCom.put(FIELD_ENTE, ((JsonObject)index).getJsonObject(FIELD_ENTE));
                objEmpCarCom.put(FIELD_ID_MOVIMIENTO, ((JsonObject)index).getInteger(FIELD_ID_MOVIMIENTO));
                objEmpCarCom.put(FIELD_AREA_ADSCRIPCION, ((JsonObject)index).getString(FIELD_AREA_ADSCRIPCION));
                objEmpCarCom.put(FIELD_EMPLEO_CARGO_COMISION, ((JsonObject)index).getString(FIELD_EMPLEO_CARGO_COMISION));
                objEmpCarCom.put(FIELD_NIVEL_EMPLEO_CARGO_COMISION, ((JsonObject)index).getString(FIELD_NIVEL_EMPLEO_CARGO_COMISION));
                if (((JsonObject)index).getInteger(FIELD_NIVEL_JERARQUICO)!= null){
                    objEmpCarCom.put(FIELD_NIVEL_JERARQUICO, catNivelPresupuestal.get(((JsonObject)index).getInteger(FIELD_NIVEL_JERARQUICO)));
                }
                objEmpCarCom.put(FIELD_CONTRATADO_POR_HONORARIOS, HONORARIOS.equals(((JsonObject)index).getString(FIELD_CONTRATADO_POR_HONORARIOS)));
                objEmpCarCom.put(FIELD_FECHA_ENCARGO, ((JsonObject)index).getString(FIELD_FECHA_ENCARGO));
                objEmpCarCom.put(FIELD_VERIFICAR, false);

                emplCargCom.add(objEmpCarCom);
            }
            datosRusp.put(FIELD_EMPLEO_CARGO_COMISION, emplCargCom);
        }else{
            logger.info("------->>>>>>>Datos Rusp es Empty<<<<<<<--------");
        }
    }
    

    /**
     * Método que realiza el tratamiento de la declaracion mongo para dejarla
     * como declaracion para presentar.
     *
     * @param declaracionOriginal declaración que se obtien de la base de datos
     * de mongo.
     * @return JsonObject objeto declaracion mongo.
     */
    private JsonObject tratamientoDeclaParaPrecarga(JsonObject declaracionOriginal, JsonObject header) {
        declaracionOriginal.put(FIELD_NIVEL_JERARQUICO, declaracionOriginal.getJsonObject(FIELD_ENCABEZADO).getJsonObject(FIELD_NIVEL_JERARQUICO));
        declaracionOriginal.putNull(FIELD_ENCABEZADO);
        declaracionOriginal.putNull(FIELD_INSTITUCION_RECEPTORA);
        declaracionOriginal.put(FIELD_FIRMADA, Boolean.FALSE);
//        declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(FIELD_DATOS_GENERALES);
//        declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(DATOS_EMPLEO_CARGO_COMISION); //actualizacion por si no viene de rusp lo precargue de la decla anteriro
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(ACTIVIDAD_ANUAL_ANTERIOR);
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(INGRESOS);
        
        IRecorreDecla.resetValuesForPrecarga(declaracionOriginal, declaracionOriginal.fieldNames().iterator(), getFechaEncargo(header));
        
        //se comenta arriba y se agrga aqui para precargar los datos de sitPerEstCiv, RegMat, PaiNac y nacionalidad
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).remove("datosPersonales");
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).remove("correoElectronico");
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).remove("telefonoCasa");
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).remove("telefonoCelular");                
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).remove("regimenMatrimonial");
        declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIELD_DATOS_GENERALES).remove("regimenMatrimonialOtro");
        
        //Realiza validacion de módulos, cuando quitan todos los objetos de un array en un módulo, 
        //el módulo deberia de ir null
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(EXPERIENCUAS_LABORALES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(EXPERIENCUAS_LABORALES) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(EXPERIENCUAS_LABORALES).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(EXPERIENCUAS_LABORALES).getJsonArray(EXPERIENCIA_LABORAL) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(EXPERIENCUAS_LABORALES).getJsonArray(EXPERIENCIA_LABORAL).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(EXPERIENCUAS_LABORALES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(DATOS_DEPENDIENTES_ECONOMICOS) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_DEPENDIENTES_ECONOMICOS) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_DEPENDIENTES_ECONOMICOS).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_DEPENDIENTES_ECONOMICOS).getJsonArray(DATOS_DEPENDIENTES_ECONOMICOS) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_DEPENDIENTES_ECONOMICOS).getJsonArray(DATOS_DEPENDIENTES_ECONOMICOS).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(DATOS_DEPENDIENTES_ECONOMICOS);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(BIENES_INMUEBLES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_INMUEBLES) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_INMUEBLES).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_INMUEBLES).getJsonArray(BIENES_INMUEBLES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_INMUEBLES).getJsonArray(BIENES_INMUEBLES).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(BIENES_INMUEBLES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(VEHICULOS) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(VEHICULOS) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(VEHICULOS).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(VEHICULOS).getJsonArray(VEHICULOS) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(VEHICULOS).getJsonArray(VEHICULOS).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(VEHICULOS);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(BIENES_MUEBLES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_MUEBLES) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_MUEBLES).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_MUEBLES).getJsonArray(BIENES_MUEBLES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BIENES_MUEBLES).getJsonArray(BIENES_MUEBLES).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(BIENES_MUEBLES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(INVERSIONES_CUENTAS_VALORES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(INVERSIONES_CUENTAS_VALORES) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(INVERSIONES_CUENTAS_VALORES).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(INVERSIONES_CUENTAS_VALORES).getJsonArray(INVERSION) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(INVERSIONES_CUENTAS_VALORES).getJsonArray(INVERSION).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(INVERSIONES_CUENTAS_VALORES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(ADEUDOS_PASIVOS) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(ADEUDOS_PASIVOS) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(ADEUDOS_PASIVOS).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(ADEUDOS_PASIVOS).getJsonArray(ADEUDOS) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(ADEUDOS_PASIVOS).getJsonArray(ADEUDOS).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(ADEUDOS_PASIVOS);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(PRESTAMO_COMATO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PRESTAMO_COMATO) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PRESTAMO_COMATO).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PRESTAMO_COMATO).getJsonArray(PRESTAMO) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PRESTAMO_COMATO).getJsonArray(PRESTAMO).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(PRESTAMO_COMATO);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION).getJsonArray(PARTICIPACIONES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION).getJsonArray(PARTICIPACIONES).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(PARTICIPA_EMPRESA_SOCIEDAD_ASOCIACION);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(PARTICIPACION_TOMA_DECISIONES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPACION_TOMA_DECISIONES) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPACION_TOMA_DECISIONES).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPACION_TOMA_DECISIONES).getJsonArray(PARTICIPACIONES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(PARTICIPACION_TOMA_DECISIONES).getJsonArray(PARTICIPACIONES).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(PARTICIPACION_TOMA_DECISIONES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(APOYOS) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(APOYOS) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(APOYOS).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(APOYOS).getJsonArray(APOYOS) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(APOYOS).getJsonArray(APOYOS).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(APOYOS);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(REPRESENTACIONES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(REPRESENTACIONES) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(REPRESENTACIONES).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(REPRESENTACIONES).getJsonArray(REPRESENTACIONES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(REPRESENTACIONES).getJsonArray(REPRESENTACIONES).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(REPRESENTACIONES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(CLIENTES_PRINCIPALES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(CLIENTES_PRINCIPALES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(CLIENTES_PRINCIPALES).getBoolean(REALIZA_ACTIVIDAD_LUCRATIVA) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(CLIENTES_PRINCIPALES).getJsonArray(CLIENTES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(CLIENTES_PRINCIPALES).getJsonArray(CLIENTES).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(CLIENTES_PRINCIPALES);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(BENEFICIOS_PRIVADOS) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BENEFICIOS_PRIVADOS) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BENEFICIOS_PRIVADOS).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BENEFICIOS_PRIVADOS).getJsonArray(BENEFICIOS) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(BENEFICIOS_PRIVADOS).getJsonArray(BENEFICIOS).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(BENEFICIOS_PRIVADOS);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(FIDEICOMISOS) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIDEICOMISOS) != null &&
                !declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIDEICOMISOS).getBoolean(NINGUNO) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIDEICOMISOS).getJsonArray(FIDEICOMISOS) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(FIDEICOMISOS).getJsonArray(FIDEICOMISOS).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(FIDEICOMISOS);
        }
        if (declaracionOriginal.getJsonObject(FIELD_DECLARACION).containsKey(DATOS_CURRICULARES) &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_CURRICULARES) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_CURRICULARES).getJsonArray(ESCOLARIDAD) != null &&
                declaracionOriginal.getJsonObject(FIELD_DECLARACION).getJsonObject(DATOS_CURRICULARES).getJsonArray(ESCOLARIDAD).isEmpty()){
            declaracionOriginal.getJsonObject(FIELD_DECLARACION).putNull(DATOS_CURRICULARES);
        }
        return declaracionOriginal;
    }
    
    private String getFechaEncargo(JsonObject header){
        String fechaEncargo;
        if (EnumTipoDeclaracion.MODIFICACION.name().equals(header.getString(FIELD_TIPO_DECLARACION))){
            try {
                Calendar fEncargo = Calendar.getInstance();
                String mayoPrimero = header.getInteger(FIELD_ANIO)+"-05-01";
                fEncargo.setTime(sDateFormat.parse(mayoPrimero));
                fEncargo.set(Calendar.DAY_OF_MONTH, fEncargo.getActualMaximum(Calendar.DATE));//ultimo dia de mayo del año de la declaración.
                fechaEncargo = sDateFormat.format(fEncargo.getTime());
            } catch (ParseException ex) {
                fechaEncargo = header.getInteger(FIELD_ANIO)+"-05-31";
                logger.severe(String.format("Error de parseo de la fecha de modificación. %s", ex));
            }
        }else{
            fechaEncargo = header.getString(FIELD_FECHA_ENCARGO);
        }
        return fechaEncargo;
    }

    @Override
    public Future<JsonObject> consultaDeclaracionDirecciones(JsonObject parametrosDeclaracion) {
        Future<JsonObject> f = Future.future();
        daoConsultDeclaInterface.consultaDeclaracion(parametrosDeclaracion, false, resultHandler -> {
            if (resultHandler.succeeded()) {
                JsonObject declaracion = resultHandler.result();
                declaracion.remove("institucionReceptora");
                declaracion.getJsonObject("declaracion").getJsonObject("domicilioDeclarante").remove("aclaracionesObservaciones");
                declaracion.getJsonObject("declaracion").getJsonObject("datosEmpleoCargoComision").remove("aclaracionesObservaciones");
                if (declaracion.getJsonObject("declaracion").getJsonObject("bienesInmuebles")!= null ){
                    declaracion.getJsonObject("declaracion").getJsonObject("bienesInmuebles").remove("aclaracionesObservaciones");
                }
                if (declaracion.getJsonObject("declaracion").getJsonObject("prestamoComodato") != null){
                    declaracion.getJsonObject("declaracion").getJsonObject("prestamoComodato").remove("aclaracionesObservaciones");
                }
                declaracion.getJsonObject("declaracion").remove("datosGenerales");
                declaracion.getJsonObject("declaracion").remove("datosCurricularesDeclarante");
                declaracion.getJsonObject("declaracion").remove("experienciasLaborales");
                declaracion.getJsonObject("declaracion").remove("datosPareja");
                declaracion.getJsonObject("declaracion").remove("datosDependientesEconomicos");
                declaracion.getJsonObject("declaracion").remove("ingresos");
                declaracion.getJsonObject("declaracion").remove("actividadAnualAnterior");
                declaracion.getJsonObject("declaracion").remove("vehiculos");
                declaracion.getJsonObject("declaracion").remove("bienesMuebles");
                declaracion.getJsonObject("declaracion").remove("inversionesCuentasValores");
                declaracion.getJsonObject("declaracion").remove("adeudosPasivos");
                declaracion.getJsonObject("declaracion").remove("participaEmpresasSociedadesAsociaciones");
                declaracion.getJsonObject("declaracion").remove("participacionTomaDecisiones");
                declaracion.getJsonObject("declaracion").remove("representaciones");
                declaracion.getJsonObject("declaracion").remove("clientesPrincipales");
                declaracion.getJsonObject("declaracion").remove("beneficiosPrivados");
                declaracion.getJsonObject("declaracion").remove("fideicomisos");
                
                f.handle(Future.succeededFuture(declaracion));
            } else {
                f.fail(resultHandler.cause());
            }
        });
        return f;
    }
}
