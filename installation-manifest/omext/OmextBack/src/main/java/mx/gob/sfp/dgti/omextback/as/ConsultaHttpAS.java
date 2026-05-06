/**
 * @(#)ConsultaAS.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as;

import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.dto.respuestas.VistaDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;

import java.util.List;

/**
 * Interface para el servicio ConsultaAS
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public interface ConsultaHttpAS {

    /**
     * Consulta al catalogo de entes al servicio de GraphQL
     *
     * @param query condiciones requeridas el la consulta
     *
     * @return catalogo de entes
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    Single<JsonArray> consultarEntes(JsonObject query);

    /**
     * Funcion para filtrar entes en memoria
     *
     * @param condiciones
     * @return
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public Single<JsonArray> filtrarEntesMemoria(JsonObject condiciones);

    /**
     * Servicio para generar un reporte
     *
     * @param
     * @return
     *
     * @author pavel.martinez
     * @since 20/05/2021
     */
    public Single<String> generarReporte(JsonObject query);


    /**
     *
     * @param nuevaVista
     * @return
     *
     * @author pavel.martinez
     * @since 20/05/2021
     */
    public Single<JsonObject> agregarFirmaAutomatizadaAVista(VistaDTO nuevaVista, boolean esFirmaListado);

    /**
     *
     * @param query
     * @return
     *
     * @author pavel.martinez
     * @since 20/05/2021
     */
    public Single<String> generarImpresionVista(JsonObject query);

    /**
     *
     * @param condiciones
     * @param collName
     * @return
     *
     * @author pavel.martinez
     * @since 20/05/2021
     */
    public Single<JsonArray> filtrarEntesMemoriaPorCollName(JsonObject condiciones, Integer collName) throws SFPException;
    /**
     *
     * @param collName
     * @param folios
     * @param idPeriodo
     *
     * @return
     *
     * @author pavel.martinez
     * @since 20/04/2021
     */
    public Single<Boolean> generarServidoresVistas(Integer collName, List<String> folios, String idPeriodo);

    /**
     *
     * @param collName
     * @return
     *
     * @author pavel.martinez
     * @since 20/04/2021
     */
    public boolean validarCollName(Integer collName) throws SFPException;

}
