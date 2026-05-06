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

    BENEFICIOS("beneficios"),
    TIPO_BENEFICIO("tipoBeneficio"),
    BENEFICIARIO("beneficiario"),
    OTORGANTE("otorgante"),
    FORMA_RECEPCION("formaRecepcion"),
    ESP_BENEFICIO("especifiqueBeneficio"),
    MONTO_MENSUAL_APROX("montoMensualAproximado"),
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
