/**
 * Bien.java Apr 8, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Apr 8, 2020
 */
public enum Bien {

    BIENES_INMUEBLES("bienesInmuebles"),
    BIENES_MUEBLES("bienesMuebles"),
    BIEN_MUEBLE("bienMueble"),
    VEHICULOS("vehiculos"),
    VEHICULO("vehiculo"),
    TIPO_INMUEBLE("tipoInmueble"),
    TIPO_INMUEBLE_OTRO("tipoInmuebleOtro"),
    TIPO_MUEBLE("tipoBien"),
    TIPO_MUEBLE_OTRO("tipoBienOtro"),
    TIPO_VEHICULO("tipoVehiculo"),
    TIPO_VEHICULO_OTRO("tipoVehiculoOtro"),
    PORCENTAJE_PROPIEDAD("porcentajePropiedad"),
    SUPERF_TERRENO_M2("superficieTerrenoM2"),
    SUPERF_CONSTR_M2("superficieConstruccionM2"),
    DATO_IDENTIFICACION("datoIdentificacion"),
    VALOR_CONFORME_A("valorConformeA"),
    BIEN_INMUEBLE("bienInmueble"),
    INMUEBLE("inmueble"),
    SUPERFICIE_TERRENO("superficieTerreno"),
    SUPERFICIE_CONSTR("superficieConstruccion"),
    TERCERO("tercero"),
    TRANSMISOR("transmisor"),
    NINGUNO("ninguno"), 
    RELACION("relacion"),
    DESCRIPCION_GENERAL_BIEN("descripcionGeneralBien"),
    MARCA("marca"),
    MODELO("modelo"),
    ANIO("anio"),
    LUGAR_REGISTRO("lugarRegistro"),
    NUMERO_SERIE("numeroSerieRegistro"), 
    TIPO_BIEN("tipoBien"),
    MOTIVO_BAJA_OTRO("motivoBajaOtro"),
    RELACION_OTRO("relacionConTitularOtro")
    ;
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
	Bien(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
