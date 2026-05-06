/**
 * @(#)GeneradorJWT.java 02/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import mx.gob.sfp.dgti.dto.DatosDTO;
import mx.gob.sfp.dgti.util.EnumCampos;

/**
 * Clase con las validaciones de el domicilio del declarante.
 *
 * @author pavel.martinez
 * @since 26/11/2019
 */
public class GeneradorJWT {

    /**
     * Llave publica
     */
    private final String llavePublica =System.getenv("LLAVE_PUBLICA");

    /**
     * Llave privada
     */
    private final String llavePrivada = System.getenv("LLAVE_PRIVADA");
    /**
     * Provider
     */
    private final JWTAuth provider;

    /**
     * El logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneradorJWT.class);

    /**
     * Constructor del GeneradorJWT
     *
     * @param elVertx
     */
    public GeneradorJWT(Vertx elVertx) {

        LOGGER.info("==== SE CREA EL PROVIDER PARA GENERAR JWT");

        JWTAuthOptions opciones = new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm(EnumCampos.ALGORITMO_RS256.getValor())
                        .setPublicKey(llavePublica)
                        .setSecretKey(llavePrivada)
                );

        provider = JWTAuth.create(elVertx, opciones);

    }

    /**
     * Metodo para generar el token de jwt
     *
     * @param datos para agregar en el payload
     *
     * @return Token nuevo
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    public Future<String> generarToken(DatosDTO datos) {
        Promise<String> promise = Promise.promise();
        String token = provider.generateToken(
                new JsonObject().put(EnumCampos.DATA.getValor(), datos.getData())
                .put(EnumCampos.USUARIO.getValor(), datos.getUsuario())
                .put(EnumCampos.TIEMPO.getValor(), datos.getExpMinutos()),
                new JWTOptions()
                .setAlgorithm(EnumCampos.ALGORITMO_RS256.getValor())
                .setExpiresInMinutes(datos.getExpMinutos())
                .setIssuer(datos.getIss()));
        promise.complete(token);
        return promise.future();
    }
    
    /**
     * Metodo para renovar un token de jwt
     *
     * @param token: token que será renovado
     *
     * @return token renovado
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    public Future<String> renovarToken(String token) {
        Promise<String> promise = Promise.promise();
        provider.authenticate(
                new JsonObject().put(EnumCampos.JWT.getValor(), token),
                res -> {
                    if (res.succeeded()) {
                        JsonObject usuario = res.result().principal();

                        LOGGER.info(usuario);
                        if (usuario.getInteger(EnumCampos.TIEMPO.getValor()) == null
                        || usuario.getString(EnumCampos.ISS.getValor()) == null) {
                            promise.complete(EnumCampos.VACIO.getValor());
                            return;
                        }
                        String nuevoToken = provider.generateToken(
                                usuario,
                                new JWTOptions()
                                .setAlgorithm(EnumCampos.ALGORITMO_RS256.getValor())
                                .setExpiresInMinutes(usuario.getInteger(EnumCampos.TIEMPO.getValor()))
                                .setIssuer(usuario.getString(EnumCampos.ISS.getValor())));

                        promise.complete(nuevoToken);
                    } else {
                        LOGGER.info(res.cause());
                        promise.complete(EnumCampos.VACIO.getValor());
                    }
                });

        return promise.future();
    }
    
    /**
     * Metodo para renovar un token de jwt
     *
     * @param token: token que será renovado
     *
     * @return token renovado
     *
     * @author cgarias
     * @since 03/03/2021
     */
    public Future<DatosDTO> obtenDatos(String token) {
        Promise<DatosDTO> promise = Promise.promise();
        provider.authenticate(
                new JsonObject().put(EnumCampos.JWT.getValor(), token),
                res -> {
                    if (res.succeeded()) {
                        JsonObject datos = res.result().principal();
                        LOGGER.info(datos);
                        promise.complete(
                                datos.mapTo(DatosDTO.class)
                        );
                    } else {
                        LOGGER.info(res.cause());
                        promise.fail("Errro Undecode pincipal to DatosDTO.class");
                    }
                });
        return promise.future();
    }
    
    /**
     * Metodo para renovar un token de jwt
     *
     * @param token: token que será renovado
     *
     * @return token renovado
     *
     * @author cgarias
     * @since 03/03/2021
     */
    public Future<JsonObject> obtenDatosToken(String token) {
        Promise<JsonObject> promise = Promise.promise();
        provider.authenticate(
                new JsonObject().put(EnumCampos.JWT.getValor(), token),
                res -> {
                    if (res.succeeded()) {
                        promise.complete(res.result().principal().getJsonObject(EnumCampos.DATA.getValor()));
                    } else {
                        LOGGER.info(res.cause());
                        promise.fail(EnumCampos.VACIO.getValor());
                    }
                });
        return promise.future();
    }
    
    /**
     * Metodo para renovar un token de jwt
     *
     * @param token: token que será renovado
     *
     * @return token renovado
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    public Future<JsonObject> renovarTokenPermisos(String token) {
        Promise<JsonObject> promise = Promise.promise();
        provider.authenticate(
                new JsonObject().put(EnumCampos.JWT.getValor(), token),
                res -> {
                    if (res.succeeded()) {
                        JsonObject usuario = res.result().principal();

                        LOGGER.info(usuario);
                        if (usuario.getInteger(EnumCampos.TIEMPO.getValor()) == null
                        || usuario.getString(EnumCampos.ISS.getValor()) == null) {
                            promise.complete(new JsonObject());
                            return;
                        }
                        String nuevoToken = provider.generateToken(
                                usuario,
                                new JWTOptions()
                                .setAlgorithm(EnumCampos.ALGORITMO_RS256.getValor())
                                .setExpiresInMinutes(usuario.getInteger(EnumCampos.TIEMPO.getValor()))
                                .setIssuer(usuario.getString(EnumCampos.ISS.getValor())));
                        promise.complete(
                                new JsonObject()
                                        .put("token", nuevoToken)
                                        .put(token, usuario.getJsonObject(EnumCampos.DATA.getValor())));
                    } else {
                        LOGGER.info(res.cause());
                        promise.complete(new JsonObject());
                    }
                });

        return promise.future();
    }

    /**
     * Metodo para validar un token de jwt
     *
     * @param token: token que será renovado
     *
     * @return true o false si se valido o no el token
     *
     * @author pavel.martinez
     * @since 26/12/2019
     */
    public Future<Boolean> validarToken(String token) {
        Promise<Boolean> promise = Promise.promise();
        LOGGER.info(token);
        provider.authenticate(
                new JsonObject().put(EnumCampos.JWT.getValor(), token),
                res -> {
                    if (res.succeeded()) {

                        promise.complete(Boolean.TRUE);
                    } else {
                        LOGGER.info(res.cause());
                        promise.complete(Boolean.FALSE);
                    }
                });
        return promise.future();
    }

    public JWTAuth getProvider() {
        return provider;
    }

}