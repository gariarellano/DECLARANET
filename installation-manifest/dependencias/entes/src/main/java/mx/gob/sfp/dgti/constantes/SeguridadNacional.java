/**
 * @(#)Poder.java 02/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

/**
 * Enum con los tipos de poder
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 02/05/2019
 */
public enum SeguridadNacional {
    NO_SEG_NAC(0, "No es una institución de seguridad nacional."),
    SI_SEG_NAC(1, "Sí es una institución de seguridad nacional.");

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
     * @param id: id del poder
     * @param descripcion: descripcion del poder
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 02/05/2019
     */
    SeguridadNacional(int id, String descripcion) {
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
