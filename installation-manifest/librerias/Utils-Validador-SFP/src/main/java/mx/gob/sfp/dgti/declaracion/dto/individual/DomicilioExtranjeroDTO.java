/**
 * @(#)DomicilioExtranjeroDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para el domicilio en el extranjero
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DomicilioExtranjeroDTO extends LocalizacionExtranjeroDTO {

    /**
     * Id del elemento del catalogo.
     */
    private String calle;

    /**
     * Numero exterior
     */
    private String numeroExterior;

    /**
     * Numero interior
     */
    private String numeroInterior;

    /**
     * Ciudad o localidad
     */
    private String ciudadLocalidad;

    /**
     * Estado o provincia
     */
    private String estadoProvincia;

    /**
     * Codigo postal
     */
    private String codigoPostal;

    /**
     * Constructor
     */
    public DomicilioExtranjeroDTO(){ };


    public DomicilioExtranjeroDTO(CatalogoDTO pais, String calle, String numeroExterior, String numeroInterior, String ciudadLocalidad, String estadoProvincia, String codigoPostal) {
        super(pais);
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.ciudadLocalidad = ciudadLocalidad;
        this.estadoProvincia = estadoProvincia;
        this.codigoPostal = codigoPostal;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DomicilioExtranjeroDTO(JsonObject json) {
        DomicilioExtranjeroDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DomicilioExtranjeroDTOConverter.toJson(this, json);
        return json;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getCiudadLocalidad() {
        return ciudadLocalidad;
    }

    public void setCiudadLocalidad(String ciudadLocalidad) {
        this.ciudadLocalidad = ciudadLocalidad;
    }

    public String getEstadoProvincia() {
        return estadoProvincia;
    }

    public void setEstadoProvincia(String estadoProvincia) {
        this.estadoProvincia = estadoProvincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return "DomicilioExtranjeroDTO{" +
                "calle='" + calle + '\'' +
                ", numeroExterior='" + numeroExterior + '\'' +
                ", numeroInterior='" + numeroInterior + '\'' +
                ", ciudadLocalidad='" + ciudadLocalidad + '\'' +
                ", estadoProvincia='" + estadoProvincia + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                "} " + super.toString();
    }
}
