package mx.gob.sfp.dgti.util;

public enum EnumTipoDeclaracion {

	INICIO("INICIO"), 
	CONCLUSION("CONCLUSIÓN"), 
	MODIFICACION("MODIFICACIÓN"), 
	AVISO("AVISO"),
	NOTA("NOTA")
	;

	private final String descripcion;

	private EnumTipoDeclaracion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

}
