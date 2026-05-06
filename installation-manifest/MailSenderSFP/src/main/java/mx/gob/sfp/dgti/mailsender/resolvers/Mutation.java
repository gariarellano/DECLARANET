/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * MailSenderSFP"oservicio que expone métodos de envío de 
 * correo electronicos de un emisor especificado a otra 
 * cuenta de receptor especificada.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.mailsender.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.DatatypeConverter;
import mx.gob.sfp.dgti.mailsender.response.Respuesta;

/**
 * Clase que permite realizar el envío de correos 
 * electronicos en varias modalidades
 * dependuendo de las necesidades.
 * 
 * @author cgarias
 * @since  30/04/2019
 */
public class Mutation implements GraphQLRootResolver{
    private static final Logger logg = Logger.getLogger(Mutation.class.getName());
    private static final String DOMINIO_SFP_ANTISPAM = "DOMINIO_SFP_ANTISPAM";
    private static final String DOMINIO_SFP = System.getenv(DOMINIO_SFP_ANTISPAM);
    private static final String MAIL_HOST_SFP ="mail.smtp.host";
    private static final Integer PUERTO_SMTP = System.getenv("PUERTO_SMTP") != null ? Integer.parseInt(System.getenv("PUERTO_SMTP")) : 587;
    // Servidor SMTP real leído desde variables de entorno
    private static final String MAIL_SMTP_HOST = System.getenv("MAIL_SMTP_HOST") != null ? System.getenv("MAIL_SMTP_HOST") : "mail.huetamo.com.mx";
    private static final String CUENTA_FROM    = System.getenv("CUENTA_CORREO_FROM") != null ? System.getenv("CUENTA_CORREO_FROM") : "declaranet@huetamo.com.mx";
    private static final String PASS_CORREO    = System.getenv("PASS_CORREO_FROM") != null ? System.getenv("PASS_CORREO_FROM") : "";
    private static final String TEXT_HTML = "text/html;charset=UTF-8";
    private static final String APPLICATION_STREM = "application/octet-stream";//este tipo se puete utilizar para archivos desconocidos.
    
    public Mutation() {
    }
    
    public Mutation sendMailObj(Mail mail, Handler<AsyncResult<String>> resultHandle){
        sendMail(mail.getFrom(), mail.getTo(), mail.getRepliTo(), mail.getCopias(), mail.getCopiasOcultas(), mail.getAsunto(), mail.getMessage(), mail.getHtml(), mail.getDocument(),mail.getDocuments(), res->{
           if (res.succeeded()) {
               resultHandle.handle(res);
           }else{               
               logg.log(Level.SEVERE, "Error de envio {0}", res.cause());
               resultHandle.handle(res);
           }
        });        
        return this;
        
    }
    
    public Mutation sendMailObjResp(Mail mail, Handler<AsyncResult<Respuesta>> resultHandle){
        sendMail(mail.getFrom(), mail.getTo(), mail.getRepliTo(), mail.getCopias(), mail.getCopiasOcultas(), mail.getAsunto(), mail.getMessage(), mail.getHtml(), mail.getDocument(),mail.getDocuments(), res->{
           if (res.succeeded()) {
               resultHandle.handle(Future.succeededFuture(new Respuesta(res.result())));
           }
           else{               
               logg.log(Level.SEVERE, "Error de envio {0}", res.cause());
               resultHandle.handle(Future.failedFuture(res.result()));
           }
        });        
        return this;
    }
    
    
   /**
     * Método genral que permite hacer uso del envío 
     * de correo electronic de un emisor a un 
     * receptordefinidos por los usuarios que lo envían.
     * 
     * @param from          Quien envía. REQUERIDO
     * @param to            Quien recibe. REQUERIDO
     * @param replyTo       Bandejas a donde se desea recibir respuestas del correo enviado.
     * @param copias        En caso de tener copias.
     * @param copiasOcultas En caso de tener copias ocultas.
     * @param asunto        Asuntoal que se refiere en el correo. REQUERIDO
     * @param message       Mensaje de tipo texto o html. REQUERIDO
     * @param isHtml        Indicador si es texto = null  o HTML = "html"
     * @param document      En caso de contener un documento.
     * @param documents     En caso de contener documentos en arregglo.
     * @param resultHandle
     * 
     * @return String respuesta al usuariofinal.
     */
    public Mutation sendMail(String from, String to, 
                                List<String> replyTo,
                                List<String> copias, List<String> copiasOcultas, 
                                String asunto,  String message, 
                                String isHtml, Document document, 
                                List<Document> documents, 
                                Handler<AsyncResult<String>> resultHandle){
        
        Properties props = System.getProperties();
        // Configuración SMTP usando variables de entorno del .env
        // mail.huetamo.com.mx → puerto 465 SSL/TLS (Secure SSL/TLS Settings)
        props.put(MAIL_HOST_SFP, MAIL_SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(PUERTO_SMTP));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", String.valueOf(PUERTO_SMTP));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.trust", MAIL_SMTP_HOST);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        final String cuentaFrom = CUENTA_FROM;
        final String passCorreo = PASS_CORREO;

