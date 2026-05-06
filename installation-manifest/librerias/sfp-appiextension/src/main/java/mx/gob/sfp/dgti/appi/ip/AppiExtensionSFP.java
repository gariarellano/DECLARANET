/*
 * Sistemna Propiedad de la SecretarÃ­a de la FunciÃ³n PÃºblica DGTI
 * "SFP-AppliExtension" Sistema que permite realizar la validacion de
 * los tokens y transacciones generados por el proveedor de identidad
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.appi.ip; 

import com.google.gson.Gson;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.gob.sfp.dgti.appi.ip.dto.DTOValidacion;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsData;
import mx.gob.sfp.dgti.appi.ip.enu.EnumPathsEnables;

/**
 * Clase que realiza la peticÃ³n de 
 * validaciones de token generados
 * por el Identity Provider de SFP.
 * 
 * @author cgarias
 * @since 20/02/2019
 */
public class AppiExtensionSFP {
    private static final Logger logger = Logger.getLogger(AppiExtensionSFP.class.getName());
    private static final String ERROR_CONEXION = "ERROR de conexiÃ³n: ErrorCode - %s";
    private static final String URL_INALCANZABLE = "URL no encontrada -- ERROR : al crear el llamado : %s";
    private static final String TOKEN_NOT_FOUND = "InformaciÃ³n del Token es Inexistente. - 401";
    private static final String INCONSISTENCIA_EN_TRANSACCION = "Se encontro inconsistencia en transacciÃ³n. - 0";

    private static final String CAHCE_CONTROL = "Cache-Control";
    private static final String CAHCE_CONTROL_VALUE = "no-cache";
    private static final String TOKEN_APPI = System.getenv("TOKEN_APPI");
    private static final String KEY_TOKEN ="VALUEAPP";
    private static final String METHOD_GET ="GET";
    private static final String UTF_8 = "UTF-8";
    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION = "TRANSACTION";
    private static final String ORIGINS = "origins";
    
    
    public Observable<JsonObject> sfpPetitionToIpToken(WebClient cliente, EnumPathsEnables enumPath, String token, String transaction) {
        return cliente
                .getAbs(enumPath.getUrl())
                .putHeader(KEY_TOKEN, TOKEN_APPI)
                .putHeader(AUTHORIZATION, token)
                .putHeader(TRANSACTION, transaction)
                .timeout(20000)
                .rxSend()
                .map(resp ->{ 
                        if (resp.statusCode()==HttpResponseStatus.OK.code()){
                            JsonObject response = resp.bodyAsJsonObject();
                            response.put("authorization", resp.getHeader(AUTHORIZATION));
                            return response;
                        }
                        return null;
                        }
                )
                .flatMapObservable(json -> Observable.just(json)
                ).doOnError(
                        error ->
                        logger.log(Level.SEVERE, "Error el arealizar el llamado a validar token y transaccion. : {0}", error.getMessage())
                );
    }
    
    public AppiExtensionSFP sfpPetitionToIpToken(io.vertx.ext.web.client.WebClient cliente, EnumPathsEnables enumPath, String token, String transaction, Handler<AsyncResult<JsonObject>> respuesta) {
        cliente.getAbs(enumPath.getUrl())
                .putHeader(KEY_TOKEN, TOKEN_APPI)
                .putHeader(AUTHORIZATION, token)
                .putHeader(TRANSACTION, transaction)
                .timeout(20000)
                .send(resp ->{ 
                    if (resp.succeeded() && resp.result().statusCode()==HttpResponseStatus.OK.code()){
                        JsonObject response = resp.result().bodyAsJsonObject();
                        response.put("authorization", resp.result().getHeader(AUTHORIZATION));
                        respuesta.handle(Future.succeededFuture(response));
                    }else{
                        if (resp.failed()){
                            logger.log(Level.SEVERE, "Error el arealizar el llamado a validar token y transaccion. AppiExtensionSFP sfpPetitionToIpToken : {0}", resp.cause());
                        }else{
                            logger.log(Level.SEVERE, "Error el arealizar el llamado a validar token y transaccion. AppiExtensionSFP sfpPetitionToIpToken status : {0}", resp.result().statusCode());
                        }
                        respuesta.handle(Future.failedFuture("Error de llamado a sfpPetitionToIpToken"));
                    }
                });
        return this;
    }
    
