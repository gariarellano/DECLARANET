/**
 * @(#)EnumCampos.java 07/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 07/11/2019
 */
public enum EnumCampos {

    ADEUDOS_PASIVOS("adeudosPasivos"),
    NINGUNO("ninguno"),
    ADEUDOS("adeudos"),
    TITULAR("titular"),
    TIPO_ADEUDO("tipoAdeudo"),
    NUMERO_CUENTA_CONTRATO("numeroCuentaContrato"),
    FECHA_ADQUISICION("fechaAdquisicion"),
    MONTO_ORIGINAL("montoOriginal"),
    SALDO_INSOLUTO("saldoInsoluto"),
    TERCERO("tercero"),
    TERCEROS("terceros"),
    OTORGANTE_CREDITO("otorganteCredito"),
    PAIS_ADEUDO("paisAdeudo"),
    ACLARACIONES("aclaracionesObservaciones");

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
