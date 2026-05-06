/**
 * Adeudo.java Apr 13, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 13, 2020
 */
public enum Adeudo {

	ADEUDOS_PASIVOS("adeudosPasivos"),
	ADEUDO("adeudo"),
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
    LOCALIZACION("localizacionAdeudo"),
    NOMBRE_INSTITUCION("nombreInstitucion"),
    TIPO_ADEUDO_OTRO("tipoAdeudoOtro");

    private final String campo;

    /**
     * Constructor con parametros
     * @param campo
     */
    Adeudo(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
    public String getCampo() {
        return campo;
    }

}