        String response = "Enviado";
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(cuentaFrom, passCorreo);
                }
            });
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(from));
            Transport transport = session.getTransport("smtp");

            transport.connect(MAIL_SMTP_HOST, PUERTO_SMTP, cuentaFrom, passCorreo);
              
            //transport.sendMessage(message, message.getAllRecipients());
            //transport.close();

            if (to!= null && !"".equals(to)){
                mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            }
            if (replyTo != null && !replyTo.isEmpty()) {
                StringBuilder strReplyTo = new StringBuilder();
                replyTo.stream().forEach((direc) -> {
                    strReplyTo.append(direc).append(",");
                });
                strReplyTo.delete(strReplyTo.length() - 1, strReplyTo.length());
                mail.setReplyTo(InternetAddress.parse(strReplyTo.toString(), true));
            }
            if (copias != null && !copias.isEmpty()) {
                StringBuilder copiass = new StringBuilder();
                copias.stream().forEach((direc) -> {
                    copiass.append(direc).append(",");
                });
                copiass.delete(copiass.length() - 1, copiass.length());
                mail.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copiass.toString(), true));
            }
            if (copiasOcultas != null && !copiasOcultas.isEmpty()) {
                StringBuilder copiassoc = new StringBuilder();
                copiasOcultas.stream().forEach((direc) -> {
                    copiassoc.append(direc).append(",");
                });
                copiassoc.delete(copiassoc.length()-1, copiassoc.length());
                mail.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(copiassoc.toString(), false));
            }
            // Siempre agrega el correo admin como copia oculta (BCC) para auditoría
            mail.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(cuentaFrom, false));
            mail.setSubject(asunto);

            BodyPart messageBodyPart = new MimeBodyPart();
            if (isHtml != null && !"".equals(isHtml))
                messageBodyPart.setContent(message, TEXT_HTML);
            else
                messageBodyPart.setText(message);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (document != null && 
                    document.getDocumento() != null && !"".equals(document.getDocumento()) &&
                    document.getFileName() != null && !"".equals(document.getFileName())){    
                messageBodyPart = new MimeBodyPart();
                DataSource ds = new ByteArrayDataSource(DatatypeConverter.parseBase64Binary(document.getDocumento()), APPLICATION_STREM);
                messageBodyPart.setFileName(document.getFileName());
                messageBodyPart.setDataHandler(new DataHandler(ds));
                multipart.addBodyPart(messageBodyPart);
            }
            
            if (documents != null && !documents.isEmpty()){
                for (Document documento : documents){
                    if (documento.getDocumento() != null && !"".equals(documento.getDocumento()) &&
                            documento.getFileName() != null && !"".equals(documento.getFileName())){    
                        messageBodyPart = new MimeBodyPart();
                        DataSource ds = new ByteArrayDataSource(DatatypeConverter.parseBase64Binary(documento.getDocumento()), APPLICATION_STREM);
                        messageBodyPart.setFileName(documento.getFileName());
                        messageBodyPart.setDataHandler(new DataHandler(ds));
                        multipart.addBodyPart(messageBodyPart);
                    }
                }
            }
            mail.setContent(multipart);
            logg.log(Level.INFO, "----------Envía email, emisor: {0} , destinatario: {1}", new Object[]{from, to});     
            resultHandle.handle(Future.succeededFuture(response));
            transport.sendMessage(mail, mail.getAllRecipients());
	    transport.close();
            logg.log(Level.INFO, "------>>>>Enviado email, emisor: {0} , destinatario: {1}", new Object[]{from, to});     
        } catch(MessagingException e){
            logg.log(Level.SEVERE, "Error al envíar correo de {0} para: {1} \n {2} ", new Object[]{from, to, e});
            resultHandle.handle(Future.failedFuture("Ocurrio un error al envíar"));
        }
        return this;
    }
}
