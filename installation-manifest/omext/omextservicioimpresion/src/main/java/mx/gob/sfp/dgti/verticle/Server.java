/**
 *
 */
package mx.gob.sfp.dgti.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import mx.gob.sfp.dgti.utils.Constantes;
import static mx.gob.sfp.dgti.utils.Constantes.ANIO;
import static mx.gob.sfp.dgti.utils.Constantes.BODY_TEXT_OFICIO;
import static mx.gob.sfp.dgti.utils.Constantes.DATA;
import static mx.gob.sfp.dgti.utils.Constantes.DEPENDENCIA_ENTIDAD;
import static mx.gob.sfp.dgti.utils.Constantes.FECHA_VENCIMIENTO;
import static mx.gob.sfp.dgti.utils.Constantes.FECHA_VISTA;
import static mx.gob.sfp.dgti.utils.Constantes.FIRMANTE_NOMBRE;
import static mx.gob.sfp.dgti.utils.Constantes.FIRM_CAR_AUTENT_LISTADO;
import static mx.gob.sfp.dgti.utils.Constantes.FIRM_CAR_AUTENT_OFICIO;
import static mx.gob.sfp.dgti.utils.Constantes.LOGO_IMAGEN;
import static mx.gob.sfp.dgti.utils.Constantes.NUMERO_OFICIO;
import static mx.gob.sfp.dgti.utils.Constantes.PARAMETROS_VISTA;
import static mx.gob.sfp.dgti.utils.Constantes.PARAMETROS_VISTA_DETALLE;
import static mx.gob.sfp.dgti.utils.Constantes.PRIMER_PARRAFO;
import static mx.gob.sfp.dgti.utils.Constantes.PUESTO_FIRMANTE;
import static mx.gob.sfp.dgti.utils.Constantes.SEGUNDO_PARRAFO;
import static mx.gob.sfp.dgti.utils.Constantes.TIPO_DECLARACION;
import static mx.gob.sfp.dgti.utils.Constantes.URL_APPI_VISTA_OMISOS;
import static mx.gob.sfp.dgti.utils.Constantes.VISTA_OMISO_DETALLE_JASPER;
import static mx.gob.sfp.dgti.utils.Constantes.VISTA_OMISO_JASPER;
import mx.gob.sfp.dgti.utils.Validaciones;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleExporterInputItem;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * Clase que contiene la logica de llenado del formato 
 * de omisos y extemporaneos en la presentación de la 
 * declaración.
 *
 * @author programador09@sfp.gob.mx
 */
