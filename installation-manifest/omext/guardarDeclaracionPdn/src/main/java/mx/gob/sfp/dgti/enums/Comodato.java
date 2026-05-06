/**
 * Comodato.java Apr 14, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @modifiedBy programador09
 * @since Apr 14, 2020
 */
public enum Comodato {

    VEHICULOS("vehiculos"),
    TIPO_VEHICULO("tipoVehiculo"),
    MARCA("marca"),
    MODELO("modelo"),
    ANIO("anio"),
    NUMERO_SERIE_REGISTRO("numeroSerieRegistro"),
    LUGAR_REGISTRO("lugarRegistro"),
    PRESTAMO("prestamo"),
    DUENO_TITULAR("duenoTitular"),
    TIPO_DUENO_TITULAR("tipoDuenoTitular"),
    NOMBRE_TITULAR("nombreTitular"),
    RELACION_CON_TITULAR("relacionConTitular"),
    TIPO("tipo");

    private final String campo;

    /**
     * Constructor con parametros
     *
     * @param campo
     */
    Comodato(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
    public String getCampo() {
        return campo;
    }
}