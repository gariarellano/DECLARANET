package mx.gob.sfp.dgti.validador.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO de respuesta para el módulo correspondiente
 * @author Ñiño Regalado aka programador02
 * @since 01/10/2019
 */
@JsonInclude(Include.NON_NULL)
public class ModuloDTO {

    /**
     * Nombre del modulo
     */
    private String modulo;

    /**
     * Bandera para verificar si el módulo está completo
     */
    private boolean incompleto;
    
    /**
     * Lista de errores por modulo
     */
    private List<PropiedadesErrorDTO> errores;

    /**
     * Constructor principal de la clase.
     * 
     * @since 02/09/2019
     * @author cgarias
     */
    public ModuloDTO(){}
        
    /**
     * Constructor princpal de la clase que permite realizar la inicializacion 
     * de la lista deerrores, con la finalidad de ir agregando errores en caso
     * de haberlos.
     * 
     * @param modulo Nombre del modulo.
     * 
     * @since 02/09/2019
     * @author cgarias
     */
    public ModuloDTO(String modulo){
        this.modulo = Objects.requireNonNull(modulo, "El nombre del modulo noi debe ser null.");
        errores = new ArrayList<>();
    }
    
    /**
     * @return the modulo
     */
    public String getModulo() {
            return modulo;
    }
    /**
     * @param modulo the modulo to set
     */
    public void setModulo(String modulo) {
            this.modulo = modulo;
    }
    /**
     * @return the errores
     */
    public List<PropiedadesErrorDTO> getErrores() {
            return errores;
    }
    /**
     * @param errores the errores to set
     */
    public void setErrores(List<PropiedadesErrorDTO> errores) {
            this.errores = errores;
    }
    /**
     * Función toString
     */
    @Override
    public String toString() {
            return "ModuloDTO [modulo=" + modulo + ", errores=" + errores + "]";
    }

	/**
	 * @return the incompleto
	 */
	public boolean isIncompleto() {
		return incompleto;
	}

	/**
	 * @param incompleto the incompleto to set
	 */
	public void setIncompleto(boolean incompleto) {
		this.incompleto = incompleto;
	}	
}
