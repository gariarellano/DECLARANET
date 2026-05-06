package mx.gob.sfp.dgti.persistence;

import io.vertx.core.AsyncResult;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.entity.AplicacionEO;
import mx.gob.sfp.dgti.entity.ConfirmacionEO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.persistence.impl.DatabaseServiceImpl;

/**
 * Interface DatabaseService para el acceso a datos
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 20/03/2019
 */
public interface DatabaseService {

    static DatabaseService create(Vertx vertx, JsonObject config) {
        return new DatabaseServiceImpl(vertx, config);
    }

    /**
     * Registro de un usuario
     *
     * @param usuario: datos del usuario a registrar
     * @return generated Future<Void>
     */
    public Future<Void> guardarUsuario(UsuarioEO usuario);

    /**
     * Registro de una aplicacion cliente
     *
     * @param aplicacion: datos de la aplicación a registrar
     * @return generated Future<Void>
     */
    public Future<Void> guardarAplicacion(AplicacionEO aplicacion);

    /**
     * Consulta de usuario
     *
     * @param origen
     * @param curp del usuario
     * @param idAplicacion id de la aplicación cliente vinculada
     * @param secretKey de la aplicación cliente
     * @return generated Future<JsonObject>
     */
    public Future<JsonObject> consultarUsuarioVinculado(String origen, String curp, Integer idAplicacion, String secretKey);

    /**
     * Consulta usuarios cuyas curps aún no han sido validadas con renapo
     *
     * @return generated Future<List<UsuarioEO>>
     */
    public Future<List<UsuarioEO>> obtenerCurpsNoValidadas();

    /**
     * Modificar usuario
     *
     * @param usuario
     * @return generated Future<Void>
     */
    public Future<Void> modificarUsuario(UsuarioEO usuario);

    /**
     * Consultar usuario por su curp
     *
     * @param curp
     * @return generated Future<UsuarioEO>
     */
    public Future<UsuarioEO> consultarUsuarioPorCurp(String curp);

    /**
     * Método que realiza la consulta de los usuarios telegram.
     *
     * @param curp Curp a consultar
     * @param idTelegram identificadorTelegram
     * @param handlerResult Handler de resultado asynchronous
     * @return DatabaseService Objeto de respuesta
     */
    public DatabaseService consultarUsuarioTelegram(String curp, String idTelegram, Handler<AsyncResult<JsonObject>> handlerResult);

    /**
     *  Método par aguardar los datos de los usuarios Telegram.
     * 
     * @param datos datos que contiene la solicitud para dar de alta los datos de telegram.
     * @param handlerResult Objeto Futuro para la recueracion de los datos
     * @return DatabaseService Objeto Asynchonous
     */
    public DatabaseService guardaUsuarioTelegram(JsonObject datos, Handler<AsyncResult<JsonObject>> handlerResult);
    /**
     * Actualizar password de un usuario
     *
     * @param usuario
     * @return generated Future<Void>
     */
    public Future<Void> actualizarPwd(UsuarioEO usuario, Integer idConfirmacion);

    /**
     * Obtener usuario por su curp
     *
     * @param curp
     * @return generated Future<UsuarioEO>
     */
    public Future<UsuarioEO> obtenerUsuarioPorCurp(String curp);

    /**
     * Obtener usuario por su curp o rfc
     *
     * @param curp
     * @return generated Future<UsuarioEO>
     */
    public Future<UsuarioEO> obtenerUsuarioPorCurp(String curp, String rfc);

    /**
     * Consultar si existe la curp o rfc en algún registro existente
     *
     * @param curp
     * @param rfc
     * @return generated Future<Integer>
     */
    public Future<Integer> consultarCurpYRFC(String curp, String rfc);

    /**
     * COnsulta de datos por parametros like para curp RFC y nombres
     *
     * @param where parametro like
     * @param aparametros valor del parametro a buscar
     * @return
     */
    public Future<List<JsonObject>> consultarLike(StringBuilder where, JsonArray aparametros);

    /**
     * Guardar confirmación para activar a un usuario y/o reestablecer password
     *
     * @param idUsuario
     * @param token
     * @return generated Future<Void>
     */
    public Future<Void> guardarConfirmacion(Integer idUsuario, String token);

    /**
     * Obtener confirmación para validación
     *
     * @param idConfirmacion
     * @param curp
     * @return generated Future<ConfirmacionEO>
     */
    public Future<ConfirmacionEO> obtenerConfirmacion(String curp, String token);

    /**
     * Eliminar confirmación
     *
     * @param idConfirmacion
     * @return generated Future<Void>
     */
    public Future<Void> eliminarConfirmacion(Integer idConfirmacion);

    /**
     * Editar usuario de acuerdo al estatusValidadoRenapo
     *
     * @param usuario
     * @param estatusValidadoRenapo
     * @return generated Future<Void>
     */
    public Future<Void> editarUsuario(UsuarioEO usuario, Integer estatusValidadoRenapo, String curp);

    /**
     * Editar la curp o emails de un usuario
     *
     * @param usuario
     * @param curp
     * @return generated Future<Void>
     */
    public Future<UsuarioEO> editarCurpYEmailsDeUsuario(UsuarioEO usuario, String curp);
}
