/**
 * @(#)EnumEtiquetas.java 19/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum de los tipos de respuesta que existen
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 19/12/2019
 */
public enum EnumCampos {
    ALGORITMO_RS256("RS256"),
    DATA("data"),
    USUARIO("usuario"),
    TIEMPO("tiempo"),
    JWT("jwt"),
    VACIO(""),
    ISS("iss"),
    AUTHORIZATION("Authorization"),
    COLNAME("colName"),
    SISTEMA("sistema"),
    CURP("curp"),
    ID_USUARIO("idUsuario"),
    BEARER("Bearer ")
    ;

    private final String valor;

    EnumCampos(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}