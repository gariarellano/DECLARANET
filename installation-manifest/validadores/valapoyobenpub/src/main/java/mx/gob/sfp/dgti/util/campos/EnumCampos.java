/**
 * @(#)EnumCampos.java 08/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 08/11/2019
 */
public enum EnumCampos {

    APOYOS("apoyos"),
    BENEFICIARIO("beneficiarioPrograma"),
    NOMBRE_PROGRAMA("nombrePrograma"),
    INSTITUCION_OTORGANTE("institucionOtorgante"),
    NIVEL_ORDEN_GOB("nivelOrdenGobierno"),
    TIPO_APOYO("tipoApoyo"),
    FORMA_RECEPCION("formaRecepcion"),
    MONTO_APOYO_MENSUAL("montoApoyoMensual"),
    ESPECIFIQUE_APOYO("especifiqueApoyo"),;

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
