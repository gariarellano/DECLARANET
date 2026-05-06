/**
 * @(#)ConvertidorRespuestaASImpl.java 19/05/2020
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
import mx.gob.sfp.dgti.omextback.as.ConvertidorRespuestaAS;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.util.*;

/**
 * Interface para el servicio ConvertidorRespuestaASImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class ConvertidorRespuestaASImpl implements ConvertidorRespuestaAS {

    /**
     * Logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(ConvertidorRespuestaASImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject extraerRespServ(JsonObject respuesta, EnumCumplimiento tipoCumplimiento) {
        JsonArray listaResultados = respuesta.getJsonArray(EnumMongoDB.RESULTADOS.getValor());
        Integer total = (respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor()).size() > 0) ?
                respuesta.getJsonArray(EnumMongoDB.TOTAL.getValor()).getJsonObject(0).getInteger(EnumMongoDB.TOTAL.getValor()):
                0;
        try {
            if(listaResultados.size() != 0) {
                JsonArray listaFormateada = new JsonArray();

                if (listaResultados.size() > 0) {

                    if (tipoCumplimiento.equals(EnumCumplimiento.PENDIENTE)) {
                        listaResultados.forEach(obj -> {
                            JsonObject servidor = (JsonObject) obj;
                            listaFormateada.add(new JsonObject()
                                            .put(EnumGraphQL.ACTIVO.getValor(), servidor.getInteger(EnumGraphQL.ACTIVO.getValor()))
                                            .put(EnumGraphQL.FECHA_REGISTRO.getValor(), servidor.getString(EnumGraphQL.FECHA_REGISTRO.getValor()))
                                            .put(EnumGraphQL.DATOS_RUSP.getValor(), servidor)
                                            .put(EnumGraphQL.CUMPLIMIENTO.getValor(), tipoCumplimiento)
                            );
                        });
                    } else {
                        listaResultados.forEach(obj -> {
                            JsonObject s = (JsonObject) obj;
                            listaFormateada
                                    .add(s
                                            .put(EnumGraphQL.EXTEMPORANEO.getValor(), s.getBoolean(EnumGraphQL.EXTEMPORANEO.getValor()))
                                            // Se agregara directo en base
                                            .put(EnumGraphQL.CUMPLIMIENTO.getValor(), s.getJsonObject(
                                                    EnumGraphQL.DATOS_RUSP.getValor()).getString(EnumGraphQL.CUMPLIMIENTO_DESC.getValor()))
                                            // .put(EnumGraphQL.CUMPLIMIENTO.getValor(), tipoCumplimiento)
                                    );
                        });
                    }

                }
                return new JsonObject()
                        .put(EnumMongoDB.RESULTADOS.getValor() , listaFormateada)
                        .put(EnumMongoDB.TOTAL.getValor(), total);
            }
        } catch (Exception e) {
            LOGGER.info("==== Ocurrio algo, se devolvio informacion vacia: " + e);
        }
        return new JsonObject()
                .put(EnumMongoDB.RESULTADOS.getValor(), new JsonArray())
                .put(EnumMongoDB.TOTAL.getValor(), total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Integer> extraerConteosPorEntes(JsonArray conteos) {
        HashMap<String, Integer> mapa = new HashMap<>();
        try {

            conteos.forEach(obj -> {
                JsonObject jo = (JsonObject) obj;
                mapa.put(
                        jo.getString(EnumMongoDB.ID.getValor()),
                        jo.getInteger(EnumMongoDB.TOTAL.getValor()));
            });
        } catch (Exception e) {
            LOGGER.info("=== Error en extracción : " + e);
        }
        return mapa;
    }

    @Override
    public void extraerPreConteosInicio(List<JsonObject> respuesta, HashMap<String, Integer> cumplio,
                                        HashMap<String, Integer> extemporaneo, HashMap<String, Integer> pendiente) {
        respuesta.forEach(jo-> {
            if(jo.getInteger("CUMPLIO") != null) {
                cumplio.put(
                        jo.getString(EnumMongoDB.ID_ENTE.getValor()),
                        jo.getInteger("CUMPLIO"));

            }
            if(jo.getInteger("EXTEMPORANEO") != null) {
                extemporaneo.put(
                        jo.getString(EnumMongoDB.ID_ENTE.getValor()),
                        jo.getInteger("EXTEMPORANEO"));

            }
            if(jo.getInteger("PENDIENTE") != null) {
                pendiente.put(
                        jo.getString(EnumMongoDB.ID_ENTE.getValor()),
                        jo.getInteger("PENDIENTE"));
            }
        });
    }

    /**
     *
     * @param ext
     * @param cump
     *
     * Ejemplo de json:
     *
     * {
     *     "_id" : "5c892fec7eefe633e42cc985",
     *     "cumplimiento" : [
     *         {
     *             "total" : 7.0
     *         },
     *         {
     *             "tipo" : true,
     *             "total" : 7.0
     *         }
     *     ]
     * }
     */
    @Override
    public void extraerConteosPorEntesCumplExt(JsonArray conteos,
                                               HashMap<String, Integer> ext, HashMap<String, Integer> cump) {
        try {
            conteos.forEach(obj -> {
                JsonObject jo = (JsonObject) obj;
                int noExt = 0;

                jo.getJsonArray("cumplimiento").forEach(
                        conteo -> {
                            JsonObject conteoJson = (JsonObject) conteo;
                            if (conteoJson.getBoolean("ext") != null && conteoJson.getBoolean("ext").equals(Boolean.TRUE)) {
                                ext.put(jo.getString("_id"), conteoJson.getInteger("total"));
                            } else {
                                cump.put(jo.getString("_id"), (noExt + conteoJson.getInteger("total")));
                            }
                        }
                );
            });
        } catch (Exception e) {
            LOGGER.info("=== En extracción error: " + e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Integer> extraerConteosPorUA(JsonArray conteos, TreeMap<String, String> mapaOrdenado) {
        HashMap<String, Integer> mapa = new HashMap<>();
        try {

            conteos.forEach(obj -> {
                JsonObject jo = (JsonObject) obj;

                mapa.put(jo.getJsonObject(EnumMongoDB.ID.getValor()).getString(EnumMongoDB.CLAVE_UA.getValor()),
                        jo.getInteger(EnumMongoDB.TOTAL.getValor()));

                mapaOrdenado.put(jo.getJsonObject(EnumMongoDB.ID.getValor()).getString(EnumMongoDB.UNIDAD_ADMIN.getValor()),
                        jo.getJsonObject(EnumMongoDB.ID.getValor()).getString(EnumMongoDB.CLAVE_UA.getValor()));
            });
        } catch (Exception e) {
            LOGGER.info("En extracción : " + e);
        }
        return mapa;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void formatearConteoPorGrupos(JsonObject respuesta) {

        JsonArray resultadosFormateados = new JsonArray();
        if(respuesta.getInteger(EnumGraphQL.TOTAL.getValor()) > 0) {

            JsonArray resultado = respuesta.getJsonArray(EnumGraphQL.RESULTADO.getValor());
            resultado.forEach(obj -> {
                JsonObject jo = (JsonObject) obj;
                Integer cumplioN = jo.getInteger("cumplio") != null ? jo.getInteger("cumplio") : 0;
                Integer pendienteN = jo.getInteger("pendiente") != null ? jo.getInteger("pendiente") : 0;
                Integer extnN = jo.getInteger("extemporaneo") != null ? jo.getInteger("extemporaneo") : 0;
                Integer obligadoN = cumplioN + pendienteN + extnN;
                Float porcentaje = (obligadoN > 0) ?
                        (100 / obligadoN.floatValue()) * cumplioN.floatValue() :
                        0;
                resultadosFormateados.add(new JsonObject()
                        .put("grupo", jo.getInteger("grupo"))
                        .put("nombreGrupo", jo.getString("nombreGrupo"))
                        .put(EnumGraphQL.CUMPLIO.getValor(), cumplioN)
                        .put(EnumGraphQL.PENDIENTE.getValor(), pendienteN)
                        .put(EnumGraphQL.EXTEMPORANEO.getValor(), extnN)
                        .put(EnumGraphQL.OBLIGADO.getValor(), obligadoN)
                        .put(EnumGraphQL.PORCENTAJE.getValor(), porcentaje)
                );
            });
        }
        respuesta.put(EnumGraphQL.RESULTADO.getValor(), resultadosFormateados);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray formatearConteoPorEntes(JsonArray entesBase, HashMap<String, Integer> cumplio,
                                             HashMap<String, Integer> pendientes, HashMap<String, Integer> ext,
                                             List<String> entesQuery) {
        JsonArray resultado = new JsonArray();

        if(entesQuery == null) {
            entesQuery = new ArrayList<>();
        }
        if(entesQuery.isEmpty()) {

            entesBase.forEach(ente -> {
                JsonObject e = (JsonObject) ente;
                try {
                    agregarInstitucion(cumplio, pendientes, ext, e, resultado);
                } catch (Exception ex) {
                    LOGGER.info("=== Al agregar institución: " + ex);
                }
            });
        } else {
            Set<String> setEntes = new HashSet<>(entesQuery);
            entesBase.forEach(ente -> {
                JsonObject e = (JsonObject) ente;
                if(setEntes.contains(e.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()))) {

                    try {
                        agregarInstitucion(cumplio, pendientes, ext, e, resultado);
                    } catch (Exception ex) {
                        LOGGER.info("=== Hubo un error: " + ex);
                    }

                }
            });
        }
        return resultado;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray formatearConteoPorEntesPeriodo(JsonArray entesBase, HashMap<String, Integer> cumplio,
                                             HashMap<String, Integer> pendientes, HashMap<String, Integer> ext,
                                             List<String> entesQuery, HashMap<String, String> extensiones,
                                             String fechaLimiteGeneral, HashMap<String, EnumEstatusVista> estVistaOmisos,
                                             HashMap<String, EnumEstatusVista> estVistaExt) {
        JsonArray resultado = new JsonArray();

        LOGGER.info("==== En formater conteos por entes - entesBase " + entesBase);
        LOGGER.info("==== En formater conteos por entes - cumplio " + cumplio);
        LOGGER.info("==== En formater conteos por entes - pendientes " + pendientes);
        LOGGER.info("==== En formater conteos por entes - ext " + ext);
        LOGGER.info("==== En formater conteos por entes - entesQuery " + entesQuery);
        LOGGER.info("==== En formater conteos por entes - extensiones " + extensiones);


        if(entesQuery == null) {
            entesQuery = new ArrayList<>();
        }
        if(entesQuery.isEmpty()) {

            entesBase.forEach(ente -> {
                JsonObject e = (JsonObject) ente;
                try {
                    Integer cumplioN = cumplio.getOrDefault(e.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()), 0);
                    Integer pendienteN = pendientes.getOrDefault(e.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()), 0);
                    Integer extnN = ext.getOrDefault(e.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()), 0);
                    Integer obligadoN = cumplioN + pendienteN + extnN;
                    String idEnte = e.getString(EnumGraphQL.ID_ENTE.getValor());

                    String extPeriodo = extensiones.getOrDefault(e.getString(EnumGraphQL.ID_ENTE.getValor()), fechaLimiteGeneral);

                    EnumEstatusVista estVistaO = estVistaOmisos.containsKey(idEnte) ? estVistaOmisos.get(idEnte) : EnumEstatusVista.NO_GENERADA;
                    EnumEstatusVista estVistaE = estVistaExt.containsKey(idEnte) ? estVistaExt.get(idEnte) : EnumEstatusVista.NO_GENERADA;

                    if(obligadoN > 0 ) {

                        resultado.add(new JsonObject()
                                .put(EnumGraphQL.ID_ENTE.getValor(), idEnte)
                                .put(EnumGraphQL.NOMBRE_ENTE.getValor(), e.getString(EnumGraphQL.NOMBRE_ENTE.getValor()))
                                .put(EnumGraphQL.OBLIGADO.getValor(), obligadoN)
                                .put(EnumGraphQL.CUMPLIO.getValor(), cumplioN)
                                .put(EnumGraphQL.PENDIENTE.getValor(), pendienteN)
                                .put(EnumGraphQL.EXTEMPORANEO.getValor(),extnN)
                                .put(EnumGraphQL.EXT_PERIODO.getValor(), extPeriodo)
                                .put(EnumGraphQL.UR.getValor(), e.getString(EnumGraphQL.UR.getValor()))
                                .put(EnumGraphQL.RAMO.getValor(), e.getInteger(EnumGraphQL.RAMO.getValor()))
                                .put(EnumGraphQL.VISTA_O_GENERADA.getValor(), estVistaO)
                                .put(EnumGraphQL.VISTA_E_GENERADA.getValor(), estVistaE)

                                .put(EnumGraphQL.NOMBRE_CORTO.getValor(), e.getString(EnumGraphQL.NOMBRE_CORTO.getValor())));

                    }
                } catch (Exception ex) {
                    LOGGER.info("=== Al agregar institución: " + ex);
                }
            });
        } else {
            Set<String> setEntes = new HashSet<>(entesQuery);
            entesBase.forEach(ente -> {
                JsonObject e = (JsonObject) ente;
                if(setEntes.contains(e.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()))) {

                        try {
                            agregarInstitucion(cumplio, pendientes, ext, e, resultado);
                        } catch (Exception ex) {
                            LOGGER.info("=== Hubo un error: " + ex);
                        }

                }
            });
        }
        LOGGER.info("=== El resultado despues de convertir la respuesta + " + resultado);

        return resultado;
    }

    /**
     * Funcion para agregar una institucion con los valores totales de cumplimiento
     *
     * @param cumplio HashMap idEnte-conteo de personas que cumplieron
     * @param pendientes HashMap idEnte-conteo de personas pendientes del cumplimiento
     * @param ente Objeto con los informacion de la institucion
     * @param resultado Objeto resultado de los datos al que se le va agregando la informacion
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private void agregarInstitucion(HashMap<String, Integer> cumplio, HashMap<String, Integer> pendientes, HashMap<String,
            Integer> ext, JsonObject ente, JsonArray resultado ) {
        Integer cumplioN = cumplio.getOrDefault(ente.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()), 0);
        Integer pendienteN = pendientes.getOrDefault(ente.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()), 0);
        Integer extnN = ext.getOrDefault(ente.getString(EnumRespuestaGraphQL.ID_ENTE.getValor()), 0);
        Integer obligadoN = cumplioN + pendienteN + extnN;
        Float porcentaje = (obligadoN > 0) ?
                (100 / obligadoN.floatValue()) * cumplioN.floatValue() :
                0;

        resultado.add(new JsonObject()
                .put(EnumGraphQL.ID_ENTE.getValor(), ente.getString(EnumGraphQL.ID_ENTE.getValor()))
                .put(EnumGraphQL.NOMBRE_ENTE.getValor(), ente.getString(EnumGraphQL.NOMBRE_ENTE.getValor()))
                .put(EnumGraphQL.CUMPLIO.getValor(), cumplioN)
                .put(EnumGraphQL.PENDIENTE.getValor(), pendienteN)
                .put(EnumGraphQL.EXTEMPORANEO.getValor(), extnN)
                .put(EnumGraphQL.OBLIGADO.getValor(), obligadoN)
                .put(EnumGraphQL.SITUACION.getValor(), EnumSituacion.NORMAL)
                .put(EnumGraphQL.PORCENTAJE.getValor(), porcentaje)
                .put(EnumGraphQL.UR.getValor(), ente.getString(EnumGraphQL.UR.getValor()))
                .put(EnumGraphQL.RAMO.getValor(), ente.getInteger(EnumGraphQL.RAMO.getValor()))
                .put(EnumGraphQL.NOMBRE_CORTO.getValor(), ente.getString(EnumGraphQL.NOMBRE_CORTO.getValor()))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonArray formatearConteoPorUA(TreeMap<String, String> mapaOrdenados, HashMap<String, Integer> cumplio,
                                              HashMap<String, Integer> pendientes, HashMap<String, Integer> ext,
                                          String idEnte, String ur) {
        JsonArray resultado = new JsonArray();

        mapaOrdenados.entrySet().forEach(ua -> {
                    try {
                        agregarUA(ua, cumplio, pendientes, ext, idEnte, ur, resultado);
                    } catch (Exception ex) {
                        LOGGER.info("=== Error al formatear conteo ua : " + ex);
                    }
                }
        );

        return resultado;
    }

    /**
     * Funcion para agregar una unidad administrativa con los valores totales de cumplimiento
     *
     * @param ordenado Map nombreInstitucion-idEnte
     * @param cumplio HashMap idEnte-conteo de personas que cumplieron
     * @param pendientes HashMap idEnte-conteo de personas pendientes del cumplimiento
     * @param idEnte idEnte de la institucion
     * @param ur unidad responsable
     * @param resultado Objeto resultado de los datos al que se le va agregando la informacion
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    private void agregarUA(Map.Entry<String, String> ordenado, HashMap<String, Integer> cumplio, HashMap<String,
        Integer> pendientes, HashMap<String, Integer> ext, String idEnte, String ur, JsonArray resultado ) {
        Integer cumplioN = cumplio.getOrDefault(ordenado.getValue(), 0);
        Integer pendienteN = pendientes.getOrDefault(ordenado.getValue(), 0);
        Integer extnN = ext.getOrDefault(ordenado.getValue(), 0);
        Integer obligadoN = cumplioN + pendienteN + extnN;
        Float porcentaje = (obligadoN > 0) ?
                (100 / obligadoN.floatValue()) * cumplioN.floatValue() :
                0;

        resultado.add(new JsonObject()
                .put(EnumGraphQL.CLAVE_UA.getValor(), ordenado.getValue())
                .put(EnumGraphQL.UNIDAD_ADMIN.getValor(), ordenado.getKey())
                .put(EnumGraphQL.CUMPLIO.getValor(), cumplioN)
                .put(EnumGraphQL.PENDIENTE.getValor(), pendienteN)
                .put(EnumGraphQL.EXTEMPORANEO.getValor(), extnN)
                .put(EnumGraphQL.OBLIGADO.getValor(), obligadoN)
                .put(EnumGraphQL.PORCENTAJE.getValor(), porcentaje)
                .put(EnumGraphQL.UR.getValor(), ur)
                .put(EnumGraphQL.ID_ENTE.getValor(), idEnte)
        );
    }

}
