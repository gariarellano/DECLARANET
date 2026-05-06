/**
 * @(#)CatalogoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.campos;

/**
 * Enum con los campos del módulo de Actividad anual anterior
 * @author Miriam Sanchez Sánchez programador07 
 * @since 31/10/2019
 */
public enum EnumActividadAnualAnt {

	SERVIDOR_PUBLICO("servidorPublicoAnioAnterior"),
	FECHA_INICIO("fechaInicio"),
	FECHA_CONCLUSION("fechaConclusion"),
	ENAJENACION_BIENES("enajenacionBienes"),
	ACTIVIDAD_ANUAL("actividadAnual");
	
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumActividadAnualAnt(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
