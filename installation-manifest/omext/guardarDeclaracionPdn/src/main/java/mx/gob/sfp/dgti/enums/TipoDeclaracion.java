/**
 * @TipoOperacion.java May 16, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sànchez programador07
 * @modifiedBy programador09
 * @since May 16, 2020
 */

import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;

public enum TipoDeclaracion {
    INICIO("INICIAL", "INICIAL"),
    CONCLUSION("CONCLUSIÓN", "CONCLUSION"),
    MODIFICACION("MODIFICACIÓN", "MODIFICACION"),
    AVISO(EnumTipoDeclaracion.AVISO.name(), "AVISO");

    private final String clave;
    private final String clavePdn;

    private TipoDeclaracion(String clave, String clavePdn) {
        this.clave = clave;
        this.clavePdn = clavePdn;
    }

    /**
     * @return the id
     */
    public String getClave() {
        return clave;
    }

    /**
     * @return the clavePdn
     */
    public String getClavePdn() {
        return clavePdn;
    }
}