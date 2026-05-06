/**
 * @(#)CreadorQueryASImpl.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as.impl;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.omextback.as.CreadorQueryAS;
import mx.gob.sfp.dgti.omextback.as.CreadorQueryUtils;
import mx.gob.sfp.dgti.omextback.dto.input.CondicionesOmextInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroOmextInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.OrdenamientoInputDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.Arrays;
import java.util.List;

/**
 * Interface para el servicio CreadorQueryASImpl encargado de crear los queries para consultas de Elastic Search y
 * GraphQL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class CreadorQueryASImpl implements CreadorQueryAS {

    /**
     * Logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(CreadorQueryASImpl.class);

    /**
     * Funciones adicionales para crear queries
     */
    CreadorQueryUtils creadorQueryUtils = new CreadorQueryUtils();

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineConsultaServ(FiltroOmextInputDTO filtro, Integer from, Integer size,
                                               EnumCumplimiento cumplimiento, boolean ordenamiento, boolean esExtYCumpl)
            throws SFPException {

        String prefijo = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumQueryMongo.VACIO.getValor()
                : EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());

        JsonArray pipeline = new JsonArray();

        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, esExtYCumpl));
        }

        JsonObject facet = new JsonObject();
        facet.put(EnumMongoDB.TOTAL.getValor(), new JsonArray()
                .add(new JsonObject().put(EnumMongoDB.COUNT.getValor(), EnumMongoDB.TOTAL.getValor())));

        JsonArray pipelineFacet = new JsonArray();

        if(size != 0) {
            if (ordenamiento) {
                if (filtro.getOrdenamiento() != null) {
                    pipelineFacet.add(crearSort(filtro.getOrdenamiento(), prefijo));
                }

                pipelineFacet.add(new JsonObject().put(EnumMongoDB.SKIP.getValor(), from));
                pipelineFacet.add(new JsonObject().put(EnumMongoDB.LIMIT.getValor(), size));
            }
            pipelineFacet.add(crearFieldDescCumplimientoTipoDeclaracion(prefijo, cumplimiento,
                    filtro.getCondiciones().getTipoDeclaracion(), esExtYCumpl));

            facet.put(EnumMongoDB.RESULTADOS.getValor(), pipelineFacet);
        }

        pipeline.add(new JsonObject().put(EnumMongoDB.FACET.getValor(), facet));


        //LOGGER.info("EL PIPELINE QUEDO consultaServ -> " + pipeline);
        return pipeline;
    }

    @Override
    public JsonArray crearPipelineCountServ(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException {

        String prefijo = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumQueryMongo.VACIO.getValor()
                : EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());

        JsonArray pipeline = new JsonArray();

        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, false));
        }

        pipeline.add(new JsonObject().put(EnumMongoDB.COUNT.getValor(), EnumMongoDB.TOTAL.getValor()));

        //LOGGER.info("EL PIPELINE QUEDO PipelineCountServ -> " + pipeline);
        return pipeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineReporteServ(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException {

        String prefijo = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumQueryMongo.VACIO.getValor()
                : EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());

        JsonArray pipeline = new JsonArray();

        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, false));
        }

        if (filtro.getOrdenamiento() != null) {
            pipeline.add(crearSort(filtro.getOrdenamiento(), prefijo));
        }

        pipeline.add(crearFieldDescCumplimientoTipoDeclaracion(prefijo, cumplimiento,
                filtro.getCondiciones().getTipoDeclaracion(), false));

        pipeline.add(crearProjectReporteServidores(prefijo, cumplimiento, filtro.getCondiciones().getTipoDeclaracion()));

        //LOGGER.info("EL PIPELINE QUEDO eporteServ -> " + pipeline);
        return pipeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineBusqServ(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento, boolean esExtYCumpl)
            throws SFPException {

        String prefijo = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumQueryMongo.VACIO.getValor()
                : EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());

        JsonArray pipeline = new JsonArray();
        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, esExtYCumpl));
        } else {

            pipeline.add(
                    new JsonObject().put(EnumMongoDB.SORT.getValor(),
                            new JsonObject().put(EnumMongoDB.SCORE.getValor(), new JsonObject()
                                    .put(EnumMongoDB.META.getValor(), EnumMongoDB.TEXT_SCORE.getValor())
                            )
                    )
            );
        }
        pipeline.add(new JsonObject().put(EnumMongoDB.LIMIT.getValor(), 200));

        //LOGGER.info("EL PIPELINE QUEDO BusqServ -> " + pipeline);
        return pipeline;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearQueryGraficasConteo(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException {

        String prefijo = (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) ?
                EnumQueryMongo.VACIO.getValor()
                : EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());

        JsonArray pipeline = new JsonArray();
        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, false));
        }
        pipeline.add(new JsonObject().put(EnumMongoDB.COUNT.getValor(), EnumMongoDB.TOTAL.getValor()));

        //LOGGER.info("EL PIPELINE QUEDO GraficasConteo -> " + pipeline);
        return pipeline;
    }


    @Override
    public JsonArray crearPipelineCountCumplExtServ(FiltroOmextInputDTO filtro)
            throws SFPException {

        String prefijo = EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());

        JsonArray pipeline = new JsonArray();
        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, EnumCumplimiento.CUMPLIO, true));
        }

        pipeline.add(new JsonObject(
                "{ \"$group\" : { \"_id\" : { \"idEnte\" : \"$datosRusp.idEnte\", \"ext\" : \"$extemporaneo\" }, \"num\" : { \"$sum\" : 1.0 } } }"));
        pipeline.add(new JsonObject(
                "{ \"$group\" : { \"_id\" : \"$_id.idEnte\", \"cumplimiento\" : { \"$push\" : { \"ext\" : \"$_id.ext\", \"total\" : \"$num\" } } } }"));


        //LOGGER.info("EL PIPELINE QUEDO ConsultaCountExtServ -> " + pipeline);
        return pipeline;

    }

    @Override
    public JsonArray crearPipelineCumplimientoGrupo(FiltroOmextInputDTO filtro)
            throws SFPException {
        JsonObject match = new JsonObject()
                .put(EnumMongoDB.MATCH.getValor(), new JsonObject()
                    .put("_id.anio", filtro.getCondiciones().getAnio())
                    .put("_id.tipoObligacion", filtro.getCondiciones().getTipoDeclaracion().getNombreRusp())
                    .put("grupo", new JsonObject().put(EnumMongoDB.EXISTS.getValor(), true))
                );
        JsonObject group = new JsonObject()
                .put(EnumMongoDB.GROUP.getValor(), new JsonObject()
                    .put(EnumMongoDB.ID.getValor(), new JsonObject()
                        .put("grupo", "$grupo")
                            .put("nombreGrupo", "$nombreGrupo")
                    )
                    .put("cumplio", new JsonObject().put("$sum", "$CUMPLIO"))
                    .put("extemporaneo", new JsonObject().put("$sum", "$EXTEMPORANEO"))
                    .put("pendiente", new JsonObject().put("$sum", "$PENDIENTE"))
                );
        JsonObject addFields = new JsonObject()
                .put(EnumMongoDB.ADD_FIELDS.getValor(), new JsonObject()
                    .put("grupo", "$_id.grupo")
                    .put("nombreGrupo", "$_id.nombreGrupo")
                );
        return new JsonArray()
                .add(match)
                .add(group)
                .add(addFields);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineAggConteoInst(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException {

        String prefijo;
        if (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) {
            prefijo = EnumQueryMongo.VACIO.getValor();
        } else {
            prefijo = EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());
        }

        JsonArray pipeline = new JsonArray();
        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, false));
        }

        pipeline.add(new JsonObject()
                .put(EnumMongoDB.GROUP.getValor(), new JsonObject()
                        .put(EnumMongoDB.ID.getValor(), "$".concat(prefijo).concat(EnumMongoDB.ID_ENTE.getValor()))
                        .put(EnumMongoDB.TOTAL.getValor(), new JsonObject()
                                .put(EnumMongoDB.SUM.getValor(), 1)
                        )
                ));
        //LOGGER.info("EL PIPELINE QUEDO GraficasConteo-> " + pipeline);
        return pipeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearQueryAggConteoUA(FiltroOmextInputDTO filtro, EnumCumplimiento cumplimiento)
            throws SFPException {

        String prefijo;
        if (cumplimiento.equals(EnumCumplimiento.PENDIENTE)) {
            prefijo = EnumQueryMongo.VACIO.getValor();
        } else {
            prefijo = EnumQueryMongo.DATOS_RUSP.getValor().concat(EnumQueryMongo.PUNTO.getValor());
        }

        JsonArray pipeline = new JsonArray();
        if (filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.INICIO.getValor()) ||
                filtro.getCondiciones().getTipoDeclaracion().getValor().equals(EnumTipoDeclaracion.CONCLUSION.getValor())) {
            pipeline.add(creadorQueryUtils.crearFieldFechaRegistro(prefijo));
        }

        if (filtro.getCondiciones() != null) {
            pipeline.add(crearMatch(filtro.getCondiciones(), prefijo, cumplimiento, false));
        }

        pipeline.add(new JsonObject()
                .put(EnumMongoDB.GROUP.getValor(), new JsonObject()
                        .put(EnumMongoDB.ID.getValor(), new JsonObject()
                                .put(EnumMongoDB.CLAVE_UA.getValor(), "$".concat(prefijo).concat(EnumMongoDB.CLAVE_UA.getValor()))
                                .put(EnumMongoDB.UNIDAD_ADMIN.getValor(), "$".concat(prefijo).concat(EnumMongoDB.UNIDAD_ADMIN.getValor()))
                        )
                        .put(EnumMongoDB.TOTAL.getValor(), new JsonObject()
                                .put(EnumMongoDB.SUM.getValor(), 1)
                        )
                ));

        return pipeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject crearQueryConsultaEntes(FiltroOmextInputDTO filtro, Integer collname) {
        CondicionesOmextInputDTO condiciones = filtro.getCondiciones();
        JsonObject filtros = new JsonObject();

        if (condiciones.getIdEnte() != null && condiciones.getIdEnte().size() == 1) {
            filtros.put(EnumQueryGraphQL.ID.getValor(), condiciones.getIdEnte().get(0));
        } else {

            if (condiciones.getRamo() != null) {
                filtros.put(EnumQueryGraphQL.RAMO.getValor(), condiciones.getRamo());
            }

            if (condiciones.getUr() != null) {
                filtros.put(EnumQueryGraphQL.UNIDAD_RESPONSABLE.getValor(), condiciones.getUr());
            }
        }

        return new JsonObject()
                .put(EnumQueryGraphQL.QUERY.getValor(), EnumQueryGraphQL.QUERY_OBTENER_ENTES.getValor())
                .put(EnumQueryGraphQL.VARIABLES.getValor(), new JsonObject()
                        .put(EnumQueryGraphQL.FILTRO.getValor(), filtros));
    }


    /**
     * Funcion para crear el sort de la consulta
     *
     * @param sort objeto con los paramentros de ordenamiento
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private JsonObject crearSort(List<OrdenamientoInputDTO> sort, String prefijo) {
        JsonObject orden = new JsonObject();

        sort.forEach(cond -> orden.put(prefijo.concat(cond.getCampo().getCampo()), cond.getOrden().getCampo()));

        return new JsonObject()
                .put(EnumMongoDB.SORT.getValor(), orden);
    }

    /**
     * @param prefijo
     * @param cumplimiento
     * @param tipoObligacion
     * @return
     */
    private JsonObject crearFieldDescCumplimientoTipoDeclaracion(String prefijo, EnumCumplimiento cumplimiento,
                                                                 EnumTipoDeclaracion tipoObligacion, boolean esExtYCumpl) {
        JsonObject switchTipoObligacion = null;
        if (esExtYCumpl && cumplimiento.equals(EnumCumplimiento.OBLIGADO)) {
            switchTipoObligacion = obtenerSwitchTipoCumplimiento();
        }

        return new JsonObject()
                .put(EnumMongoDB.ADD_FIELDS.getValor(),
                        new JsonObject().put(prefijo.concat(EnumGraphQL.TIPO_DECLARACION_DESC.getValor()),  obtenerSwitchTipoObligacion(prefijo))
                                .put(prefijo.concat(EnumGraphQL.CUMPLIMIENTO_DESC.getValor()),
                                        (switchTipoObligacion != null) ? switchTipoObligacion :  cumplimiento.getValor()));
    }

    /**
     * Funcion para agregar condiciones para diferenciar busquedas relacionados con el campo de extemporaneo
     *
     * @param condiciones  condiciones en donde se agrega
     * @param cumplimiento tipo de cumplimiento
     * @author pavel.martinez
     * @since 31/07/2020
     */
    private void crearCondicionExtemporaneos(JsonObject condiciones, EnumCumplimiento cumplimiento,
                                             JsonArray condicionesAnd) {

        if (cumplimiento.equals(EnumCumplimiento.EXTEMPORANEO)) {
            condiciones
                    .put(EnumMongoDB.EXTEMPORANEO.getValor(), new JsonObject()
                            .put(EnumMongoDB.EXISTS.getValor(), true)
                            .put(EnumMongoDB.IN.getValor(), new JsonArray()
                                    .add(true))
                    );
        } else if (cumplimiento.equals(EnumCumplimiento.CUMPLIO)) {
            condicionesAnd.add(new JsonObject()
                    .put(EnumMongoDB.OR.getValor(), new JsonArray()
                            .add(new JsonObject()
                                    .put(EnumMongoDB.EXTEMPORANEO.getValor(), new JsonObject()
                                            .put(EnumMongoDB.EXISTS.getValor(), false)))
                            .add(new JsonObject()
                                    .put(EnumMongoDB.EXTEMPORANEO.getValor(), false))));

        }
    }

    /**
     *
     * @param prefijo
     * @param estatus
     * @param tipoDeclaracion
     * @return
     */
    private JsonObject crearProjectReporteServidores(String prefijo, EnumCumplimiento estatus,
                                                     EnumTipoDeclaracion tipoDeclaracion ) {
        return new
                JsonObject().put("$project", new JsonObject()
                    .put(prefijo.concat("idPuesto"),1)
                    .put(prefijo.concat("activo") ,1)
                    //.put(prefijo.concat("tipoObligacion"), tipoDeclaracion.getValorFront())
                    .put(prefijo.concat("anio"), 1)
                    .put(prefijo.concat("fechaTomaPosesionPuesto"),1)
                    .put(prefijo.concat("curp"),1)
                    .put(prefijo.concat("nombres"),1)
                    .put(prefijo.concat("segundoApellido"),1)
                    .put(prefijo.concat("primerApellido"),1)
                    .put(prefijo.concat("unidadAdministrativa"),1)
                    .put(prefijo.concat("empleoCargoComision"),1)
                    //.put("estatus", estatus.getValorFront())
                    .put(prefijo.concat("tipoDeclaracionDesc"), 1)
                    .put(prefijo.concat("cumplimientoDesc"), 1)
        );
    }

    /**
     * Funcion para agregar un field con la descripcion del tipo de obligacion
     *
     * @return JsonObject con la parte del query
     */
    public static JsonObject obtenerSwitchTipoObligacion(String prefijo) {
        JsonArray branches = new JsonArray();
        Arrays.stream(EnumTipoDeclaracion.values())
                .forEach(miEnum ->
                        branches.add(new JsonObject().put(EnumMongoDB.CASE.getValor(),
                                new JsonObject().put(EnumMongoDB.EQ.getValor(),
                                        new JsonArray()
                                                .add("$".concat(prefijo).concat("tipoObligacion"))
                                                .add(miEnum.getNombreRusp())
                                ))
                                .put(EnumMongoDB.THEN.getValor(), miEnum.getValorFront()))
                );
        return new JsonObject().put(EnumMongoDB.SWITCH.getValor(),
                new JsonObject()
                        .put(EnumMongoDB.BRANCHES.getValor(), branches)
                        .put(EnumMongoDB.DEFAULT.getValor(), ""));
    }


    /**
     * Funcion para agregar un field con la descripcion del tipo de cumplimiento
     *
     * @return JsonObject con la parte del query
     */
    public static JsonObject obtenerSwitchTipoCumplimiento() {

        JsonArray branches = new JsonArray();

        branches.add(new JsonObject().put(EnumMongoDB.CASE.getValor(),
                new JsonObject().put(EnumMongoDB.EQ.getValor(),
                        new JsonArray()
                                .add("$extemporaneo")
                                .add(true)
                ))
                .put(EnumMongoDB.THEN.getValor(), EnumCumplimiento.EXTEMPORANEO.getValor()));

        return new JsonObject().put(EnumMongoDB.SWITCH.getValor(),
                new JsonObject()
                        .put(EnumMongoDB.BRANCHES.getValor(), branches)
                        .put(EnumMongoDB.DEFAULT.getValor(), EnumCumplimiento.CUMPLIO.getValor()));
    }

    /**
     * ESPECIAL
     *4
     * @param condiciones
     * @param prefijo
     * @param cumplimiento
     * @throws SFPException
     */
    private JsonObject crearMatch(CondicionesOmextInputDTO condiciones, String prefijo, EnumCumplimiento cumplimiento, boolean esExtYCumpl)
            throws SFPException {

        JsonObject query = new JsonObject();
        JsonArray queryAnd = new JsonArray();

        query.put(prefijo.concat(EnumQueryMongo.ACTIVO.getValor()), 1);

        if (condiciones.getIdEnte() != null && condiciones.getIdEnte().size() > 0) {
            query.put(prefijo.concat(EnumMongoDB.ID_ENTE.getValor()), new JsonObject()
                    .put(EnumMongoDB.IN.getValor(), condiciones.getIdEnte())
            );
        } else {

            if (condiciones.getRamo() != null) {
                query.put(prefijo.concat(EnumQueryMongo.RAMO.getValor()), condiciones.getRamo().toString());
            }

            if (condiciones.getUr() != null) {
                query.put(prefijo.concat(EnumQueryMongo.UR.getValor()), condiciones.getUr());
            }
        }

        if (cumplimiento.equals(EnumCumplimiento.CUMPLIO) || cumplimiento.equals(EnumCumplimiento.EXTEMPORANEO)) {
            query.put(EnumQueryMongo.ACTIVO.getValor(), 1);
        }

        if(!esExtYCumpl) {
            crearCondicionExtemporaneos(query, cumplimiento, queryAnd);
        }


        if (condiciones.getNombreEnte() != null) {
            query.put(prefijo.concat(EnumQueryMongo.NOMBRE_ENTE.getValor()), condiciones.getNombreEnte());
        }

        if (condiciones.getClaveUa() != null) {
            query.put(prefijo.concat(EnumQueryMongo.CLAVE_UA.getValor()), condiciones.getClaveUa());
        }

        if (condiciones.getSindicalizado() != null && condiciones.getSindicalizado().equals(EnumSindicalizados.INCLUIR_SINDICALIZADOS)) {
            JsonArray condicionesOr = new JsonArray();
            condicionesOr.add(
                    new JsonObject()
                            .put(
                                    prefijo.concat(EnumQueryMongo.SINDICALIZADO.getValor()),
                                    condiciones.getSindicalizado().getValorMongo()));

            queryAnd.add(new JsonObject().put(EnumMongoDB.OR.getValor(), condicionesOr));

            if (condiciones.getIdNivelJerarquico() != null && condiciones.getIdNivelJerarquico().size() > 0) {
                condicionesOr.add(new JsonObject()
                        .put(prefijo.concat(EnumQueryMongo.ID_NIVEL_JERARQUICO.getValor()), new JsonObject()
                                .put(EnumMongoDB.IN.getValor(), condiciones.getIdNivelJerarquico())));
            }

        } else {
            if (condiciones.getIdNivelJerarquico() != null && condiciones.getIdNivelJerarquico().size() > 0) {
                query.put(prefijo.concat(EnumQueryMongo.ID_NIVEL_JERARQUICO.getValor()), new JsonObject()
                        .put(EnumMongoDB.IN.getValor(), condiciones.getIdNivelJerarquico())
                );
            }
            if (condiciones.getSindicalizado() != null) {
                if (condiciones.getSindicalizado().equals(EnumSindicalizados.SIN_SINDICALIZADOS)) {
                    JsonArray condicionesOr = new JsonArray()
                            .add(new JsonObject()
                                    .put(prefijo.concat(EnumQueryMongo.SINDICALIZADO.getValor()), new JsonObject()
                                            .put(EnumMongoDB.EXISTS.getValor(), false)))
                            .add(new JsonObject()
                                    .put(prefijo.concat(EnumQueryMongo.SINDICALIZADO.getValor()), false));

                    queryAnd.add(new JsonObject().put(EnumMongoDB.OR.getValor(), condicionesOr));
                } else {
                    query.put(
                            prefijo.concat(EnumQueryMongo.SINDICALIZADO.getValor()),
                            condiciones.getSindicalizado().getValorMongo());
                }
            }
        }

        if (condiciones.getTipoDeclaracion() != null) {
            query.put(prefijo.concat(EnumQueryMongo.TIPO_DECLARACION.getValor()),
                    condiciones.getTipoDeclaracion().getNombreRusp());
        }

        if (condiciones.getUr() != null) {
            query.put(prefijo.concat(EnumQueryMongo.UR.getValor()), condiciones.getUr());
        }

        if (condiciones.getRamo() != null) {
            query.put(prefijo.concat(EnumQueryMongo.RAMO.getValor()), condiciones.getRamo());
        }

        creadorQueryUtils.crearCondicioNombreCompletoCurp(condiciones, prefijo, query, queryAnd);

        if(condiciones.getEnVista() != null) {
            if(condiciones.getEnVista()) {
                query.put(EnumMongoDB.EN_VISTA.getValor(), true);
            } else {
                query.put(EnumMongoDB.EN_VISTA.getValor(), new JsonObject().put(EnumMongoDB.EXISTS.getValor(), false));
            }
        }

        if (condiciones.getAnio() != null) {
            query.put(prefijo.concat(EnumQueryMongo.ANIO.getValor()), condiciones.getAnio());
            if (condiciones.getTipoDeclaracion().equals(EnumTipoDeclaracion.INICIO) ||
                    condiciones.getTipoDeclaracion().equals(EnumTipoDeclaracion.CONCLUSION)) {
                if (condiciones.getMes() != null && !condiciones.getMes().isEmpty()) {
                    creadorQueryUtils.crearIntervalosAnioMes(prefijo, query, condiciones.getMes(), condiciones.getAnio());
                } else {
                    throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS,
                            EnumDetalleParamInc.MESES_REQUERIDOS.getValor());
                }
            } else if (condiciones.getMes() != null && !condiciones.getMes().isEmpty()) {
                throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS,
                        EnumDetalleParamInc.MESES_MODIFICACION.getValor());
            }
        }

        if (!queryAnd.isEmpty()) {
            query.put(EnumMongoDB.AND.getValor(), queryAnd);
        }

        return new JsonObject().put(EnumMongoDB.MATCH.getValor(), query);

    }

    @Override
    public boolean esConsultaInicio(FiltroOmextInputDTO filtro) {
        CondicionesOmextInputDTO condiciones = filtro.getCondiciones();

        if(condiciones != null && condiciones.getUr()== null
            && condiciones.getClaveUa() == null
            && condiciones.getNombreEnte() == null
            && condiciones.getNombresApellidos() == null
            && condiciones.getRamo() == null
            && (condiciones.getIdNivelJerarquico() == null || condiciones.getIdNivelJerarquico().isEmpty())
            && condiciones.getMes() ==null
                && condiciones.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)
            && condiciones.getSindicalizado() == null) {
            //LOGGER.info("=== Uso de preconteos con condiciones: " + condiciones);
            return true;
        }
        return false;

    }

}
