package mx.gob.sfp.dgti.entity;

import io.vertx.core.json.JsonObject;

/**
 * Entidad de transaccion
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 11/03/2019
 */
public class TransaccionEO {

    private Integer idTransaccion;
    private String codigoSha;
    private String codigoBase64;
    private String codigoToken;
    private String fecha;
    private Integer idAplicacion;
    private String curp;
    private Integer idUsuario;

    /**
     * Constructor con parámetros
     *
     * @param idTransaccion
     * @param codigoSha
     * @param codigoBase64
     * @param codigoToken
     * @param fecha
     * @param idAplicacion
     * @param curp
     */
    public TransaccionEO(Integer idTransaccion, String codigoSha, String codigoBase64, String codigoToken, String fecha, Integer idAplicacion, String curp, Integer idUsuario) {
        this.idTransaccion = idTransaccion;
        this.idAplicacion = idAplicacion;
        this.codigoSha = codigoSha;
        this.codigoBase64 = codigoBase64;
        this.codigoToken = codigoToken;
        this.fecha = fecha;
        this.curp = curp;
        this.idUsuario = idUsuario;
    }

    public TransaccionEO(JsonObject jsonObject) {
        this(
                jsonObject.getInteger("idTransaccion"),
                jsonObject.getString("codigoSha"),
                jsonObject.getString("codigoBase64"),
                jsonObject.getString("codigoToken"),
                jsonObject.getString("fecha"),
                jsonObject.getInteger("idAplicacion"),
                jsonObject.getString("curp"),
                jsonObject.getInteger("idUsuario")
        );
    }

    /**
     * @return the idTransaccion
     */
    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * @param idTransaccion the idTransaccion to set
     */
    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    /**
     * @return the codigosha
     */
    public String getCodigoSha() {
        return codigoSha;
    }

    /**
     * @param codigosha the codigosha to set
     */
    public void setCodigoSha(String codigoSha) {
        this.codigoSha = codigoSha;
    }

    /**
     * @return the codigobase64
     */
    public String getCodigoBase64() {
        return codigoBase64;
    }

    /**
     * @param codigobase64 the codigobase64 to set
     */
    public void setCodigoBase64(String codigoBase64) {
        this.codigoBase64 = codigoBase64;
    }

    /**
     * @return the codigoToken
     */
    public String getCodigoToken() {
        return codigoToken;
    }

    /**
     * @param codigoToken the codigoToken to set
     */
    public void setCodigoToken(String codigoToken) {
        this.codigoToken = codigoToken;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the idAplicacion
     */
    public Integer getIdAplicacion() {
        return idAplicacion;
    }

    /**
     * @param idAplicacion the idAplicacion to set
     */
    public void setIdAplicacion(Integer idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    /**
     * @return the curp
     */
    public String getCurp() {
        return curp;
    }

    /**
     * @param curp the curp to set
     */
    public void setCurp(String curp) {
        this.curp = curp;

    }

    /**
     * @return the idUsuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    /**
     * @param idUsuario to set
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Función toString
     *
     * @return String cadena con las propiedades de la clase
     */
    @Override
    public String toString() {
        return "TransaccionEO [idTransaccion=" + idTransaccion + ", codigoSha=" + codigoSha + ", codigoBase64="
                + codigoBase64 + ", codigoToken=" + codigoToken + ", fecha=" + fecha + ", idAplicacion=" + idAplicacion
                + ", curp=" + curp + ", idUsuario=" + idUsuario + "]";
    }

}
