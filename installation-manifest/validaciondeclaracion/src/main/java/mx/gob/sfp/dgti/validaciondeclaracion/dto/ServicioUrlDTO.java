/**
 * @(#)ServicioUrl.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.dto;

import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumModulo;

/**
 * Clase que contiene el DTO para ServicioUrlDTO
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class ServicioUrlDTO {

    /**
     * Nombre del servicio al que se llamara, debe de coincidir con el valor en el schema de graphQL
     */
    private EnumModulo servicio;

    /**
     * Url del servicio al que se llamara, se manejan rutas absolutas
     */
    private String url;

    /**
     * Constructor
     *
     * @param servicio
     * @param url
     */
    public ServicioUrlDTO(EnumModulo servicio, String url) {
        this.servicio = servicio;
        this.url = url;
    }

    public EnumModulo getServicio() {
        return servicio;
    }

    public void setServicio(EnumModulo servicio) {
        this.servicio = servicio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
