/**
 * @(#)EnumCampos.java 12/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 12/11/2019
 */
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