    /**
     *  MÃ©todo que realiza la autenticaciÃ³n de la transacciÃ³n y valida la vida de token y regresa la curp.
     *  
     * @param enumPath          Enum que permite seleccionar a donde se quiere generar la peticion de validacion.
     * @param token		Token recibido del llamado a nuestra aplicaciÃ³n.
     * @param transaction	NÃºmero de transacciÃ³n recibida.
     * 
     * @return	CURP		En caso exitoso, retornara el CURP del usuario que se encuentra realizando el LOGUEO.
     * @throws Exception	En coso de existir una incosistencia o 
     */
    @Deprecated
    public String sfpPetitionToIpToken(EnumPathsEnables enumPath, String token, String transaction) throws Exception{
        return sfpPetitionToIpTokenObject(enumPath, token, transaction).getCurp(); 
    }
    
    /**
     *  MÃ©todo que realiza la autenticaciÃ³n de la transacciÃ³n y valida la vida de token y un objeto json
     * que contiene transaccion, curp y token de session para interacciÃ³n con paths Kong, protegidos con 
     * JWT.
     *  
     * @param enumPath          Enum que permite seleccionar a donde se quiere generar la peticion de validacion.
     * @param token		Token recibido del llamado a nuestra aplicaciÃ³n.
     * @param transaction	NÃºmero de transacciÃ³n recibida.
     * 
     * @return	String		Objeto Json que contiene los valores de la validaciÃ³n.
     * @throws Exception	En coso de existir una incosistencia o error
     */
    @Deprecated
    public String sfpPetitionToIpTokenJson(EnumPathsEnables enumPath, String token, String transaction) throws Exception{
        return new Gson().toJson(sfpPetitionToIpTokenObject(enumPath, token, transaction));
    }
    
    
    /**
     * MÃ©todo que contiene la logica de obtencion de token con IP.
     * 
     * @param enumPath          Enum que permite seleccionar a donde se quiere generar la peticion de validacion.
     * @param token		Token recibido del llamado a nuestra aplicaciÃ³n.
     * @param transaction	NÃºmero de transacciÃ³n recibida.
     * 
     * @return DTOValidacion    Objeto que contiene los valores de la validacion de la transacciÃ³n.
     * @throws Exception        En coso de existir una incosistencia o error
     */
    private DTOValidacion sfpPetitionToIpTokenObject(EnumPathsEnables enumPath, String token, String transaction) throws Exception{
        if (enumPath == null || 
                token == null || token.isEmpty() || 
                transaction == null || transaction.isEmpty()) { 
            throw new IllegalArgumentException();
        }
        DTOValidacion usrDecla;
        URL url = new URL(enumPath.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        StringBuilder jsonResponse = new StringBuilder();
        try {
            connection.setDoOutput(true); 
            connection.setInstanceFollowRedirects(false); 
            connection.setRequestProperty(KEY_TOKEN, TOKEN_APPI);
            connection.setRequestProperty (AUTHORIZATION, token);
            connection.setRequestProperty (TRANSACTION, transaction);
            connection.setRequestMethod(METHOD_GET); 
            connection.setRequestProperty(CAHCE_CONTROL, CAHCE_CONTROL_VALUE);
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();   
            if (HttpURLConnection.HTTP_OK <= responseCode && 202 > responseCode){
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(UTF_8)));
                
                String line;
                while ((line = rd.readLine()) != null) {                    
                    jsonResponse.append(line);
                }
                usrDecla = new Gson().fromJson(jsonResponse.toString(), DTOValidacion.class);
                usrDecla.setAuthorization(connection.getHeaderField(AUTHORIZATION));
            }else{
                if (responseCode == 401){
                    throw new Exception(TOKEN_NOT_FOUND);
                }else{
                    throw new Exception(String.format(ERROR_CONEXION, connection.getResponseCode()));
                }
            }
            if (!transaction.equals(usrDecla.getTransaccion())){
                logger.log(Level.SEVERE, "validacion de libreria: validacion de transaccion enviada {0} recibida {1}", new Object[]{transaction, usrDecla.getTransaccion()});
                throw new Exception(INCONSISTENCIA_EN_TRANSACCION);
            }
            connection.disconnect(); 
        }catch(TimeoutException e) {
            connection.disconnect(); 
            throw new Exception("El time out de 10 segundos se alcanzo. "+e.getMessage());
        }catch(IllegalStateException e) {
            connection.disconnect(); 
            throw new Exception(String.format(URL_INALCANZABLE, jsonResponse.toString())+"\n" +e.getMessage());
        }catch(Exception e){
            connection.disconnect(); 
            throw new Exception(String.format(URL_INALCANZABLE, e.getMessage()));
        }        
        return usrDecla;
    }
    
    
    /**
     *  MÃ©todo que realiza la autenticaciÃ³n de la transacciÃ³n y valida la vida de token.
     *  
     * @param enumPath          Enum que permite seleccionar a donde se quiere generar la peticion de validacion.
     * @param idCliente		Identificador del sistema cliente.
     * @param secretkey         llave secreta del cliente.
     * @param curp              curp por la cual se estan consultando los datos.
     * @return String           Json as String.
     * 
     * @throws Exception	En coso de existir una incosistencia.
     */
    @Deprecated
    public String getDataUser(EnumPathsData enumPath, String idCliente, String secretkey, String curp) throws Exception{
        if (enumPath== null ||
                idCliente == null || idCliente.isEmpty() ||
                secretkey == null || secretkey.isEmpty() ||
                curp==null || curp.isEmpty()) { 
            throw new IllegalArgumentException();
        }    
        return this.getDataUser(enumPath,  idCliente,  secretkey,  curp,  enumPath.getUrl().replace(":id", idCliente).replace(":CURP", curp).replace(":SECRET", secretkey));
    } 
    
    @Deprecated
    public String getDataUser(EnumPathsData enumPath, String idCliente, String secretkey, String curp, String urlPeticion) throws Exception{
        if (enumPath== null ||
                idCliente == null || idCliente.isEmpty() ||
                secretkey == null || secretkey.isEmpty() ||
                curp==null || curp.isEmpty()) { 
            throw new IllegalArgumentException();
        }
        URL url = new URL(urlPeticion.replace(":id", idCliente).replace(":CURP", curp).replace(":SECRET", secretkey));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        StringBuilder jsonResponse = new StringBuilder();
        try {
            connection.setConnectTimeout(10000);
            connection.setDoOutput(true); 
            connection.setInstanceFollowRedirects(false); 
            connection.setAllowUserInteraction(true);
            connection.setRequestMethod(METHOD_GET);
            connection.setRequestProperty(CAHCE_CONTROL, CAHCE_CONTROL_VALUE);
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false );
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty (ORIGINS, enumPath.getUrlPet());
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK <= responseCode && 202 > responseCode){
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(UTF_8)));
                
                String line;
                while ((line = rd.readLine()) != null) {                    
                    jsonResponse.append(line);
                }
                logger.log(Level.INFO, "Recupera datos de : {0}", jsonResponse.toString());
            }else{
                throw new Exception(String.format(ERROR_CONEXION, connection.getResponseCode()));
            }
            connection.disconnect(); 
        }catch(TimeoutException e) {
            connection.disconnect(); 
            throw new Exception("El time out de 10 segundos se alcanzo. "+e.getMessage());
        }catch(IllegalStateException e) {
            connection.disconnect(); 
            throw new Exception(String.format(URL_INALCANZABLE, jsonResponse.toString())+"\n" +e.getMessage());
        }catch(Exception e){
            connection.disconnect(); 
            throw new Exception(String.format(URL_INALCANZABLE, e.getMessage()));
        }        
        return jsonResponse.toString();
    } 
    
    public Observable<JsonObject> getDataUser(WebClient cliente, EnumPathsData enumPath, String idCliente, String secretkey, String curp) {
        return cliente
                .getAbs(enumPath.getUrl().replace(":id", idCliente).replace(":CURP", curp).replace(":SECRET", secretkey))
                .putHeader(ORIGINS, enumPath.getUrlPet())
                .timeout(20000)
                .rxSend()
                .map(resp ->{ 
                        if (resp.statusCode()==HttpResponseStatus.OK.code()){
                            return resp.bodyAsJsonObject();
                        }
                        return null;
                        }
                )
                .flatMapObservable(json -> Observable.just(json)
                ).doOnError(
                        error ->
                        logger.log(Level.SEVERE, "Error al consultar los datos de usuario. : {0}", error.getMessage())
                );
    }
    
    public AppiExtensionSFP getDataUser(io.vertx.ext.web.client.WebClient cliente, EnumPathsData enumPath, String idCliente, String secretkey, String curp, Handler<AsyncResult<JsonObject>> resultado) {
        cliente.getAbs(enumPath.getUrl().replace(":id", idCliente).replace(":CURP", curp).replace(":SECRET", secretkey))
                .putHeader(ORIGINS, enumPath.getUrlPet())
                .timeout(20000)
                .send(resp -> { 
                    if (resp.succeeded() && resp.result().statusCode()==HttpResponseStatus.OK.code()){
                        resultado.handle(Future.succeededFuture(resp.result().bodyAsJsonObject()));
                    }else{
                        if (resp.failed()){
                            logger.log(Level.SEVERE, "Error al consultar los datos de usuario. AppiExtensionSFP getDataUser {0}", resp.cause());
                        }else{
                            logger.log(Level.SEVERE, "Error al consultar los datos de usuario. AppiExtensionSFP getDataUser status {0}", resp.result().statusCode());
                        }                        
                        resultado.handle(Future.failedFuture("Error de llamado a getDataUser"));
                    }
                });
        return this;
    }
}