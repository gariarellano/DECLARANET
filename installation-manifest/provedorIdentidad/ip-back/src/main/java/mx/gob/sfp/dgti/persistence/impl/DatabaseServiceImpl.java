package mx.gob.sfp.dgti.persistence.impl;

import io.vertx.core.AsyncResult;
import static mx.gob.sfp.dgti.util.Constantes.ACCESO;
import static mx.gob.sfp.dgti.util.Constantes.IP_DOMINIO_FRONT;
import static mx.gob.sfp.dgti.util.Constantes.IP_DOMINIO_IDENTIDAD;
import static mx.gob.sfp.dgti.util.Constantes.LOGIN_APLICACION;
import static mx.gob.sfp.dgti.util.Constantes.LOGIN_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.NO_EXISTE;
import static mx.gob.sfp.dgti.util.Constantes.TOKEN_BEARER_KEY;
import static mx.gob.sfp.dgti.util.Constantes.VINCULACION;

import java.util.Date;
import java.util.List;

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
import mx.gob.sfp.dgti.entity.AplicacionEO;
import mx.gob.sfp.dgti.entity.BitacoraEO;
import mx.gob.sfp.dgti.entity.ConfirmacionEO;
import mx.gob.sfp.dgti.entity.TransaccionEO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.entity.VinculacionUAEO;
import mx.gob.sfp.dgti.helper.AutenticacionHelper;
import mx.gob.sfp.dgti.helper.ValidacionHelper;
import mx.gob.sfp.dgti.persistence.DatabaseService;
import static mx.gob.sfp.dgti.util.Constantes.LOCALIZADO;
import static mx.gob.sfp.dgti.util.Constantes.NO_LOCALIZADO;

/**
 * Implementacion de DatabaseService
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 20/03/2019
 */
