/**
 * @(#)DomicilioMexicoDTO.java 12/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para el domicilio en Mexico
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class DomicilioMexicoDTO extends LocalizacionMexicoDTO {

    /**
     * Calle
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
     * Colonia o localidad
     */
    private String coloniaLocalidad;

    /**
     * Municipio o alcaldia
     */
    private CatalogoFkDTO municipioAlcaldia;

    /**
     * Codigo postal
     */
    private String codigoPostal;

    /**
     * Constructor
     */
    public DomicilioMexicoDTO(){ };

    public DomicilioMexicoDTO(CatalogoDTO entidadFederativa, String calle, String numeroExterior,
                              String numeroInterior, String coloniaLocalidad, CatalogoFkDTO municipioAlcaldia,
                              String codigoPostal) {
        super(entidadFederativa);
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.coloniaLocalidad = coloniaLocalidad;
        this.municipioAlcaldia = municipioAlcaldia;
        this.codigoPostal = codigoPostal;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public DomicilioMexicoDTO(JsonObject json) {
        DomicilioMexicoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DomicilioMexicoDTOConverter.toJson(this, json);
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

    public String getColoniaLocalidad() {
        return coloniaLocalidad;
    }

    public void setColoniaLocalidad(String coloniaLocalidad) {
        this.coloniaLocalidad = coloniaLocalidad;
    }

    public CatalogoFkDTO getMunicipioAlcaldia() {
        return municipioAlcaldia;
    }

    public void setMunicipioAlcaldia(CatalogoFkDTO municipioAlcaldia) {
        this.municipioAlcaldia = municipioAlcaldia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return "DomicilioMexicoDTO{" +
                "calle='" + calle + '\'' +
                ", numeroExterior='" + numeroExterior + '\'' +
                ", numeroInterior='" + numeroInterior + '\'' +
                ", coloniaLocalidad='" + coloniaLocalidad + '\'' +
                ", municipioAlcaldia=" + municipioAlcaldia +
                ", codigoPostal=" + codigoPostal +
                "} " + super.toString();
    }
}
