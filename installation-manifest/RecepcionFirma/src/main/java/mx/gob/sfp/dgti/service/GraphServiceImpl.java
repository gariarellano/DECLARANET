package mx.gob.sfp.dgti.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.util.CallResolver;
import mx.gob.sfp.dgti.verticle.Server;

/**
 * Clase con la implementación de consumo de api's desarrolladas en graphql
 * @author Miriam Sánchez Sánchez programador07
 * @since 10/05/2019 
 */
public class GraphServiceImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphServiceImpl.class);
	private static final String URL_DOMINIO_EMAIL = System.getenv("DOMINIO_CORREO") != null ? System.getenv("DOMINIO_CORREO"): Server.obtenerPropiedad("dominio.correo");
	private static final String OPERACION_EMAIL = "sendMailObjResp";
	private static final String NOMBRE_INPUT = "input";
	private static final String NOMBRE_PARAMETRO_PARA = "to";
	private static final String NOMBRE_PARAMETRO_DE = "from";
	private static final String NOMBRE_PARAMETRO_ASUNTO = "asunto";
	private static final String NOMBRE_PARAMETRO_MESSAGE = "message";
	private static final String NOMBRE_PARAMETRO_HTML = "html";
	private static final String NOMBRE_PARAMETRO_DOCUMENTOS = "documents";
	private static final String NOMBRE_PARAMETRO_DOCUMENTO = "documento";
	private static final String NOMBRE_PARAMETRO_FILENAME = "fileName";
	private static final String HTML = "html";
	private static final String ENVIADO = "Enviado";
	private static final String NOMBRE_ACUSE = "Acuse.pdf";
	private static final String NOMBRE_DECLARACION = "Declaracion.pdf";
	private static final String[] QUERY_ATRIBUTES_FROM = new String[] {"mensaje"};
	private static final String DATA = "data";
	private static final String MENSAJE = "mensaje";
	public static final String DE_SFP = System.getenv("CUENTA_CORREO_DECLARANET");
	public static final String ASUNTO = "Acuse de la declaración patrimonial. (%s)";
	private static final String PLANTILLA = "<html><body style='font-size: 14px;'><div>Estimado(a) Servidor(a) P&uacute;blico(a), la presentaci&oacute;n de tu declaraci&oacute;n patrimonial ha sido exitosa."
			+ "<br/><br/>RFC: <label style='font-weight: bold !important; font-size: 14px;'>%s</label><br/>Tipo de Declaraci&oacute;n: "
			+ "<label style='font-weight: bold !important; font-size: 14px;'>%s</label><br/>Fecha presentaci&oacute;n: <label style='font-weight: bold !important; font-size: 14px;'>%s</label><br/>"
			+ "<br/>Recuerda que el acuse de recibo que se anexa al presente correo, constituye el &uacute;nico medio probatorio de la presentaci&oacute;n de tu declaraci&oacute;n patrimonial, "
			+ "por lo que te recomendamos lo conserves para el caso de cualquier aclaraci&oacute;n.<br/><br/>%s.</div></body></html>";
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean consumirServicio(String correo, String tipoFirmado, String rfc, 
			String tipoDeclaracion,	String fechaPresentacion, String acuse, String declaracion, String ente) {
//		LOGGER.info(correo + " - " + tipoFirmado + " - " + rfc + " - " + tipoDeclaracion + " - " + fechaPresentacion + "-" + ente);
		try {
			CallResolver resolver = new CallResolver(URL_DOMINIO_EMAIL);
		    resolver.setHeadersToclient();
		    resolver.setMutationName(OPERACION_EMAIL);
		    
		    
		    List<HashMap<String, String>> mapDoc = new ArrayList<HashMap<String, String>>();
		    mapDoc.add(obtenerDocumento(NOMBRE_ACUSE, acuse));
		    mapDoc.add(obtenerDocumento(NOMBRE_DECLARACION, declaracion));
		    
		    Map map = new HashMap<String, Object>();
		    map.put(NOMBRE_PARAMETRO_PARA, correo);
		    map.put(NOMBRE_PARAMETRO_DE, DE_SFP);
		    map.put(NOMBRE_PARAMETRO_HTML, HTML);
		    map.put(NOMBRE_PARAMETRO_ASUNTO, String.format(ASUNTO, tipoFirmado));
		    map.put(NOMBRE_PARAMETRO_MESSAGE, String.format(PLANTILLA, rfc, tipoDeclaracion, fechaPresentacion, ente));
		    map.put(NOMBRE_PARAMETRO_DOCUMENTOS, mapDoc);
		    resolver.putParameterToMutation(NOMBRE_INPUT, map);
		    resolver.setAtributesFromMutation(QUERY_ATRIBUTES_FROM);

		    if(ENVIADO.equals(((Map)((Map)resolver.getResultMutation().get(DATA)).get(OPERACION_EMAIL)).get(MENSAJE))) {
		    	return true;
		    } else {
		    	return false;
		    }
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap obtenerDocumento(String fileName, String documento) {
		HashMap docAcuse = new HashMap<String, String>();
	    docAcuse.put(NOMBRE_PARAMETRO_DOCUMENTO, documento);
	    docAcuse.put(NOMBRE_PARAMETRO_FILENAME, fileName);
	    return docAcuse;
	}
	
}