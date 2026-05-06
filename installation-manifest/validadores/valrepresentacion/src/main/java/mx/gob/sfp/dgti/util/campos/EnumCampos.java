/**
 * @(#)EnumCampos.java 10/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 10/11/2019
 */
public enum EnumCampos {

    REPRESENTACIONES("representaciones"),
    PARTICIPANTE("participante"),
    TIPO_REPRESENTACION("tipoRepresentacion"),
    FECHA_IN_REPRESENTACION("fechaInicioRepresentacion"),
    REPRES_REPRES("representanteRepresentado"),
    RECIBE_REMUNERACION("recibeRemuneracion"),
    MONTO_MENSUAL("montoMensual"),
    LOCALIZACION("localizacion"),
    SECTOR("sector");

    private final String campo;

    /**
     * Constructor con parametros
     * @param campo
     */
    EnumCampos(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
    public String getCampo() {
        return campo;
    }

}
