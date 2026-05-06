/**
 * @(#)EnumGraphQL.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Enum con etiquetas usadas para el servicio de impresion
 *
 * @author pavel.martinez
 * @since 08/03/2021
 */
public enum EnumServicioImpresion {

	NUMERO_OFICIO("numeroOficio"),
	FECHA_GENERACION_VISTA("fechaGeneracionVista"),
	DEPENDENCIA_ENTIDAD("dependenciaEntidad"),
	FECHA_VENCIMIENTO("fechaVenciemiento"),
	BODY_TEXT_OFICIO("bodyTextOficio"),
	LOGO_IMAGEN("logoImagen"),
	PUESTO_FIRMANTE("puestoFirmante"),
	FIRMA_OFICIO("firmaOficioCaracteresAutenticidad"),
	FIRMA_LISTADO("firmaListadoCaracteresAutenticidad"),
	FIRMANTE_NOMBRE("firmanteNombre"),
	PRIMER_PARRAFO("primerParrafo"),
	SEGUNDO_PARRAFO("segundoParrafo"),
	TIPO_DECLARACION("tipoDeclaracion"),
	ANIO("anio"),
	DATA("data");

	/**
	 * El valor
	 */
	private final String valor;

    /**
     * Constructor con parametros
     * @param valor
	 */
	EnumServicioImpresion(String valor) {
        this.valor = valor;
    }

    /**
     * @return the modulo
     */
	public String getValor() {
		return valor;
	}

}
