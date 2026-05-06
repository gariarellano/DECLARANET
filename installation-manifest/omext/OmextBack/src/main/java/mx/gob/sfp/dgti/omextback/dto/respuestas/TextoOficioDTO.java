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
 * DTO del texto del oficio
 *
 * @author pavel.martinez
 * @since 23/02/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TextoOficioDTO {

    /**
     * Id del texto oficio
     */
    private String idTextoOficio;

    /**
     * Texto completo del oficio
     */
    private String bodyTextOficio;

    /**
     * Logo
     */
    private String logoImagen;

    /**
     * Primer parrafo
     */
    private String primerParrafo;

    /**
     * Segundo parrafo
     */
    private String segundoParrafo;

    /**
     * Activo
     */
    private Boolean activo;

    /**
     * Constructor
     */
    public TextoOficioDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public TextoOficioDTO(JsonObject json) {
        TextoOficioDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TextoOficioDTOConverter.toJson(this, json);
        return json;
    }

    public String getIdTextoOficio() {
        return idTextoOficio;
    }

    public void setIdTextoOficio(String idTextoOficio) {
        this.idTextoOficio = idTextoOficio;
    }

    public String getBodyTextOficio() {
        return bodyTextOficio;
    }

    public void setBodyTextOficio(String bodyTextOficio) {
        this.bodyTextOficio = bodyTextOficio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getLogoImagen() {
        return logoImagen;
    }

    public void setLogoImagen(String logoImagen) {
        this.logoImagen = logoImagen;
    }

    public String getPrimerParrafo() {
        return primerParrafo;
    }

    public void setPrimerParrafo(String primerParrafo) {
        this.primerParrafo = primerParrafo;
    }

    public String getSegundoParrafo() {
        return segundoParrafo;
    }

    public void setSegundoParrafo(String segundoParrafo) {
        this.segundoParrafo = segundoParrafo;
    }

    @Override
    public String toString() {
        return "TextoOficioDTO{" +
                "idTextoOficio='" + idTextoOficio + '\'' +
                ", bodyTextOficio='" + bodyTextOficio + '\'' +
                ", logoImagen='" + logoImagen + '\'' +
                ", primerParrafo='" + primerParrafo + '\'' +
                ", segundoParrafo='" + segundoParrafo + '\'' +
                ", activo=" + activo +
                '}';
    }
}
