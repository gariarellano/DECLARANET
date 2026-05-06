/**
 * @(#)Server.java 26/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import mx.gob.sfp.dgti.dto.DatosDTO;
import mx.gob.sfp.dgti.util.Constantes;
import mx.gob.sfp.dgti.util.EnumEtiquetas;
import mx.gob.sfp.dgti.validacion.GeneradorJWT;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mx.gob.sfp.dgti.DAOPermisosInterface;

import static mx.gob.sfp.dgti.util.Constantes.*;
import mx.gob.sfp.dgti.util.EnumCampos;

/**
 * Verticle para el server
 *
 * @author pavel.martinez
 * @since 26/11/2019
 */
public class Server extends AbstractVerticle {

    /**
     * El logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * El generador
     */
    private GeneradorJWT generadorJWT;
    /**
     * Router principal para creacion de http
     */
    private Router router;
    
    /**
     * Objeto de acceso a bases de datos.
     */
    private DAOPermisosInterface daoPermisos;
    
    /**
     * Método principal para el arranque del servicio.
     * @param vertx
     * @param context 
     */
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context); 
        router = Router.router(vertx);
        
        router.route(PATH).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader(HEADER_CONTENT_TYPE, TEXT_HTML).end("Hola GeneradorJWT");
        });
//        router.route().handler(BodyHandler.create());
//        router.route().handler(StaticHandler.create());
        this.getVertx().createHttpServer().requestHandler(router).listen(
            config().getInteger(CONFIG_PORT, Integer.parseInt(System.getenv(SERVER_PORT) != null
                                    ? System.getenv(SERVER_PORT) : "5000")), ar -> {
                if (ar.succeeded()) {
                    LOGGER.info("En ejecución...");
                } else {
                    LOGGER.info(ar.cause());
                }
            });
        generadorJWT = new GeneradorJWT(vertx);
        daoPermisos= DAOPermisosInterface.init(vertx);
        router.route().handler(CorsHandler.create(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()).allowedMethods(getAllowedMethods()));
    }
    
    /**
     * Start inicializar router y la definición de paths
     *
     * @param promise
     * @throws Exception
     */
    @Override
    public void start(Promise<Void> promise) throws Exception {
        super.start(promise);  
        router.route().handler(BodyHandler.create());
        router.post(PATH_GENERADOR).handler(this::generarToken);
        router.post(PATH_RENOVADOR).handler(this::renovarToken);
        router.post(PATH_VALIDADOR).handler(this::validarToken);
        
        //Estas dos son el mismo path, diferente método
        //El método get solo recibe el header y devuelve el contenido del token y token actializado
        //El método post recibe header y body agrega el contenido del body al token y devuelve token con body.
        router.get(PATH_SELECCIONADO).handler(this::renovarSeleccionado);
        router.post(PATH_SELECCIONADO).handler(this::renovarSeleccionado);
                
//        router.route(PATH_PERFIL).handler(JWTAuthHandler.create(generadorJWT.getProvider()));
        router.route(PATH_PERFIL).handler(this::validaParametros);
        router.post(PATH_PERFIL).handler(this::consultaPerfil);
        router.put(PATH_PERFIL).handler(this::guardaPerfil);
        router.patch(PATH_PERFIL).handler(this::guardaPerfil);
        router.delete(PATH_PERFIL).handler(this::eliminaPerfil);
                
//        router.route(PATH_ASIGNACION).handler(JWTAuthHandler.create(generadorJWT.getProvider()));
        router.route(PATH_ASIGNACION).handler(this::validaParametros);
        router.post(PATH_ASIGNACION_TOKEN).handler(this::consultaAsignacionToken);
        router.post(PATH_ASIGNACION).handler(this::consultaAsignacion);
        router.put(PATH_ASIGNACION).handler(this::guardaAsignacion);
        router.patch(PATH_ASIGNACION).handler(this::actualizarAsignacion);
        router.delete(PATH_ASIGNACION).handler(this::eliminaAsignacion);
        
        router.get(PATH_ASIGNACION_POR_ID_O_CURP).handler(this::consultaAsignacionporIdUsuariooCurp);
        router.route().handler(StaticHandler.create());
    }

    /**
     * Funcion para generar tokens
     *
     * @param context: contexto de la petición que contiene los stringParams,
     * header y body de la petición
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    private void generarToken(RoutingContext context) {
        obtenerHeaders(context);
        if (!context.getBodyAsString().equals(EnumCampos.VACIO.getValor())) {
            try {
                DatosDTO datos = Json.decodeValue(context.getBodyAsString(), DatosDTO.class);
                if (datos.getExpMinutos() == 0 || datos.getIss() == null || datos.getUsuario() == null) {
                    context.response().end(Json.encode("PARAMETROS_INCOMPLETOS"));
                }
                generadorJWT.generarToken(datos).setHandler(token -> {
                    if (token.succeeded()) {
                        if (token.result().isEmpty()) {
                            context.response().end(new JsonObject().toString());
                        } else {
                            context.response().end(Json.encode(token.result()));
                        }
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            } catch (DecodeException | EncodeException e) {
                LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + e.getMessage() + e.getCause().getMessage());
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    } 
    
    /**
     * Funcion para renovar tokens
     *
     * @param context: contexto de la petición que contiene los stringParams,
     * header y body de la petición
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    private void renovarToken(RoutingContext context) {
        obtenerHeaders(context);
        if (!context.getBodyAsString().equals(EnumCampos.VACIO.getValor())) {
            try {
                String token = Json.decodeValue(context.getBodyAsString(), String.class);
                generadorJWT.renovarToken(token).setHandler(nuevoToken -> {
                    if (nuevoToken.succeeded()) {
                        if (nuevoToken.result().isEmpty()) {
                            context.response().end("IMPOSIBLE_RENOVAR");
                        } else {
                            context.response().end(Json.encode(nuevoToken.result()));
                        }
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            } catch (Exception e) {
                LOGGER.info(EnumEtiquetas.OCURRIO_ALGO.getValor() + e.getMessage() + e.getCause().getMessage());
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método para renova y encriptar datos en token seleccionado con body enviado.
     * en caso de get devuelve lo que contenga el token de body.
     *
     * @param context: contexto de la petición que contiene los stringParams,
     * header y body de la petición
     *
     * @author cgarias
     * @since 03/03/2021
     */
    private void renovarSeleccionado(RoutingContext context) {
        obtenerHeaders(context);
        if (context.request().method().equals(HttpMethod.GET)){
            if (!context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).isEmpty()){
                try {
                    generadorJWT.obtenDatosToken(context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).replace(EnumCampos.BEARER.getValor(), EnumCampos.VACIO.getValor())).setHandler(datos -> {
                        if (datos.succeeded()){
                            generadorJWT.renovarToken(context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).replace(EnumCampos.BEARER.getValor(), EnumCampos.VACIO.getValor())).setHandler(nuevoToken -> {
                                if (nuevoToken.succeeded()) {
                                    if (nuevoToken.result().isEmpty()) {
                                        context.response().end("IMPOSIBLE_RENOVAR");
                                    } else {
                                        context.response()
                                                .putHeader(EnumCampos.AUTHORIZATION.getValor(), EnumCampos.BEARER.getValor()+nuevoToken.result())
                                                .end(datos.result().encode());
                                    }
                                } else {
                                    LOGGER.error("Error al renovar token de renovarSeleccionado");
                                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                                }
                            });
                        }else{
                            LOGGER.error("Error al obtener los datos del token en renovarSeleccionado");
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });
                } catch (Exception e) {
                    LOGGER.info("renovarSeleccionado método GET" + e.getMessage() + e.getCause().getMessage());
                    context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                }
            }else{
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }else if (context.request().method().equals(HttpMethod.POST)){
            if (context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()) != null && !context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).isEmpty() && 
                    context.getBodyAsJson().getString("perfil") != null
                    ) {
                try {
                    generadorJWT.obtenDatos(context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).replace(EnumCampos.BEARER.getValor(), EnumCampos.VACIO.getValor())).setHandler(datos->{
                        DatosDTO datosToken = datos.result();
                        JsonObject data = new JsonObject((Map)datosToken.getData());
                        JsonObject query = new JsonObject();
                        query.put(EnumCampos.COLNAME.getValor(), data.getInteger(EnumCampos.COLNAME.getValor()))
                                .put(EnumCampos.SISTEMA.getValor(), datosToken.getUsuario())
                                .put("perfil", context.getBodyAsJson().getString("perfil"));
                        daoPermisos.buscaPerfil(query, localizado->{
                            if (localizado.succeeded()){
                                data.put("perfilBody", context.getBodyAsJson());
                                data.put("perfil", localizado.result().get(0));                                
                                datosToken.setExpMinutos(Integer.parseInt(datosToken.getTiempo()));
                                datosToken.setData(data);
                                generadorJWT.generarToken(datosToken).setHandler(nuevoToken -> {
                                    if (nuevoToken.succeeded()) {
                                        context.response().setStatusCode(HttpResponseStatus.OK.code())
                                                .putHeader(EnumCampos.AUTHORIZATION.getValor(), EnumCampos.BEARER.getValor()+nuevoToken.result())
                                                .end(data.encode());
                                    } else {
                                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                                    }
                                });
                            }else{
                                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                            }
                        });
                    });
                } catch (Exception e) {
                    LOGGER.info("renovarSeleccionado método POST" + e.getMessage() + e.getCause().getMessage());
                    context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                }
            } else {
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }
    }

    /**
     * Funcion para validar tokens
     *
     * @param context: contexto de la petición que contiene los stringParams,
     * header y body de la petición
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    private void validarToken(RoutingContext context) {
        obtenerHeaders(context);
        if (!context.getBodyAsString().equals(EnumCampos.VACIO.getValor())) {
            try {
                String token = Json.decodeValue(context.getBodyAsString(), String.class);
                generadorJWT.validarToken(token).setHandler(respuesta -> {
                    if (respuesta.succeeded()) {
                        if (!respuesta.result()) {
                            context.response().end(Json.encode(false));
                        } else {
                            context.response().end(Json.encode(respuesta.result()));
                        }
                    } else {
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            } catch (Exception e) {
                LOGGER.info(EnumEtiquetas.OCURRIO_ALGO + e.getMessage() + e.getCause().getMessage());
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        } else {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
        }
    }
    
    /**
     * Método generico para los paths de perfiles, que valida los parametros.
     * @param context Contexto de request.
     */
    private void validaParametros(RoutingContext context){
        obtenerHeaders(context);
        if (context.pathParam(EnumCampos.COLNAME.getValor()).isEmpty()){
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro collName");            
        }
        if (context.pathParam(EnumCampos.SISTEMA.getValor()).isEmpty()){
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro institucion");
        }
        try{
            Integer.parseInt(context.pathParam(EnumCampos.SISTEMA.getValor()));
            Integer.parseInt(context.pathParam(EnumCampos.COLNAME.getValor()));
        }catch(Exception e){
            LOGGER.info("error parametro collName no es integer" + e);
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Parametro collName y sistema debe ser numero");
        }
        JsonObject data = new JsonObject()
                .put(EnumCampos.COLNAME.getValor(), Integer.parseInt(context.pathParam(EnumCampos.COLNAME.getValor())))
                .put(EnumCampos.SISTEMA.getValor(), context.pathParam(EnumCampos.SISTEMA.getValor()));
        
                
        if (context.request().getParam("institucion") != null  && context.request().getParam("institucion").isEmpty()){
            data.put("institucion", context.pathParam("institucion"));
        }
        context.put(EnumCampos.DATA.getValor(), data);
        context.next();
    }
    
    /**
     * Métodos para realizar consulta de los perfiles creados
     * @param context 
     */
    private void consultaPerfil(RoutingContext context){
        try{
            JsonObject query = context.getBodyAsJson().mergeIn(context.get(EnumCampos.DATA.getValor()));

            daoPermisos.buscaPerfil(query, response->{
                if (response.succeeded()){
                    context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.OK.code()).end(Json.encode(response.result()));
                }else{
                    LOGGER.error(response.cause());
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            });
        }catch(Exception e){
            LOGGER.error(e);
            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
        }
    }
    /**
     * Métodos para realizar el guardado de los perfiles creados
     * @param context 
     */
    private void guardaPerfil(RoutingContext context){
        if (!context.pathParam("perfil").isEmpty()){
            try{
                JsonObject query = context.getBodyAsJson().mergeIn(context.get(EnumCampos.DATA.getValor()));
                query.put("perfil", context.pathParam("perfil"));
                daoPermisos.guardaPerfil(query, response->{
                    if (response.succeeded()){
                        context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.CREATED.code()).end(Json.encode(response.result()));
                    }else{
                        LOGGER.error(response.cause());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro perfil");
        }
    }    
    /**
     * Métodos para realizar el eliminar el perfiles creado
     * @param context 
     */
    private void eliminaPerfil(RoutingContext context){
        if (!context.pathParam("perfil").isEmpty()){    
            try{
                JsonObject query = context.getBodyAsJson().mergeIn(context.get(EnumCampos.DATA.getValor()));
                query.put("perfil", context.pathParam("perfil"));
                daoPermisos.eliminaPerfil(query, response->{
                    if (response.succeeded()){
                        context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.OK.code()).end(Json.encode(response.result()));
                    }else{
                        LOGGER.error(response.cause());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro perfil");
        }
    }
    
    /**
     * Métodos para realizar la consulta de las asignaciones que se tengan para los datos del token creado en IDP
     *
     * @param context
     */
    private void consultaAsignacionToken(RoutingContext context){
        if (!context.pathParam(EnumCampos.CURP.getValor()).isEmpty() && 
                context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()) != null &&!context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).isEmpty()){
            try{
                generadorJWT.obtenDatos(context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).replace(EnumCampos.BEARER.getValor(), EnumCampos.VACIO.getValor())).setHandler(datos->{
                    DatosDTO objetoToken = datos.result();
                    JsonObject query = new JsonObject();
                    
                    JsonObject data = new JsonObject((Map)objetoToken.getData());
                    query.put(EnumCampos.CURP.getValor(), data.getString(EnumCampos.CURP.getValor()));
                    query.put(EnumCampos.ID_USUARIO.getValor(), data.getInteger(EnumCampos.ID_USUARIO.getValor()));
                    query.put(EnumCampos.SISTEMA.getValor(), objetoToken.getUsuario());
                    query.put(EnumCampos.COLNAME.getValor(), Integer.valueOf(context.pathParam(EnumCampos.COLNAME.getValor())));
                    daoPermisos.buscaAsignacion(query, response->{
                        if (response.succeeded()){
                            if (response.result()!= null && !response.result().isEmpty()){
                                objetoToken.setExpMinutos(Integer.parseInt(objetoToken.getTiempo()));
                                objetoToken.setData(
                                        new JsonObject()
                                                .put(EnumCampos.COLNAME.getValor(), Integer.valueOf(context.pathParam(EnumCampos.COLNAME.getValor())))
                                                .put(EnumCampos.CURP.getValor(), data.getString(EnumCampos.CURP.getValor()))
                                );
                                generadorJWT.generarToken(objetoToken).setHandler(nuevoToken->{
                                    if (nuevoToken.succeeded()){
                                        context.response().setStatusCode(HttpResponseStatus.OK.code())
                                                .putHeader(EnumCampos.AUTHORIZATION.getValor(), EnumCampos.BEARER.getValor()+nuevoToken.result())
                                                .end(Json.encode(response.result().get(0)));
                                    }else{
                                        LOGGER.error("Error de creacion de token en ");
                                        context.response().setStatusCode(HttpResponseStatus.CONFLICT.code()).end();
                                    }
                                });
                            }else{
                                context.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end(Json.encode(response.result()));
                            }
                        }else{
                            LOGGER.error(response.cause());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });
                });
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro curp");
        }
    }
    
    /**
     * Métodos para realizar la consulta de las asignaciones creadas
     *
     * @param context
     */
    private void consultaAsignacion(RoutingContext context){
        if (!context.pathParam(EnumCampos.CURP.getValor()).isEmpty()){
            try{
                JsonObject query = context.getBodyAsJson().mergeIn(context.get(EnumCampos.DATA.getValor()));
                query.put(EnumCampos.CURP.getValor(), context.pathParam(EnumCampos.CURP.getValor()));
                daoPermisos.buscaAsignacion(query, response->{
                    if (response.succeeded()){
                        context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.OK.code()).end(Json.encode(response.result()));
                    }else{
                        LOGGER.error(response.cause());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                    }
                });  
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro curp");
        }
    }
    
    /**
     * Métodos para realizar el guardado de las asignaciones
     *
     * @param context
     */
    private void guardaAsignacion(RoutingContext context){
        if (!context.pathParam(EnumCampos.CURP.getValor()).isEmpty()){
            if (context.getBodyAsJson()!= null && !context.getBodyAsJson().isEmpty() &&
                context.getBodyAsJson().containsKey("perfiles") &&
                !context.getBodyAsJson().containsKey("_id") &&
                !context.getBodyAsJson().getJsonArray("perfiles").isEmpty()){
                try{
                    JsonObject query = context.getBodyAsJson().mergeIn(context.get(EnumCampos.DATA.getValor()));
                    query.put(EnumCampos.CURP.getValor(), context.pathParam(EnumCampos.CURP.getValor()));
                    daoPermisos.guardaAsignaciones(query, response->{
                        if (response.succeeded()){
                            context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.CREATED.code()).end(Json.encode(response.result()));
                        }else{
                            LOGGER.error(response.cause());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });   
                }catch(Exception e){
                    LOGGER.error(e);
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            }else{
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("No contiene perfiles o es vacio");
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro curp");
        }
    }
    
    /**
     * Métodos para realizar el guardado de las asignaciones
     *
     * @param context
     */
    private void actualizarAsignacion(RoutingContext context){
        if (!context.pathParam(EnumCampos.CURP.getValor()).isEmpty()){
            if (context.getBodyAsJson()!= null && !context.getBodyAsJson().isEmpty() &&
                    context.getBodyAsJson().containsKey("perfiles") &&
                    context.getBodyAsJson().containsKey("_id") &&
                    !context.getBodyAsJson().getJsonArray("perfiles").isEmpty() &&
                    !context.getBodyAsJson().getString("_id").isEmpty()){
                try{
                    JsonObject query = context.getBodyAsJson().mergeIn(context.get("data"));
                    daoPermisos.guardaAsignaciones(query, response->{
                        if (response.succeeded()){
                            context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.ACCEPTED.code()).end(Json.encode(response.result()));
                        }else{
                            LOGGER.error(response.cause());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });   
                }catch(Exception e){
                    LOGGER.error(e);
                    context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                }
            }else{
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("No contiene perfiles o es vacio");
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro curp");
        }
    }
    
    /**
     * Métodos para realizar la eliminacion de las asignaciones creadas
     *
     * @param context
     */
    private void eliminaAsignacion(RoutingContext context){
        if (!context.pathParam(EnumCampos.CURP.getValor()).isEmpty()){
            try{
                JsonObject query = context.getBodyAsJson().mergeIn(context.get(EnumCampos.DATA.getValor()));
                query.put(EnumCampos.CURP.getValor(), context.pathParam(EnumCampos.CURP.getValor()));
               daoPermisos.eliminaAsignaciones(query, response->{
                   if (response.succeeded()){
                       context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.OK.code()).end(Json.encode(response.result()));
                   }else{
                       LOGGER.error(response.cause());
                       context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                   }
               });  
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro curp");
        }
    }
    
    
    /**
     * Método para consultar asignacion por idusuario y o curp
     * @param context 
     */
    private void consultaAsignacionporIdUsuariooCurp(RoutingContext context){
        if (context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()) != null &&
                !context.pathParam(EnumCampos.COLNAME.getValor()).isEmpty() && 
                !context.pathParam(EnumCampos.CURP.getValor()).isEmpty() && 
                !context.pathParam(EnumCampos.ID_USUARIO.getValor()).isEmpty()){
            try{
                generadorJWT.obtenDatos(context.request().getHeader(EnumCampos.AUTHORIZATION.getValor()).replace(EnumCampos.BEARER.getValor(), EnumCampos.VACIO.getValor())).setHandler(datos->{
                    JsonObject query = 
                            new JsonObject().put(EnumCampos.SISTEMA.getValor(), datos.result().getUsuario())
                                    .put("$or", new JsonArray()
                                            .add(new JsonObject().put(EnumCampos.CURP.getValor(), context.pathParam(EnumCampos.CURP.getValor())))
                                            .add(new JsonObject().put(EnumCampos.ID_USUARIO.getValor(), Integer.valueOf(context.pathParam(EnumCampos.ID_USUARIO.getValor()))))
                                    ).put(EnumCampos.COLNAME.getValor(), Integer.valueOf(context.pathParam(EnumCampos.COLNAME.getValor())));
                    daoPermisos.buscaAsignacionCurpOYidUsuario(query, response->{
                        if (response.succeeded()){
                            context.response().setStatusCode(response.result()== null ? HttpResponseStatus.NO_CONTENT.code() :HttpResponseStatus.OK.code()).end(Json.encode(response.result()));
                        }else{
                            LOGGER.error(response.cause());
                            context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
                        }
                    });  
                });
            }catch(Exception e){
                LOGGER.error(e);
                context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
            }
        }else{
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Erro de parametro Authentication curp y idUsuario");
        }
    }
    
    /**
     * Se agregan los headers correspondientes para el Intercambio de recursos
     * de origen cruzado (CORS).
     *
     * @param routingContext: contexto
     * @return HttpServerResponse
     */
    public HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response().putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(ACCESS_CONTROL_ALLOW_METHODS, HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HEADER_AUTHORIZATION)                
                .putHeader(HEADER_CONTENT_TYPE, HEADER_CONTENT_APPLICATION_JSON);
    }

    /**
     * Headers permitidos para CORS
     *
     * @return Set(String): Headers permitidos en las peticiones
     */
    public static Set<String> getAllowedHeaders() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(Constantes.ACCESS_CONTROL_ALLOW_ORIGIN);
        allowedHeaders.add(Constantes.ACCESS_CONTROL_ALLOW_METHODS);
        allowedHeaders.add(Constantes.HEADER_CONTENT_TYPE);
        allowedHeaders.add(Constantes.ACCEPT);
        allowedHeaders.add(Constantes.HEADER_AUTHORIZATION);
        return allowedHeaders;
    }
    public static Set<HttpMethod> getAllowedMethods() {
        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.PUT);
        allowedMethods.add(HttpMethod.OPTIONS);
        return allowedMethods;
    }
    

    /**
     * Main para pruebas locales
     *
     * @param args
     */
    public static void main(String[] args) {
        Vertx vertex = Vertx.vertx();
        vertex.deployVerticle(new ServerLauncher());
    }

}