package mx.gob.sfp.dgti.service.impl;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.dto.UsuarioRenapoDTO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.service.CallResolver;
import mx.gob.sfp.dgti.service.GraphService;
/**
 * Clase con la implementación de consumo de api's desarrolladas en graphql
 * @author Miriam Sánchez Sánchez programador07
 * @since 10/05/2019
 */
public class GraphServiceImpl implements GraphService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphServiceImpl.class);
	private static final String EMPTY = "";
	private static final String NOMBRE_OPERACION_RENAPO_CURP = "renapoCurp";
	private static final String NOMBRE_PARAMETRO_CURP = "curp";
	private static final String DATA = "data";
	private static final String[] QUERY_ATRIBUTES_FROM = new String[] {"statusOper", "message", "curp", "nombres", "primerApellido", "segundoApellido"};
	private static final String NOMBRE_OPERACION_ENVIO_EMAIL = "sendMailObjResp";
	private static final String NOMBRE_INPUT = "input";
	private static final String NOMBRE_PARAMETRO_PARA = "to";
	private static final String NOMBRE_PARAMETRO_DE = "from";
	private static final String NOMBRE_PARAMETRO_ASUNTO = "asunto";
	private static final String NOMBRE_PARAMETRO_MESSAGE = "message";
	private static final String NOMBRE_PARAMETRO_HTML = "html";
	private static final String HTML = "html";
	private static final String ENVIADO = "Enviado";
	private static final String[] QUERY_ATRIBUTES_FROM_EMAIL = new String[] {"mensaje"};
	private static final String DE_SFP = System.getenv("CUENTA_CORREO_FROM");
	private static final String ASUNTO_REESTABLECER_PWD = "Reestablecer contraseña";
	private static final String ASUNTO_CAMBIO_CURP = "Modificación de curp";
	private static final String PLANTILLA = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'><h2>IDENTIDAD FUNCI&Oacute;N P&Uacute;BLICA"
			+ "</h2></div></center></header><body><center style='font-size: 19px;'> Est&aacute;"
			+ " recibiendo este correo porque hemos recibido su solicitud para reestablecer la contrase&ntilde;a de su cuenta.<h3><label style='font-weight: bold !important; font-size: 19px;'>"
			+ "<a href='%s' class='btn btn-success'>Si desea cambiar su contrase&ntilde;a d&eacute; click aqu&iacute;</a>"
			+ "</label></h3>Si no desea reestablecer la contrase&ntilde;a haga caso omiso de este correo.</center></body></html>";
	private static final String PLANTILLA_CORREO_CAMBIO_CURP = "<html><header><center style='font-weight: bold; font-size: 19px;'><div style='color:#b21f2d'><h2>IDENTIDAD FUNCI&Oacute;N P&Uacute;BLICA"
			+ "</h2></div></center></header><body><center style='font-size: 19px;'>Estimado usuario, se ha actualizado correctamente su curp a:" 
			+ "<h3><span style='color:#2980b9'> %s </span></h3>Si no reconoce este cambio favor de comunicarse con el administrador</center></body></html>";
	private static final String MENSAJE = "mensaje";
	
	/**
	 * {@inheritDoc}
	 */
	public UsuarioRenapoDTO consumirServicioValidarCurp2(String url, String curp) {
		UsuarioRenapoDTO usuarioRenapoDTO = null;
		try {
			CallResolver resolver = new CallResolver(url, UsuarioRenapoDTO.class);
		    resolver.setHeadersToclient();
		    resolver.setQueryName(NOMBRE_OPERACION_RENAPO_CURP);
		    resolver.putParameterToQuery(NOMBRE_PARAMETRO_CURP, curp);
		    resolver.setAtributesFromQuery(QUERY_ATRIBUTES_FROM);
		    usuarioRenapoDTO = (UsuarioRenapoDTO) resolver.getResultQueryCasting();
		} catch(Exception e) {
			LOGGER.error("Error consumirServicioValidarCurp2 " + url + "-" + curp + e.getMessage());
		}
		return usuarioRenapoDTO;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean consumirServicioEnvioEmail(String url, String urlReestablecerPwdFront, String correo) {
//		try {
//			if(correo != null && !EMPTY.equals(correo) 
//					&& url != null && !EMPTY.equals(url) 
//					&& urlReestablecerPwdFront != null && !EMPTY.equals(urlReestablecerPwdFront)) {
//				CallResolver resolver = new CallResolver(url);
//			    resolver.setHeadersToclient();
//			    resolver.setMutationName(NOMBRE_OPERACION_ENVIO_EMAIL);
//			    Map<String, String> map = new HashMap<>();  
//			    map.put(NOMBRE_PARAMETRO_PARA, correo);
//			    map.put(NOMBRE_PARAMETRO_DE, DE_SFP);
//			    map.put(NOMBRE_PARAMETRO_HTML, HTML);
//			    map.put(NOMBRE_PARAMETRO_ASUNTO, ASUNTO_REESTABLECER_PWD);
//			    map.put(NOMBRE_PARAMETRO_MESSAGE, String.format(PLANTILLA, urlReestablecerPwdFront));
//			    resolver.putParameterToMutation(NOMBRE_INPUT, map);
//			    resolver.setAtributesFromMutation(QUERY_ATRIBUTES_FROM_EMAIL);
//			    if(ENVIADO.equals(((Map)((Map)resolver.getResultMutation().get(DATA)).get(NOMBRE_OPERACION_ENVIO_EMAIL)).get(MENSAJE))) {
//			    	return true;
//			    } else {
//			    	return false;
//			    }
//			} else {
//				return false;
//			}
//		} catch(Exception e) {
//			LOGGER.error("Error al consumirServicioEnvioEmail " + correo + e.getMessage());
//			return false;
//		}
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean consumirServicioEnvioEmailCurp(String url, UsuarioEO usuario, String correo) {
		try {
			if(correo != null && !EMPTY.equals(correo) 
					&& url != null && !EMPTY.equals(url) 
					&& usuario.getCurp() != null && !EMPTY.equals(usuario.getCurp())) {
				CallResolver resolver = new CallResolver(url);
			    resolver.setHeadersToclient();
			    resolver.setMutationName(NOMBRE_OPERACION_ENVIO_EMAIL);
			    Map<String, String> map = new HashMap<>(); 
			    map.put(NOMBRE_PARAMETRO_PARA, correo);
			    map.put(NOMBRE_PARAMETRO_DE, DE_SFP);
			    map.put(NOMBRE_PARAMETRO_HTML, HTML);
			    map.put(NOMBRE_PARAMETRO_ASUNTO, ASUNTO_CAMBIO_CURP);
			    map.put(NOMBRE_PARAMETRO_MESSAGE, String.format(PLANTILLA_CORREO_CAMBIO_CURP, usuario.getCurp()));
			    resolver.putParameterToMutation(NOMBRE_INPUT, map);
			    resolver.setAtributesFromMutation(QUERY_ATRIBUTES_FROM_EMAIL);
			    if(ENVIADO.equals(((Map)((Map)resolver.getResultMutation().get(DATA)).get(NOMBRE_OPERACION_ENVIO_EMAIL)).get(MENSAJE))) {
			    	return true;
			    } else {
			    	return false;
			    }
			} else {
				return false;
			}
		} catch(Exception e) {
			LOGGER.error("Error al consumirServicioEnvioEmailCurp " + correo + usuario.toString() + e.getMessage());
			return false;
		}
	}

}