public class Server extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);    
    private static final String MESSAGE_TEST_SANITY = "Servicio que permitira obtener el formato PDF de reportes de OMEXT";
    
    private static Router router;
    ResourceBundle rcImages;

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
        this.getVertx().createHttpServer().requestHandler(router::accept)
                .listen(config().getInteger(Constantes.CONFIG_PORT, 5000), ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("Running servicio reportes");
                    } else {
                        LOGGER.info(ar.cause());
                    }
                });
        rcImages = ResourceBundle.getBundle("imgs.ImagesB64");
        
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        super.start(future);
        router.route().handler(BodyHandler.create());
        router.post(URL_APPI_VISTA_OMISOS).handler(this::generaVistaOmiso);
    }
    
    
    private void generaVistaOmiso(RoutingContext routingContext){
        Map<String, Object> parametros = new HashMap<>();
        if (validaParametrosAsignaDatos(routingContext, parametros)){
            try {
                JasperPrint vistaOmiso = JasperFillManager.fillReport(VISTA_OMISO_JASPER, (Map<String, Object>) parametros.get(PARAMETROS_VISTA), new JREmptyDataSource());
                JasperPrint vistaOmisoDetalle = 
                        JasperFillManager.fillReport(
                                VISTA_OMISO_DETALLE_JASPER,
                                (Map<String, Object>)parametros.get(PARAMETROS_VISTA_DETALLE), 
                                new JsonDataSource(new ByteArrayInputStream(routingContext.getBodyAsJson().getJsonArray(DATA).toString().getBytes(StandardCharsets.UTF_8))));


                List<ExporterInputItem> listDocto = new ArrayList<>();
                listDocto.add(new SimpleExporterInputItem(vistaOmiso));
                listDocto.add(new SimpleExporterInputItem(vistaOmisoDetalle));

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(listDocto));
                ByteArrayOutputStream exporterOutput = new ByteArrayOutputStream();
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exporterOutput));
                exporter.setConfiguration(new SimplePdfExporterConfiguration());
                exporter.exportReport();

                //LOGGER.info(Json.encode(Base64.getEncoder().encodeToString(exporterOutput.toByteArray())));
                routingContext.response()
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(Json.encode(Base64.getEncoder().encodeToString(exporterOutput.toByteArray())));
            } catch (JRException | EncodeException e) {
                LOGGER.fatal(e);
                routingContext.response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end();
            }
        }else{
            routingContext.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("Parametros incorrectos");
        }
    }
        
    private boolean validaParametrosAsignaDatos(RoutingContext routingContext, Map<String, Object> parametros){
        HashMap<String, Object> parametersVista = new HashMap<>();
        Map<String, Object> parametersVistaDetalle= new HashMap<>();
        JsonObject datos = routingContext.getBodyAsJson();
        if (datos.getString(NUMERO_OFICIO) == null && datos.getString(NUMERO_OFICIO).isEmpty()){
            return false;
        }
        if ((datos.getString(FECHA_VISTA) == null && datos.getString(FECHA_VISTA).isEmpty()) 
                | !Validaciones.validaFormatoFecha(datos.getString(FECHA_VISTA))){
                return false;
        }
        if (datos.getString(DEPENDENCIA_ENTIDAD) == null && datos.getString(DEPENDENCIA_ENTIDAD).isEmpty()){
            return false;
        }
        if (datos.getString(BODY_TEXT_OFICIO) == null && datos.getString(BODY_TEXT_OFICIO).isEmpty()){
            return false;
        }
        if ((datos.getString(FECHA_VENCIMIENTO) == null && datos.getString(FECHA_VENCIMIENTO).isEmpty())
                | !Validaciones.validaFormatoFecha(datos.getString(FECHA_VENCIMIENTO))){
            return false;
        }
        if (datos.getString(LOGO_IMAGEN) == null && datos.getString(LOGO_IMAGEN).isEmpty()){
            return false;
        }
        if (datos.getString(PUESTO_FIRMANTE) == null && datos.getString(PUESTO_FIRMANTE).isEmpty()){
            return false;
        }
        if (datos.getString(FIRM_CAR_AUTENT_OFICIO) == null && datos.getString(FIRM_CAR_AUTENT_OFICIO).isEmpty()){
            return false;
        }        
        if (datos.getString(FIRM_CAR_AUTENT_LISTADO) == null && datos.getString(FIRM_CAR_AUTENT_LISTADO).isEmpty()){
            return false;
        }
        if (datos.getString(FIRMANTE_NOMBRE) == null && datos.getString(FIRMANTE_NOMBRE).isEmpty()){
            return false;
        }
        if (datos.getString(PRIMER_PARRAFO) == null && datos.getString(PRIMER_PARRAFO).isEmpty()){
            return false;
        }        
        if (datos.getString(SEGUNDO_PARRAFO) == null && datos.getString(SEGUNDO_PARRAFO).isEmpty()){
            return false;
        }
        if (datos.getString(TIPO_DECLARACION) == null && datos.getString(TIPO_DECLARACION).isEmpty()){
            return false;
        }
        if (datos.getString(ANIO) == null && datos.getString(ANIO).isEmpty()){
            return false;
        }
        if (datos.getString(LOGO_IMAGEN) == null && datos.getString(LOGO_IMAGEN).isEmpty()){
            return false;
        }
        if (datos.getJsonArray(DATA) == null && datos.getJsonArray(DATA).isEmpty()){
            return false;
        }
        parametersVista.put(NUMERO_OFICIO, datos.getString(NUMERO_OFICIO));
        parametersVista.put(FECHA_VISTA, datos.getString(FECHA_VISTA));
        parametersVista.put(DEPENDENCIA_ENTIDAD, datos.getString(DEPENDENCIA_ENTIDAD));
        parametersVista.put(BODY_TEXT_OFICIO, datos.getString(BODY_TEXT_OFICIO));
        parametersVista.put(FECHA_VENCIMIENTO, datos.getString(FECHA_VENCIMIENTO));
        parametersVista.put(TIPO_DECLARACION, datos.getString(TIPO_DECLARACION));
        parametersVista.put(LOGO_IMAGEN, rcImages.getString(datos.getString(LOGO_IMAGEN)));
        parametersVista.put(PUESTO_FIRMANTE, datos.getString(PUESTO_FIRMANTE));
        parametersVista.put(FIRM_CAR_AUTENT_OFICIO, datos.getString(FIRM_CAR_AUTENT_OFICIO));
        parametersVista.put(FIRMANTE_NOMBRE, datos.getString(FIRMANTE_NOMBRE));

        parametersVistaDetalle.put(PRIMER_PARRAFO, datos.getString(PRIMER_PARRAFO));
        parametersVistaDetalle.put(SEGUNDO_PARRAFO, datos.getString(SEGUNDO_PARRAFO));
        parametersVistaDetalle.put(PUESTO_FIRMANTE, datos.getString(PUESTO_FIRMANTE));
        parametersVistaDetalle.put(FIRM_CAR_AUTENT_LISTADO, datos.getString(FIRM_CAR_AUTENT_LISTADO));
        parametersVistaDetalle.put(FIRMANTE_NOMBRE, datos.getString(FIRMANTE_NOMBRE));
        parametersVistaDetalle.put(TIPO_DECLARACION, datos.getString(TIPO_DECLARACION));
        parametersVistaDetalle.put(ANIO, datos.getString(ANIO));       
        parametersVistaDetalle.put(FECHA_VENCIMIENTO, datos.getString(FECHA_VENCIMIENTO));
        parametersVistaDetalle.put(LOGO_IMAGEN, rcImages.getString(datos.getString(LOGO_IMAGEN)));

        parametros.put(PARAMETROS_VISTA, parametersVista);
        parametros.put(PARAMETROS_VISTA_DETALLE, parametersVistaDetalle);
        return true;
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