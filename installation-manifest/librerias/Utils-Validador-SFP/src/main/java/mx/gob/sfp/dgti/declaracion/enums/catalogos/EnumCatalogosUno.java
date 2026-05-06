/**
 * @(#)EnumTipoResp.java 27/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de los tipos de respuesta que existen
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 27/09/2019
 */
public enum EnumCatalogosUno {
    CAT_TITULAR,
    CAT_PAIS_LADA;

    EnumCatalogosUno() {
    }

    public static boolean existeValor(String valor) {
        try {
            EnumCatalogosUno.valueOf(valor);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
