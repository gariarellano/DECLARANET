package mx.gob.sfp.dgti.persistence.impl;

import static mx.gob.sfp.dgti.util.Constantes.IP_DOMINIO_FRONT;
import static mx.gob.sfp.dgti.util.Constantes.IP_DOMINIO_IDENTIDAD;
import static mx.gob.sfp.dgti.util.Constantes.REGISTRO_APLICACION;
import static mx.gob.sfp.dgti.util.Constantes.REGISTRO_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_INCONSISTENTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_NO_EXISTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_NO_VALIDADO;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_VALIDADO;
import static mx.gob.sfp.dgti.util.Constantes.TOKEN_BEARER_KEY;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AsyncResult;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.rxjava.ext.unit.Async;
import mx.gob.sfp.dgti.entity.AplicacionEO;
import mx.gob.sfp.dgti.entity.BitacoraEO;
import mx.gob.sfp.dgti.entity.ConfirmacionEO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.persistence.DatabaseService;
import mx.gob.sfp.dgti.util.AutenticacionHelper;
import mx.gob.sfp.dgti.util.Constantes;
import mx.gob.sfp.dgti.util.EncripDecla;
import mx.gob.sfp.dgti.util.ValidacionHelper;

/**
 * Implementacion de DatabaseService
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 20/03/2019
 */
