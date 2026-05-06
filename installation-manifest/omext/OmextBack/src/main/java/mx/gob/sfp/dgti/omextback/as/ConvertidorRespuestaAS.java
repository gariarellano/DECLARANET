/**
 * @(#)ConvertidorRespuestaAS.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCumplimiento;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumEstatusVista;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Interface para el servicio ConvertidorRespuestaAS
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public interface ConvertidorRespuestaAS {

    /**
     * Funcion que recibe la respuesta de la consulta de ELastic y la devuelve como la necesitamos
     *
     * @param respuesta objeto al que se le agrega la respuesta
     * @param tipoCumplimiento tipo de cumplimiento
     *
     * @return servidores extraidos en un arreglo
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    public JsonObject extraerRespServ(JsonObject respuesta, EnumCumplimiento tipoCumplimiento);

    /**
     *
     * @param conteos
     * @param ext
     * @param cump
     */
    public void extraerConteosPorEntesCumplExt(JsonArray conteos,
                                               HashMap<String, Integer> ext, HashMap<String, Integer> cump);

    /**
     * Funcion que recibe conteos por institucion y por periodo
     *
     * @param entesBase
     * @param cumplio
     * @param pendientes
     * @param ext
     * @param entesQuery
     * @param extensiones
     *
     * @return JsonArray con los conteos por institucion
     *
     * @author pavel.martinez
     * @since 18/02/2021
     */
    public JsonArray formatearConteoPorEntesPeriodo(JsonArray entesBase, HashMap<String, Integer> cumplio,
                                                    HashMap<String, Integer> pendientes, HashMap<String, Integer> ext,
                                                    List<String> entesQuery, HashMap<String, String> extensiones,
                                                    String fechaLimiteGeneral, HashMap<String, EnumEstatusVista> estVistaOmisos,
                                                    HashMap<String, EnumEstatusVista> estVistaExt);

    /**
     * Funcion que recibe la respuesta obtenida directamente de ElasticSearch a otro formato para utilizarlo
     * posteriormente
     *
     * @param conteos respuesta obtenida del conteo
     *
     * @return un HashMap de idEnte-conteoDelCumplimiento que se utilizara despues
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    HashMap<String, Integer> extraerConteosPorEntes(JsonArray conteos);

    /**
     * Funcion que recibe la respuesta obtenida directamente de ElasticSearch a otro formato para utilizarlo
     * posteriormente
     *
     * @param conteos respuesta obtenida del conteo
     * @param mapaOrdenado TreeMap donde se van agregando y al mismo tiempo ordenado los nombres de las unidades
     *                     responsables encontradas nombreUa-claveUa
     *
     * @return un HashMap de claveUa-conteoDelCumplimiento que se utilizara despues
     */
    HashMap<String, Integer> extraerConteosPorUA(JsonArray conteos, TreeMap<String, String> mapaOrdenado);

    /**
     * Funcion que utiliza la informacion obtenida y entrega un formato final de conteo por entes
     *
     * @param entesBase lista de entes que se mostraran, traidos del servicio de catalogo
     * @param cumplio HashMap con la relacion idEnte-conteoCumplimiento de servidores que cumplieron
     * @param pendientes HashMap con la relacion idEnte-conteoCumplimiento de servidores pedientes
     * @param entesQuery el query de entes
     *
     * @return arreglo final de instituciones con los conteos de cumpliemieto
     *
     * @author pavel.martinez
     * @since 19/05/2020
     */
    JsonArray formatearConteoPorEntes(JsonArray entesBase, HashMap<String, Integer> cumplio,
                                             HashMap<String, Integer> pendientes, HashMap<String, Integer> ext,
                                      List<String> entesQuery);

    /**
     * Funcion que utiliza la informacion obtenida y entrega un formato final de conteo por unidad administrativa
     *
     * @param mapaOrdenados TreeMap donde se agregaron los nombres de ua encontradas nombreUa-claveUa
     * @param cumplio HashMap con la relacion idEnte-conteoCumplimiento de servidores que cumplieron
     * @param pendientes HashMap con la relacion idEnte-conteoCumplimiento de servidores pedientes
     * @param idEnte id del ente al que pertenecen las unidades administrativas
     * @param ur ur al que pertenece la institucion
     *
     * @return arreglo final de unidades administrativas con los conteos de cumpliemieto
     */
    JsonArray formatearConteoPorUA(TreeMap<String, String> mapaOrdenados, HashMap<String, Integer> cumplio,
                                          HashMap<String, Integer> pendientes, HashMap<String, Integer> ext,
                                   String idEnte, String ur);


    /**
     *
     * @param respuesta
     * @param cumplio
     * @param extemporaneo
     * @param pendiente
     */
    public void extraerPreConteosInicio(List<JsonObject> respuesta, HashMap<String, Integer> cumplio,
                                        HashMap<String, Integer> extemporaneo, HashMap<String, Integer> pendiente);

    /**
     * sad
     * @param respuesta
     *
     * @author pavel.martinez
     * @since 24/05/2021
     */
    public void formatearConteoPorGrupos(JsonObject respuesta);
}
