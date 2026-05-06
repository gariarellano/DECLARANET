/**
 *
 */
package mx.gob.sfp.dgti.verticle;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.regex.Pattern;
import mx.gob.sfp.dgti.declaracion.encrypt.im.ValidaMensajeFront;
import mx.gob.sfp.dgti.enums.EnumMes;
import mx.gob.sfp.dgti.enums.EnumReporte;
import mx.gob.sfp.dgti.utils.Constantes;
import static mx.gob.sfp.dgti.utils.Constantes.PATH_CONSULTA_DECLARACION_SI;
import static mx.gob.sfp.dgti.utils.Constantes.PATH_CONSULTA_FIRMADO_DECLARACION;
import static mx.gob.sfp.dgti.utils.Constantes.PATH_DECLARACION;
import mx.gob.sfp.dgti.utils.Utilerias;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleExporterInputItem;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * Clas que cpntiene la logica de llenado del formato de declaraciones.
 *
 * @author programador09@sfp.gob.mx
 * @modifiedBy cgarias
 */
public class Server extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final Pattern PATHERN_DIGITOS = Pattern.compile("\\d+");

    private static final String MESSAGE_TEST_SANITY = "Servicio que permitira obtener el formato PDF de la declaracion";
    private static final String MESSAGE_OCURRIO_ERROR = "Ocurrio un error intentar de nuevo ";

    private static final String PARRAFO_P1 = "LA DECLARACIÓN DE SITUACIÓN PATRIMONIAL Y DE INTERESES HA SIDO PRESENTADA DE MANERA ELECTRÓNICA CON CARACTERES DE AUTENTICIDAD DE ";
    private static final String PARRAFO_P2 = "; Y CON CARACTERES DE AUTENTICIDAD DEL ACUSE DE RECIBO ELECTRÓNICO ";
    private static final String PARRAFO_P3 = ", FIRMADA ELECTRÓNICAMENTE CON EL NÚMERO ";
    private static final String PARRAFO_P4 = ", EN SUSTITUCIÓN DE LA AUTÓGRAFA, Y CON EL MISMO VALOR PROBATORIO";
    private static final String PARRAFO_P4_FIR = ", EN SUSTITUCIÓN DE LA AUTÓGRAFA, Y CON EL MISMO VALOR PROBATORIO.";
    private static final String PARRAFO_P5 = ", ACEPTANDO LAS CONDICIONES GENERALES PARA LA UTILIZACIÓN DE LA CLAVE ÚNICA DE REGISTRO DE POBLACIÓN (CURP) Y CONTRASEÑA COMO FIRMA DE LA DECLARACIÓN DE SITUACIÓN PATRIMONIAL Y DE INTERESES.";
    private static final String CARTA_P1 = "C.";
    private static final String CARTA_P2 = " con Clave Única de Registro de Población ";
    private static final String CARTA_P3 = " y correo electrónico ";
    private static final String CARTA_P4 = ", con fundamento en los artículos 108 y 109 de la Constitución Política de los Estados Unidos Mexicanos; 1, 2, 14, 16, 26 y 37, fracción XVI de la Ley Orgánica de la Administración Pública Federal; 1, 2, fracción I, 4, fracción I, 9, 29, 32, 33, 34, 35, 46, primer párrafo, 47 y 48 de la Ley General de Responsabilidades Administrativas, publicada en el Diario Oficial de la Federación el dieciocho de julio de dos mil dieciséis, en el ACUERDO por el que el Comité Coordinador del Sistema Nacional Anticorrupción emite el formato de declaraciones: de situación patrimonial y de intereses; y expide las normas e instructivo para su llenado y presentación, publicado en el Diario Oficial de la Federación el dieciséis de noviembre de dos mil dieciocho, en el ACUERDO por el que se modifican los Anexos Primero y Segundo del Acuerdo por el que el Comité Coordinador del Sistema Nacional Anticorrupción emite el formato de declaraciones: de situación patrimonial y de intereses; y expide las normas e instructivo para su llenado y presentación, publicado en el Diario Oficial de la Federación el veintitrés de septiembre de dos mil diecinueve, por el que se establece que de conformidad con los artículos 34 y 48 de la Ley, las declaraciones de situación patrimonial y de intereses deberán ser presentadas a través de medios electrónicos, empleándose FIEL o usuario y contraseña utilizados para ingresar al sistema DeclaraNet, así como en términos de las DISPOSICIONES Generales que establecen los mecanismos de identificación digital y control de acceso que deberán observar las dependencias y entidades de la Administración Pública Federal y las empresas productivas del Estado, publicadas en el Diario Oficial de la Federación el diez de mayo de dos mil dieciocho, procedo a realizar las siguientes:  ";

    private static final String DECLARACIONES_P1 = "1. Que la declaración de situación patrimonial de ";
    private static final String DECLARACIONES_P2 = " que bajo protesta de decir verdad presento ante ";
    private static final String DECLARACIONES_P3 = " con fecha ";
    private static final String DECLARACIONES_P4 = " es auténtica y atribuible a mi persona.";

    private static final String DECIMOPRIMERA = " DECIMOPRIMERA ";
    private static final String DECIMOSEGUNDA = " DECIMOSEGUNDA ";
    private static final String SIN_FORMATO = " FALTA FORMATO ";
    private static final String PARRAFO_P0_COMPLETA_0 = "CON ESTA FECHA SE RECIBIÓ SU DECLARACIÓN DE ";
    private static final String PARRAFO_P0_COMPLETA_1 = ", EN TÉRMINOS DE LA";
    private static final String PARRAFO_P0_COMPLETA_2 = "DE LAS NORMAS E INSTRUCTIVO PARA EL LLENADO Y PRESENTACIÓN DEL FORMATO DE DECLARACIONES: DE SITUACIÓN PATRIMONIAL Y DE INTERESES, PUBLICADAS EN EL DIARIO OFICIAL DE LA FEDERACIÓN EL 23 DE SEPTIEMBRE DE 2019, PRESENTADA BAJO PROTESTA DE DECIR VERDAD, EN CUMPLIMIENTO DE LO DISPUESTO EN LOS ARTÍCULOS 32 Y 33 FRACCIÓN I, DE LA LEY GENERAL DE RESPONSABILIDADES ADMINISTRATIVAS, DE LA QUE SE ACUSA DE RECIBO. ";

    private static final String SIMPLIFICADO = "SIMPLIFICADO";
    private static final String COMPLETO = "COMPLETO";
    private static final String NUMERO_DECLARACION = "numeroDeclaracion";
    private static final String IMAGEN_OFICIAL_B64 = "imagenOficialB64";
    private static final String NOMBRE = "nombre";
    private static final String FIRMA_ACUSE = "firmaAcuse";
    private static final String TIPO_FORMATO = "tipoFormato";
    private static final String TIPO_DECLARACION = "tipoDeclaracion";
    private static final String FECHA_RECEPCION = "fechaRecepcion";
    private static final String FECHA_ENCARGO = "fechaEncargo";

    private static final String DIRECTORIO = "//reportes//";
    private static final String SUBDIRECTORIO = DIRECTORIO.concat("subreportes//");
    private static final String PETICION_V2 = "PETICION_V2";
    private static final String PETICION_V2_VALUE = System.getenv(PETICION_V2);
    private static final String FIELD_NO_DOCUMENTO = "noDocumento";
    private static final String FIELD_FORMATO_DOCUMENTO = "formatoDocumento";
    private static final String FIEL_PDF_DECLARACION = "pdf_declaracion";
    private static final String FIEL_HTMLDECLARACION = "html_consulta_publica";
    private static final String PDF_AVISO_CAMBIO_DEP= "pdf_aviso_cambio_depcia";
    private static final String PDF_AVISO_CAMBIO_DEP_PUB= "pdf_aviso_cambio_depcia_pub";
    private static final String FIEL_PDF_NOTA = "pdf_nota";
    private static final String FIEL_PDF_ACUSE = "pdf_acuse";
    private static final String FIEL_PDF_ACUSE_NOTA = "pdf_nota_acuse";
    private static final String FIEL_PDF_ACUSE_AVISO = "pdf_aviso_acuse";
    private static final String VARIABLE_URL_DECLARACION_V2 = "URL_DECLARACION_V2";
    private static final String FIELD_DOCUMENTO = "documento";
    private static final String DECLARACION = "declaracion";
    private static final String INSTITUCION_RECEPTORA = "institucionReceptora";
    private static final String ENCABEZADO = "encabezado";
    private static final String DECLARANTE = "declarante";
    private static final String RECEPCION_WEB = "recepcionWeb";
    private static final String FIRMA_DECLARACION = "firmaDeclaracion";
    private static final String PARRAFO_GRL = "parrafo";
    private static final String PARRAFO_CARTAP1 = "cartaP1";
    private static final String PARRAFO_CARTAP2 = "cartaP2";
    private static final String URL_DECLARACION = System.getenv(PATH_DECLARACION) != null ? System.getenv(PATH_DECLARACION) : "";
    private static final String URL_CONSULTA_DECLARACION = URL_DECLARACION + PATH_CONSULTA_DECLARACION_SI;
    private static final String URL_CONSULTA_FIRMADO_DECLARACION = URL_DECLARACION + PATH_CONSULTA_FIRMADO_DECLARACION;
    private static final String URL_DECLARACION_V2 = System.getenv(VARIABLE_URL_DECLARACION_V2);

    private static Router router;
    private String nombreFirm;
    private WebClient client;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        router = Router.router(vertx);
        router.route().handler(
                CorsHandler.create(Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).allowedHeaders(getAllowedHeaders()));
        router.route(Constantes.PATH).handler(routingContext -> {
            HttpServerResponse httpResponse = routingContext.response();
            httpResponse.putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.TEXT_HTML).end(MESSAGE_TEST_SANITY);
        });
        client = WebClient.create(vertx);
        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(Constantes.CONFIG_PORT, 5000), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Running servicio reportes");
                    } else {
                        LOGGER.info(ar.cause());
                    }
                });
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start(future);
        router.route().handler(BodyHandler.create());
        router.post(Constantes.URL_APPI_CONSULTA_DECLARACION).handler(this::consultarDeclaracion);
        router.post(Constantes.URL_APPI_CONSULTA_ACUSE).handler(this::consultarAcuse);
        router.post(Constantes.URL_APPI_CONSULTA_PUBLICA).handler(this::consultaPublica);
        router.get(Constantes.URL_APPI_CONSULTA_PUBLICA).handler(this::consultaPublicaInai);
        router.post("/get-decla").handler(this::getDecla);
        router.post("/get-decla-pub").handler(this::getDeclaPub);
        
        router.post("/get-decla-html").handler(this::getDeclaHtml);
        router.post("/get-decla-html-pdf").handler(this::getDeclaHtmlAsPdf);
    }
    
    
    /**
     *
     * @param routingContext
     */
    private void consultarDeclaracion(RoutingContext routingContext) {
        obtenerHeaders(routingContext);
        /**
         * JsonObject data =
         * ValidaMensajeFront.fidelidadMensaje(routingContext.getBodyAsJson());
         * JsonObject data =routingContext.getBodyAsJson(); //local
         */
        JsonObject data = ValidaMensajeFront.fidelidadMensaje(routingContext.getBodyAsJson());
        if (data != null) {
            if (!isNumber(data.getString(NUMERO_DECLARACION))) {
                try {
                    client.postAbs(URL_CONSULTA_DECLARACION)
                            .timeout(30000)
                            .sendJsonObject(data, ar -> {
                                if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                    byte[] pdf;
                                    String tipoDeclaracion = ar.result().bodyAsJsonObject().getJsonObject(DECLARACION)
                                    .getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION);
                                    EnumReporte valores = Enum.valueOf(EnumReporte.class, tipoDeclaracion);
                                    switch (valores) {
                                        case AVISO:
                                            pdf = generarReporteCambioDependencia(ar.result().body().toString());
                                            break;
                                        case NOTA:
                                            pdf = generarReporteNotaAclaratoria(ar.result().body().toString());
                                            break;
                                        default:
                                            pdf = generarReporteCompleto(ar.result().body().toString());
                                            break;
                                    }
                                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                    .end(Json.encode(Base64.getEncoder().encodeToString(pdf)));
                                } else {
                                    if (ar.succeeded()) {
                                        LOGGER.error(" Status code : " + ar.result().statusCode() + URL_CONSULTA_DECLARACION + " " + data);
                                    }

                                    LOGGER.error(ar.cause());
                                    routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                    .end("MESSAGE_OCURRIO_ERROR");
                                }
                            });

                } catch (Exception e) {
                    LOGGER.fatal(e);
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                }
            } else {
                JsonObject peticion = new JsonObject(PETICION_V2_VALUE);
                peticion.put(FIELD_NO_DOCUMENTO, data.getString(NUMERO_DECLARACION))
                        .put(FIELD_FORMATO_DOCUMENTO, FIEL_PDF_DECLARACION);
                if (null != data.getString(TIPO_DECLARACION)) {
                    switch (data.getString(TIPO_DECLARACION)) {
                        case "AVISO":
                            peticion.put(FIELD_FORMATO_DOCUMENTO, PDF_AVISO_CAMBIO_DEP);
                            break;
                        case "NOTA":
                            peticion.put(FIELD_FORMATO_DOCUMENTO, FIEL_PDF_NOTA);
                            break;
                        default:
                            break;
                    }
                }

                client.postAbs(URL_DECLARACION_V2)
                        .timeout(30000)
                        .sendJsonObject(peticion, ar -> {
                            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(
                                        Json.encode(new JsonObject(ar.result().body().toString()).getString(FIELD_DOCUMENTO)));
                            } else {
                                if (ar.succeeded()) {
                                    LOGGER.error(" Status code : " + ar.result().statusCode() + URL_DECLARACION_V2 + " " + data);
                                }

                                LOGGER.error(ar.cause());
                                routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                .end(MESSAGE_OCURRIO_ERROR);
                            }
                        });
            }
        } else {
            LOGGER.info("Erroe al verificar la fidelidad");
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end("Error en fidelidad mensaje");
        }
    }

    /**
     *
     * @param routingContext
     */
    private void consultarAcuse(RoutingContext routingContext) {

        /**
         * JsonObject data =routingContext.getBodyAsJson(); //local JsonObject
         * data =
         * ValidaMensajeFront.fidelidadMensaje(routingContext.getBodyAsJson());
         */
        JsonObject data = ValidaMensajeFront.fidelidadMensaje(routingContext.getBodyAsJson());
        obtenerHeaders(routingContext);
        if (null != data) {
            if (!isNumber(data.getString(NUMERO_DECLARACION))) {
                Integer collName = data.getInteger("collName");
                client.postAbs(URL_CONSULTA_FIRMADO_DECLARACION)
                        .timeout(30000)
                        .sendJsonObject(data, ar -> {
                            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                JsonObject response = new JsonObject(ar.result().body().toString());
                                Map<String, Object> parametersAcuse = generarParametros(response);
                                byte[] pdf;
                                try {
                                    pdf = generarAcuse(parametersAcuse, collName);
                                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                    .end(Json.encode(Base64.getEncoder().encodeToString(pdf)));
                                } catch (JRException e) {
                                    LOGGER.fatal(e);
                                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                    .end();
                                }

                            } else {
                                if (ar.succeeded()) {
                                    LOGGER.error(" Status code : " + ar.result().statusCode() + URL_CONSULTA_FIRMADO_DECLARACION + " " + data);
                                }

                                LOGGER.error(ar.cause());
                                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
                            }
                        });
            } else {
                JsonObject peticion = new JsonObject(PETICION_V2_VALUE);
                peticion.put(FIELD_NO_DOCUMENTO, data.getString(NUMERO_DECLARACION))
                        .put(FIELD_FORMATO_DOCUMENTO, FIEL_PDF_ACUSE);
                if (null != data.getString(TIPO_DECLARACION)) {
                    switch (data.getString(TIPO_DECLARACION)) {
                        case "AVISO":
                            peticion.put(FIELD_FORMATO_DOCUMENTO, FIEL_PDF_ACUSE_AVISO);
                            break;
                        case "NOTA":
                            peticion.put(FIELD_FORMATO_DOCUMENTO, FIEL_PDF_ACUSE_NOTA);
                            break;
                        default:
                            break;
                    }
                }
                client.postAbs(URL_DECLARACION_V2)
                        .timeout(30000)
                        .sendJsonObject(peticion, ar -> {
                            if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(
                                        Json.encode(new JsonObject(ar.result().body().toString()).getString(FIELD_DOCUMENTO)));
                            } else {
                                if (ar.succeeded()) {
                                    LOGGER.error(" Status code : " + ar.result().statusCode() + URL_DECLARACION_V2 + " " + data);
                                }

                                LOGGER.error(ar.cause());
                                routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                .end(MESSAGE_OCURRIO_ERROR);
                            }
                        });
            }
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end("Error en fidelidad mensaje");
        }
    }
    
    private void consultaPublicaInai(RoutingContext routingContext) {
        try {
            if (routingContext.request().getParam("dI") != null && !routingContext.request().getParam("dI").isEmpty()){
                String[] parametros = Utilerias.decrypt(URLDecoder.decode(routingContext.request().getParam("dI"), StandardCharsets.UTF_8.toString())).split("#");
                declaracionPublica(
                        routingContext, 
                        new JsonObject()
                                .put(NUMERO_DECLARACION, parametros[0])
                                .put(TIPO_DECLARACION, "DECLA")
                                .put("idUsuario", Integer.parseInt(parametros[1]))
                                .put("collName", parametros.length == 3 ? Integer.parseInt(parametros[2]):"V2")
                );
            }else {
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end("Error la consulta no contiene valor en el parametro dI");
            }
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Error en desencriptar parametro dI,", ex);
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end("Error la consulta no contiene valor en el parametro dI");
        }
    }

    /**
     * *
     *
     * @param routingContext
     */
    private void consultaPublica(RoutingContext routingContext) {
        /**
         * JsonObject data = routingContext.getBodyAsJson(); //local JsonObject
         * data =
         * ValidaMensajeFront.fidelidadMensaje(routingContext.getBodyAsJson());
         */
        JsonObject data = ValidaMensajeFront.fidelidadMensaje(routingContext.getBodyAsJson());
        declaracionPublica(routingContext, data);
    }
    
    private void declaracionPublica(RoutingContext routingContext, JsonObject data){
        obtenerHeaders(routingContext);
        if (null != data) {
            try {
                if (!isNumber(data.getString(NUMERO_DECLARACION))) {
                    client.postAbs(URL_CONSULTA_DECLARACION)
                            .timeout(30000)
                            .sendJsonObject(data.put("html", Boolean.TRUE), ar -> {
                                if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                    byte[] pdf;
                                    if (ar.result().bodyAsJsonObject().containsKey("html")){
                                        ByteArrayOutputStream pdfBa = new ByteArrayOutputStream();
                                        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfBa));
                                        pdfDocument.setDefaultPageSize(PageSize.LETTER.rotate());
                                        ConverterProperties properties = new ConverterProperties();
                                        MediaDeviceDescription med = new MediaDeviceDescription(MediaType.ALL);
                                        properties.setMediaDeviceDescription(med); 
                                        properties.setCharset("UTF-8");
                                        try {
                                            HtmlConverter.convertToPdf(ar.result().bodyAsJsonObject().getString("html"), pdfDocument, properties);
                                            pdf = pdfBa.toByteArray();
                                        } catch (IOException ex) {
                                            LOGGER.fatal(ex);
                                            pdf = null;
                                            routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
                                        }
                                    }else{
                                        switch (EnumReporte.valueOf(ar.result().bodyAsJsonObject().getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION))) {
                                            case AVISO:
                                                pdf = generarReporteCambioDependenciaPublica(ar.result().body().toString());
                                                break;
                                            default:
                                                pdf = generarReporteConsultaPublica(ar.result().body().toString());
                                                break;
                                        }
                                    }
                                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                                            .end(Json.encode(Base64.getEncoder().encodeToString(pdf)));
                                } else {
                                    if (ar.succeeded()) {
                                        LOGGER.error(" Status code : " + ar.result().statusCode() + URL_CONSULTA_DECLARACION + " " + data);
                                    }

                                    LOGGER.error(ar.cause());
                                    routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                    .end("MESSAGE_OCURRIO_ERROR");
                                }
                            });
                }else{
                    JsonObject peticion = new JsonObject(PETICION_V2_VALUE);
                    peticion.put(FIELD_NO_DOCUMENTO, data.getString(NUMERO_DECLARACION))
                            .put("aplicacionCliente", "4")
                            .put(FIELD_FORMATO_DOCUMENTO, "AVISO".equals(data.getString(TIPO_DECLARACION))? PDF_AVISO_CAMBIO_DEP_PUB:FIEL_HTMLDECLARACION);
                    client.postAbs(URL_DECLARACION_V2)
                            .timeout(30000)
                            .sendJsonObject(peticion, ar -> {
                                if (ar.succeeded() && ar.result().statusCode() == HttpResponseStatus.OK.code()) {
                                    routingContext.response().setStatusCode(HttpResponseStatus.OK.code()).end(
                                            Json.encode(new JsonObject(ar.result().body().toString()).getString(FIELD_DOCUMENTO)));
                                } else {
                                    if (ar.succeeded()) {
                                        LOGGER.error(" Status code : " + ar.result().statusCode() +" "+ URL_DECLARACION_V2 + " " + data + "\n data:" + peticion);
                                    }

                                    LOGGER.error(ar.cause());
                                    routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code())
                                    .end(MESSAGE_OCURRIO_ERROR);
                                }
                            });
                }

            } catch (Exception e) {
                LOGGER.fatal(e);
                routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();

            }
        } else {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end("Error en fidelidad mensaje");
        }
    }

    /**
     * Método graph para poder obtener información de declaración,, a partir de objeto Json.     * 
     * @param routingContext 
     */
    private void getDecla(RoutingContext routingContext) {
        routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                .end(Json.encode(Base64.getEncoder()
                                .encodeToString(generarReporteCompleto(routingContext.getBodyAsString()))));
    }
    
    /**
     * Método que permite obtener una declaracion as html.
     * 
     * @param routingContext 
     */
    private void getDeclaHtml(RoutingContext routingContext) {
        JasperPrint jasperPrint;
        Map<String, Object> parameters = new HashMap<>();
        JsonObject datos = new JsonObject(routingContext.getBodyAsString());
        String tipoDec = datos.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION);
        String tipoFormato = datos.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_FORMATO);
        parameters.put(TIPO_DECLARACION, tipoDec);
        parameters.put(TIPO_FORMATO, tipoFormato);
        if (null != datos.getJsonObject(INSTITUCION_RECEPTORA)) {
            String imagen = datos.getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
            parameters.put(IMAGEN_OFICIAL_B64, imagen);
        } else {
            parameters.put(IMAGEN_OFICIAL_B64, null);
        }
        
        try{
            Exporter exporter = new HtmlExporter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {                
                jasperPrint = JasperFillManager
                        .fillReport(
                                DIRECTORIO + "declaracionPublica.jasper", 
                                parameters, 
                                new JsonDataSource(new ByteArrayInputStream(routingContext.getBodyAsString().getBytes(StandardCharsets.UTF_8)))
                        );
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(out)); 
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.exportReport();
            } catch (JRException e) {
                LOGGER.fatal(routingContext.getBodyAsString(), e);
            }
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                    .end(out.toString("UTF-8"));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } 
        routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end("ERROR");
    }
    
    private void getDeclaHtmlAsPdf(RoutingContext routingContext) {
        ByteArrayOutputStream pdfBa = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfBa));
        pdfDocument.setDefaultPageSize(PageSize.LETTER.rotate());
        ConverterProperties properties = new ConverterProperties();
        MediaDeviceDescription med = new MediaDeviceDescription(MediaType.ALL);
        properties.setMediaDeviceDescription(med); 
        properties.setCharset("UTF-8");
        try {
            HtmlConverter.convertToPdf(routingContext.getBodyAsString(), pdfDocument, properties);
            routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                    .end(Json.encode(Base64.getEncoder()
                                .encodeToString(pdfBa.toByteArray())));
        } catch (IOException ex) {
            LOGGER.fatal(ex);
            routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        }
        routingContext.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }
    
    /**
     * Método graph para obtener la declaracion pública, a partir de objeto Json
     * @param routingContext 
     */
    private void getDeclaPub(RoutingContext routingContext) {        
         routingContext.response().setStatusCode(HttpResponseStatus.OK.code())
                .end(Json.encode(Base64.getEncoder()
                                .encodeToString(generarReporteConsultaPublica(routingContext.getBodyAsString()))));
    }
    
    /**
     *
     * @param bodyAsString
     * @return
     */
    private byte[] generarReporteConsultaPublica(String bodyAsString) {

        List<ExporterInputItem> listDocto = new ArrayList<>();
        JasperPrint jasperPrint;
        Map<String, Object> parameters = new HashMap<>();
        JsonObject datos = new JsonObject(bodyAsString);
        String tipoDec = datos.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION);
        String tipoFormato = datos.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_FORMATO);
        parameters.put(TIPO_DECLARACION, tipoDec);
        parameters.put(TIPO_FORMATO, tipoFormato);
        if (null != datos.getJsonObject(DECLARACION).getJsonObject(INSTITUCION_RECEPTORA)) {
            String imagen = datos.getJsonObject(DECLARACION).getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
            parameters.put(IMAGEN_OFICIAL_B64, imagen);
        } else {
            parameters.put(IMAGEN_OFICIAL_B64, null);
        }
        JRDataSource datasource;
        try {
            datasource = new JsonDataSource(new ByteArrayInputStream(bodyAsString.getBytes(StandardCharsets.UTF_8)));
            jasperPrint = JasperFillManager.fillReport(DIRECTORIO + "declaracionPublica.jasper", parameters, datasource);
            listDocto.add(new SimpleExporterInputItem(jasperPrint));
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(listDocto));
            ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
            exporter.setConfiguration(new SimplePdfExporterConfiguration());
            exporter.exportReport();
            return exporterOutput.toByteArray();

        } catch (JRException e) {
            LOGGER.fatal(e);
        }
        return new byte[0];
    }

    private byte[] generarAcuse(Map<String, Object> parametersAcuse, Integer collName) throws JRException {

        List<ExporterInputItem> listDocto = new ArrayList<>();
        boolean flag = nombreFirm.contains("fup");
        parametersAcuse.put("flag", flag);
        parametersAcuse.put("collName", collName);

        try {

            JasperPrint jasperCarta;
            if ((boolean) parametersAcuse.get("esAviso")) {
                jasperCarta = JasperFillManager.fillReport(SUBDIRECTORIO + "cartaAceptacionAviso.jasper",
                        parametersAcuse, new JREmptyDataSource());
            } else {
                jasperCarta = JasperFillManager.fillReport(SUBDIRECTORIO + "cartaAceptacion.jasper",
                        parametersAcuse, new JREmptyDataSource());
            }
            JasperPrint jasperAcuse;
            if ((boolean) parametersAcuse.get("esNota")) {
                jasperAcuse = JasperFillManager.fillReport(DIRECTORIO + "acuseNota.jasper", parametersAcuse,
                        new JREmptyDataSource());
            } else {
                jasperAcuse = JasperFillManager.fillReport(DIRECTORIO + "acuse.jasper", parametersAcuse,
                        new JREmptyDataSource());
            }

            listDocto.add(new SimpleExporterInputItem(jasperAcuse));
            if (flag) {
                listDocto.add(new SimpleExporterInputItem(jasperCarta));
            }

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(listDocto));
            ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
            exporter.setConfiguration(new SimplePdfExporterConfiguration());
            exporter.exportReport();

            return exporterOutput.toByteArray();

        } catch (Exception e) {
            LOGGER.fatal(e);
        }
        return new byte[0];
    }

    /**
     *
     * @param response2
     * @return
     * @throws ParseException
     */
    private Map<String, Object> generarParametros(JsonObject json) {
        Map<String, Object> parameters = new HashMap<>();
        String digestionDcn = json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_DECLARACION)
                .getString("digestionDcn");
        String digestionAcuse = json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_ACUSE)
                .getString("digestionAcuse");
        String tipoDeclaracion = json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION)
                .getString(TIPO_DECLARACION);
        nombreFirm = json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_DECLARACION).getString("nombreFirm");
        boolean flag = nombreFirm.contains("fup");
        String tipoFormato = json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getString(TIPO_FORMATO);
        StringBuilder parrafo;
        parameters.put("esAviso", verificarEsAviso(tipoDeclaracion));
        parameters.put("esNota", verificarEsNota(tipoDeclaracion));

        if (null != tipoFormato) {
            parrafo = new StringBuilder();
            if (flag) {
                if (tipoFormato.equalsIgnoreCase(SIMPLIFICADO)) {
                    parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1
                    ).append(DECIMOSEGUNDA).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2
                    ).append(digestionAcuse).append(PARRAFO_P3).append(nombreFirm).append(PARRAFO_P4).append(PARRAFO_P5);
                } else if (tipoFormato.equalsIgnoreCase(COMPLETO)) {
                    parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1
                    ).append(DECIMOPRIMERA).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2
                    ).append(digestionAcuse).append(PARRAFO_P3).append(nombreFirm).append(PARRAFO_P4).append(PARRAFO_P5);
                } else {
                    parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1
                    ).append(SIN_FORMATO).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2
                    ).append(digestionAcuse).append(PARRAFO_P3).append(nombreFirm).append(PARRAFO_P4).append(PARRAFO_P5);
                }
            } else {
                if (tipoFormato.equalsIgnoreCase(SIMPLIFICADO)) {
                    parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1
                    ).append(DECIMOSEGUNDA).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2
                    ).append(digestionAcuse).append(PARRAFO_P3).append(nombreFirm).append(PARRAFO_P4_FIR);
                } else if (tipoFormato.equalsIgnoreCase(COMPLETO)) {
                    parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1
                    ).append(DECIMOPRIMERA).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2
                    ).append(digestionAcuse).append(PARRAFO_P3).append(nombreFirm).append(PARRAFO_P4_FIR);
                } else {
                    parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1
                    ).append(SIN_FORMATO).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2
                    ).append(digestionAcuse).append(PARRAFO_P3).append(nombreFirm).append(PARRAFO_P4_FIR);
                }
            }

        } else {
            parrafo = new StringBuilder();
            parrafo.append(PARRAFO_P0_COMPLETA_0).append(tipoDeclaracion.toUpperCase()).append(PARRAFO_P0_COMPLETA_1).append(SIN_FORMATO
            ).append(PARRAFO_P0_COMPLETA_2).append(PARRAFO_P1).append(digestionDcn).append(PARRAFO_P2).append(digestionAcuse).append(PARRAFO_P3
            ).append(nombreFirm).append(PARRAFO_P4_FIR);
        }
        parameters.put("nombreFirm", nombreFirm);

        parameters.put("noComprobante",
                json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getValue("noComprobante"));
        parameters.put("rfc", json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARANTE).getValue("rfc"));
        parameters.put(FECHA_RECEPCION,
                json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getValue(FECHA_RECEPCION));
        parameters.put("mes",
                generarMes(json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getString(FECHA_RECEPCION)));
        parameters.put(NOMBRE, json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARANTE).getValue(NOMBRE));
        parameters.put(TIPO_DECLARACION, tipoDeclaracion);
        parameters.put("puestoFirmante",
                json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_ACUSE).getValue("puestoFirmante"));
        parameters.put("texto", json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_ACUSE).getValue("texto"));
        parameters.put(FIRMA_ACUSE, json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_ACUSE).getValue(FIRMA_ACUSE));
        parameters.put("tituloFirmante",
                json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_ACUSE).getValue("tituloFirmante"));
        parameters.put("nombreFirmante",
                json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_ACUSE).getValue("nombreFirmante"));
        parameters.put(NUMERO_DECLARACION,
                json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getString(NUMERO_DECLARACION));
        parameters.put("transaccion",
                json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_DECLARACION).getValue("transaccion"));
        parameters.put("numeroCertificado",
                json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_DECLARACION).getValue("numeroCertificado"));
        parameters.put("anio", json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getValue("anio"));
        parameters.put(FECHA_ENCARGO,
                json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getValue(FECHA_ENCARGO));
        parameters.put("mesEncargo",
                generarMes(json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getString(FECHA_ENCARGO)));

        String base64 = json.getJsonObject(INSTITUCION_RECEPTORA).getString("imagenInstitucionalB64");
        String imagenOficialB64 = json.getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
        parameters.put("imagenInstitucionalB64", base64);
        parameters.put(IMAGEN_OFICIAL_B64, imagenOficialB64);

        String nombre = json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARANTE).getString(NOMBRE);
        String curp = json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARANTE).getString("curp");
        parameters.put("curp", curp);
        String correoInstitucional = json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARANTE)
                .getString("correoElectronico");

        String nombreEnte = json.getJsonObject(INSTITUCION_RECEPTORA).getJsonObject("ente").getString(NOMBRE);
        String prefijo = json.getJsonObject(INSTITUCION_RECEPTORA).getString("prefijo");
        parameters.put("nombreEnte", nombreEnte);
        String fecha = generarFechaCarta(json.getJsonObject(RECEPCION_WEB).getJsonObject(DECLARACION).getString(FECHA_RECEPCION));

        parameters.put("folio", json.getJsonObject(RECEPCION_WEB).getJsonObject(FIRMA_DECLARACION).getValue("folio"));
        parameters.put("prefijo", json.getJsonObject(INSTITUCION_RECEPTORA).getString("prefijo"));

        StringBuilder parrafoAux;
        if (verificarEsAviso(tipoDeclaracion)) {
            parrafoAux = generarParrafoAviso(digestionDcn, digestionAcuse, nombreFirm);
            parameters.put(PARRAFO_GRL, parrafoAux.toString());
            parameters.put(PARRAFO_CARTAP1, generarParrafoCataAceptacion(nombre, curp, correoInstitucional));
            parameters.put(PARRAFO_CARTAP2, generarManifestacion(nombreEnte, fecha));
        } else if (verificarEsNota(tipoDeclaracion)) {
            parrafoAux = generarParrafoNotaAclaratoria(digestionDcn, nombreFirm);
            parameters.put(PARRAFO_GRL, parrafoAux.toString());
            parameters.put("digestionAcuse", digestionAcuse);
            parameters.put(PARRAFO_CARTAP1, new StringBuilder(CARTA_P1).append(nombre).append(CARTA_P2).append(curp).append(CARTA_P3).append(correoInstitucional).append(CARTA_P4).toString());
            parameters.put(PARRAFO_CARTAP2, new StringBuilder(DECLARACIONES_P1).append(tipoDeclaracion.toLowerCase()).append(DECLARACIONES_P2).append(prefijo.toLowerCase()).append(" ").append(nombreEnte
            ).append(DECLARACIONES_P3).append(fecha).append(DECLARACIONES_P4).toString());
        } else {
            parameters.put(PARRAFO_GRL, parrafo.toString());
            parameters.put(PARRAFO_CARTAP1, new StringBuilder(CARTA_P1).append(nombre).append(CARTA_P2).append(curp).append(CARTA_P3).append(correoInstitucional).append(CARTA_P4).toString());
            parameters.put(PARRAFO_CARTAP2, new StringBuilder(DECLARACIONES_P1).append(tipoDeclaracion.toLowerCase()).append(DECLARACIONES_P2).append(prefijo.toLowerCase()).append(" ").append(nombreEnte
            ).append(DECLARACIONES_P3).append(fecha).append(DECLARACIONES_P4).toString());
        }

        return parameters;
    }

    private StringBuilder generarParrafoNotaAclaratoria(String digestionDcn, String nombreFirm) {
        StringBuilder parrafoAux = new StringBuilder();
        String parrafo = "LA NOTA ACLARATORIA HA SIDO PRESENTADA EN SOBRE QUE CONTIENE EL ARCHIVO $nombreFirm CON CARACTERES DE AUTENTICIDAD $digestionDcn, EL CUAL INCORPORA FIRMA ELECTRÓNICA, EN SUSTITUCIÓN DE LA AUTÓGRAFA Y CON EL MISMO VALOR PROBATORIO, TODA VEZ QUE HE ACEPTADO LAS CONDICIONES GENERALES QUE SE ANEXAN.";
        parrafo = parrafo.replace("$nombreFirm", nombreFirm);
        parrafo = parrafo.replace("$digestionDcn", digestionDcn);
        parrafoAux.append(parrafo);
        return parrafoAux;
    }

    /**
     *
     * @param fecha
     * @return
     */
    private String generarManifestacion(String nombreEnte, String fecha) {
        String parrafo = "Que el aviso por cambio de dependencia o entidad que bajo protesta de decir verdad presento ante la $nombreEnte con fecha $fecha es auténtica y atribuible a mi persona.";
        parrafo = parrafo.replace("$nombreEnte", nombreEnte);
        parrafo = parrafo.replace("$fecha", fecha);
        return parrafo;
    }

    /**
     * *
     *
     * @param nombre
     * @param curp
     * @param correoInstitucional
     * @return
     */
    private String generarParrafoCataAceptacion(String nombre, String curp, String correoInstitucional) {
        String parrafo = "C. $nombre con Clave Única de Registro de Población $curp y correo electrónico $correo, con fundamento en los artículos 108 y 109 de la Constitución Política de los Estados Unidos Mexicanos; 1, 2, 14, 16, 26 y 37, fracción XVI de la Ley Orgánica de la Administración Pública Federal; 1, 2 y 7 de la Ley de Firma Electrónica Avanzada; 1, 2, 3, 9 y 33 segundo párrafo de la Ley General de Responsabilidades Administrativas, publicada en el Diario Oficial de la Federación el dieciocho de julio de dos mil dieciséis; y en virtud de haber optado por firmar mi aviso de cambio por dependencia o entidad a través del uso de mi Clave Única de Registro de Población y contraseña utilizados para ingresar al sistema DeclaraNet, procesado a realizar las siguientes: ";
        parrafo = parrafo.replace("$nombre", nombre);
        parrafo = parrafo.replace("$curp", curp);
        parrafo = parrafo.replace("$correo", correoInstitucional);
        return parrafo;
    }

    private StringBuilder generarParrafoAviso(String digestionDcn, String digestionAcuse, String nombreFirm) {
        StringBuilder aux = new StringBuilder();
        String parrafoAvisoCD = "CON ESTA FECHA SE RECIBIÓ SU AVISO POR CAMBIO DE DEPENDENCIA O ENTIDAD, PRESENTADO BAJO PROTESTA DE DECIR VERDAD, EN CUMPLIMIENTO DE LO DISPUESTO POR EL ARTICULO 33, PÁRRAFO SEGUNDO, DE LA LEY GENERAL DE RESPONSABILIDADES ADMINISTRATIVAS, DE LA QUE SE ACUSA DE RECIBO. HA SIDO PRESENTADO DE MANERA ELECTRÓNICA CON CARACTERES DE AUTENTICIDAD DE $digestionDcn; Y CON CARACTERES DE AUTENTICIDAD DEL ACUSE DE RECIBO ELECTRÓNICO $digestionAcuse, FIRMADO ELECTRÓNICAMENTE CON EL NÚMERO $nombreFirm, EN SUSTITUCIÓN DE LA AUTÓGRAFA, Y CON EL MISMO VALOR PROBATORIO, ACEPTANDO LAS CONDICIONES GENERALES PARA LA UTILIZACIÓN DE LA CLAVE ÚNICA DE REGISTRO DE POBLACIÓN (CURP) Y CONTRASEÑA COMO FIRMA DEL AVISO DE CAMBIO DE DEPENDENCIA.";
        parrafoAvisoCD = parrafoAvisoCD.replace("$digestionDcn", digestionDcn);
        parrafoAvisoCD = parrafoAvisoCD.replace("$digestionAcuse", digestionAcuse);
        parrafoAvisoCD = parrafoAvisoCD.replace("$nombreFirm", nombreFirm);
        aux.append(parrafoAvisoCD);
        return aux;
    }

    /**
     * verifica que sea aviso
     *
     * @param tipoFormato
     * @return
     */
    private boolean verificarEsAviso(String tipoFormato) {
        return EnumReporte.AVISO.name().equals(tipoFormato);

    }

    /**
     * *
     * verifica que sea NOTA
     */
    private boolean verificarEsNota(String tipoFormato) {
        return EnumReporte.NOTA.name().equals(tipoFormato);

    }

    /**
     *
     * @param string
     * @return
     */
    private String generarFechaCarta(String fecha) {
        return fecha.substring(8, 10).concat(" de ").concat((generarMes(fecha)).toLowerCase()).concat(" de ")
                .concat(fecha.substring(0, 4));
    }

    /**
     * Calula la fecha
     *
     * @param fecha
     * @return
     */
    private String generarMes(String fecha) {
        String mes= "";
        if (null != fecha) {
            mes = EnumMes.valueOf(String.format("MES%s",fecha.substring(5, 7))).getDecripcion();
        }
        return mes;
    }


    private byte[] generarReporteNotaAclaratoria(String bodyAsString) {

        Map<String, Object> parametersNotaAclaratoria = new HashMap<>();
        List<ExporterInputItem> listDocto = new ArrayList<>();
        JasperPrint jasperPrint;
        JRDataSource datasource;
        try {
            datasource = new JsonDataSource(new ByteArrayInputStream(bodyAsString.getBytes(StandardCharsets.UTF_8)));
            JsonObject data = new JsonObject(bodyAsString);
            String tipoDec = data.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION);
            String tipoFormato = data.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_FORMATO);
            parametersNotaAclaratoria.put(TIPO_DECLARACION, tipoDec);
            parametersNotaAclaratoria.put(TIPO_FORMATO, tipoFormato);
            String imagenOficialB64 = data.getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
            parametersNotaAclaratoria.put(IMAGEN_OFICIAL_B64, imagenOficialB64);
            jasperPrint = JasperFillManager.fillReport(DIRECTORIO + "notaAclaratoria.jasper",
                    parametersNotaAclaratoria, datasource);
            listDocto.add(new SimpleExporterInputItem(jasperPrint));
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(listDocto));
            ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
            exporter.setConfiguration(new SimplePdfExporterConfiguration());
            exporter.exportReport();
            return exporterOutput.toByteArray();

        } catch (JRException e) {
            LOGGER.fatal(e);
        }
        return new byte[0];
    }

    private byte[] generarReporteCambioDependencia(String bodyAsString) {

        Map<String, Object> parametersCambioDependencia = new HashMap<>();
        List<ExporterInputItem> listDocto = new ArrayList<>();
        JasperPrint jasperPrint;
        JRDataSource datasource;
        try {
            datasource = new JsonDataSource(new ByteArrayInputStream(bodyAsString.getBytes(StandardCharsets.UTF_8)));
            JsonObject data = new JsonObject(bodyAsString);
            String imagenOficialB64 = data.getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
            parametersCambioDependencia.put(IMAGEN_OFICIAL_B64, imagenOficialB64);
            jasperPrint = JasperFillManager.fillReport(DIRECTORIO + "avisoCambioDependencia.jasper",
                    parametersCambioDependencia, datasource);
            listDocto.add(new SimpleExporterInputItem(jasperPrint));
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(listDocto));
            ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
            exporter.setConfiguration(new SimplePdfExporterConfiguration());
            exporter.exportReport();
            return exporterOutput.toByteArray();

        } catch (JRException e) {
            LOGGER.fatal(e);
        }
        return new byte[0];
    }
    
    private byte[] generarReporteCambioDependenciaPublica(String bodyAsString) {

        Map<String, Object> parametersCambioDependencia = new HashMap<>();
        List<ExporterInputItem> listDocto = new ArrayList<>();
        JasperPrint jasperPrint;
        JRDataSource datasource;
        try {
            datasource = new JsonDataSource(new ByteArrayInputStream(bodyAsString.getBytes(StandardCharsets.UTF_8)));
            JsonObject data = new JsonObject(bodyAsString);
            String imagenOficialB64 = data.getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
            parametersCambioDependencia.put(IMAGEN_OFICIAL_B64, imagenOficialB64);
            jasperPrint = JasperFillManager.fillReport(DIRECTORIO + "avisoCambioDependenciaPublica.jasper",
                    parametersCambioDependencia, datasource);
            listDocto.add(new SimpleExporterInputItem(jasperPrint));
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(listDocto));
            ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
            exporter.setConfiguration(new SimplePdfExporterConfiguration());
            exporter.exportReport();
            return exporterOutput.toByteArray();

        } catch (JRException e) {
            LOGGER.fatal(e);
        }
        return new byte[0];
    }

    /**
     *
     * @return @throws JRException
     * @throws UnsupportedEncodingException
     */
    private byte[] generarReporteCompleto(String bodyAsString) {

        List<ExporterInputItem> listDocto = new ArrayList<>();
        JasperPrint jasperPrint;
        Map<String, Object> parameters = new HashMap<>();
        JsonObject datos = new JsonObject(bodyAsString);
        String tipoDec = datos.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION);
        String tipoFormato = datos.getJsonObject(DECLARACION).getJsonObject(ENCABEZADO).getString(TIPO_FORMATO);
        parameters.put(TIPO_DECLARACION, tipoDec);
        parameters.put(TIPO_FORMATO, tipoFormato);
        if (null != datos.getJsonObject(INSTITUCION_RECEPTORA)) {
            String imagen = datos.getJsonObject(INSTITUCION_RECEPTORA).getString(IMAGEN_OFICIAL_B64);
            parameters.put(IMAGEN_OFICIAL_B64, imagen);
        } else {
            parameters.put(IMAGEN_OFICIAL_B64, null);
        }
        JRDataSource datasource;
