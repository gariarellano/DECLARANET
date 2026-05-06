package mx.gob.sfp.dgti.persistence;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.entity.AplicacionEO;
import mx.gob.sfp.dgti.entity.ConfirmacionEO;
import mx.gob.sfp.dgti.entity.TransaccionEO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.entity.VinculacionUAEO;
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
     * Obtener la informacion de una aplicacion
     *
     * @param dominioCliente de la aplicación cliente
     * @param secretKey de la aplicación cliente
     *
     * @param idAplicacion de la aplicación cliente
     * @param guardaBitacora
     * @return generated Future<AplicacionEO> Objecto que regresa la búsqueda de
     * la aplicación cliente
     */
    public Future<AplicacionEO> obtenerAplicacion(String dominioCliente, Integer idAplicacion, String secretKey, boolean guardaBitacora);

    /**
     * Obtener la informacion de un usuario
     *
     * @param curp del usuario a consultar
     * @return generated Future<UsuarioEO> Objecto que regresa la búsqueda en la
     * BD
     */
    public Future<UsuarioEO> obtenerUsuario(String curp);
    
    /**
     * Método que recupera los datos del usuario para generar OTP y mandar mensaje a Telegram
     * 
     * @param idUsuario Identificador del usaurio.
     * @param resultado Objeto respuesta de la consulta de los datos.
     * @return DatabaseService objeto creado para recuperar los datos del usuario.
     */
    public DatabaseService consultaDatosTelByIdUsr(String curp, Handler<AsyncResult<JsonObject>> resultado);

    /**
     * Obtener la informacion de la vinculacion entre una aplicacion y un
     * usuario
     *
     * @param usuarioId Id del usuario
     * @param curp del usuario
     * @param aplicacionId Id de la aplicación cliente a quien se encuentra
     * vinculado el usuario
     * @return generated Future<VinculacionUAEO>
     */
    public Future<VinculacionUAEO> obtenerVinculacionUAEO(Integer usuarioId, Integer aplicacionId, String curp);

    /**
     * Guardar un registro de la vinculacion entre una aplicacion y un cliente
     *
     * @param idAplicacion Id de la aplicación cliente
     * @param fecha la fecha cuando se recibió la petición de vincular al
     * usuario con la aplicación cliente
     * @param activo
     * @param curp del usuario
     * @return generated Future<VinculacionUAEO> objeto que regresa la inserción
     * de la vinculación
     */
    public Future<VinculacionUAEO> guardarVinculacionUAEO(Integer idAplicacion, String fecha, Integer activo, String curp);

    /**
     * Guardar un registro de una transaccion solicitada
     *
     * @param sha código encriptado (sha256)
     * @param base64 código en base64
     * @param token generado para la transacción
     * @param clienteId Id de la aplicación cliente
     * @param fecha cuando se generó la petición de guardar transacción
     * @param curp del usuario
     * @param idUsuario identificador de usuario.
     * @return generated Future<TransaccionEO> Objecto que regresa la inserción
     * de la transacción
     */
    public Future<TransaccionEO> guardarTransaccion(String sha, String base64, String token, Integer clienteId, String fecha, String curp, Integer idUsuario);

    /**
     * Obtener la informacion de una transaccion
     *
     * @param transaccionSha código sha de la transacción a buscar
     * @param token de la transacción
     * @return generated Future<TransaccionEO> objecto de transacción que
     * regresa la búsqueda
     */
    public Future<TransaccionEO> obtenerTransaccion(String transaccionSha, String token);

    /**
     * Guardar la confirmacion para el registro de un usuario
     *
     * @param idUsuario Ientificador único del usuario
     * @param token que se generó para la confirmación
     * @return generated Future<ConfirmacionEO>
     */
    public Future<ConfirmacionEO> guardarConfirmacion(Integer idUsuario, String token);

    /**
     * Obtener la confirmacion para activar al usuario
     *
     * @param curp
     * @param token
     * @return generated Future(ConfirmacionEO)
     */
    public Future<ConfirmacionEO> obtenerConfirmacion(String curp, String token);

    /**
     * Activar al usuario nuevo por código de confirmación
     *
     * @param idUsuario id del usuario a activar
     * @param idConfirmacion id de la confirmación para activar al usuario
     * @return generated Future<Void>
     */
    public Future<Void> activarUsuarioCodigoConfirmacion(Integer idUsuario, String idConfirmacion);

    /**
     * Activar a un usuario por enlace enviado por correo
     *
     * @param idUsuario id del usuario a activar
     * @param idConfirmacion id de la confirmación para activar al usuario
     * @return generated Future
     */
    public Future<Void> activarUsuarioPorEnlace(Integer idUsuario, Integer idConfirmacion);

    /**
     * Función para modificar un usuario
     *
     * @param usuario objeto que contiene las propiedades del usuario a
     * modificar
     * @return generated Future<Void>
     */
    public Future<Void> modificarUsuario(UsuarioEO usuario);

    /**
     * Función para obtener una aplicacion por el id y secretkey
     *
     * @param idAplicacion
     * @param secretKey
     *
     * @return
     */
    public Future<AplicacionEO> obtenerAplicacionPorId(Integer idAplicacion, String secretKey);

    /**
     * Función para obtener aplicacion por una transacción
     *
     * @param transaccion
     * @param token
     * @return
     */
    public Future<AplicacionEO> obtenerAppPorTrans(String transaccion, String token);
    
    /**
     * Método que permite realizar el guardado de un token para poder utilizarlo solamente una vez.
     * 
     * @param token Cadena de caracteres que conforman un token.
     * @param respuesta Cadena de respuesta.
     * @return DatabaseService funcion completa de forma lambda.
     */
    public DatabaseService guardaTokenUnSoloUSo(String token, Handler<AsyncResult<String>> respuesta);
    
    /**
     * Método que realiza la validacion de existencia de token de un solo uso.
     * 
     * @param token Cadena que compone un token.
     * @param respuesta String que indica la existencia de un token.
     * @return DatabaseService interface lambda de respeusa.
     */
    public DatabaseService validaTokenUnSoloUSo(String token, Handler<AsyncResult<String>> respuesta);
    
    /**
     * Método que realiza la eliminacion de datos cada 24 hora, de todos aquellos token que 
     * no hayan sido utilizados y se quedan como basura.
     */
    public void eliminaTokenUnSoloUSo();
    
    /**
     * Métod que realiza la actualizacion o la insercion 
     * de un OTP para el manejo de aciones en sistemas.
     * 
     * @param idUsuario Identificador del usuario al que le corresponde el OTP.
     * @param otp OTP que se genera para el usuario.
     * @param inOup  Objeto que indica el funcionamiento correcto de insercion o actualizacion de OTP
     * @return DatabaseService Objeto lamda para el manejo de acciones con base de datos.
     */
    public DatabaseService creaOactualizaOtp(Integer idUsuario, String otp,Handler<AsyncResult<Boolean>> inOup);
    
    /**
     * Método que realiza la validacion del OTP en caso de localizarlo lo elimina.
     * 
     * @param otp Alrfanumerico que representa un otp para autenticar usuarios.
     * @param idUsuario Identificador del usuario al que se aplicara la operacioón.
     * @param validado Respuesta de validacionde un OTP.
     * @return DatabaseService Objeto lamba que realiza la funcionalidad de validación de OTP.
     */
    public DatabaseService validaOtp(String otp, Integer idUsuario, Handler<AsyncResult<Boolean>> validado);
    
    /**
     * Método que realiza la eliminacion del preregistro.
     * 
     * @param curp String que representa la curp de una persona.
     * @param valida Objeto respuesta de la validadción.
     * @return DatabaseService Objeto lamda que se crea para la respuesta.
     */
    public DatabaseService eliminaPreregistroTele(Integer curp, Handler<AsyncResult<Boolean>> valida);
}