public class DatabaseServiceImpl implements DatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseServiceImpl.class);
    private static final String DOMINIO_FRONT = System.getenv(IP_DOMINIO_FRONT) != null ? System.getenv(IP_DOMINIO_FRONT) : AutenticacionHelper.obtenerPropiedad("ip.dominio.front");
    private static final String DOMINIO_IDENTIDAD = System.getenv(IP_DOMINIO_IDENTIDAD) != null ? System.getenv(IP_DOMINIO_IDENTIDAD) : "";
    private static final String QUERY_DATAOS_LIKE = "SELECT iu.id_usuario \"idUsuario\", iu.nombre, iu.primer_apellido \"primerApellido\", iu.segundo_apellido \"segundoApellido\", iu.curp, iu.rfc || iu.homocve \"rfc\", validado_renapo \"validadoRenapo\"  FROM ip_usuario iu  INNER JOIN ip_vinculacion_usuario_aplicacion ivua on ivua.id_usuario = iu.id_usuario  INNER JOIN ip_aplicacion ia on ia.id_aplicacion = ivua.id_aplicacion  WHERE ia.id_aplicacion = ? and ia.secretkey = ?  and iu.";
    private static final Integer IP_FRONT = -1;
    private static final String NO_EXISTE = "No existe";
    private static final Integer PROCESO_REESTABLECER_PWD = 1;
    private static final String ID_USUARIO = "idUsuario";
    private static final String NOMBRE = "nombre";
    private static final String PRIMER_APELLIDO = "primerApellido";
    private static final String SEGUNDO_APELLIDO = "segundoApellido";
    private static final String EMAIL = "email";
    private static final String EMAIL_ALT = "emailAlt";
    private static final String CURP = "curp";
    private SQLClient postgreSQL;
    private static final String USER_AUTOMATIC_ACTIVATION = "USER_AUTOMATIC_ACTIVATION";
    

    /**
     * Constructor sin parámetros
     */
    public DatabaseServiceImpl() {
        // Constructor DatabaseServiceImpl
    }

    /**
     * Constructor con parámetros
     *
     * @param vertx
     * @param configuracion para la conexión a la BD
     */
    public DatabaseServiceImpl(Vertx vertx, JsonObject configuracion) {
        this.postgreSQL = PostgreSQLClient.createShared(vertx, configuracion);
    }

    /**
     * {@inheritDoc}
     */
    public Future<Void> guardarUsuario(UsuarioEO usuario) {
        Future<Void> future = Future.future();
        StringBuilder sql = new StringBuilder();

        this.obtenerSecuenciaUsuario().setHandler(secuenciaLogin -> {
            postgreSQL.close();
            if (secuenciaLogin.succeeded()) {
                String pwdEnc = EncripDecla.digestPassword(String.valueOf(secuenciaLogin.result().longValue()), usuario.getPwdEnc().toUpperCase());
                usuario.setPwdEnc(pwdEnc);
                sql.append("INSERT INTO IP_USUARIO (login, pwd_enc, nombre, primer_apellido, segundo_apellido, curp, rfc, homocve, num_celular, email_alt, email, validado_renapo, fecha_registro, activo) ")
                        .append(" VALUES ( ")
                        .append(secuenciaLogin.result().longValue())
                        .append(", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_usuario");
                postgreSQL.queryWithParams(sql.toString(), obtenerJsonUsuarioI(usuario), resultado -> {
                    postgreSQL.close();
                    if (resultado.failed()) {
                        LOGGER.error("Error guardar en usuario " + usuario.toString() + resultado.cause().getMessage());
                        future.fail(resultado.cause());
                    } else {
                        this.guardarBitacora(IP_FRONT, new Date(), usuario.getCurp(), REGISTRO_USUARIO).setHandler(res -> {
                            if (res.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(res.cause());
                            }
                        });
                    }
                }
                );
            } else {
                LOGGER.error("Error obtenerSecuenciaUsuario" + secuenciaLogin.cause().getMessage());
                future.fail(secuenciaLogin.cause());
            }
        });
        return future;
    }

    /**
     * Método para obtener el valor de la secuencia de la tabla de usuarios
     *
     * @return generated Future<Long>
     */
    private Future<Long> obtenerSecuenciaUsuario() {
        Future<Long> future = Future.future();
        postgreSQL.queryWithParams("select nextval('ip_usuario_login_seq')", new JsonArray(), resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.handle(resultado.map(rs -> {
                    return (Long) rs.getRows().get(0).getValue("nextval");
                }));
            } else {
                LOGGER.error("Error obtenerSecuenciaUsuario " + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * Metodo para guardar en bitacora
     *
     * @return generated Future<BitacoraEO>
     */
    public Future<BitacoraEO> guardarBitacora(Integer idAplicacion, Date fecha, String curp, Integer accion) {
        Future<BitacoraEO> future = Future.future();
        postgreSQL.updateWithParams(validarSqlBitacora(idAplicacion, curp, accion),
                obtenerJsonBitacora(idAplicacion, fecha, curp, accion), resultado -> {
                    postgreSQL.close();
                    if (resultado.failed()) {
                        LOGGER.error("Error guardarBitacora " + idAplicacion + "-" + curp + resultado.cause());
                        future.fail(resultado.cause());
                    } else {
                        future.handle(resultado.map(res -> new BitacoraEO(res.toJson())));
                    }
                });
        return future;
    }

    /**
     * Metodo para obtener el json de parametros para el query de la tabla
     * aplicacion
     *
     * @param dominio de la app cliente
     * @param idAplicacion id de la app cliente
     * @param secretKey de la aplicación
     * @return JsonArray con los parámetros para la consulta
     */
    private JsonArray obtenerJsonAplicacion(AplicacionEO aplicacion) {
        JsonArray json = new JsonArray();
        json.add(aplicacion.getNombre());
        json.add(aplicacion.getSecretKey());
        json.add(aplicacion.getUrl());
        json.add(aplicacion.getDominio());
        return json;
    }

    /**
     * Metodo para validar el sql al guardar la bitacora
     *
     * @param idAplicacion de la app cliente
     * @param curp del usuario
     * @param accion a realizar
     * @return String: cadena con el query a ejecutar
     */
    private String validarSqlBitacora(Integer idAplicacion, String curp, Integer accion) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO IP_BITACORA (");
        StringBuilder parametros = new StringBuilder();
        if (ValidacionHelper.isNotNull(idAplicacion)) {
            sql.append("id_aplicacion,");
            parametros.append(" ?, ");
        }
        if (ValidacionHelper.isNotNull(curp)) {
            sql.append("curp,");
            parametros.append(" ?, ");
        }
        sql.append("accion,");
        sql.append(" fecha ");
        sql.append(") VALUES (" + parametros + " ?,?)");
        return sql.toString();
    }

    /**
     * Metodo para obtener el json de parametros para el query de la tabla
     * bitacora
     *
     * @param curp del usuario
     * @param password del usuario
     * @return JsonArray con los parámetros para la consulta
     */
    private JsonArray obtenerJsonBitacora(Integer idAplicacion, Date fecha, String curp, Integer accion) {
        JsonArray json = new JsonArray();
        if (ValidacionHelper.isNotNull(idAplicacion)) {
            json.add(idAplicacion);
        }
        if (ValidacionHelper.isNotNull(curp)) {
            json.add(curp);
        }
        json.add(accion);
        json.add(ValidacionHelper.getFechaFormatoISO8601(fecha));
        return json;
    }

    /**
     * Crear el json para registrar un usuario
     *
     * @param usuario: objeto que contiene las propiedades del usuario
     * @return {@link JsonArray} con los parámetros para la consulta
     */
    private JsonArray obtenerJsonUsuarioI(UsuarioEO usuario) {
        JsonArray json = new JsonArray();
        json.add(usuario.getPwdEnc());
        json.add(usuario.getNombre());
        json.add(usuario.getPrimerApellido());
        json.add(usuario.getSegundoApellido());
        json.add(usuario.getCurp());
        json.add(usuario.getRfc());
        json.add(usuario.getHomoclave());
        json.add(usuario.getNumCelular());
        json.add(usuario.getEmailAlt());
        json.add(usuario.getEmail());
        json.add(usuario.getValidadoRenapo() == null ? RENAPO_ESTATUS_NO_VALIDADO : RENAPO_ESTATUS_VALIDADO);
        json.add(ValidacionHelper.getFechaFormatoISO8601(new Date()));
        json.add(System.getenv(USER_AUTOMATIC_ACTIVATION) != null ? Constantes.ACTIVO : Constantes.INACTIVO);
        return json;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> guardarAplicacion(AplicacionEO aplicacion) {
        Future<Void> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO IP_APLICACION (nombre, secretkey, url, dominio) VALUES (?,?,?,?) RETURNING id_aplicacion");

        postgreSQL.queryWithParams(sql.toString(), obtenerJsonAplicacion(aplicacion), resultado -> {
            postgreSQL.close();
            if (resultado.failed()) {
                LOGGER.error("Error guardarAplicacion " + resultado.cause().getMessage());
                future.fail(resultado.cause());
            } else {
                ResultSet result = resultado.result();
                this.guardarBitacora(result.getResults().get(0).getInteger(0), new Date(), null, REGISTRO_APLICACION).setHandler(res -> {
                    if (res.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(res.cause());
                    }
                });
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<JsonObject> consultarUsuarioVinculado(String origen, String curp, Integer idAplicacion, String secretKey) {

        Future<JsonObject> future = Future.future();

        postgreSQL.queryWithParams(validarSqlConsultaUsuario(origen), obtenerJsonConsultaUsuario(curp, origen, idAplicacion, secretKey), resultado -> {
            postgreSQL.close();
            LOGGER.info("Resultado query: " + resultado.result().toString());
            if (resultado.succeeded()) {
                future.handle(resultado.map(rs -> {
                    if (rs.getRows().isEmpty()) {
                        LOGGER.info("Sin datos consultarUsuarioVinculado: " + curp + " - idAplicacion: " + idAplicacion + " secretkey:" + secretKey);
                        throw new NoSuchElementException(NO_EXISTE);
                    } else {
                        return rs.getRows().get(0);
                    }
                }));
            } else {
                LOGGER.error("Error consultarUsuarioVinculado " + curp + "-" + idAplicacion + "-" + secretKey + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<UsuarioEO>> obtenerCurpsNoValidadas() {
        Future<List<UsuarioEO>> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT iu.id_usuario \"idUsuario\", iu.nombre, iu.primer_apellido \"primerApellido\", iu.segundo_apellido \"segundoApellido\", iu.curp, iu.validado_renapo \"validadoRenapo\"")
                .append(" FROM ip_usuario iu WHERE iu.validado_renapo = '0'");

        postgreSQL.queryWithParams(sql.toString(), new JsonArray(), resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.handle(resultado.map(rs -> {
                    List<JsonObject> registros = rs.getRows();
                    if (registros.isEmpty()) {
                        throw new NoSuchElementException(NO_EXISTE);
                    } else {
                        ObjectMapper mapper = new ObjectMapper();
                        List<UsuarioEO> usuarios = null;
                        try {
                            usuarios = mapper.readValue(rs.getRows().toString(), new TypeReference<List<UsuarioEO>>() {
                            });
                        } catch (IOException e) {
                            LOGGER.error(e.getCause().getMessage());
                        }
                        return usuarios;
                    }
                })
                );
            } else {
                LOGGER.error("Error obtenerCurpsNoValidadas " + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> modificarUsuario(UsuarioEO usuario) {
        Future<Void> future = Future.future();
        LOGGER.info("Modificar validado_renapo::: " + usuario.getIdUsuario() + usuario.getValidadoRenapo());
        String sql = "UPDATE ip_usuario SET validado_renapo = ? WHERE id_usuario = ?";

        postgreSQL.queryWithParams(sql, new JsonArray().add(usuario.getValidadoRenapo()).add(usuario.getIdUsuario()), resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.complete();
            } else {
                LOGGER.error("Error modificarUsuario " + usuario.toString() + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<UsuarioEO> consultarUsuarioPorCurp(String curp) {
        Future<UsuarioEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_usuario as \"idUsuario\", login, pwd_enc as \"pwdEnc\", nombre, primer_apellido as \"primerApellido\",")
                .append(" segundo_apellido as \"segundoApellido\", curp, rfc, homocve as \"homoclave\", num_celular as \"numCelular\", ")
                .append(" email, email_alt as \"emailAlt\", validado_renapo as \"validadoRenapo\", fecha_registro as \"fechaRegistro\", activo")
                .append(" FROM ip_usuario iu WHERE iu.curp = ?");
        postgreSQL.queryWithParams(sql.toString(), new JsonArray().add(curp), resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.handle(resultado.map(rs -> {
                    List<JsonObject> registros = rs.getRows();
                    if (registros.isEmpty()) {
                        LOGGER.error("Sin datos consultarUsuarioPorCurp: " + curp);
                        throw new NoSuchElementException(NO_EXISTE);
                    } else {
                        return new UsuarioEO(registros.get(0));
                    }
                }));
            } else {
                LOGGER.error("Error consultarUsuarioPorCurp " + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseService consultarUsuarioTelegram(String curp, String idTelegram, Handler<AsyncResult<JsonObject>> handlerResult){
        String queryJoinTel = "select U.ID_USUARIO as \"idUsuario\", U.EMAIL as \"email\", U.EMAIL_ALT as \"emailAlt\", N.ID_TELEGRAM as \"idTelegram\", N.TELEGRAM_ACTIVO as \"telegramActivo\" from ip_usuario U  left join ip_notificaciones N on U.id_usuario = N.id_usuario where U.CURP = ?";
        String queryDatTel = "select ID_USUARIO as \"idUsuarioT\", ID_TELEGRAM as \"idTelegramT\", TELEGRAM_ACTIVO as \"telegramActivoT\" from ip_notificaciones where id_telegram = ?";
        consultaDatosTelegram(queryJoinTel, new JsonArray().add(curp), localizaDoJoin->{
            if (localizaDoJoin.succeeded()){
                consultaDatosTelegram(queryDatTel, new JsonArray().add(idTelegram), localizadoDatTel->{
                    if (localizadoDatTel.succeeded()){
                        handlerResult.handle(Future.succeededFuture(localizaDoJoin.result().mergeIn(localizadoDatTel.result())));
                    }else{
                        handlerResult.handle(Future.failedFuture(localizadoDatTel.cause()));
                    }
                });
            }else{
                handlerResult.handle(Future.failedFuture(localizaDoJoin.cause()));
            }
        });
        return this;
    }
    
    private DatabaseServiceImpl consultaDatosTelegram(String query, JsonArray params, Handler<AsyncResult<JsonObject>> handlerResult){
        postgreSQL.queryWithParams(query, params, localizado->{
            postgreSQL.close();
            if (localizado.succeeded()){
                if (localizado.result().getRows().isEmpty()){
                    handlerResult.handle(Future.succeededFuture(new JsonObject()));
                }else{
                    handlerResult.handle(Future.succeededFuture(localizado.result().getRows().get(0)));
                }
            }else{
                LOGGER.error("erro en query "+query+" error : "+localizado.cause());
                handlerResult.handle(Future.failedFuture("Error en consulta."));
            }
        });
        return this;
    }
    
    /**
     * @{inheritDoc}
     */
    @Override
    public DatabaseService guardaUsuarioTelegram(JsonObject datos, Handler<AsyncResult<JsonObject>> handlerResult){
        String sql;
        String operacion;
        JsonArray params;
        if (datos.getLong("idTelegramHisto") != null && datos.getLong("idTelegramT")==null){            
            sql = "UPDATE ip_notificaciones set id_telegram = ? , fecha_registro = now() where  id_usuario = ?";
            params = new JsonArray().add(datos.getString("idTelegram")).add(datos.getInteger("idUsuario"));
            operacion = "actualia id_telegram";
            ejecutaTransactionTelegram(operacion, sql, params, handlerResult);
        }else{
            if (datos.getLong("idTelegramT")!=null){
                sql = "DELETE FROM ip_notificaciones WHERE ID_TELEGRAM = ? ";
                params = new JsonArray().add(datos.getString("idTelegram"));
                operacion = "inserta registro nuevo";
                ejecutaTransactionTelegram(operacion, sql, params, delete->{
                    if (delete.succeeded()){
                        String sqlIn = "INSERT INTO ip_notificaciones (ID_USUARIO, id_telegram, telegram_activo, fecha_registro) VALUES (?, ?, 0, now())";
                        JsonArray paramsIn = new JsonArray().add(datos.getInteger("idUsuario")).add(datos.getString("idTelegram"));
                        String operacionin = "inserta registro nuevo";
                        ejecutaTransactionTelegram(operacionin, sqlIn, paramsIn, handlerResult);
                    }else{
                        handlerResult.handle(Future.failedFuture(delete.cause()));
                    }
                });
                
            }else{
                sql = "INSERT INTO ip_notificaciones (ID_USUARIO, id_telegram, telegram_activo, fecha_registro) VALUES (?, ?, 0, now())";
                params = new JsonArray().add(datos.getInteger("idUsuario")).add(datos.getString("idTelegram"));
                operacion = "inserta registro nuevo";
                ejecutaTransactionTelegram(operacion, sql, params, handlerResult);
            }
        }
        return this;
    }
    
    private DatabaseServiceImpl ejecutaTransactionTelegram(String operacion, String sql, JsonArray params, Handler<AsyncResult<JsonObject>> respuesta){
        postgreSQL.queryWithParams(sql, params, resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                respuesta.handle(Future.succeededFuture());
            } else {
                LOGGER.error("Failed telegram" + params.toString()+" - OPERACION: "+operacion+"  -  "+ resultado.cause());
                respuesta.handle(Future.failedFuture(resultado.cause()));
            }
        });
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<UsuarioEO> obtenerUsuarioPorCurp(String curp) {
        Future<UsuarioEO> future = Future.future();

        this.consultarUsuarioPorCurp(curp).setHandler(resultado -> {
            if (resultado.failed()) {
                LOGGER.error("Error obtenerUsuarioPorCurp " + curp + resultado.cause().getMessage());
                future.fail(resultado.cause());
            } else {
                UsuarioEO usuario = new UsuarioEO();
                usuario.setIdUsuario(resultado.result().getIdUsuario());
                usuario.setNombre(resultado.result().getNombre());
                usuario.setPrimerApellido(resultado.result().getPrimerApellido());
                usuario.setSegundoApellido(resultado.result().getSegundoApellido());
                usuario.setEmail(resultado.result().getEmail());
                usuario.setEmailAlt(resultado.result().getEmailAlt());
                usuario.setCurp(resultado.result().getCurp());
                future.complete(usuario);
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<UsuarioEO> obtenerUsuarioPorCurp(String curp, String rfc) {
        Future<UsuarioEO> future = Future.future();
        JsonArray parametros = new JsonArray();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT id_usuario as \"idUsuario\", nombre, primer_apellido as \"primerApellido\" ")
                .append(", segundo_apellido as \"segundoApellido\", curp, email, email_alt as \"emailAlt\" ")
                .append(" FROM ip_usuario iu ");

        if (curp != null) {
            sql.append(" WHERE iu.curp = ? ");
            parametros.add(curp);
        }

        if (rfc != null) {
            sql.append(" WHERE iu.rfc = ? and iu.homocve = ?");
            parametros.add(rfc.substring(0, 10));
            parametros.add(rfc.substring(10, 13));
        }

        LOGGER.info(sql.toString());
        LOGGER.info(parametros);

        postgreSQL.queryWithParams(sql.toString(), parametros, resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.handle(resultado.map(rs -> {
                    List<JsonObject> registros = rs.getRows();
                    if (registros.isEmpty()) {
                        LOGGER.error("Sin datos consultarUsuarioPorCurp: " + curp);
                        throw new NoSuchElementException(NO_EXISTE);
                    } else {
                        UsuarioEO usuario = new UsuarioEO();
                        usuario.setIdUsuario(registros.get(0).getInteger(ID_USUARIO));
                        usuario.setNombre(registros.get(0).getString(NOMBRE));
                        usuario.setPrimerApellido(registros.get(0).getString(PRIMER_APELLIDO));
                        usuario.setSegundoApellido(registros.get(0).getString(SEGUNDO_APELLIDO));
                        usuario.setCurp(registros.get(0).getString(CURP));
                        usuario.setEmail(registros.get(0).getString(EMAIL));
                        usuario.setEmailAlt(registros.get(0).getString(EMAIL_ALT));
                        return usuario;
                    }
                }));
            } else {
                LOGGER.error("Error consultarUsuarioPorCurp " + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> actualizarPwd(UsuarioEO usuario, Integer idConfirmacion) {
        Future<Void> future = Future.future();
        this.consultarUsuarioPorCurp(usuario.getCurp()).setHandler(resultadoCurp -> {
            String pwdEnc = EncripDecla.digestPassword(resultadoCurp.result().getLogin(), usuario.getPwdEnc().toUpperCase());
            usuario.setPwdEnc(pwdEnc);
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE IP_USUARIO SET pwd_enc = ?  ")
                    .append("WHERE IP_USUARIO.ID_USUARIO = ? ");
            postgreSQL.queryWithParams(sql.toString(), new JsonArray().add(pwdEnc).add(resultadoCurp.result().getIdUsuario()), resultado -> {
                postgreSQL.close();
                if (resultado.succeeded()) {
                    future.complete();
                    if(idConfirmacion != null){
                        this.eliminarConfirmacion(idConfirmacion);
                    }
                } else {
                    LOGGER.error("Error actualizarPwd " + usuario.toString() + idConfirmacion + resultado.cause());
                    future.fail(resultado.cause());
                }
            });
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Integer> consultarCurpYRFC(String curp, String rfc) {
        Future<Integer> future = Future.future();
        JsonArray json = new JsonArray();
        StringBuilder sql = new StringBuilder();

        sql.append("select count(0) as existe from ip_usuario where ");

        if (curp != null) {
            sql.append(" curp = ? ");
            json.add(curp);
        }
        if (curp != null && rfc != null) {
            sql.append(" or ");
        }
        if (rfc != null) {
            sql.append(" (rfc = ? and homocve = ?)");
            json.add(rfc.substring(0, 10));
            json.add(rfc.substring(10, 13));
        }

        postgreSQL.queryWithParams(sql.toString(), json, resultado -> {
            postgreSQL.close();
            if (resultado.failed()) {
                LOGGER.error("Failed consultarCurpYRFC " + curp + "-" + rfc + resultado.cause());
                future.fail(resultado.cause());
            } else {
                future.handle(resultado.map(rs -> {
                    return rs.getRows().get(0).getInteger("existe");
                }));
            }
        });
        return future;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<JsonObject>> consultarLike(StringBuilder where, JsonArray aparametros) {
        Future<List<JsonObject>> future = Future.future();
        postgreSQL.queryWithParams(QUERY_DATAOS_LIKE.concat(where.toString()), aparametros, resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.handle(Future.succeededFuture(resultado.result().getRows()));
            } else {
                LOGGER.error(String.format("Failed consultarLike %s",  resultado.cause()));
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> guardarConfirmacion(Integer idUsuario, String token) {
        Future<Void> future = Future.future();
        String sql = "INSERT INTO IP_CONFIRMACION (ID_USUARIO, FECHA, TOKEN, PROCESO) VALUES (?, now(), ?, ?)";
        postgreSQL.queryWithParams(sql, new JsonArray().add(idUsuario).add(token).add(PROCESO_REESTABLECER_PWD), resultado -> {
            postgreSQL.close();
            if (resultado.failed()) {
                LOGGER.error("Failed guardarConfirmacion " + idUsuario + "-" + token + resultado.cause());
                future.fail(resultado.cause());
            } else {
                future.complete();
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<ConfirmacionEO> obtenerConfirmacion(String curp, String token) {
        String tokenR = token.replace(TOKEN_BEARER_KEY, "");
        Future<ConfirmacionEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ipc.id_confirmacion \"idConfirmacion\", ipc.id_usuario \"idUsuario\", ipc.fecha, ipc.token ")
                .append(" FROM IP_CONFIRMACION ipc inner join IP_USUARIO ipu on IPU.id_usuario = ipc.id_usuario")
                .append(" WHERE ipu.curp = ? and ipc.proceso = ? and ipc.token = ?");
        postgreSQL.queryWithParams(sql.toString(), new JsonArray().add(curp).add(PROCESO_REESTABLECER_PWD).add(tokenR), resultado -> {
            postgreSQL.close();
            if (resultado.succeeded()) {
                future.handle(
                        resultado.map(rs -> {
                            List<JsonObject> registros = rs.getRows();
                            if (registros.isEmpty()) {
                                LOGGER.error("Sin datos obtenerConfirmacion: " + curp + token);
                                throw new NoSuchElementException(NO_EXISTE);
                            } else {
                                return new ConfirmacionEO(registros.get(0));
                            }
                        })
                );
            } else {
                LOGGER.error("Error obtenerConfirmacion " + curp + "-" + token + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> eliminarConfirmacion(Integer idConfirmacion) {
        Future<Void> future = Future.future();
        postgreSQL.queryWithParams("DELETE FROM IP_CONFIRMACION WHERE id_confirmacion = ?",
                new JsonArray().add(idConfirmacion), result -> {
                    postgreSQL.close();
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        LOGGER.error("Error eliminarConfirmacion " + idConfirmacion);
                        future.fail(result.cause());
                    }
                });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> editarUsuario(UsuarioEO usuario, Integer estatusValidadoRenapo, String curp) {
        Future<Void> future = Future.future();
        postgreSQL.queryWithParams(obtenerSqlEditarUsuario(usuario, estatusValidadoRenapo),
                obtenerJsonEditarUsuario(usuario, estatusValidadoRenapo, curp), result -> {
                    postgreSQL.close();
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        LOGGER.error("Error editarUsuario " + usuario.toString() + curp + result.cause());
                        future.fail(result.cause());
                    }
                });
        return future;
    }

    /**
     * Crear el json para editar un usuario
     *
     * @param usuario: objeto con los datos del usuario
     * @param estatusValidadoRenapo: estatus de la validación con renapo
     * @param curp del usuario
     * @return {@link JsonArray} con los parámetros para la consulta
     */
    private JsonArray obtenerJsonEditarUsuario(UsuarioEO usuario, Integer estatusValidadoRenapo, String curp) {
        JsonArray json = new JsonArray();
        json.add(RENAPO_ESTATUS_NO_VALIDADO);

        if (RENAPO_ESTATUS_NO_EXISTE.equals(estatusValidadoRenapo)) {
            json.add(usuario.getCurp());
        }
        if (RENAPO_ESTATUS_INCONSISTENTE.equals(estatusValidadoRenapo)) {
            json.add(usuario.getNombre());
            json.add(usuario.getPrimerApellido());
            json.add(usuario.getSegundoApellido());
        }
        json.add(curp);
        return json;
    }

    /**
     * Obtener el sql para editar un usuario
     *
     * @param usuario: objeto que contiene los datos del usuario
     * @param estatusValidadoRenapo estatus de la validación con renapo
     * @return String: cadena con el query
     */
    private String obtenerSqlEditarUsuario(UsuarioEO usuario, Integer estatusValidadoRenapo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE IP_USUARIO SET validado_renapo = ?, ");

        if (RENAPO_ESTATUS_NO_EXISTE.equals(estatusValidadoRenapo)) {
            sql.append(" curp = ? ");
        }
        if (RENAPO_ESTATUS_INCONSISTENTE.equals(estatusValidadoRenapo)) {
            sql.append(" nombre = ?, primer_apellido = ?, segundo_apellido = ? ");
        }
        sql.append(" WHERE curp = ? ");
        return sql.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<UsuarioEO> editarCurpYEmailsDeUsuario(UsuarioEO usuario, String curp) {
        Future<UsuarioEO> future = Future.future();
        StringBuilder sql = new StringBuilder("UPDATE IP_USUARIO SET ");
        JsonArray json = new JsonArray();

        if (usuario.getCurp() != null) {
            sql.append("curp = ?, validado_renapo = ?");
            json.add(usuario.getCurp()).add(RENAPO_ESTATUS_NO_VALIDADO);
        }
        if (usuario.getEmail() != null) {
            sql.append(usuario.getCurp() != null ? "," : "");
            sql.append("email = ?");
            json.add(usuario.getEmail());
        }
        if (usuario.getEmailAlt() != null) {
            sql.append(usuario.getCurp() != null || usuario.getEmail() != null ? "," : "");
            sql.append("email_alt = ?");
            json.add(usuario.getEmailAlt());
        }
        sql.append(" WHERE id_usuario = ? RETURNING id_usuario as \"idUsuario\", email, email_alt as \"emailAlt\"");
        json.add(usuario.getIdUsuario());

        postgreSQL.queryWithParams(sql.toString(), json, result -> {
            postgreSQL.close();

            if (result.succeeded()) {
                future.handle(
                        result.map(rs -> {
                            if (rs.getRows().isEmpty()) {
                                LOGGER.error("Sin datos editarCurpYEmailsDeUsuario: " + curp + usuario);
                                throw new NoSuchElementException(NO_EXISTE);
                            } else {
                                return new UsuarioEO(Integer.parseInt(rs.getRows().get(0).getValue("idUsuario").toString()),
                                        rs.getRows().get(0).getString("email"),
                                        rs.getRows().get(0).getString("emailAlt"));
                            }
                        })
                );

            } else {
                LOGGER.error("Error editarCurpYEmailsDeUsuario " + curp + usuario.toString());
                future.fail(result.cause());
            }
        });
        return future;
    }

    /**
     * Metodo para construir el query de la tabla usuario
     *
     * @param curp del usuario
     * @param password del usuario
     * @return String cadena con el query
     */
    private String validarSqlConsultaUsuario(String origen) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT iu.id_usuario \"idUsuario\", iu.nombre, iu.primer_apellido \"primerApellido\", iu.segundo_apellido \"segundoApellido\", iu.curp, iu.rfc, iu.homocve homoclave, iu.email ")
                .append(" FROM ip_usuario iu ");
        if ((!DOMINIO_FRONT.equals(origen)
                && !DOMINIO_IDENTIDAD.equals(origen))
                && origen != null) {
            sql.append(" INNER JOIN ip_vinculacion_usuario_aplicacion ivua on ivua.id_usuario = iu.id_usuario ")
                    .append(" INNER JOIN ip_aplicacion ia on ia.id_aplicacion = ivua.id_aplicacion ");
        }
        sql.append(" WHERE iu.curp = ? ");
        if ((!DOMINIO_FRONT.equals(origen) && !DOMINIO_IDENTIDAD.equals(origen)) && ValidacionHelper.isNotNull(origen)) {
            sql.append("and ia.secretkey = ? and ia.dominio = ?  and ivua.id_aplicacion = ? ");
        }
        LOGGER.info(sql.toString());
        return sql.toString();
    }

    /**
     * Metodo para obtener el json de parametros para el query de la tabla
     * usuario
     *
     * @param curp del usuario
     * @param password del usuario
     * @return JsonArray con los parámetros para la consulta
     */
    private JsonArray obtenerJsonConsultaUsuario(String curp, String origen, Integer idAplicacion, String secretKey) {
        JsonArray json = new JsonArray().add(curp);
        if ((!DOMINIO_FRONT.equals(origen) && !DOMINIO_IDENTIDAD.equals(origen)) && ValidacionHelper.isNotNull(origen)) {
            json.add(secretKey).add(origen).add(idAplicacion);
        }
        LOGGER.info(json.toString());
        return json;
    }
}
