/**
 * @(#)NivelOrdenGobiernoDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.respuestas;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO los datos que se regresan referente al firmado de vistas
 *
 * @author pavel.martinez
 * @since 23/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class FirmanteDTO {

    /**
     *
     */
    private String cliente_id;

    /**
     *
     */
    private String secret_key;

    /**
     *
     */
    private String servicio;

    /**
     *
     */
    private Integer activo;

    /**
     *
     */
    private String nombre;

    /**
     *
     */
    private String puesto;

    /**
     * Constructor
     */
    public FirmanteDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public FirmanteDTO(JsonObject json) {
        FirmanteDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        FirmanteDTOConverter.toJson(this, json);
        return json;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    @Override
    public String toString() {
        return "FirmanteDTO{" +
                "cliente_id='" + cliente_id + '\'' +
                ", secret_key='" + secret_key + '\'' +
                ", servicio='" + servicio + '\'' +
                ", activo=" + activo +
                ", nombre='" + nombre + '\'' +
                ", puesto='" + puesto + '\'' +
                '}';
    }
}
