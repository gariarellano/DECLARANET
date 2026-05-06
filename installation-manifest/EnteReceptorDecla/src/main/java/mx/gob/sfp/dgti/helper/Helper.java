package mx.gob.sfp.dgti.helper;

import java.util.ArrayList;
import java.util.List;

import mx.gob.sfp.dgti.dto.RespuestaGraphEntesDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErrorMensajesDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;

public class Helper {

	private static final String MODULO_ENTE_ALTA= "ALTA NUEVO ENTE RECEPTOR";
	private static final String ENTE= "Ente";
	private static final String MENSAJE_EXITO = "Ente guardado con éxito";
	public static RespuestaGraphEntesDTO crearMensajeExito() {
		RespuestaGraphEntesDTO res= new RespuestaGraphEntesDTO();
		res.setModulo(MODULO_ENTE_ALTA);
		res.setIncompleto(Boolean.FALSE);
		List<PropiedadesErrorDTO>  errores = new ArrayList<PropiedadesErrorDTO>();
			errores.add(new PropiedadesErrorDTO(ENTE, 0));
			List<ErrorMensajesDTO> mensajes = new ArrayList<ErrorMensajesDTO>();
				mensajes.add(new ErrorMensajesDTO(0, MENSAJE_EXITO));
				errores.get(0).setListErrorMensajes(mensajes);
		res.setErrores(errores);
		return res;
	}
	
	public static RespuestaGraphEntesDTO crearMensajeError(String mensaje) {
		RespuestaGraphEntesDTO res= new RespuestaGraphEntesDTO();
		res.setModulo(MODULO_ENTE_ALTA);
		res.setIncompleto(Boolean.TRUE);
		List<PropiedadesErrorDTO>  errores = new ArrayList<PropiedadesErrorDTO>();
			errores.add(new PropiedadesErrorDTO(ENTE, 0));
			List<ErrorMensajesDTO> mensajes = new ArrayList<ErrorMensajesDTO>();
				mensajes.add(new ErrorMensajesDTO(0, mensaje));
				errores.get(0).setListErrorMensajes(mensajes);
		res.setErrores(errores);
		return res;
	}
}
