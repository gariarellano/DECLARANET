package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ADDRESS;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_INCONSISTENTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_NO_EXISTE;
import static mx.gob.sfp.dgti.util.Constantes.RENAPO_ESTATUS_VALIDADO;
import static mx.gob.sfp.dgti.util.Constantes.VALIDA_CURP_ESTATUS_EXITOSO;
import static mx.gob.sfp.dgti.util.Constantes.VALIDA_CURP_ESTATUS_NO_EXITOSO;
import static mx.gob.sfp.dgti.util.Constantes.VALIDA_CURP_MENSAJE_NO_EXITOSO;

import java.text.Collator;
import java.util.Locale;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.dto.UsuarioRenapoDTO;
import mx.gob.sfp.dgti.entity.UsuarioEO;
import mx.gob.sfp.dgti.persistence.DatabaseService;
import mx.gob.sfp.dgti.service.GraphService;
import mx.gob.sfp.dgti.util.AutenticacionHelper;

/**
 * Verticle para consumir el servicio de valida curp con Renapo
 * @author Miriam Sánchez Sánchez programador07
 * @since 11/06/2019
 */
public class ValidadoRenapoVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidadoRenapoVerticle.class);
	private static final String URL_VALIDAR_CURP = System.getenv("IP_DOMINIO_VALIDAR_CURP") != null ? System.getenv("IP_DOMINIO_VALIDAR_CURP") : AutenticacionHelper.obtenerPropiedad("ip.dominio.validar.curp");
	private DatabaseService databaseService;
	private GraphService graphService;
	
	/**
	 * Start del verticle
	 */
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start();
		this.databaseService  = DatabaseService.create(vertx, AutenticacionHelper.getConfig());
		this.graphService = GraphService.create();

		vertx.eventBus().consumer(ADDRESS, receivedCurp -> {
			
			String curp = receivedCurp.body().toString();
			UsuarioRenapoDTO usuarioRenapo =  this.graphService.consumirServicioValidarCurp2(URL_VALIDAR_CURP, curp);
			
			LOGGER.info("=============  validarCurpRenapo =============  " + curp);
			
			if(usuarioRenapo != null) {
				this.databaseService.consultarUsuarioPorCurp(curp).setHandler(usuarioBD -> {
					if(usuarioBD.succeeded()) {
						UsuarioEO usuario = new UsuarioEO();
						usuario.setIdUsuario(usuarioBD.result().getIdUsuario());
						
						if(VALIDA_CURP_ESTATUS_EXITOSO.equals(usuarioRenapo.getStatusOper())) {
							LOGGER.info("Validando estatus ");
							Collator c = Collator.getInstance(new Locale("es"));
				            c.setStrength(Collator.PRIMARY);
				          
				            if(c.equals(usuarioRenapo.getNombres(), usuarioBD.result().getNombre()) 
									&& c.equals(usuarioRenapo.getPrimerApellido(), usuarioBD.result().getPrimerApellido())
									&& c.equals(usuarioRenapo.getSegundoApellido(), usuarioBD.result().getSegundoApellido())) {
				            		LOGGER.info("Validado");
				            	usuario.setValidadoRenapo(RENAPO_ESTATUS_VALIDADO);
							} else {
								LOGGER.info("Inconsistencias");
								usuario.setValidadoRenapo(RENAPO_ESTATUS_INCONSISTENTE);
							}
							this.databaseService.modificarUsuario(usuario);
						}
						else if(VALIDA_CURP_ESTATUS_NO_EXITOSO.equals(usuarioRenapo.getStatusOper()) && VALIDA_CURP_MENSAJE_NO_EXITOSO.equals(usuarioRenapo.getMessage())) {
							LOGGER.info("No existe");
			                usuario.setValidadoRenapo(RENAPO_ESTATUS_NO_EXISTE);
							this.databaseService.modificarUsuario(usuario);
			            }
					} else {
						LOGGER.info("La curp no existe");
					}
				});
			}
		});
	}
}
