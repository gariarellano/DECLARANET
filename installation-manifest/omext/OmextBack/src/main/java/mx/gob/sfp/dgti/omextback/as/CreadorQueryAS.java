/**
 * @(#)CreadorQueryAS.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroOmextInputDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCumplimiento;

/**
 * Interface para el servicio CreadorQueryAS
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public interface CreadorQueryAS {

    /**
     * Creador del query de ElasticSearch para obtener los informacion de los servidores por su cumplimiento
     *
     * @param filtro condiciones requeridas por el usuario
     * @param from indica a partir de donde se consulta
     * @param size tamanio de las paginas que se consultan
     * @param cumplimiento indica que tipo de coleccion se consulta de acuerdo al cumplimiento, dado que las fuentes de
     *                     informacion difieren es posible que se utilice un prefijo dependiendo del tipo de
     *                     cumpliemiento
     * @param ordenamiento ordenamiento requerido
     *
     * @return JsonObject con la consulta a realizar
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public JsonArray crearPipelineConsultaServ(FiltroOmextInputDTO filtro, Integer from, Integer size,
                                               EnumCumplimiento cumplimiento, boolean ordenamiento, boolean esExtYCumpl)
            throws SFPException;


    public JsonArray crearPipelineCountServ(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException;

    /**
     * Creador del query de ElasticSearch para obtener los informacion de los servidores por su cumplimiento
     *
     * @param filtro condiciones requeridas por el usuario
     * @param cumplimiento indica que tipo de coleccion se consulta de acuerdo al cumplimiento, dado que las fuentes de
     *                     informacion difieren es posible que se utilice un prefijo dependiendo del tipo de
     *                     cumpliemiento
     *
     * @return JsonObject con la consulta a realizar
     *
     * @author pavel.martinez
     * @since 26/07/2020
     */
    public JsonArray crearPipelineBusqServ(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento, boolean esExtYCumpl)
            throws SFPException;

    /**
     * Creador del query de ElasticSearch para obtener los informacion de cumplimiento para graficas
     *
     * @param filtro condiciones requeridas por el usuario
     * @param cumplimiento indica que tipo de coleccion se consulta de acuerdo al cumplimiento, dado que las fuentes de
     *                     informacion difieren es posible que se utilice un prefijo dependiendo del tipo de
     *                     cumpliemiento
     *
     * @return JsonObject con la consulta a realizar
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public JsonArray crearQueryGraficasConteo(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException;

    /**
     * Creador del query de ElasticSearch para obtener los informacion de cumplimiento por institucion
     *
     * @param filtro condiciones requeridas por el usuario
     * @param cumplimiento indica que tipo de coleccion se consulta de acuerdo al cumplimiento, dado que las fuentes de
     *                     informacion difieren es posible que se utilice un prefijo dependiendo del tipo de
     *                     cumpliemiento
     *
     * @return JsonObject con la consulta a realizar
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public JsonArray crearPipelineAggConteoInst(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException;

    /**
     * Creador del query de ElasticSearch para obtener los informacion cumplimiento por unidadAdministrativa, si es
     * posible, se consultaran en caso de que la ur de la institucion sea 0
     *
     * @param filtro condiciones requeridas por el usuario
     * @param cumplimiento indica que tipo de coleccion se consulta de acuerdo al cumplimiento, dado que las fuentes de
     *                     informacion difieren es posible que se utilice un prefijo dependiendo del tipo de
     *                     cumpliemiento
     *
     * @return JsonObject con la consulta a realizar
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public JsonArray crearQueryAggConteoUA(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException;

    /**
     * Creador del query de GraphQL para obtener la informacion de los entes
     *
     * @param filtro condiciones del o los entes requeridos por el usuario
     * @param collname indica el collName para filtrar instituciones
     *
     * @return JsonObject con la consulta a realizar
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public JsonObject crearQueryConsultaEntes(FiltroOmextInputDTO filtro, Integer collname);

    public JsonArray crearPipelineCountCumplExtServ(FiltroOmextInputDTO filtro) throws SFPException;

    /**
     *
     *
     * @param filtro
     * @param cumplimiento
     * @return
     * @throws SFPException
     */
    public JsonArray crearPipelineReporteServ(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException;


    /**
     *
     * @param filtro
     * @return
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 24/05/2021
     */
    public JsonArray crearPipelineCumplimientoGrupo(FiltroOmextInputDTO filtro)
            throws SFPException;

    public boolean esConsultaInicio(FiltroOmextInputDTO filtro);


}
