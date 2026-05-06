/**
 * @(#)CreadorQueryASImpl.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.as;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.omextback.dto.input.CondicionesOmextInputDTO;
import mx.gob.sfp.dgti.omextback.exception.SFPException;
import mx.gob.sfp.dgti.omextback.util.constantes.*;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Interface para el servicio CreadorQueryASImpl encargado de crear los queries para consultas de Elastic Search y
 * GraphQL
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public class CreadorQueryUtils {

    /**
     * Logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(CreadorQueryUtils.class);

    /**
     * Expresion regular para CURP
     */
    public static final String REGEX_CURP =
            "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$";

    /**
     * Pattern para curps
     */
    public static Pattern curpPattern = Pattern.compile(REGEX_CURP);

    /**
     * Pattern para identificar que una cadena tiene numeros
     */
    public static Pattern conNumerosPattern = Pattern.compile("[0-9]");

    /**
     * Funcion para crear un objeto de match para buscar por nombres y apellidos
     *
     * @param nombreCompleto string que debe de coincidir
     * @return el JsonObject
     * @author pavel.martinez
     * @since 25/07/2020
     */
    private void crearTextNombreCompleto(String nombreCompleto, String prefijo, JsonArray condicionesAnd) throws
            SFPException {

        List<String> palabras = crearWildCardDinamico(nombreCompleto);
        if (!palabras.isEmpty()) {
            palabras.forEach(palabra ->
                    condicionesAnd.add(
                            new JsonObject()
                                    .put(prefijo.concat(EnumMongoDB.NOMBRE_COMPLETO.getValor()), new JsonObject()
                                            .put(EnumMongoDB.REGEX.getValor(), palabra))));
        } else {
            throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS, EnumDetalleParamInc.PARAMETROS_NO_VALIDOS.getValor());
        }
    }

    private List<String> crearWildCardDinamico(String nombreCompleto) {
        String normalizado = Normalizer.normalize(nombreCompleto.trim(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        normalizado = normalizado.replaceAll("[^a-zA-Z ]", "");

        return
                Arrays.asList(normalizado.toUpperCase().split("\\W+")).stream()
                        .filter(s -> (s.length() > 1))
                        .collect(Collectors.toList());
    }

    /**
     * Funcion para crear un intervalo de mes de acuerdo al anio
     *
     * @param mes  fecha que se consulta
     * @param anio anio de la declaracion
     * @return JsonObject para poder obtener el rango fechas del query
     * @author pavel.martinez
     * @since 15/06/2020
     */
    private JsonObject crearIntervaloAnioMes(EnumMes mes, Integer anio) {

        String fechaAux = anio.toString().concat("-").concat(mes.getNum()).concat("-01");

        String fechaFin = LocalDate.parse(fechaAux, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .with(TemporalAdjusters.lastDayOfMonth()).toString();

        String fechaInicio = LocalDate.parse(fechaAux, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .with(TemporalAdjusters.firstDayOfMonth()).toString();

        return new JsonObject().put(EnumMongoDB.AND.getValor(), new JsonArray()
                .add(new JsonObject()
                        .put(EnumMongoDB.FECHA_REGISTRO_AUX.getValor(), new JsonObject()
                                .put(EnumMongoDB.GTE.getValor(), new JsonObject()
                                        .put(EnumMongoDB.DATE.getValor(), fechaInicio + "T00:00:00Z")//"T00:00:00.000Z"
                                )
                        )
                )
                .add(new JsonObject()
                        .put(EnumMongoDB.FECHA_REGISTRO_AUX.getValor(), new JsonObject()
                                .put(EnumMongoDB.LTE.getValor(), new JsonObject()
                                        .put(EnumMongoDB.DATE.getValor(), fechaFin + "T23:59:59Z")//"T00:00:00.000Z"
                                ))
                )
        );

    }

    /**
     * Funcion para crear los intervalos de mes de acuerdo al anio
     *
     * @param prefijo prefijo que corresponde a la coleccion consultada
     * @param query   query al que se le agregan los requisitos
     * @param meses   fechas que se consultan
     * @param anio    anio de la declaracion
     * @author pavel.martinez
     * @since 15/06/2020
     */
    public void crearIntervalosAnioMes(String prefijo, JsonObject query, List<EnumMes> meses, Integer anio) {

        JsonArray orFechas = new JsonArray(
                meses.stream().map(
                        mes -> crearIntervaloAnioMes(mes, anio)).collect(Collectors.toList())
        );
        query
                .put(EnumMongoDB.OR.getValor(), orFechas);
    }

    /**
     * Funcion para crear un field de fecha
     *
     * @param cumplimiento
     * @return
     */
    public JsonObject crearFieldFechaRegistro(String cumplimiento) {
        return new JsonObject()
                .put(EnumMongoDB.ADD_FIELDS.getValor(), new JsonObject()
                        .put(EnumMongoDB.FECHA_REGISTRO_AUX.getValor(), new JsonObject()
                                .put(EnumMongoDB.DATE_FROM_STRING.getValor(), new JsonObject()
                                        .put(EnumMongoDB.DATE_STRING.getValor(),
                                                "$".concat(cumplimiento).concat(EnumMongoDB.FECHA_REGISTRO.getValor())))));
    }

    /**
     * @param query
     * @param prefijo
     * @param condiciones
     * @param condicionesAnd
     * @throws SFPException
     */
    public void crearCondicioNombreCompletoCurp(CondicionesOmextInputDTO query, String prefijo,
                                                 JsonObject condiciones, JsonArray condicionesAnd)
            throws SFPException {
        if (query.getNombresApellidos() != null && !query.getNombresApellidos().trim().isEmpty()) {
            if (conNumerosPattern.matcher(query.getNombresApellidos().trim()).find()) {
                if (curpPattern.matcher(query.getNombresApellidos()).matches()) {
                    condiciones.put(prefijo.concat(EnumQueryMongo.CURP.getValor()), query.getNombresApellidos().trim());
                } else {
                    throw new SFPException(EnumError.ERROR_PARAMETROS_INCORRECTOS, EnumDetalleParamInc.CURP_NO_VALIDA.getValor());
                }
            } else {
                crearTextNombreCompleto(query.getNombresApellidos(), prefijo, condicionesAnd);
            }
        }
    }

    /**
     *
     * @param fechaInicio
     * @return
     */
    public static JsonObject crearFechaMongo(String fechaInicio) {
        try {
            if (fechaInicio == null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String fechaAhora = format.format(new Date());

                LOGGER.info("Se genero una fecha: null " + fechaAhora);
                return new JsonObject().put(EnumMongoDB.DATE.getValor(), fechaAhora);
            } else {
                if( fechaInicio.length() == 10 && fechaInicio.split("-").length == 3) {
                    LOGGER.info("Se genero una fecha no null: " + fechaInicio + "T00:00:00Z");
                    return new JsonObject().put(EnumMongoDB.DATE.getValor(), fechaInicio + "T00:00:00Z");
                } else {
                    return null;
                }
            }
        }catch (Exception e) {
            LOGGER.info("=== Error al crear fechaMongo + " + e);
            return  null;
        }
    }

    /**
     * Funcion para consultar fechas como String
     *
     * @param addFields JsonObject de addFields
     * @param nombreCampo nombre del campo de la fecha
     *
     * @since 15/02/2021
     */
    public void anadirDateToString(JsonObject addFields, String nombreCampo) {
        addFields.put(nombreCampo,
                new JsonObject().put(
                        "$dateToString" , new JsonObject()
                        .put("format", "%Y-%m-%d")
                        .put("date", "$" + nombreCampo)
                ));
    }



}
