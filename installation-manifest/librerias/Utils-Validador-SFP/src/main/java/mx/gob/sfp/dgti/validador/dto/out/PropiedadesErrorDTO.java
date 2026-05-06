/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.validador.dto.out;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene las propiedades de las validaciones para
 * el contenido de la propiedad a validar.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class PropiedadesErrorDTO implements Serializable{
    /**
     * Propiedad que describe el nombre del campo de donde proviene la propiedad a evaluar.
     */
    private String  nombreCampo;
    /**
     * Propiedad que contiene la descripcion del valor a validar.
     */
    private Object propiedadValor;
    /**
     * Propiedad que contiene la lista de errores que contiene la propiedad.
     */
    private List<ErrorMensajesDTO> listErrorMensajes;
    
    /**
     * Constructor principal de la clase.
     * No permitira tener null.
     * @param nombreCampo
     * @param propiedadValor propiedad a validar as String
     */
    public PropiedadesErrorDTO(String nombreCampo, Object propiedadValor){
        this.nombreCampo = nombreCampo;
        this. propiedadValor = propiedadValor;
        this.listErrorMensajes = new ArrayList<>();
    }

    /**
     * @return the propiedadValor
     */
    public Object getPropiedadValor() {
        return propiedadValor;
    }

    /**
     * @param propiedadValor the propiedadValor to set
     */
    public void setPropiedadValor(Object propiedadValor) {
        this.propiedadValor = propiedadValor;
    }

    /**
     * @return the listErrorMensajes
     */
    public List<ErrorMensajesDTO> getListErrorMensajes() {
        return listErrorMensajes;
    }

    /**
     * @param listErrorMensajes the listErrorMensajes to set
     */
    public void setListErrorMensajes(List<ErrorMensajesDTO> listErrorMensajes) {
        this.listErrorMensajes = listErrorMensajes;
    }

    /**
     * @return the nombreCampo
     */
    public String getNombreCampo() {
        return nombreCampo;
    }

    /**
     * @param nombreCampo the nombreCampo to set
     */
    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PropiedadesErrorDTO{");
        sb.append("nombreCampo='").append(nombreCampo).append('\'');
        sb.append(", propiedadValor=").append(propiedadValor);
        sb.append(", listErrorMensajes=").append(listErrorMensajes);
        sb.append('}');
        return sb.toString();
    }
}
