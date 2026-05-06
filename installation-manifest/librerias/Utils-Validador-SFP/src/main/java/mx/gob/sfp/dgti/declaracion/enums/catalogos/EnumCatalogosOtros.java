/**
 * @(#)EnumCatalogosOtros.java 28/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de CatalgosOtros
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 28/12/2019
 */
public enum EnumCatalogosOtros {
    CAT_ENTES;

    EnumCatalogosOtros() {
    }

    public static boolean existeValor(String valor) {
        try {
            EnumCatalogosOtros.valueOf(valor);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
