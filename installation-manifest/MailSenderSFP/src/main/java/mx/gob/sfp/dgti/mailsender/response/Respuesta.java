/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * MailSenderSFP"oservicio que expone métodos de envío de 
 * correo electronicos de un emisor especificado a otra 
 * cuenta de receptor especificada.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.mailsender.response;

/**
 * Clase que permite realizar la respuesta del correo como objeto.
 * 
 * @author cgarias
 */
public class Respuesta {
    private String mensaje;
    
    public Respuesta(){}
    public Respuesta(String mensaje){
        this.mensaje = mensaje;
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
}
