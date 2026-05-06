/**
 * DatosEmpleo.java Mar 31, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author Miriam Sanchez programador07
 * @since Mar 31, 2020
 */
public enum DatosEmpleo {

	EMPLEO_CARGO("empleoCargoComision"),
	AREA_ADSCRIPCION("areaAdscripcion"),
	NIVEL_JERARQUICO("nivelJerarquico"),
	NIVEL_EMPLEO_CARGO("nivelEmpleoCargoComision"),
	CONTRATADO_HONORARIOS("contratadoPorHonorarios"),
	REMUNERACION_NETA("remuneracionNeta"),
	TIPO_REMUNERACION("tipoRemuneracion"),
	FUNCION_PRINCIPAL("funcionPrincipal"),
	FECHA_ENCARGO("fechaEncargo"),
	FECHA_POSESION("fechaTomaPosesion"),
	TELEFONO_OFICINA("telefonoOficina"),
	TELEFONO("telefono"),
	EXTENSION("extension"),
	CUENTA_OTRO_CARGO_PUB("cuentaConOtroCargoPublico"),
	OTRO_EMPLEO_CARGO("otroEmpleoCargoComision");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param campo 
	 */
	DatosEmpleo(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}
}
