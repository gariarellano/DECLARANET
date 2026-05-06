package mx.gob.sfp.dgti.declaracion.enums.catalogos;


/**
 * Enum para el tipo de bien para préstamo o comodato
 *
 * @author programador04
 * @since 11/12/2019
 */
public enum EnumTipoBienPrestamo {
	INMUEBLE(1,"INMUEBLE"),
	VEHICULO(2,"VEHÍCULO");
	

	private final Integer id;
	private final String descripcion;

    /**
     * Constructor
     * @param id
     */
	EnumTipoBienPrestamo(Integer id, String descripcion) {
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
