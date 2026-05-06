/**
 * @(#)DatosDTO.java 02/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para el modulo de domicilio del declarante
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 26/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DatosDTO{

    /**
     * Usuario del token
     */
    private String usuario;

    /**
     * datos de expiracion para los tokens
     */
    private String tiempo;
    
    
    /**
     * Issuer del token
     */
    private String iss;

    /**
     * Datos adicionales en el token
     */
    private Object data;

    /**
     * Tiempo de exp en minutos
     */
    private int expMinutos;
    
    //datos de control de token
    private Long iat;
    private Long exp;

    public DatosDTO() {

    }

    public DatosDTO(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DatosDTO(JsonObject json) {
        DatosDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DatosDTOConverter.toJson(this, json);
        return json;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getExpMinutos() {
        return expMinutos;
    }

    public void setExpMinutos(int expMinutos) {
        this.expMinutos = expMinutos;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatosDTO{");
        sb.append("usuario='").append(usuario).append('\'');
        sb.append(", iss='").append(iss).append('\'');
        sb.append(", data=").append(data);
        sb.append(", expMinutos=").append(expMinutos);
        sb.append('}');
        return sb.toString();
    }
}