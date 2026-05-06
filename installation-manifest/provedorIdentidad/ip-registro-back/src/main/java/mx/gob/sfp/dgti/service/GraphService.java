package mx.gob.sfp.dgti.service;

import mx.gob.sfp.dgti.dto.UsuarioRenapoDTO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.service.impl.GraphServiceImpl;

/**
 * Interface con las funciones para consumir servicios en graphql
 * @author Miriam Sánchez Sánchez programador07
 * @since 10/06/2019
 *
 */
public interface GraphService {
	
	static GraphService create() {
		return new GraphServiceImpl();
	}
	
	/**
	 * Implementación de consumo del servicio de validado de curp con graphql
	 * @param url del servicio a consumir
	 * @param curp del usuario a consultar
	 * @return UsuarioRenapoDTO: objeto que regresa el servicio con los datos de la curp
	 */
	public UsuarioRenapoDTO consumirServicioValidarCurp2(String url, String curp);

	/**
	 * Implementación de consumo del servicio de envío de email con graphql
 	 * @param url del servicio a consumir
	 * @param urlReestablecerPwdFront parámetro de la url para generar el enlace para enviar en el correo 
	 * @param curp del usuario
	 * @return boolean
	 */
//	public boolean consumirServicioEnvioEmail(String url, String urlReestablecerPwdFront, String curp);
	
	/**
	 * Implementación de consumo del servicio de envío de emails para cuando se cambia la curp
	 * @param url
	 * @param usuario
	 * @return
	 */
	public boolean consumirServicioEnvioEmailCurp(String url, UsuarioEO usuario, String correo);
	
}
