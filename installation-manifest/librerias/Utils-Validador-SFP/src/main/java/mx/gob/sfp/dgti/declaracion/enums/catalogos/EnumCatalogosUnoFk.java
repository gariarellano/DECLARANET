/**
 * @(#)EnumCatalogosUnoFk.java 15/01/2019
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de los CatalogosUnoFk
 *
 * @author pavel.martinez
 * @since 15/01/2019
 */
public enum EnumCatalogosUnoFk {
    CAT_NIVEL_JERARQUICO;

    EnumCatalogosUnoFk() {
    }

    public static boolean existeValor(String valor) {
        try {
            EnumCatalogosUnoFk.valueOf(valor);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
