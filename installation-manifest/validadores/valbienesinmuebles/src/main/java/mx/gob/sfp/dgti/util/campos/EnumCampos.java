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

    BIENES_INMUEBLES("bienesInmuebles"),
    TIPO_INMUEBLE("tipoInmueble"),
    PORCENTAJE_PROPIEDAD("porcentajePropiedad"),
    SUPERF_TERRENO_M2("superficieTerrenoM2"),
    SUPERF_CONSTR_M2("superficieConstruccionM2"),
    DATO_IDENTIFICACION("datoIdentificacion"),
    VALOR_CONFORME_A("valorConformeA"),
    DOMICILIO("domicilio");

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
