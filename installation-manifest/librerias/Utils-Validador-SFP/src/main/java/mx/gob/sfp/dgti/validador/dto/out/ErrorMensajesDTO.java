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

/**
 * Clase que contendra los vlaores de los mensajes 
 * de las validaciones relaixzadas a una propiedad.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class ErrorMensajesDTO implements Serializable{
    /**
     * Propiedad que contiene el Identificador del tipo de error al que le corresponde.
     */
    private Integer errorId;
    /**
     * Propiedad que contiene la cadena de error d la validación.
     */
    private String mensaje;
    /**
     * Propiedad que contiene la cadena de error d la validación alterna.
     */
    private String mensajeAlterno;

    /**
     * Constructor principal de la clase que permite 
     * asignar propiedades mediante la instancia.
     * @param errorId Identificador del error.
     * @param mensaje Cadena qu edescribe el mensaje de erorr.
     */
    public ErrorMensajesDTO(Integer errorId, String mensaje){
        this.errorId = errorId;
        this.mensaje = mensaje;
        
    }
    /**
     * Constructor principal de la clase que permite 
     * asignar propiedades mediante la instancia.
     * @param errorId Identificador del error.
     * @param mensaje Cadena que edescribe el mensaje de erorr.
     * @param mensajeAlterno  En caso de obtener tene un mensaje alterno se asginara.
     * 
     */
    public ErrorMensajesDTO(Integer errorId, String mensaje, String mensajeAlterno){
        this.errorId = errorId;
        this.mensaje = mensaje;
        this.mensajeAlterno = mensajeAlterno;
    }
    
    /**
     * @return the errorId
     */
    public Integer getErrorId() {
        return errorId;
    }

    /**
     * @param errorId the errorId to set
     */
    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return the mensajeAlterno
     */
    public String getMensajeAlterno() {
        return mensajeAlterno;
    }

    /**
     * @param mensajeAlterno the mensajeAlterno to set
     */
    public void setMensajeAlterno(String mensajeAlterno) {
        this.mensajeAlterno = mensajeAlterno;
    }
}
