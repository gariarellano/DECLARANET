/**
 * @(#)EnumValorConformeA.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

/**
 * Enum de catalogo de si el valor de adquisicion del inmueble es conforme a
 * @author pavel.martinez
 * @since 11/11/2019
 */
public enum EnumValorConformeA {

    ESCRITURA_PUBLICA(1, "ESCRITURA PÚBLICA"),
    SENTENCIA(2, "SENTENCIA"),
    CONTRATO(3, "CONTRATO");

    private final Integer id;
    private final String descripcion;

    /**
     * Constructor con parametros
     * @param id
     * @param descripcion
     */
    EnumValorConformeA(Integer id, String descripcion) {
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
