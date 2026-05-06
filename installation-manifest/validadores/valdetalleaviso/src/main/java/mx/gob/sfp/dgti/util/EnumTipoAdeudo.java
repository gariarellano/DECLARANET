/**
 * @(#)EnumTipoAdeudo.java 21/01/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 21/01/2020
 */
public enum EnumTipoAdeudo {

    CREDITO_HIPOTECARIO(1, "CRÉDITO HIPOTECARIO"),
    CREDITO_AUTOMOTRIZ(2, "CRÉDITO AUTOMOTRÍZ"),
    CREDITO_PERSONAL(3, "CRÉDITO PERSONAL"),
    TARJETA_CREDITO_BANC(4, "TARJETA DE CRÉDITO BANCARIA"),
    TARJETA_CREDITO_DEP(5, "TARJETA DE CRÉDITO DEPARTAMENTAL"),
    PRESTAMO_PERSONAL(6, "PRÉSTAMO PERSONAL"),
    OTRO(9999, "OTRO (ESPECIFIQUE)");

    private final Integer id;
    private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
     */
    EnumTipoAdeudo(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }
}
