/**
 * @(#)EnumRelacionDeclarante.java 04/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catálogo de relacion declarante
 * @author pavel.martinez
 * @since 04/11/2019
 */
public enum EnumRelacionDeclarante {

    CONYUGE(1, "CÓNYUGE"),
    CONCUBINARIO(2, "CONCUBINA / CONCUBINARIO / UNIÓN LIBRE / OTRA"),
    SOCIEDAD_CONVIVENCIA(3, "SOCIEDAD EN CONVIVENCIA");

    private final Integer id;
    private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
     */
    EnumRelacionDeclarante(Integer id, String descripcion) {
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
