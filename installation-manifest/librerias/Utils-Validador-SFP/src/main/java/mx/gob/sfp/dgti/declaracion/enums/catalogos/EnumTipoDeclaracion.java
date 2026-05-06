package mx.gob.sfp.dgti.declaracion.enums.catalogos;
/**
 * Enum con el catálogo de tipos de declaraciones
 *
 * @author Gustavo Gutiérrez programador04
 * @since 29/10/2019
 */
public enum EnumTipoDeclaracion {

	INICIO(0, "INICIO"), 
	CONCLUSION(1, "CONCLUSIÓN"),
	MODIFICACION(2, "MODIFICACIÓN"),
	AVISO(6, "AVISO"),
//	COMPLEMENTARIA(8, "COMPLEMENTARIA"),
//	NOTA(9, "NOTA")
	;

	private final Integer id;
	private final String descripcion;

	private EnumTipoDeclaracion(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

}