public class DatabaseServiceImpl implements DatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseServiceImpl.class);
    private static final String DOMINIO_FRONT = System.getenv(IP_DOMINIO_FRONT) != null ? System.getenv(IP_DOMINIO_FRONT) : AutenticacionHelper.obtenerPropiedad("ip.dominio.front");
    private static final String DOMINIO_IDENTIDAD = System.getenv(IP_DOMINIO_IDENTIDAD) != null ? System.getenv(IP_DOMINIO_IDENTIDAD) : AutenticacionHelper.obtenerPropiedad("ip.dominio.front");
    private static final Integer PROCESO_ACTIVAR_USUARIO = 0;
    private SQLClient postgreSQLt;

    /**
     * Constructor sin parámetros
     */
    public DatabaseServiceImpl() {
        // Constructor
    }

    /**
     * Constructor con parámetros
     *
     * @param vertx
     * @param configuracion para la conexión a la BD
     */
    public DatabaseServiceImpl(Vertx vertx, JsonObject configuracion) {
        this.postgreSQLt = PostgreSQLClient.createShared(vertx, configuracion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<AplicacionEO> obtenerAplicacion(String dominioCliente, Integer idAplicacion, String secretKey, boolean guardaBitacora) {
        Future<AplicacionEO> future = Future.future();
        postgreSQLt.queryWithParams(validarSqlAplicacion(dominioCliente, idAplicacion, secretKey),
                obtenerJsonAplicacion(dominioCliente, idAplicacion, secretKey), resultado -> {
                    postgreSQLt.close();
                    if (resultado.succeeded()) {
                        List<JsonObject> registros = resultado.result().getRows();
                        if (registros.isEmpty()) {
                            future.fail(NO_EXISTE);
                        } else {
                            if (guardaBitacora) {
                                this.guardarBitacora(idAplicacion, new Date(), null, LOGIN_APLICACION);
                            }
                            future.handle(Future.succeededFuture(new AplicacionEO(registros.get(0))));
                        }
                    } else {
                        LOGGER.error("Error al ObtenerAplicacion: " + idAplicacion + "-" + secretKey + resultado.cause().getMessage());
                        future.fail(resultado.cause());
                    }
                });
        return future;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<UsuarioEO> obtenerUsuario(String curp) {
        Future<UsuarioEO> future = Future.future();
        if (!ValidacionHelper.isNull(curp)) {
            postgreSQLt.queryWithParams(AutenticacionHelper.obtenerPropiedad("query_get_usuario"), new JsonArray().add(curp), resultado -> {
                postgreSQLt.close();
                if (resultado.succeeded()) {
                    List<JsonObject> registros = resultado.result().getRows();
                    if (!registros.isEmpty()) {
                        future.handle(Future.succeededFuture(new UsuarioEO(registros.get(0))));
                    } else {
                        LOGGER.error("obtenerUsuario No existe: " + curp);
                        future.handle(Future.failedFuture(NO_EXISTE));
                    }
                } else {
                    LOGGER.error("Error al obtenerUsuario " + curp + resultado.cause().getMessage());
                    future.fail(resultado.cause());
                }
            });
        } else {
            LOGGER.error("Parametros no validos" + curp);
            future.fail(NO_EXISTE);
        }
        return future;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseService consultaDatosTelByIdUsr(String curp, Handler<AsyncResult<JsonObject>> resultado){
        postgreSQLt.queryWithParams(AutenticacionHelper.obtenerPropiedad("query_get_data_tele"), new JsonArray().add(curp), localizado->{
            if (localizado.succeeded()){
                if (localizado.result().getRows().isEmpty()){
                    resultado.handle(Future.succeededFuture(new JsonObject()));
                }else{
                    resultado.handle(Future.succeededFuture(localizado.result().getRows().get(0)));
                }
            }else{
                LOGGER.error(String.format("Erro en consultaDatosTelByIdUsr %s",localizado.cause()));
                resultado.handle(Future.failedFuture("Error de consulta de datos Tele"));
            }
        });
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<VinculacionUAEO> obtenerVinculacionUAEO(Integer usuarioId, Integer aplicacionId, String curp) {
        Future<VinculacionUAEO> future = Future.future();        
        postgreSQLt.queryWithParams(AutenticacionHelper.obtenerPropiedad("query_obten_vinculacion_usuario_aplicacion"), new JsonArray().add(usuarioId).add(aplicacionId), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                List<JsonObject> registros = resultado.result().getRows();
                if (!registros.isEmpty()) {
                    this.guardarBitacora(aplicacionId, new Date(), curp, LOGIN_USUARIO);
                    LOGGER.info("cgac IP_VINCULACION_USUARIO_APLICACION " + registros.get(0));
                    future.handle(Future.succeededFuture(new VinculacionUAEO(registros.get(0))));
                } else {
                    LOGGER.error("No hay vinculacion " + usuarioId + "-" + aplicacionId + "-" + curp);
                    future.fail(NO_EXISTE);
                }
            } else {
                LOGGER.error("Error en obtenerVinculacionUAEO " + usuarioId + "-" + aplicacionId + "-" + curp + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<VinculacionUAEO> guardarVinculacionUAEO(Integer idAplicacion, String fecha, Integer activo, String curp) {
        Future<VinculacionUAEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO IP_VINCULACION_USUARIO_APLICACION (id_usuario, id_aplicacion, fecha_vinculacion, activo_logico_vinculacion) ")
                .append(" SELECT id_usuario, ?, ?, ? ")
                .append(" FROM IP_USUARIO ")
                .append(" WHERE curp = ?");

        postgreSQLt.updateWithParams(sql.toString(), new JsonArray().add(idAplicacion).add(fecha).add(activo).add(curp), resultado -> {
            postgreSQLt.close();
            if (resultado.failed()) {
                LOGGER.error("Error guardarVinculacionUAEO " + idAplicacion + "-" + curp + resultado.cause().getMessage());
                future.fail(resultado.cause());
            } else {
                this.guardarBitacora(idAplicacion, ValidacionHelper.convertStringToDate(fecha), curp, VINCULACION);
                this.guardarBitacora(idAplicacion, ValidacionHelper.convertStringToDate(fecha), curp, LOGIN_USUARIO);
                future.handle(resultado.map(res -> new VinculacionUAEO(res.toJson())));
            }
        }
        );
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<TransaccionEO> guardarTransaccion(String sha, String base64, String token, Integer clienteId, String fecha, String curp, Integer idUsuario) {
        Future<TransaccionEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO IP_TRANSACCION (codigo_sha, codigo_base64, codigo_token, id_aplicacion, fecha, curp, id_usuario) ")
                .append(" VALUES (?,?,?,?,?,?,?)");

        postgreSQLt.updateWithParams(sql.toString(), new JsonArray().add(sha).add(base64).add(token).add(clienteId).add(fecha).add(curp).add(idUsuario),
                resultado -> {
                    postgreSQLt.close();
                    if (resultado.failed()) {
                        LOGGER.error("Error guardarTransaccion " + clienteId + "-" + curp + resultado.cause().getMessage());
                        future.fail(resultado.cause());
                    } else {
                        future.handle(resultado.map(res -> new TransaccionEO(res.toJson())));
                    }
                }
        );
        return future;
    }

    /**
     * {@inheritDoc}
     */
    public Future<ConfirmacionEO> guardarConfirmacion(Integer idUsuario, String token) {
        Future<ConfirmacionEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO IP_CONFIRMACION (id_usuario, fecha, token, proceso) VALUES (?,now(),?,?) RETURNING id_confirmacion \"idConfirmacion\", id_usuario \"idUsuario\", fecha, token ");

        postgreSQLt.queryWithParams(sql.toString(), new JsonArray().add(idUsuario).add(token).add(PROCESO_ACTIVAR_USUARIO),
                resultado -> {
                    postgreSQLt.close();
                    if (resultado.failed()) {
                        LOGGER.error("Error guardarConfirmacion " + idUsuario + "-" + token + resultado.cause());
                        future.fail(resultado.cause());
                    } else {
                        ResultSet result = resultado.result();
                        future.handle(resultado.map(res -> new ConfirmacionEO(result.getRows().get(0))));
                    }
                }
        );
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<TransaccionEO> obtenerTransaccion(String transaccionSha, String token) {
        String tokenR = token.replace(TOKEN_BEARER_KEY, "");
        Future<TransaccionEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_transaccion as \"idTransaccion\", codigo_sha as \"codigoSha\", codigo_base64 as \"codigoBase64\", ")
            .append("codigo_token as \"codigoToken\", id_aplicacion as \"idAplicacion\", fecha, curp, id_usuario \"idUsuario\" ")
            .append("FROM IP_TRANSACCION ")
            .append("WHERE codigo_sha = ? AND codigo_token = ?")
            .append(" limit 1 ");

        postgreSQLt.queryWithParams(sql.toString(), new JsonArray().add(transaccionSha).add(tokenR), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                List<JsonObject> registros = resultado.result().getRows();
                if (!registros.isEmpty()) {
                    JsonObject row = registros.get(0);
                        this.guardarBitacora(Integer.parseInt(row.getValue("idAplicacion").toString()), new Date(), row.getValue("curp").toString(), ACCESO);
                        future.handle(Future.succeededFuture(new TransaccionEO(row)));
                }else{
                    LOGGER.error("Sin transaccion " + transaccionSha + " - token - " + token);
                    future.fail(NO_EXISTE);
                }
            } else {
                LOGGER.error("Error obtenerTransaccion " + transaccionSha + "-" + token + " ERROR" + resultado.cause());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * Metodo para guardar en bitacora
     *
     */
    private Future<BitacoraEO> guardarBitacora(Integer idAplicacion, Date fecha, String curp, Integer accion) {
        Future<BitacoraEO> future = Future.future();
        postgreSQLt.updateWithParams(validarSqlBitacora(idAplicacion, curp),
                obtenerJsonBitacora(idAplicacion, fecha, curp, accion), resultado -> {
                    postgreSQLt.close();
                    if (resultado.failed()) {
                        LOGGER.error("Fallo guardarBitacora " + idAplicacion + "-" + curp + "-" + accion);
                        future.fail(resultado.cause());
                    } else {
                        future.handle(resultado.map(res -> new BitacoraEO(res.toJson())));
                    }
                });
        return future;
    }

    /**
     * Función para construir el query de la tabla BITACORA
     *
     * @param idAplicacion id de la aplicación cliente
     * @param fecha en que se hizo la petición
     * @param curp del usuario
     * @param accion a realizar
     * @return String cadena que se generó para el query
     */
    private String validarSqlBitacora(Integer idAplicacion, String curp) {
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
        sql.append(") VALUES (").append(parametros).append(" ?,?)");

        return sql.toString();
    }

    /**
     * Metodo para construir el query de la tabla aplicacion
     *
     * @param dominio de la aplicación
     * @param idAplicacion cliente
     * @param secretKey de la aplicacion cliente
     * @return String cadena con el query generado
     */
    private String validarSqlAplicacion(String dominioCliente, Integer idAplicacion, String secretKey) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_aplicacion as \"idAplicacion\", nombre, secretkey, url, dominio, obligatorio_curp_valida as \"obligatorioCurpValida\", iss, minutos ")
                .append("FROM IP_APLICACION WHERE 1=1 ");

        if ((!DOMINIO_FRONT.equals(dominioCliente) && !DOMINIO_IDENTIDAD.equals(dominioCliente)) && ValidacionHelper.isNotNull(dominioCliente)) {
            sql.append(" AND dominio = ? ");
        }
        if (ValidacionHelper.isNotNull(idAplicacion)) {
            sql.append(" AND id_aplicacion = ? ");
        }
        if (ValidacionHelper.isNotNull(secretKey)) {
            sql.append(" AND secretkey = ?");
        }
        sql.append(" limit 1 ");
        return sql.toString();
    }

    /**
     * Metodo para obtener el json de parametros para el query de la tabla
     * aplicacion
     *
     * @param dominio de la aplicación cliente
     * @param idAplicacion cliente
     * @param secretKey de la aplicacion cliente
     * @return JsonArray generado que contiene los parámetros para la consulta
     */
    private JsonArray obtenerJsonAplicacion(String dominioCliente, Integer idAplicacion, String secretKey) {
        JsonArray json = new JsonArray();
        if ((!DOMINIO_FRONT.equals(dominioCliente) && !DOMINIO_IDENTIDAD.equals(dominioCliente)) && ValidacionHelper.isNotNull(dominioCliente)) {
            json.add(dominioCliente);
        }
        if (ValidacionHelper.isNotNull(idAplicacion)) {
            json.add(idAplicacion);
        }
        if (ValidacionHelper.isNotNull(secretKey)) {
            json.add(secretKey);
        }
        return json;
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
     * {@inheritDoc}
     */
    @Override
    public Future<ConfirmacionEO> obtenerConfirmacion(String curp, String token) {
        String tokenR = token.replace(TOKEN_BEARER_KEY, "");
        Future<ConfirmacionEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ipc.id_confirmacion \"idConfirmacion\", ipc.id_usuario \"idUsuario\", ipc.fecha, ipc.token ")
                .append(" FROM IP_CONFIRMACION ipc inner join IP_USUARIO ipu on IPU.id_usuario = ipc.id_usuario")
                .append(" WHERE ipu.curp = ? and ipc.proceso = ? and ipc.token = ?")
                .append(" limit 1");

        postgreSQLt.queryWithParams(sql.toString(), new JsonArray().add(curp).add(PROCESO_ACTIVAR_USUARIO).add(tokenR), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                List<JsonObject> registros = resultado.result().getRows();
                if (!registros.isEmpty()) {
                    future.handle(Future.succeededFuture(new ConfirmacionEO(registros.get(0))));
                } else {
                    LOGGER.error("Sin datos obtenerConfirmacion: " + curp + "-" + token);
                    future.fail(NO_EXISTE);
                }
            } else {
                LOGGER.error("Error obtenerConfirmacion: " + curp + "-" + token + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> activarUsuarioCodigoConfirmacion(Integer idUsuario, String idConfirmacion) {
        Future<Void> future = Future.future();
        String sql = "UPDATE IP_USUARIO SET activo = 1 WHERE id_usuario = ?";
        postgreSQLt.queryWithParams(sql, new JsonArray().add(idUsuario), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                future.complete();
            } else {
                LOGGER.error("Error activarUsuarioCodigoConfirmacion: " + idUsuario + "-" + idConfirmacion + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> activarUsuarioPorEnlace(Integer idUsuario, Integer idConfirmacion) {
        Future<Void> future = Future.future();
        String sql = "UPDATE IP_USUARIO SET activo = 1 WHERE id_usuario = ?";

        postgreSQLt.queryWithParams(sql, new JsonArray().add(idUsuario), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                future.complete();
                this.eliminarConfirmacion(idConfirmacion);
            } else {
                LOGGER.error("Error activarUsuarioPorEnlace: " + idUsuario + "-" + idConfirmacion + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    public Future<Void> eliminarConfirmacion(Integer idConfirmacion) {
        Future<Void> future = Future.future();
        postgreSQLt.queryWithParams("DELETE FROM IP_CONFIRMACION WHERE id_confirmacion = ?",
                new JsonArray().add(idConfirmacion), result -> {
                    postgreSQLt.close();
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        LOGGER.error("Error eliminarConfirmacion " + idConfirmacion + result.cause().getMessage());
                        future.fail(result.cause());
                    }
                });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> modificarUsuario(UsuarioEO usuario) {
        LOGGER.info("Modificar validado_renapo::: " + usuario.getIdUsuario() + usuario.getValidadoRenapo());
        Future<Void> future = Future.future();
        String sql = "UPDATE ip_usuario SET validado_renapo = ? WHERE id_usuario = ?";

        postgreSQLt.queryWithParams(sql, new JsonArray().add(usuario.getValidadoRenapo()).add(usuario.getIdUsuario()), resultado -> {
            postgreSQLt.close();
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
    public Future<AplicacionEO> obtenerAplicacionPorId(Integer idAplicacion, String secretKey) {
        Future<AplicacionEO> future = Future.future();
        postgreSQLt.queryWithParams(validarSqlAplicacion(null, idAplicacion, secretKey), obtenerJsonApp(idAplicacion, secretKey), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                future.handle(resultado.map(rs -> {
                    List<JsonObject> registros = rs.getRows();
                    return new AplicacionEO(registros.get(0));
                }));
            } else {
                LOGGER.error("Error obtenerAplicacionPorId " + idAplicacion + "-" + secretKey + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<AplicacionEO> obtenerAppPorTrans(String transaccion, String token) {
        String tokenR = token.replace(TOKEN_BEARER_KEY, "");
        Future<AplicacionEO> future = Future.future();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ipa.id_aplicacion as \"idAplicacion\", ipa.iss, ipa.minutos ");
        sql.append(" from ip_aplicacion ipa inner join ip_transaccion ipt on ipa.id_aplicacion = ipt.id_aplicacion ");
        sql.append(" where ipt.codigo_sha = ? and ipt.codigo_token = ? ");
        sql.append(" limit 1 ");

        postgreSQLt.queryWithParams(sql.toString(), new JsonArray().add(transaccion).add(tokenR), resultado -> {
            postgreSQLt.close();
            
            if (resultado.succeeded()) {
                List<JsonObject> res = resultado.result().getRows();
                if (!res.isEmpty()){
                    future.handle(Future.succeededFuture(new AplicacionEO(res.get(0))));
                }else{
                    LOGGER.error("Error no se localizo para tans y tok : " + transaccion + "-" + token );
                }
            } else {
                LOGGER.error("Error obtenerAppPorTrans " + transaccion + "-" + token + " ERROR : " + resultado.cause().getMessage());
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    /**
     * Metodo para obtener el json de parametros para el query de la tabla
     * aplicacion
     *
     * @param idAplicacion
     * @param secretKey
     * @return JsonArray con los parámetros para la consulta
     */
    private JsonArray obtenerJsonApp(Integer idAplicacion, String secretKey) {
        JsonArray json = new JsonArray();
        if (ValidacionHelper.isNotNull(idAplicacion)) {
            json.add(idAplicacion);
        }
        if (ValidacionHelper.isNotNull(secretKey)) {
            json.add(secretKey);
        }
        return json;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DatabaseService guardaTokenUnSoloUSo(String token, Handler<AsyncResult<String>> respuesta) {
        String insert = "INSERT INTO ip_token_un_solo_uso(token, fecha_creacion) VALUES(?, current_timestamp)";
        postgreSQLt.updateWithParams(insert, new JsonArray().add(token), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                respuesta.handle(Future.succeededFuture());
            } else {
                LOGGER.error("Error token un solo uso");
                respuesta.handle(Future.failedFuture(resultado.cause()));
            }
        });
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DatabaseService validaTokenUnSoloUSo(String token, Handler<AsyncResult<String>> respuesta) {
        String update = "delete from ip_token_un_solo_uso WHERE token = ? ";
        postgreSQLt.updateWithParams(update, new JsonArray().add(token), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                LOGGER.info(resultado.result());
                if (resultado.result().getUpdated()==1){
                    respuesta.handle(Future.succeededFuture(LOCALIZADO));
                }else{
                    respuesta.handle(Future.succeededFuture(NO_LOCALIZADO));
                }
            } else {
                LOGGER.error("Error : validaTokenUnSoloUSo " + resultado.cause().getMessage());
                respuesta.handle(Future.failedFuture("ERROR al realizar Acualizado"));
            }
        });
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void eliminaTokenUnSoloUSo() {
        String update = "DELETE from ip_token_un_solo_uso where fecha_creacion  < now()- interval '2 day' ";
        postgreSQLt.update(update,resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                LOGGER.info("tokens un solo uso eliminados " +resultado.result().getUpdated());
            } else {
                LOGGER.error("Error al eliminar tokens un solo uso" + resultado.cause().getMessage());
            }
        });
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public DatabaseService creaOactualizaOtp(Integer idUsuario, String otp,Handler<AsyncResult<Boolean>> inOup){
        postgreSQLt.updateWithParams(AutenticacionHelper.obtenerPropiedad("update_otp_by_id_user"), new JsonArray().add(otp).add(idUsuario), resul->{
            postgreSQLt.close();
            if (resul.succeeded()){
                if (resul.result().getUpdated() == 1){
                    inOup.handle(Future.succeededFuture());
                }else{
                    postgreSQLt.updateWithParams(AutenticacionHelper.obtenerPropiedad("insert_otp_by_id_user"), new JsonArray().add(idUsuario).add(otp), insert->{
                        postgreSQLt.close();
                        if (insert.succeeded()){
                            inOup.handle(Future.succeededFuture());
                        }else{
                            LOGGER.error(String.format("Error en insert de otp %s",resul.cause()));
                            LOGGER.error(String.format("Error al realizar la insercion de otp. %s para id : %d", otp, idUsuario));
                            inOup.handle(Future.failedFuture("Error al realizar la insercion de otp"));
                        }
                    });
                }
            }else{
                LOGGER.error(String.format("Error en update de otp %s",resul.cause()));
                LOGGER.error(String.format("Error al realizar la actualziacion de otp. %s para id : %d", otp, idUsuario));
                inOup.handle(Future.failedFuture("Error al realizar la actualziacion de otp."));
            }
        });
        return this;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public DatabaseService validaOtp(String otp, Integer idUsuario, Handler<AsyncResult<Boolean>> validado){
        postgreSQLt.updateWithParams(AutenticacionHelper.obtenerPropiedad("delete_otp_by_id_user"), new JsonArray().add(otp).add(idUsuario), localizado->{
            postgreSQLt.close();
            if (localizado.succeeded()){
                if (localizado.result().getUpdated()==1){
                    postgreSQLt.updateWithParams(AutenticacionHelper.obtenerPropiedad("update_notificacion_activo"), new JsonArray().add(idUsuario), actualizado->{
                        postgreSQLt.close();
                        if (actualizado.succeeded()){
                            validado.handle(Future.succeededFuture(true));
                        }else{
                            LOGGER.error(String.format("Error al Actualizar activacion en ip_notificaciones para el usuario %d", idUsuario));
                            validado.handle(Future.failedFuture("Error al Actualizar activacion en ip_notificaciones "));
                        }
                    });
                }else{
                    validado.handle(Future.succeededFuture(false));
                }
            }else{
                LOGGER.error(String.format("Error al realizar el borrado de OTP para id_usuario %d", idUsuario));
                validado.handle(Future.failedFuture("Error al eliminar el OTP"));
            }
        });
        return this;
    }
    
    @Override
    public DatabaseService eliminaPreregistroTele(Integer idUsuario, Handler<AsyncResult<Boolean>> valida){
        postgreSQLt.updateWithParams(AutenticacionHelper.obtenerPropiedad("delete_ip_notificacion_by_curp"), new JsonArray().add(idUsuario), deleted->{
            if (deleted.succeeded()){
                if (deleted.result().getUpdated() ==1){
                    valida.handle(Future.succeededFuture(true));
                }else{
                    valida.handle(Future.succeededFuture(false));
                }
            }else{
                LOGGER.error(deleted.cause());
                valida.handle(Future.failedFuture("Error al realizar la eliminación del preregistro."));
            }
        });
        return this;
    }
}
