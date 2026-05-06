//package mx.gob.sfp.dgti.service;
//
//import mx.gob.sfp.dgti.dto.UsuarioRenapoDTO;
//import mx.gob.sfp.dgti.service.impl.GraphServiceImpl;
//
///**
// * Interface con las funciones para consumir servicios en graphql
// * @author Miriam Sánchez Sánchez programador07
// * @since 10/06/2019
// *
// */
//public interface GraphService {
//	
//	static GraphService create() {
//		return new GraphServiceImpl();
//	}
//
//	/**
//	 * Función para consumir el servicio de envío de emails con código de confirmación
//	 * @param correo del usuario
//	 * @param idConfirmacion  código de la confirmación
//	 * @return boolean si fue enviado el correo = true, no enviado = false
//	 */
//	public boolean consumirServicio(String correo, Integer idConfirmacion);
//	
//	/**
//	 * Función para consumir el servicio de envío de emails con enlace
//	 * @param correo
//	 * @param urlFrontActivacion
//	 * @return boolean si fue enviado el correo = true, no enviado = false
//	 */
//	public boolean consumirServicio(String correo, String curp, String urlFrontActivacion);
//	
//	/**
//	 * Función para consumir el servicio de validar curp con RENAPO
//	 * @param url del servicio de validaCurp
//	 * @param curp del usuario
//	 * @return UsuarioRenapoDTO objeto que regresa el servicio con los datos de la curp
//	 */
//	public UsuarioRenapoDTO consumirServicioValidarCurp(String url, String curp);
//	
//}
