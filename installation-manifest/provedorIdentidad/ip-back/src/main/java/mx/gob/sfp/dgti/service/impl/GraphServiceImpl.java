//package mx.gob.sfp.dgti.service.impl;
//
//import java.util.HashMap;
//
//import java.util.Map;
//
//import io.vertx.core.logging.Logger;
//import io.vertx.core.logging.LoggerFactory;
//import mx.gob.sfp.dgti.dto.UsuarioRenapoDTO;
//import mx.gob.sfp.dgti.helper.AutenticacionHelper;
//import mx.gob.sfp.dgti.service.CallResolver;
//import mx.gob.sfp.dgti.service.GraphService;
//
///**
// * Clase con la implementación de consumo de api's desarrolladas en graphql
// * @author Miriam Sánchez Sánchez programador07
// * @since 10/05/2019 
// */
//public class GraphServiceImpl implements GraphService {
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(GraphServiceImpl.class);
//	private static final String URL_ENVIO_EMAIL = System.getenv("IP_DOMINIO_CORREO") != null ? System.getenv("IP_DOMINIO_CORREO"): AutenticacionHelper.obtenerPropiedad("ip.dominio.correo");
//	private static final String NOMBRE_OPERACION_ENVIO_EMAIL = "sendMailObjResp";
//	private static final String NOMBRE_INPUT = "input";
//	private static final String NOMBRE_PARAMETRO_PARA = "to";
//	private static final String NOMBRE_PARAMETRO_DE = "from";
//	private static final String NOMBRE_PARAMETRO_ASUNTO = "asunto";
//	private static final String NOMBRE_PARAMETRO_MESSAGE = "message";
//	private static final String NOMBRE_PARAMETRO_HTML = "html";
//	private static final String NOMBRE_OPERACION_RENAPO_CURP = "renapoCurp";
//	private static final String NOMBRE_PARAMETRO_CURP = "curp";
//	private static final String[] RENAPO_QUERY_ATRIBUTES_FROM = new String[] {"statusOper", "message", "curp", "nombres", "primerApellido", "segundoApellido"};
//	private static final String HTML = "html";
//	private static final String ENVIADO = "Enviado";
//	private static final String[] QUERY_ATRIBUTES_FROM = new String[] {"mensaje"};
//	private static final String DATA = "data";
//	private static final String EMPTY = "";
//	private static final String MENSAJE = "mensaje";
//	public static final String DE_SFP = System.getenv("CUENTA_CORREO_FROM");
//	public static final String ASUNTO = "Código de confirmación";
//	public static final String INPUT = "{\"query\":\"mutation sendMailObj($input:Mail!){sendMailObj(input:$input)}\",\"variables\":{\"input\":"
//			+ "{\"from\": \"%s\", \"to\":\"%s\",\"asunto\":\"%s\",\"message\":\"%s\",\"html\":\"html\"}},\"operationName\":\"sendMailObj\"}";
//	public static final String PLANTILLA = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'>"
//			+ "<h2>IDENTIDAD FUNCI&Oacute;N P&Uacute;BLICA</h2></div></center></header><body><center style='font-size: 19px;'>"
//			+ "C&Oacute;DIGO DE CONFIRMACI&Oacute;N<h3><label style='font-weight: bold !important; font-size: 19px;'> %s </label></h3></center></body></html>";
//	private static final String PLANTILLA_ACTIVACION_ENLACE = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'><h2>IDENTIDAD FUNCI&Oacute;N P&Uacute;BLICA"
//			+ "</h2></div></center></header><body><center style='font-size: 19px;'> Est&aacute;"
//			+ " recibiendo este correo porque hemos recibido su solicitud para activar su cuenta.<h3><label style='font-size: 19px;'>"
//			+ "<h2> CURP: <span style='color:#2980b9'> %s </span></h2>"
//			+ "<a href='%s' class='btn btn-success'>D&eacute; click aqu&iacute;</a>"
//			+ "</label></h3></center></body></html>";
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean consumirServicio(String correo, Integer idConfirmacion) {
//		LOGGER.info("correo: " + correo + " idConfirmacion: " + idConfirmacion);
//		try {
//			if(correo != null && !EMPTY.equals(correo) && idConfirmacion != null) {
//				CallResolver resolver = new CallResolver(URL_ENVIO_EMAIL);
//			    resolver.setHeadersToclient();
//			    resolver.setMutationName(NOMBRE_OPERACION_ENVIO_EMAIL);
//			    Map map = new HashMap<String, String>(); 
//			    map.put(NOMBRE_PARAMETRO_PARA, correo);
//			    map.put(NOMBRE_PARAMETRO_DE, DE_SFP);
//			    map.put(NOMBRE_PARAMETRO_HTML, HTML);
//			    map.put(NOMBRE_PARAMETRO_ASUNTO, ASUNTO);
//			    map.put(NOMBRE_PARAMETRO_MESSAGE, String.format(PLANTILLA, idConfirmacion));
//			    resolver.putParameterToMutation(NOMBRE_INPUT, map);
//			    resolver.setAtributesFromMutation(QUERY_ATRIBUTES_FROM);
//	
//			    if(ENVIADO.equals(((Map)((Map)resolver.getResultMutation().get(DATA)).get(NOMBRE_OPERACION_ENVIO_EMAIL)).get(MENSAJE))) {
//			    	return true;
//			    
//			    } else {
//			    	return false;
//			    }
//			    
//			} else {
//				return false;
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean consumirServicio(String correo, String curp, String urlFrontActivacion) {
//		LOGGER.info("correo: " + correo + " urlFrontActivacion: " + urlFrontActivacion + " Curp: " + curp);
//		try {
//			if(correo != null && !EMPTY.equals(correo) 
//					&& curp != null && !EMPTY.equals(curp) 
//					&& urlFrontActivacion != null && !EMPTY.equals(urlFrontActivacion)) {
//				CallResolver resolver = new CallResolver(URL_ENVIO_EMAIL);
//			    resolver.setHeadersToclient();
//			    resolver.setMutationName(NOMBRE_OPERACION_ENVIO_EMAIL);
//			    Map map = new HashMap<String, String>(); 
//			    map.put(NOMBRE_PARAMETRO_PARA, correo);
//			    map.put(NOMBRE_PARAMETRO_DE, DE_SFP);
//			    map.put(NOMBRE_PARAMETRO_HTML, HTML);
//			    map.put(NOMBRE_PARAMETRO_ASUNTO, ASUNTO);
//			    map.put(NOMBRE_PARAMETRO_MESSAGE, String.format(PLANTILLA_ACTIVACION_ENLACE, curp, urlFrontActivacion));
//			    resolver.putParameterToMutation(NOMBRE_INPUT, map);
//			    resolver.setAtributesFromMutation(QUERY_ATRIBUTES_FROM);
//			    
//			    if(ENVIADO.equals(((Map)((Map)resolver.getResultMutation().get(DATA)).get(NOMBRE_OPERACION_ENVIO_EMAIL)).get(MENSAJE))) {
//			    	return true;
//			    
//			    } else {
//			    	return false;
//			    }
//			    
//			} else {
//				return false;
//			}
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	public UsuarioRenapoDTO consumirServicioValidarCurp(String url, String curp) {
//		UsuarioRenapoDTO usuarioRenapoDTO = null;
//		try {
//			CallResolver resolver = new CallResolver(url, UsuarioRenapoDTO.class);
//		    resolver.setHeadersToclient();
//		    resolver.setQueryName(NOMBRE_OPERACION_RENAPO_CURP);
//		    resolver.putParameterToQuery(NOMBRE_PARAMETRO_CURP, curp);
//		    resolver.setAtributesFromQuery(RENAPO_QUERY_ATRIBUTES_FROM);
//		    usuarioRenapoDTO = (UsuarioRenapoDTO) resolver.getResultQueryCasting();
//		} catch(Exception e) {
//			LOGGER.error("Error consumirServicioValidarCurp, el servicio no responde: " + curp);
//		}
//		return usuarioRenapoDTO;
//	}
//	
//}
