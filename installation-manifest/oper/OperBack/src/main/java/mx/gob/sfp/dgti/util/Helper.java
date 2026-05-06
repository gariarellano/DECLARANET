package mx.gob.sfp.dgti.util;

import static mx.gob.sfp.dgti.util.Constantes.BOOLEAN_FALSE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESTATUS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE;

import io.vertx.core.json.JsonObject;

public class Helper {

	public static JsonObject crearRespuestaErrorEstructura( String mensaje) {
		return new JsonObject().put(PROP_ESTATUS, BOOLEAN_FALSE)
				.put(PROP_MENSAJE, mensaje);
	}
	
	public static boolean isNull(Object object) {
		return object == null;
	}
	
	public static boolean isNotEmpty(String string){
	       
        return !isNull(string) && !string.isEmpty();
	}
}
