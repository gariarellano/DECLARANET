/**
 * @(#)RepartidorAS.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.as;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.LlamadoDTO;

import java.util.List;
import java.util.Set;

/**
 * Interface para el servicio ReparidorAS
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public interface RepartidorAS {

    /**
     * Metodo para repartir la declaracion de JsonObject a una lista de LlamadoDTO
     * que son enviados a su respectivos servicios.
     *
     * @param declaracion: declaracion como JsonObject
     * @return Single con una lista de llamados que seran enviados
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    public Single<List<LlamadoDTO>> repartirDeclaracion(JsonObject declaracion, Set<String> mandados);

    /**
     * Metodo para repartir el aviso de cambio de dependencia de JsonObject a una lista de LlamadoDTO
     * que son enviados a su respectivos servicios.
     *
     * @param avisoCambio: aviso de cambio de dependencia como JsonObject
     * @return Single con una lista de llamados que seran enviados
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 28/01/2020
     */
    public Single<List<LlamadoDTO>> repartirAvisoCambio(JsonObject avisoCambio, Set<String> mandados);

    /**
     * Metodo para obtener el llamado al servicio de validacion de notas
     * @param notasCompleto: Objeto con las notas
     *
     * @return objeto de LlamadoDTO
     *
     * @author pavel.martinez
     * @since 21/02/2020
     */
    public Single<LlamadoDTO> repartirNotas(JsonObject notasCompleto);
}
