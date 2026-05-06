/**
 * @(#)EnumDomicilio.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los nombres de todos los campos utilizados por el validador
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
public enum EnumDomicilio {

	DOMICILIO("domicilio"),
	DOMICILIO_EXTRANJERO("domicilioExtranjero"),
	DOMICILIO_MEXICO("domicilioMexico"),
	LOCALIZACION("localizacion"),
	LOCALIZACION_EXTRANJERO("localizacionExtranjero"),
	LOCALIZACION_MEXICO("localizacionMexico"),
	UBICACION("ubicacion"),
	CALLE("calle"),
	CODIGO_POSTAL("codigoPostal"),
	COLONIA("coloniaLocalidad"),
	CIUDAD_LOCALIDAD("ciudadLocalidad"),
	PAIS("pais"),
	ESTADO_PROVINCIA("estadoProvincia"),
	ENTIDAD_FED("entidadFederativa"),
	MUNICIPIO("municipioAlcaldia"),
	NUMERO_EXT("numeroExterior"),
	NUMERO_INT("numeroInterior");

	private final String campo;

    /**
     * Constructor
     * @param
	 */
    EnumDomicilio(String nombre) {
        this.campo = nombre;
    }

    /**
     * @return the campo
     */
	public String getNombre() {
		return campo;
	}

}
