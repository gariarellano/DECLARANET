/**
 * @(#)NivelGobierno.java 02/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

/**
 * Enum con los tipos de nivel de gobierno
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 02/05/2019
 */
public enum NivelGobierno {
    FEDERAL(1, "Federal."),
    ESTATAL(2, "Estatal."),
    MUNICIPAL(3, "Municipal.");

    /**
     * Id del nivel de gobierno
     */
    private final int id;

    /**
     * Descripcion del nivel de gobierno
     */
    private final String descripcion;

    /**
     * Metodo constructor
     *
     * @param id: id del nivel de gobierno
     * @param descripcion: descripcion del nivel de gobierno
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 02/05/2019
     */
    NivelGobierno(int id, String descripcion) {
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
