package mx.gob.sfp.dgti.verticle;

import static mx.gob.sfp.dgti.util.Constantes.ADDRESS;
import static mx.gob.sfp.dgti.util.Constantes.ANIO;
import static mx.gob.sfp.dgti.util.Constantes.COLLNAME;
import static mx.gob.sfp.dgti.util.Constantes.CORREO_ALTERNO;
import static mx.gob.sfp.dgti.util.Constantes.CORREO_ELECTRONICO;
import static mx.gob.sfp.dgti.util.Constantes.CORREO_INSTITUCIONAL;
import static mx.gob.sfp.dgti.util.Constantes.DATOS_GENERALES;
import static mx.gob.sfp.dgti.util.Constantes.DATOS_PERSONALES;
import static mx.gob.sfp.dgti.util.Constantes.DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.DECLARACION_ORIGEN;
import static mx.gob.sfp.dgti.util.Constantes.EMPTY;
import static mx.gob.sfp.dgti.util.Constantes.ENCABEZADO;
import static mx.gob.sfp.dgti.util.Constantes.ESPACIO;
import static mx.gob.sfp.dgti.util.Constantes.ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.NUMERO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.RFC;
import static mx.gob.sfp.dgti.util.Constantes.TIPO_DECLARACION;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dto.FirmaDTO;
import mx.gob.sfp.dgti.service.GraphServiceImpl;
import mx.gob.sfp.dgti.util.Constantes;
import mx.gob.sfp.dgti.util.EnumTipoDeclaracion;

public class MailServer extends Server {

	/**
	 * Start del verticle
	 */
	@Override
	public void start(Future<Void> future) throws Exception {
		super.start();

		vertx.eventBus().consumer(ADDRESS, message -> {
			
			JsonObject jsonRecibido = new JsonObject(message.body().toString());
			FirmaDTO firma = new FirmaDTO(jsonRecibido);
			
			String ente = jsonRecibido.getString(Constantes.ENTE);
			JsonObject datosPersonales = null;
			JsonObject datosGenerales = null;
			Integer anio = null;
			
			if(EnumTipoDeclaracion.NOTA.name().equals(firma.getDigestion().getDeclaracion().getJsonObject(ENCABEZADO).getString(TIPO_DECLARACION))) {
				datosPersonales = firma.getDigestion().getDeclaracion()
						.getJsonObject(ENCABEZADO)
						.getJsonObject(DATOS_PERSONALES);
				anio = firma.getDigestion().getDeclaracion()
						.getJsonObject(ENCABEZADO)
						.getJsonObject(DECLARACION_ORIGEN)
						.getJsonObject(ENCABEZADO)
						.getInteger(ANIO);
				
			} else {
				datosGenerales = firma.getDigestion().getDeclaracion().getJsonObject(DECLARACION).getJsonObject(DATOS_GENERALES);
				anio = firma.getDigestion().getDeclaracion().getJsonObject(ENCABEZADO).getInteger(ANIO);
			}
			
			String correoInstitucional = datosGenerales == null 
					? datosPersonales.getString(CORREO_INSTITUCIONAL) 
					: datosGenerales.getJsonObject(CORREO_ELECTRONICO).getString(CORREO_INSTITUCIONAL);
					
			String correoAlterno = datosGenerales == null 
					? datosPersonales.getString(CORREO_ALTERNO) 
					: datosGenerales.getJsonObject(CORREO_ELECTRONICO).getString(CORREO_ALTERNO);
					
			String rfc = datosGenerales == null 
					? datosPersonales.getString(RFC) 
					: datosGenerales.getJsonObject(DATOS_PERSONALES).getString(RFC);
			
			String correo = correoInstitucional != null ? correoInstitucional : correoAlterno;
			
			if(!EMPTY.equals(correo)) {
				
				String tipoDeclaracionComp = firma.getDigestion().getDeclaracion().getJsonObject(ENCABEZADO)
						.getString(TIPO_DECLARACION) + ESPACIO + anio;
				
				GraphServiceImpl graph = new GraphServiceImpl();
				JsonObject json = new JsonObject();
				json.put(NUMERO_DECLARACION, firma.getDigestion().getNumeroDeclaracion());
				json.put(ID_USUARIO, firma.getDigestion().getIdUsuario());
				json.put(COLLNAME, firma.getDigestion().getCollName());
				
				obtenerAcuseYDeclaracion(json, URL_CONSULTA_ACUSE).setHandler(acuseRes -> {
					if(acuseRes.succeeded()) {
						
						obtenerAcuseYDeclaracion(json, URL_CONSULTA_DECLA_REPORTE).setHandler(reporteRes -> {
							if(reporteRes.succeeded()) {
								
								String acuse = acuseRes.result().replace("\"", EMPTY);
								String reporte = reporteRes.result().replace("\"", EMPTY);
								
								graph.consumirServicio(correo,	firma.getTipoFirma().toString(), rfc, tipoDeclaracionComp, firma.getFechaRecepcion(), acuse, reporte, ente);
							
							} else {
								LOGGER.error("[OBTENER REPORTE DECLARACION != 200] " + reporteRes.cause().getMessage());
							}
						});
						
					} else {
						LOGGER.error("[OBTENER ACUSE != 200] " + acuseRes.cause().getMessage());
					}
				});
			}
			
		});
	}
}
