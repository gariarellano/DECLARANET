/**
 * @(#)EnumCampos.java 19/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.util;

/**
 * Enum de los tipos de respuesta que existen
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 19/12/2019
 */
public enum EnumCampos {
    CAT("cat"),
    ESTADO("estado"),
    VARIABLES("variables"),
    REGISTRO("registro"),
    ENUMS("enums"),
    CATALOGO("catalogo"),
    CATALOGOS("catalogos"),
    DATA("data"),
    INFO("info"),
    RESPUESTA("respuesta"),
    VALIDAR_INFO_CATALOGO("validarInfoCatalogo");

    private final String valor;

    EnumCampos(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
