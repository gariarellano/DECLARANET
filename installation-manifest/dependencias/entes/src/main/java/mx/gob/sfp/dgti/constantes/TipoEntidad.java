/**
 * @(#)TipoEntidad.java 02/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

/**
 * Enum con los tipos de entidades
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 02/05/2019
 */
public enum TipoEntidad {
    SECRETARIA(1, "Secretaría o Dependencia."),
    ORGANO_DESCONCENTRADO(2, "Organismo Descentralizado."),
    ORGANO_DESCENTRALIZADO(3, "Organismo Descentralizado."),
    EMPRESA_PARTIC_ESTATAL(4, "Empresa de Participación Estatal."),
    INST_NAC_CREDITO(5, "Institución Nacional de Crédito."),
    INST_NAC_SEGUROS_FIANZAS(6, "Institución Nacional de Seguros y de Fianzas."),
    FIDEICOMISO(7, "Fideicomiso."),
    UNIDAD_ADMIN(8, "Unidad Administrativa."),
    PROGRAMA(9, "Programa."),
    ORGANO_AUTONOMO(10, "Órgano autónomo."),
    ORGANO_REGULADOR_ENERG(11, "Órgano Regulador Coordinado en Materia Energética."),
    EMPRESA_PROD_ESTADO(12, "Empresa Productiva del Estado."),
    FIDEICOMISO_PUBLICO(13, "Fideicomiso Público que forma parte del Sistema Financiero Mexicano.");

    /**
     * Id del estatus
     */
    private final int id;

    /*
     * Descripcion del tipo de entidad
     */
    private final String descripcion;

    /**
     * Metodo constructor
     *
     * @param id: id del estatus
     * @param descripcion: descripcion del estatus
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 02/05/2019
     */
    TipoEntidad(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