//        JasperReport jasp;
        try {
            datasource = new JsonDataSource(new ByteArrayInputStream(bodyAsString.getBytes(StandardCharsets.UTF_8)));
            jasperPrint = JasperFillManager.fillReport(DIRECTORIO + "main.jasper", parameters, datasource);

            listDocto.add(new SimpleExporterInputItem(jasperPrint));
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(listDocto));
            ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
            exporter.setConfiguration(new SimplePdfExporterConfiguration());
            exporter.exportReport();
            return exporterOutput.toByteArray();

        } catch (JRException e) {
            LOGGER.fatal(bodyAsString, e);
        }
        return new byte[0];
    }

    /**
     * Headers permitidos para CORS
     *
     * @return Set<String> Headers permitidos en las peticiones
     */
    private static Set<String> getAllowedHeaders() {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(Constantes.HEADER_CONTENT_TYPE);
        allowedHeaders.add(Constantes.HEADER_AUTHORIZATION);
        return allowedHeaders;
    }

    /**
     * @param cadena
     * @return
     */
    public boolean isNumber(String cadena) {
        return PATHERN_DIGITOS.matcher(cadena).matches();
    }

    /*
     * Se agregan los headers correspondientes para la respuesta de las solicitudes
     * 
     * @param routingContext: contexto
     * 
     * @return HttpServerResponse
     */
    public static HttpServerResponse obtenerHeaders(RoutingContext routingContext) {
        return routingContext.response()
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_ORIGIN, Constantes.HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                        Constantes.HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_METHODS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_METHODS)
                .putHeader(Constantes.ACCESS_CONTROL_ALLOW_HEADERS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(Constantes.ACCESS_CONTROL_EXPOSE_HEADERS, Constantes.HEADER_ACCESS_CONTROL_ALLOW_HEADERS)
                .putHeader(Constantes.HEADER_CONTENT_TYPE, Constantes.HEADER_CONTENT_APPLICATION_JSON)
                .putHeader(Constantes.HEADER_PRAGMA, Constantes.PRAGMA)
                .putHeader(Constantes.HEADER_CACHE_CONTROL, Constantes.CACHE_CONTROL);

    }

    /**
     * Main para pruebas locales
     *
     * @param args
     */
    public static void main(String[] args) {
        Vertx vertex = Vertx.vertx();
        vertex.deployVerticle(new ServerLauncher());
    }
}