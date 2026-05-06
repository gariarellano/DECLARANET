/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * MailSenderSFP"oservicio que expone métodos de envío de 
 * correo electronicos de un emisor especificado a otra 
 * cuenta de receptor especificada.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.mailsender.resolvers;

/**
 * Clase que contiene las propiedades de un docuento
 * para poderlo envíar por un correo.
 * 
 * 
 * @author cgarias
 * @since 03/05/2019
 */
public class Document{
 
    private String fileName;
    private String documento;
    
    public Document(){}

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
}