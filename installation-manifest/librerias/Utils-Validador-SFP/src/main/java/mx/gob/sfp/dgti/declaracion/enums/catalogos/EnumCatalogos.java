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
public enum EnumCatalogos {
    CAT_ESTADO_CIVIL,
    CAT_PAIS,
    CAT_REGIMEN_MATRIMONIAL,
    CAT_NACIONALIDAD,
    CAT_NIVEL_ACADEMICO,
    CAT_TIPO_PARTICIPACION,
    CAT_MONEDA,
    CAT_ENTIDAD_FEDERATIVA,
    CAT_AMBITO_SECTOR,
    CAT_TIPO_BIEN_INMUEBLE,
    CAT_TIPO_BIEN_MUEBLE,
    CAT_TIPO_DECLARACION,
    CAT_TIPO_INVERSION,
    CAT_SECTOR_PRIVADO,
    CAT_TIPO_RELACION_DEPENDIENTES,
    CAT_TIPO_INSTRUMENTO,
    CAT_TIPO_RELACION_BIENES,
    CAT_FORMA_ADQUISICION_BIEN,
    CAT_MOTIVO_BAJA_BIEN,
    CAT_TIPO_VEHICULO,
    CAT_TIPO_ADEUDO,
    CAT_TIPO_INSTITUCION,
    CAT_BENEFICIARIO_PROGRAMA,
    CAT_TIPO_APOYO,
    CAT_TIPO_BENEFICIO
    ;

    EnumCatalogos() {
    }

    public static boolean existeValor(String valor) {
        try {
            EnumCatalogos.valueOf(valor);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
