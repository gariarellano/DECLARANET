/**
 * @(#)EnumCatalogosFk.java 27/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de CatalgosGrandes
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 18/12/2019
 */
public enum EnumCatalogosFk {
    CAT_MUNICIPIO_ALCALDIA,
    CAT_SUBTIPO_INVERSION,
    CAT_CODIGO_POSTAL;

    EnumCatalogosFk() {
    }

    public static boolean existeValor(String valor) {
        try {
            EnumCatalogosFk.valueOf(valor);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
