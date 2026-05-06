/**
 * @(#)Respuestas.java 20/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

import io.vertx.core.json.JsonObject;

/**
* Enum con respuestas para el usuario
* 
* @author Pável Alexei Martínez Regalado aka programador02
* @since 20/02/2019
*/
public enum Respuestas {
	EXITO(1, "Se realizó la operación con éxito."),
	ERROR(2, "Ocurrió un error y no se pudo completar la operación."),
	ERROR_BASE(3, "Ocurrió un error en base y no se pudo completar la operación."),
	ENTE_EXISTENTE(4, "Un ente con esas características ya existe, no se puede crear uno nuevo."),
	ENTE_ACTUALIZACION_REQ(5, "Dados los cambios realizados es necesario utilizar "),
	PARAMETROS_INCOMPLETOS(6,"Parámetros incompletos, para realizar la consulta es requerido:"
			+ " ramo, unidadResponsable, idNivelGobierno, idPoder, idEntidadFederativa"),
	SIN_RESULTADOS(7,"No se encontraron resultados para la consulta."),
	JSON_INCORRECTO(8,"Los parámetros enviados no son correctos."),
	ENTE_NO_ENCONTRADO(9,"No se encontró dicho ente.");

	
	/**
	 * Id de la respuesta
	 */
    private final int id;
    
    /*
     * Descripcion de la respuesta para el usuario
     */
    private final String descripcion;

    /**
     * Metodo constructor
     * 
     * @param id: id de la respuesta
     * @param descripcion
     * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	Respuestas(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

	public int getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Funci�n para crear una respuesta json a partir del enum
	 * 
	 * @return json: objeto json
	 * 
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 19/02/2019
	 */
	public static JsonObject crearRespuestaJson(int id) {
		for (Respuestas respuesta : Respuestas.values()) {
			if (respuesta.id == id) {
				return new JsonObject().put("respuesta",
						new JsonObject()
							.put("idRespuesta", respuesta.id)
							.put("respuestaDesc", respuesta.descripcion));
			}
		}
		throw new IllegalArgumentException("No existe la respuesta.");
	}
}
