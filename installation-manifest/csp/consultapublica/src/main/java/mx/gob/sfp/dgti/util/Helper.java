package mx.gob.sfp.dgti.util;

import io.vertx.core.json.JsonObject;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESTATUS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE;
public class Helper {

	public static JsonObject crearRespuestaErrorEstructura( String mensaje) {
		return new JsonObject().put(PROP_ESTATUS, Boolean.FALSE)
				.put(PROP_MENSAJE, mensaje);
	}
	
	public static boolean isNull(Object object) {
		return object == null;
	}
	
	public static boolean isNotEmpty(String string){
	       
        return !isNull(string) && !string.isEmpty();
}
}
