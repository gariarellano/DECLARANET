/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "VerificarDeclaraciones" Servicio que permite verificar si 
 * existen declaraciones en proceso o insertar el tipo de declaración a presentar
 * 
 * Proyecto desarrollado por programador04@funcionpublica.gob.mx
 */

/**
 * Clase utilizada para el envío de datos al frontend en caso de que se pueda presentar una declaración
 * */
package mx.gob.sfp.dgti.decla.presentar.dto;

import java.util.List;

public class CatTiposDeclaConAniosDTO {
	// id del tipo de declaracion
	private String idTipoDecla;

	// descripción del tipo de declaración
	private String descDecla;

	// lista de años en que puede seleccionar dependiendo del tipo de declaración
	private List<Integer> anios;

	/**
	 * @return the idTipoDecla
	 */
	public String getIdTipoDecla() {
		return idTipoDecla;
	}

	/**
	 * @param idTipoDecla the idTipoDecla to set
	 */
	public void setIdTipoDecla(String idTipoDecla) {
		this.idTipoDecla = idTipoDecla;
	}

	/**
	 * @return the descDecla
	 */
	public String getDescDecla() {
		return descDecla;
	}

	/**
	 * @param descDecla the descDecla to set
	 */
	public void setDescDecla(String descDecla) {
		this.descDecla = descDecla;
	}

	/**
	 * @return the anios
	 */
	public List<Integer> getAnios() {
		return anios;
	}

	/**
	 * @param anios the anios to set
	 */
	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}

}
