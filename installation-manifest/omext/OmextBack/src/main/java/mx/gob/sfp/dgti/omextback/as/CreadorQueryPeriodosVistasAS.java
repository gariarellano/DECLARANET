/**
 * @(#)CreadorQueryPeriodosVistasAS.java 02/02/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as;

import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroOmextInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.FiltroPeriodosVistasInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.PeriodoInputDTO;
import mx.gob.sfp.dgti.omextback.dto.input.UsuarioInputDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.FirmanteDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.PeriodoDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.TextoOficioDTO;
import mx.gob.sfp.dgti.omextback.dto.respuestas.VistaDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface para el creador de queries para lo relacionado con los periodos y vistas
 *
 * @author pavel.martinez
 * @since 02/02/2021
 */
public interface CreadorQueryPeriodosVistasAS {

    /**
     * Creador de pipeline de consulta de periodos
     *
     * @return JsonArray con el pipeline necesario
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public JsonArray crearPipelineConsultaPeriodos(FiltroPeriodosVistasInputDTO filtro)
            throws SFPException;

    /**
     * Creador del pipeline de periodos por institucion
     *
     * @param filtro filtro general de periodos y vistas
     * @return JsonArray con el pipeline necesario
     *
     * @author pavel.martinez
     * @since 26/07/2020
     */
    public JsonArray crearPipelineInstPeriodo(FiltroPeriodosVistasInputDTO filtro)
            throws SFPException;

    /**
     * Creador de query de vistas
     *
     * @param filtro filtro general de periodos y vistas
     * @return JsonArray con el pipeline necesario
     *
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public JsonArray crearPipelineVista(FiltroPeriodosVistasInputDTO filtro) throws SFPException;

    /**
     * Creador de query de servidores de una vista
     *
     * @param filtro filtro general de periodos y vistas
     * @return JsonArray con el pipeline necesario
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public JsonArray crearPipelineServidoresVista(FiltroPeriodosVistasInputDTO filtro) throws SFPException;


    /**
     * Creador del save de perido
     *
     * @param periodo objeto de periodo a guardar
     * @return jsonObject con informacion para guardar
     *
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 08/02/2021
     */
    public JsonObject crearNuevoPeriodo(PeriodoInputDTO periodo, String version) throws SFPException;

    /**
     * Crear pipeline que traera el ultimo periodo con las condiciones introducidas para crear versiones del mismo
     * @param periodo
     * @return JsonArray con el pipeline necesario
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public JsonArray crearPipelineUltimoPeriodo(PeriodoInputDTO periodo) throws SFPException;

    /**
     *
     * @param idPeriodo
     * @return JsonArray con el pipeline necesario
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public JsonArray crearPipelinePeriodoPorId(String idPeriodo) throws SFPException;

    /**
     * Creador del save de perido
     *
     * @param periodo periodo a actualizar
     * @return jsonObject con informacion para actualizar
     *
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 08/02/2021
     */
    public JsonObject actualizarFechaPeriodo(PeriodoInputDTO periodo) throws SFPException;

    /**
     * Creador del save para extender periodos por instituciones
     *
     * @param filtro
     * @return JsonArray con el pipeline necesario
     *
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 08/03/2021
     */
    public JsonArray crearPipelineVistaPorFolio(FiltroPeriodosVistasInputDTO filtro ) throws SFPException;

    /**
     *
     * @param filtro general de periodos vistas
     * @return JsonArray con el pipeline necesario
     * @throws SFPException
     *
     * @author pavel.martinez
     * @since 08/03/2021
     */
    public JsonArray crearPipelineServidoresVistaImpresion(FiltroPeriodosVistasInputDTO  filtro) throws SFPException;

    /**
     *
     * @param filtroVistas
     * @param periodo
     * @return
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public FiltroOmextInputDTO cambiarTipoFiltro(FiltroPeriodosVistasInputDTO filtroVistas, PeriodoDTO periodo);

    /**
     *
     * @param periodo
     * @param datosFirma
     * @param entes
     * @param textoOficio
     * @param posicionFolio
     * @return
     *
     * @author pavel.martinez
     * @since 02/02/2021
     */
    public Observable<VistaDTO> obtenerVistasNuevasOmisos(String remitente, PeriodoDTO periodo,
                                                          FirmanteDTO datosFirma,
                                                          HashMap<String, JsonObject> entes,
                                                          TextoOficioDTO textoOficio,
                                                          AtomicReference<Integer> posicionFolio,
                                                          UsuarioInputDTO usuarioRegistro);

}
