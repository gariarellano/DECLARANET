/**
 * @(#)CreadorQueryASImpl.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as.impl;

import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.omextback.as.CreadorQueryPeriodosVistasAS;
import mx.gob.sfp.dgti.omextback.as.CreadorQueryUtils;
import mx.gob.sfp.dgti.omextback.dto.input.*;
import mx.gob.sfp.dgti.omextback.dto.respuestas.*;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface para el servicio CreadorQueryASImpl encargado de crear los queries para consultas de Elastic Search y
 * GraphQL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class CreadorQueryPeriodosVistasASImpl implements CreadorQueryPeriodosVistasAS {

    /**
     * Funciones adicionales para crear queries
     */
    CreadorQueryUtils creadorQueryUtils = new CreadorQueryUtils();

    /**
     * Anio actual
     */
    static int elAnio = Calendar.getInstance().get(Calendar.YEAR);

    /**
     * Inicio de la cadena para el folio
     */
    static String cadenaFolioVistas = "SRCI/UEPPCI/317/CRPISPS/V/";

    /**
     * Logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(CreadorQueryPeriodosVistasASImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineConsultaPeriodos(FiltroPeriodosVistasInputDTO filtro) throws SFPException {
        JsonObject match = new JsonObject();
        CondicionesPeriodosVistasInputDTO condiciones = filtro.getCondiciones();

        if (condiciones.getIdPeriodo() != null) {
            match.put(EnumMongoDB.ID_PERIODO.getValor(), condiciones.getIdPeriodo());
        } else {
            if(condiciones.getTipoDeclaracion() != null) {
                match.put(EnumMongoDB.TIPO_DECLARACION.getValor(), condiciones.getTipoDeclaracion());

                if (!condiciones.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {
                    if (condiciones.getMes() == null) {
                        throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
                    } else {
                        match.put(EnumMongoDB.MES.getValor(), condiciones.getMes());
                    }
                }

            }
            if(condiciones.getAnio() != null) {
                match.put(EnumMongoDB.ANIO.getValor(), condiciones.getAnio());
            }

        }
        JsonObject facet = new JsonObject();

        JsonArray pipelineResultados = new JsonArray();

        JsonObject fechasString = new JsonObject();
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_LIMITE.getValor());
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_REGISTRO.getValor());
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_ACTUALIZACION.getValor());
        agregarFieldInstExt(fechasString);
        JsonObject addFields = new JsonObject().put(EnumMongoDB.ADD_FIELDS.getValor(), fechasString);

        if(filtro.getOffset() != null) {
            pipelineResultados.add(new JsonObject().put(EnumMongoDB.SKIP.getValor(), filtro.getOffset()));
        }
        if(filtro.getTamanio() != null) {
            pipelineResultados.add(new JsonObject().put(EnumMongoDB.LIMIT.getValor(), filtro.getTamanio()));
        }
        pipelineResultados.add(addFields);

        facet.put(EnumMongoDB.VENCE_FECHA_LIMITE.getValor(), agregarJsonArrayVencFechaLimite());
        facet.put(EnumMongoDB.TOTAL.getValor(),
                new JsonArray().add(new JsonObject().put(EnumMongoDB.COUNT.getValor(), EnumMongoDB.TOTAL.getValor())));
        facet.put(EnumMongoDB.RESULTADOS.getValor(), pipelineResultados);

        LOGGER.info("\n \n ==== Gran arreglo queda : " + new JsonArray()
                .add(new JsonObject().put(EnumMongoDB.MATCH.getValor(), match))
                .add(new JsonObject().put(EnumMongoDB.FACET.getValor(), facet)));

        return new JsonArray()
                .add(new JsonObject().put(EnumMongoDB.MATCH.getValor(), match))
                .add(new JsonObject().put(EnumMongoDB.FACET.getValor(), facet));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineInstPeriodo(FiltroPeriodosVistasInputDTO filtro) throws SFPException {
        JsonObject match = new JsonObject();

        CondicionesPeriodosVistasInputDTO condiciones = filtro.getCondiciones();
        match.put(EnumMongoDB.TIPO_DECLARACION.getValor(), condiciones.getTipoDeclaracion());
        match.put(EnumMongoDB.ID_PERIODO.getValor(), condiciones.getIdPeriodo());

        return new JsonArray().add(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelinePeriodoPorId(String idPeriodo) throws SFPException {
        JsonObject match = new JsonObject();
        JsonObject fechasString = new JsonObject();
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_LIMITE.getValor());
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_REGISTRO.getValor());
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_ACTUALIZACION.getValor());
        agregarFieldInstExt(fechasString);
        JsonObject addFields = new JsonObject().put(EnumMongoDB.ADD_FIELDS.getValor(), fechasString);
        match.put(EnumMongoDB.ID_PERIODO.getValor(), idPeriodo);
        return new JsonArray().add(
                new JsonObject().put(EnumMongoDB.MATCH.getValor(), match)
        ).add(addFields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineVista(FiltroPeriodosVistasInputDTO filtro) throws SFPException {

        JsonObject match = new JsonObject();
        CondicionesPeriodosVistasInputDTO condiciones = filtro.getCondiciones();
        if(condiciones.getTipoIncumplimiento() != null) {
            match.put(EnumMongoDB.TIPO_INCUMPLIMIENTO.getValor(), condiciones.getTipoIncumplimiento());
        }
        if(condiciones.getAnio() != null) {
            match.put(EnumMongoDB.ANIO.getValor(), condiciones.getAnio());
        }
        if(condiciones.getIdPeriodo() != null) {
            match.put(EnumMongoDB.ID_PERIODO.getValor(), condiciones.getIdPeriodo());
        }
        if(!condiciones.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {
            if(condiciones.getMes() == null) {
                throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
            } else {
                match.put(EnumMongoDB.MES.getValor(), condiciones.getMes());
            }
        }

        return new JsonArray()
                .add(new JsonObject().put("$match", match));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineServidoresVistaImpresion(FiltroPeriodosVistasInputDTO  filtro) throws SFPException {
        try {
            return new JsonArray()
                    .add(new JsonObject()
                            .put(
                                    EnumMongoDB.MATCH.getValor(), new JsonObject().put(
                                            EnumMongoDB.FOLIO.getValor(), filtro.getCondiciones().getFolio())))
                    .add(agregarProjectVistasImpresion());
        } catch (Exception e ) {
            throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineServidoresVista(FiltroPeriodosVistasInputDTO filtro) throws SFPException {
        JsonObject match = new JsonObject();
        LOGGER.info("CONDICIONES PRELIMINARES + " + filtro);

        CondicionesPeriodosVistasInputDTO condiciones = filtro.getCondiciones();

        if(condiciones.getFolio() != null) {
            match.put(EnumMongoDB.FOLIO.getValor(), condiciones.getFolio());
        }
        if(condiciones.getTipoIncumplimiento() != null) {
            match.put(EnumMongoDB.TIPO_INCUMPLIMIENTO.getValor(), condiciones.getTipoIncumplimiento());
        }
        if(condiciones.getAnio() != null) {
            match.put(EnumMongoDB.ANIO.getValor(), condiciones.getAnio());
        }
        if(condiciones.getIdEnte() != null) {
            match.put(EnumMongoDB.ID_ENTE.getValor(), condiciones.getIdEnte());
        }
        if(condiciones.getIdPeriodo() != null) {
            match.put(EnumMongoDB.ID_PERIODO.getValor(), condiciones.getIdPeriodo());
        }
//        if (!condiciones.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {
//            if (condiciones.getMes() == null) {
//                throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
//            } else {
//                match.put(EnumMongoDB.MES.getValor(), condiciones.getMes());
//            }
//        }

        JsonObject facet = new JsonObject();

        JsonArray pipelineResultados = new JsonArray();

        JsonObject fechasString = new JsonObject();
        if(filtro.getOrdenamiento() != null && filtro.getOrdenamiento().size() > 0) {
            pipelineResultados.add(crearSort(filtro.getOrdenamiento()));
        }

        pipelineResultados.add(new JsonObject().put(EnumMongoDB.SKIP.getValor(), filtro.getOffset()));
        pipelineResultados.add(new JsonObject().put(EnumMongoDB.LIMIT.getValor(), filtro.getTamanio()));

        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_LIMITE.getValor());
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_REGISTRO.getValor());
        JsonObject addFields = new JsonObject().put(EnumMongoDB.ADD_FIELDS.getValor(), fechasString);

        pipelineResultados.add(addFields);

        facet.put(EnumMongoDB.TOTAL.getValor(),
                new JsonArray().add(new JsonObject().put(EnumMongoDB.COUNT.getValor(), EnumMongoDB.TOTAL.getValor())));

        facet.put(EnumMongoDB.SERVIDORES.getValor(), pipelineResultados);

        LOGGER.info("\n \n ==== Gran arreglo queda : " + new JsonArray()
                .add(new JsonObject().put(EnumMongoDB.MATCH.getValor(), match))
                .add(new JsonObject().put(EnumMongoDB.FACET.getValor(), facet)));

        return new JsonArray()
                .add(new JsonObject().put(EnumMongoDB.MATCH.getValor(), match))
                .add(new JsonObject().put(EnumMongoDB.FACET.getValor(), facet));

    }

    /**
     * Funcion para crear el sort de la consulta
     *
     * @param sort objeto con los paramentros de ordenamiento
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private JsonObject crearSort(List<OrdenamientoInputDTO> sort) {
        JsonObject orden = new JsonObject();
        sort.forEach(cond -> orden.put(cond.getCampo().getCampo(), cond.getOrden().getCampo()));

        return new JsonObject()
                .put(EnumMongoDB.SORT.getValor(), orden);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineVistaPorFolio(FiltroPeriodosVistasInputDTO filtro)  throws SFPException {

        JsonArray pipeline = new JsonArray();

        pipeline.add(new JsonObject().put(EnumMongoDB.MATCH.getValor(),
                new JsonObject().put(EnumMongoDB.FOLIO.getValor(), filtro.getCondiciones().getFolio())));
        JsonObject fechasString = new JsonObject();
        pipeline.add(new JsonObject().put(EnumMongoDB.LIMIT.getValor(), 1));

        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_LIMITE.getValor());
        creadorQueryUtils.anadirDateToString(fechasString, EnumMongoDB.FECHA_REGISTRO.getValor());
        JsonObject addFields = new JsonObject().put(EnumMongoDB.ADD_FIELDS.getValor(), fechasString);

        pipeline.add(addFields);
        return pipeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray crearPipelineUltimoPeriodo(PeriodoInputDTO periodo) throws SFPException {
        JsonObject match = new JsonObject();
        match.put(EnumMongoDB.TIPO_DECLARACION.getValor(), periodo.getTipoDeclaracion());
        match.put(EnumMongoDB.ANIO.getValor(), periodo.getAnio());

        if(!periodo.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {
            if(periodo.getMes() == null) {
                throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
            } else {
                match.put(EnumMongoDB.MES.getValor(), periodo.getMes());
            }
        }
        return new JsonArray()
                .add(new JsonObject().put(EnumMongoDB.MATCH.getValor(), match))
                .add(new JsonObject().put(EnumMongoDB.SORT.getValor(),
                        new JsonObject().put( EnumMongoDB.FECHA_REGISTRO.getValor(), -1)))
                .add(new JsonObject(). put(EnumMongoDB.LIMIT.getValor(), 1));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject crearNuevoPeriodo(PeriodoInputDTO periodo, String version) throws SFPException {

        JsonObject nuevoPeriodo = new JsonObject();

        nuevoPeriodo.put(EnumMongoDB.TIPO_DECLARACION.getValor(), periodo.getTipoDeclaracion());
        nuevoPeriodo.put(EnumMongoDB.ANIO.getValor(), periodo.getAnio());

        if(! periodo.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {

            if(periodo.getMes() != null) {
                nuevoPeriodo.put(EnumMongoDB.MES.getValor(), periodo.getMes());
            } else {
                throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS);
            }
        }
        nuevoPeriodo.put(EnumMongoDB.ID.getValor(), definirIdNombrePeriodo(periodo, version));
        nuevoPeriodo.put(EnumMongoDB.ID_PERIODO.getValor(), definirIdNombrePeriodo(periodo, version));
        nuevoPeriodo.put(EnumMongoDB.VISTAS_GENERADAS.getValor(), EnumEstatusPeriodo.SIN_VISTAS.getValor());
        nuevoPeriodo.put(EnumMongoDB.EXTENSIONES.getValor(), new JsonArray());
        nuevoPeriodo.put(EnumMongoDB.FECHA_LIMITE.getValor(), creadorQueryUtils.crearFechaMongo(periodo.getFechaLimite()));
        nuevoPeriodo.put(EnumMongoDB.FECHA_REGISTRO.getValor(), creadorQueryUtils.crearFechaMongo(null));
        nuevoPeriodo.put(EnumMongoDB.FECHA_ACTUALIZACION.getValor(), creadorQueryUtils.crearFechaMongo(null));
        nuevoPeriodo.put(EnumMongoDB.USUARIO_CREACION.getValor(), periodo.getUsuarioRegistro().toJson());

        LOGGER.info("EL JSON PARA EL NUEVO PERIODO QUEDA :  " + nuevoPeriodo);
        return nuevoPeriodo;
    }

    private String definirIdNombrePeriodo(PeriodoInputDTO periodo, String version) {
        StringBuilder nombre = new StringBuilder();
        nombre.append(periodo.getTipoDeclaracion().getValor());
        nombre.append(periodo.getAnio().toString());
        if (!periodo.getTipoDeclaracion().equals(EnumTipoDeclaracion.MODIFICACION)) {
           nombre.append(periodo.getMes());
        }
        nombre.append(version);
        LOGGER.info("=== Nombre definido -> " + nombre);
        return nombre.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject actualizarFechaPeriodo(PeriodoInputDTO periodo) throws SFPException {
        return new JsonObject().put(EnumMongoDB.SET.getValor(), new JsonObject()
                .put(EnumMongoDB.FECHA_LIMITE.getValor(), creadorQueryUtils.crearFechaMongo(periodo.getFechaLimite()))
                .put(EnumMongoDB.FECHA_ACTUALIZACION.getValor(), creadorQueryUtils.crearFechaMongo(null))

        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FiltroOmextInputDTO cambiarTipoFiltro(FiltroPeriodosVistasInputDTO filtroVistas, PeriodoDTO periodo) {
        FiltroOmextInputDTO filtroCambiado = new FiltroOmextInputDTO();

        CondicionesOmextInputDTO condicionesOmext  =new CondicionesOmextInputDTO();
        condicionesOmext.setTipoDeclaracion(filtroVistas.getCondiciones().getTipoDeclaracion());

        if(!periodo.getVistasGeneradas().equals(EnumEstatusPeriodo.SIN_VISTAS)) {
            condicionesOmext.setEnVista(true);
            condicionesOmext.setIdPeriodo(periodo.getIdPeriodo());
        }
        if(periodo.getMes() != null) {
            List<EnumMes> mes = new ArrayList<EnumMes>();
            mes.add(periodo.getMes());
            condicionesOmext.setMes(mes);
        }
        condicionesOmext.setEnVista(false);
        condicionesOmext.setAnio(periodo.getAnio());

        filtroCambiado.setCumplimiento(EnumCumplimiento.OBLIGADO);
        filtroCambiado.setCollName(filtroVistas.getCollName());
        filtroCambiado.setCondiciones(condicionesOmext);

        return filtroCambiado;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Observable<VistaDTO> obtenerVistasNuevasOmisos(String remitente, PeriodoDTO periodo,
                                                          FirmanteDTO datosFirma,
                                                          HashMap<String, JsonObject> entes,
                                                          TextoOficioDTO textoOficio,
                                                          AtomicReference<Integer> posicionFolio,
                                                          UsuarioInputDTO usuarioRegistro) {
        List<VistaDTO> vistasNuevas = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        Integer anio = cal.get(Calendar.YEAR);
        Integer mesInt = cal.get(Calendar.MONTH) + 1;
        String mes = (mesInt >=10) ? mesInt.toString() : "0" + mesInt.toString();

        entes.values().forEach(e -> {
            VistaDTO nuevaVista = new VistaDTO();
            //Datos del ente

            String idEnte = e.getString(EnumGraphQL.ID_ENTE.getValor());

            nuevaVista.setUsuarioRegistro(usuarioRegistro);
            nuevaVista.setIdEnte(idEnte);
            nuevaVista.setVistaGenerada(EnumEstatusVista.EN_PROCESO);
            EnteVistaDTO enteVista = new EnteVistaDTO();
            enteVista.setIdEnte(idEnte);
            enteVista.setNombreEnte(e.getString(EnumGraphQL.NOMBRE_ENTE.getValor()));
            enteVista.setNombreCorto(e.getString(EnumGraphQL.NOMBRE_CORTO.getValor()));
            enteVista.setUr(e.getString(EnumGraphQL.UR.getValor()));
            enteVista.setRamo(e.getInteger(EnumGraphQL.RAMO.getValor()));
            nuevaVista.setEnte(enteVista);

            // Setear fechas limites y extensiones a las vistas
            if(periodo.getExtensionesMapa().containsKey(idEnte)) {
                nuevaVista.setFechaLimite(periodo.getExtensionesMapa().get(idEnte));
            } else {
                nuevaVista.setFechaLimite(periodo.getFechaLimite());
            }

            //Datos del periodo
            nuevaVista.setIdPeriodo(periodo.getIdPeriodo());
            nuevaVista.setAnioDeclaracion(periodo.getAnio());
            nuevaVista.setMesDeclaracion(periodo.getMes());
            nuevaVista.setTipoDeclaracion(periodo.getTipoDeclaracion());

            //Id texto
            nuevaVista.setIdTextoOficio(textoOficio.getIdTextoOficio());

            StringBuilder folio = new StringBuilder(cadenaFolioVistas);
            folio.append(EnumTipoIncumplimiento.OMISO)
                    .append("/")
                    .append(posicionFolio.get())
                    .append("/")
                    .append(elAnio);

            // Datos para el firmado
            nuevaVista.setNumOficio(posicionFolio.get());
            nuevaVista.setRemitente(remitente);
            nuevaVista.setAnio(anio);
            nuevaVista.setMes(mes);

            //Vista
            nuevaVista.setFolio(folio.toString());
            nuevaVista.setTipoIncumplimiento(EnumTipoIncumplimiento.OMISO);

            //Datos de la base datos fiel
            FirmanteDTO firmante = new FirmanteDTO();
            firmante.setCliente_id(datosFirma.getCliente_id());
            firmante.setServicio(datosFirma.getServicio());
            firmante.setSecret_key(datosFirma.getSecret_key());
            nuevaVista.setFirmante(firmante);

            vistasNuevas.add(nuevaVista);
            posicionFolio.set(posicionFolio.get() +1 );
        });

        return Observable.fromIterable(vistasNuevas);
    }

    private JsonObject agregarProjectVistasImpresion() {
       return new JsonObject()
            .put(EnumMongoDB.PROJECT.getValor(), new JsonObject()
                .put(EnumMongoDB.ID.getValor(), 0)
                .put(EnumMongoDB.IMPRESION_NOMBRE.getValor(), EnumMongoDB.NOMBRE_COMPLETO_$.getValor())
                .put(EnumMongoDB.IMPRESION_CURP.getValor(), 1)
                .put(EnumMongoDB.IMPRESION_UA.getValor(), EnumMongoDB.UNIDAD_ADMINISTRATIVA_$.getValor())
                .put(EnumMongoDB.IMPRESION_EMPLEO.getValor(), EnumMongoDB.EMPLEO_$.getValor()));
    }

    /**
     * Regresa parte del pipeline del $facet que traera los periodos con fechas limites vencidas
     * @return
     */
    private JsonArray agregarJsonArrayVencFechaLimite() {
        String fechaAhora = Instant.now().toString();

        return new JsonArray("[\n" +
                "    {\n" +
                "      \"$match\" :     {\n" +
                "        \"$or\" :[\n" +
                "            {\n" +
                "              \"extensiones\" : {\n" +
                "              \"$elemMatch\" : {\n" +
                "                \"fechaLimite\" : \n" +
                "                {\n" +
                "                  \"$gt\": {\"$date\": \"" + fechaAhora + "\"}\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "          ,\n" +
                "          {\n" +
                "            \"fechaLimite\" : {\n" +
                "              \"$gt\": {\"$date\": \"" + fechaAhora + "\"}\n" +
                "            }\n" +
                "          }\n" +
                "          ]\n" +
                "        }\n" +
                "    }, \n" +
                "    {\n" +
                "      \"$project\" : {\n" +
                "          \n" +
                "          \"idPeriodo\":1\n" +
                "        }}\n" +
                "  ]");

    }

    /**
     * Agrega map para dar formato en fechas de extension
     *
     *
     * @param addFields
     */
    private void agregarFieldInstExt(JsonObject addFields) {
        addFields.put("extensiones", new JsonObject("{\n" +
                "            \"$map\" : {\"input\": \"$extensiones\",\n" +
                "                    \"as\": \"item\",\n" +
                "                    \"in\": {\n" +
                "                \"idEnte\": \"$$item.idEnte\",\n" +
                "                        \"fechaLimite\": {\n" +
                "                    \"$dateToString\": { \"format\": \"%Y-%m-%d\", \"date\": \"$$item.fechaLimite\" }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "    }"));
    }

}
