/**
 * @EstructuraPdnServiceImpl.java Mar 23, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.text.SimpleDateFormat;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import mx.gob.sfp.dgti.enums.CamposPDN;
import mx.gob.sfp.dgti.enums.Encabezados;
import mx.gob.sfp.dgti.enums.TipoDeclaracion;
import static mx.gob.sfp.dgti.util.Constantes.STRING_FORMAT_TIMEZONE;
import mx.gob.sfp.dgti.utils.FechaUtil;

/**
 * Clase que define el formato para la PDN
 *
 * @author Miriam Sanchez Sanchez programador07
 * @modifiedBy programador09
 * @since Mar 23, 2020
 */
public class EstructuraPdnServiceImpl implements EstructuraPdnService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstructuraPdnServiceImpl.class);
    private final SituacionPatrimonialService situacionPatrimonialService;
    private final InteresService interesService;

    /**
     *
     */
    public EstructuraPdnServiceImpl() {
        situacionPatrimonialService = new SituacionPatrimonialService();
        interesService = new InteresService();
    }

    /**
     * Se crea la estructura de Result
     *
     * @return {@link JsonObject}
     */
    @Override
    public Future<JsonObject> crearEstructuraDeclaracionPDN(JsonObject original) {
        return Future.future(future->{
            try {
                if (EnumTipoDeclaracion.AVISO.name().equals(original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                        .getJsonObject(EnumGeneral.ENCABEZADO.getCampo()).getString(Encabezados.TIPO_DECLARACION.getCampo()))) {

                    future.complete(
                            new JsonObject()
                            .put("_id", original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                    .getJsonObject(Encabezados.ENCABEZADO.getCampo()).getString(Encabezados.NUM_DEC.getCampo()))
                            .put(CamposPDN.ID.getCampo(), original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                    .getJsonObject(Encabezados.ENCABEZADO.getCampo()).getString(Encabezados.NUM_DEC.getCampo()))
                            .put(CamposPDN.DECLARACION.getCampo(), original.getJsonObject(CamposPDN.DECLARACION.getCampo())));

                } else {
                    JsonObject declaracionEstructuradaPdn = new JsonObject();
                    JsonObject metadata = new JsonObject();

                    metadata.put(CamposPDN.FECHA_ACT.getCampo(),
                            new SimpleDateFormat(STRING_FORMAT_TIMEZONE).format(FechaUtil.getFechaFormatoISO8601(                                    
                                    original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                            .getJsonObject(EnumGeneral.ENCABEZADO.getCampo()).getString(Encabezados.FECHA_ACTUALIZACION.getCampo())
                                        )
                            )
                    );
                    metadata.put(CamposPDN.INSTITUCION.getCampo(),
                            original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                            .getJsonObject(Encabezados.INSTITUCION_RECEPTORA.getCampo()).getString(Encabezados.NOMBRE.getCampo()));
                    metadata.put(CamposPDN.TIPO.getCampo(),
                            TipoDeclaracion.valueOf(original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                    .getJsonObject(EnumGeneral.ENCABEZADO.getCampo()).getString(Encabezados.TIPO_DECLARACION.getCampo())).getClave());
                    metadata.put(CamposPDN.TIPO_ENUM.getCampo(),
                            TipoDeclaracion.valueOf(original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                    .getJsonObject(EnumGeneral.ENCABEZADO.getCampo()).getString(Encabezados.TIPO_DECLARACION.getCampo())).getClavePdn());
                    metadata.put(CamposPDN.TIPO_FORMATO.getCampo(), original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                    .getJsonObject(EnumGeneral.ENCABEZADO.getCampo()).getString(Encabezados.TIPO_FORMATO.getCampo()));
                    metadata.put(CamposPDN.DECLARACION_COMPLETA.getCampo(), original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                    .getJsonObject(EnumGeneral.ENCABEZADO.getCampo()).getString(Encabezados.TIPO_FORMATO.getCampo()).equals("COMPLETO"));

                    declaracionEstructuradaPdn.put(CamposPDN.ID.getCampo(),
                            original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                            .getJsonObject(Encabezados.ENCABEZADO.getCampo())
                            .getString(Encabezados.NUM_DEC.getCampo()));

                    declaracionEstructuradaPdn.put(CamposPDN.METADATA.getCampo(), metadata);

                    declaracionEstructuradaPdn.put(Encabezados.RECEPCION_WEB.getCampo(), original.getJsonObject(Encabezados.RECEPCION_WEB.getCampo()));

                    future.complete(
                            new JsonObject().put("_id",original.getJsonObject(CamposPDN.DECLARACION.getCampo())
                                                                .getJsonObject(Encabezados.ENCABEZADO.getCampo())
                                                                .getString(Encabezados.NUM_DEC.getCampo()))
                                    .put(CamposPDN.DECLARACION.getCampo(),
                                    obtenerDeclaracion(original.getJsonObject(CamposPDN.DECLARACION.getCampo()), declaracionEstructuradaPdn)));
                }
            } catch (Exception e) {
                LOGGER.info("===Error con ==== " + original);
                LOGGER.fatal("AQUII", e);
                future.fail("Hubo errores al guardar ");
            }
        });

    }

    /**
     * Obtener declaracion con la estructura de pdn
     *
     * @param original
     * @param declaracionEstructurada
     * @return {@link JsonObject}
     */
    public JsonObject obtenerDeclaracion(JsonObject original, JsonObject declaracionEstructurada) {
        JsonObject encabezado = original.getJsonObject(EnumGeneral.ENCABEZADO.getCampo());

        boolean formato = EnumTipoFormatoDeclaracion.COMPLETO.name()
                .equals(encabezado.getString(CamposPDN.FORMATO.getCampo()));

        JsonObject declaracion = new JsonObject();

        declaracion.put(CamposPDN.TIPO_FORMATO.getCampo(), declaracionEstructurada.getJsonObject(CamposPDN.METADATA.getCampo()).getString(CamposPDN.TIPO_FORMATO.getCampo()));
        
        declaracion.put(CamposPDN.SITUACION_PATRIMONIAL.getCampo(),
                situacionPatrimonialService.crearEstructuraSituacionP(
                        original, encabezado.getString(Encabezados.TIPO_DECLARACION.getCampo()), formato));

        if (formato) {
            declaracion.put(CamposPDN.INTERESES.getCampo(), interesService.crearEstructuraIntereses(original));
        }
        return declaracionEstructurada.put(CamposPDN.DECLARACION.getCampo(), declaracion);
    }
}