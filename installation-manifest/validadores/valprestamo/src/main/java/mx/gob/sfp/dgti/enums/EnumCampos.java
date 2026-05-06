/**
 * @(#)EnumCampos.java 09/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */

/**
 * Enums de campos
 * @author programador04
 * @since 09/12/2019
 */

package mx.gob.sfp.dgti.enums;

public enum EnumCampos {

    VEHICULOS("vehiculos"),
    TIPO_VEHICULO("tipoVehiculo"),
    MARCA("marca"),
    MODELO("modelo"),
    ANIO("anio"),
    NUMERO_SERIE_REGISTRO("numeroSerieRegistro"),
    LUGAR_REGISTRO("lugarRegistro");

    private final String campo;

    /**
     * Constructor con parametros
     * @param campo
     */
    EnumCampos(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
    public String getCampo() {
        return campo;
    }

}
