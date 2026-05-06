package mx.gob.sfp.dgti.dto;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;

@DataObject(generateConverter = true, inheritConverter = true)
public class EnteReceptorDeclaDTO {

	private String _id;
	private String id;
	private boolean activo;
	private int collName;
	private String fechaIncorporacion;
	private String imagenB64;
	private String imagenOficialB64;
	private String imagenInstitucionalB64;
	private String nombre;
	private String path;
	private String llaveEncriptado;
	private String fechaFirmaConvenio;
	private String prefijo;
	private EnteReceptorDTO ente;
	private DatosContactoDTO datosContacto;
	private String urlConsDecla;
	private String urlPrecarga;
	
	
	public EnteReceptorDeclaDTO() {
		
	}
	
	public EnteReceptorDeclaDTO(String _id, String id, boolean activo, int collName, String fechaIncorporacion, String imagenB64,
			String imagenOficialB64, String imagenInstitucionalB64, String nombre, String path, String llaveEncriptado,
			String fechaFirmaConvenio, String prefijo, EnteReceptorDTO ente, DatosContactoDTO datosContacto,String urlConsDecla, String urlPrecarga) {
		super();
		this._id = _id;
		this.id = id;
		this.activo = activo;
		this.collName = collName;
		this.fechaIncorporacion = fechaIncorporacion;
		this.imagenB64 = imagenB64;
		this.imagenOficialB64 = imagenOficialB64;
		this.imagenInstitucionalB64 = imagenInstitucionalB64;
		this.nombre = nombre;
		this.path = path;
		this.llaveEncriptado = llaveEncriptado;
		this.fechaFirmaConvenio = fechaFirmaConvenio;
		this.prefijo = prefijo;
		this.ente = ente;
		this.datosContacto = datosContacto;
		this.urlConsDecla = urlConsDecla;
		this.urlPrecarga = urlPrecarga;
	}
	
	 /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public EnteReceptorDeclaDTO(JsonObject json) {
    	EnteReceptorDeclaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        EnteReceptorDeclaDTOConverter.toJson(this, json);
        return json;
    }
	
    
    
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	/**
	 * @return the collName
	 */
	public int getCollName() {
		return collName;
	}
	/**
	 * @param collName the collName to set
	 */
	public void setCollName(int collName) {
		this.collName = collName;
	}
	/**
	 * @return the fechaIncorporacion
	 */
	public String getFechaIncorporacion() {
		return fechaIncorporacion;
	}
	/**
	 * @param fechaIncorporacion the fechaIncorporacion to set
	 */
	public void setFechaIncorporacion(String fechaIncorporacion) {
		this.fechaIncorporacion = fechaIncorporacion;
	}
	/**
	 * @return the imagenB64
	 */
	public String getImagenB64() {
		return imagenB64;
	}
	/**
	 * @param imagenB64 the imagenB64 to set
	 */
	public void setImagenB64(String imagenB64) {
		this.imagenB64 = imagenB64;
	}
	/**
	 * @return the imagenOficialB64
	 */
	public String getImagenOficialB64() {
		return imagenOficialB64;
	}
	/**
	 * @param imagenOficialB64 the imagenOficialB64 to set
	 */
	public void setImagenOficialB64(String imagenOficialB64) {
		this.imagenOficialB64 = imagenOficialB64;
	}
	/**
	 * @return the imagenInstitucionalB64
	 */
	public String getImagenInstitucionalB64() {
		return imagenInstitucionalB64;
	}
	/**
	 * @param imagenInstitucionalB64 the imagenInstitucionalB64 to set
	 */
	public void setImagenInstitucionalB64(String imagenInstitucionalB64) {
		this.imagenInstitucionalB64 = imagenInstitucionalB64;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the llaveEncriptado
	 */
	public String getLlaveEncriptado() {
		return llaveEncriptado;
	}
	/**
	 * @param llaveEncriptado the llaveEncriptado to set
	 */
	public void setLlaveEncriptado(String llaveEncriptado) {
		this.llaveEncriptado = llaveEncriptado;
	}
	/**
	 * @return the fechaFirmaConvenio
	 */
	public String getFechaFirmaConvenio() {
		return fechaFirmaConvenio;
	}
	/**
	 * @param fechaFirmaConvenio the fechaFirmaConvenio to set
	 */
	public void setFechaFirmaConvenio(String fechaFirmaConvenio) {
		this.fechaFirmaConvenio = fechaFirmaConvenio;
	}
	/**
	 * @return the prefijo
	 */
	public String getPrefijo() {
		return prefijo;
	}
	/**
	 * @param prefijo the prefijo to set
	 */
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	/**
	 * @return the ente
	 */
	public EnteReceptorDTO getEnte() {
		return ente;
	}
	/**
	 * @param ente the ente to set
	 */
	public void setEnte(EnteReceptorDTO ente) {
		this.ente = ente;
	}
	/**
	 * @return the datosContacto
	 */
	public DatosContactoDTO getDatosContacto() {
		return datosContacto;
	}
	/**
	 * @param datosContacto the datosContacto to set
	 */
	public void setDatosContacto(DatosContactoDTO datosContacto) {
		this.datosContacto = datosContacto;
	}

	/**
	 * @return the urlConsDecla
	 */
	public String getUrlConsDecla() {
		return urlConsDecla;
	}

	/**
	 * @param urlConsDecla the urlConsDecla to set
	 */
	public void setUrlConsDecla(String urlConsDecla) {
		this.urlConsDecla = urlConsDecla;
	}

	/**
	 * @return the urlPrecarga
	 */
	public String getUrlPrecarga() {
		return urlPrecarga;
	}

	/**
	 * @param urlPrecarga the urlPrecarga to set
	 */
	public void setUrlPrecarga(String urlPrecarga) {
		this.urlPrecarga = urlPrecarga;
	}
	
}
