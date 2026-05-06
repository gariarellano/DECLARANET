/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "OMEXT" Sistema que permite la identificación de los Omisos y Extemporaneos en 
 * presentar la declaración patrimonial y de conflicto de intereses.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.mailsender.resolvers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author cgarias
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mail {
    private String from; 
    private String to; 
    private List<String> copias; 
    private List<String> copiasOcultas; 
    private List<String> replyTo;
    private String asunto;  
    private String message; 
    private String html; 
    private Document document;
    private List<Document> documents;
    
    public Mail(){}

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the copias
     */
    public List<String> getCopias() {
        return copias;
    }

    /**
     * @param copias the copias to set
     */
    public void setCopias(List<String> copias) {
        this.copias = copias;
    }

    /**
     * @return the copiasOcultas
     */
    public List<String> getCopiasOcultas() {
        return copiasOcultas;
    }

    /**
     * @param copiasOcultas the copiasOcultas to set
     */
    public void setCopiasOcultas(List<String> copiasOcultas) {
        this.copiasOcultas = copiasOcultas;
    }
    
    /**
     * @return the replyTo
     */
    public List<String> getRepliTo() {
        return replyTo;
    }

    /**
     * @param replyTo the replyTo to set
     */
    public void setReplyTo(List<String> replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * @return the asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * @param asunto the asunto to set
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the isHtml
     */
    public String getHtml() {
        return html;
    }

    /**
     * @param html the html to set
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * @return the documents
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
