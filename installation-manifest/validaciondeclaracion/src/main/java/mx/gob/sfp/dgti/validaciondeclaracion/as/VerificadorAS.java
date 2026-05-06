/**
 * @(#)VerificadorAS.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.as;

import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;

import java.util.Set;

/**
 * Interface para el servicio VerificadorAS
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public interface VerificadorAS {

    /**
     * Completable que se completa si cumplio con las validaciones o una ServiceException
     * con el objeto RespuestaDTO como debugInfo
     *
     * @param declaracion declaracion en JsonObject a verificar
     * @return Completable completado o como error
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    public Completable validarDeclaracion(JsonObject declaracion, Set<String> mandados);

    /**
     * Completable que se completa si cumplio con las validaciones o una ServiceException
     * con el objeto RespuestaDTO como debugInfo
     *
     * @param avisoCambio aviso de cambio de dependencia en JsonObject a verificar
     * @return Completable completado o como error
     *
     * @author pavel.martinez
     * @since 28/01/2019
     */
    public Completable validarAvisoCambio(JsonObject avisoCambio, Set<String> mandados);

    /**
     * Completable que se completa si cumplio con las validaciones o una ServiceException
     * con el objeto RespuestaDTO como debugInfo
     *
     * @param notas aviso de cambio de dependencia en JsonObject a verificar
     * @return Completable completado o como error
     *
     * @author pavel.martinez
     * @since 28/01/2019
     */
    public Completable validarNotaAclaratoria(JsonObject notas, Set<String> mandados);

}
