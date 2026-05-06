package mx.gob.sfp.dgti.services;

import static mx.gob.sfp.dgti.util.Constantes.BOOLEAN_TRUE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_BITACORA;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_BITACORA_FALSE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_BITACORA_TRUE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_DATOS_PETICION;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_ACUSE;
import static mx.gob.sfp.dgti.util.Constantes.ERROR_OBTENER_PDF;
import static mx.gob.sfp.dgti.util.Constantes.MENSAJE_DECLA_PDF_EXITO;
import static mx.gob.sfp.dgti.util.Constantes.MENSAJE_HISTORIAL_EXITO;
import static mx.gob.sfp.dgti.util.Constantes.PATTERN_CURP;
import static mx.gob.sfp.dgti.util.Constantes.PATTERN_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.PATTERN_RFC;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ANIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_CABECERA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_COLL_NAME;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DATA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_DEPENDENCIA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ENTE_CARGO_COMISION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ESTATUS;
import static mx.gob.sfp.dgti.util.Constantes.PROP_FECHA_RECEPCION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_HISTORIAL;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USR_DECNET;
import static mx.gob.sfp.dgti.util.Constantes.PROP_ID_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_INSTITUCION_RECEPTORA;
import static mx.gob.sfp.dgti.util.Constantes.PROP_JSON_A_CONSULTAR;
import static mx.gob.sfp.dgti.util.Constantes.PROP_JUSTIFICACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_MENSAJE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NOMBRE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUMERO_COMPROBANTE;
import static mx.gob.sfp.dgti.util.Constantes.PROP_NUM_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_RESERVADO;
import static mx.gob.sfp.dgti.util.Constantes.PROP_TIPO_DECLARACION;
import static mx.gob.sfp.dgti.util.Constantes.PROP_USUARIO;
import static mx.gob.sfp.dgti.util.Constantes.SOLICITUD_EXITOSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import mx.gob.sfp.dgti.daopxy.DAOConsultaHistorialInterface;
import mx.gob.sfp.dgti.dto.DomicilioDTO;
import static mx.gob.sfp.dgti.util.Constantes.PATTERN_RFC_DIEZ;
import mx.gob.sfp.dgti.util.EnumTipoSolicitud;

public class ServiceConsultaHistorialImpl implements ServiceConsultaHistorialInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsultaHistorialImpl.class);
	private static final String URL_DECLARACION_PDF = System.getenv("URL_DECLARACION") != null
			? System.getenv("URL_DECLARACION")
			: "";
	private static final String URL_ACUSE_PDF = System.getenv("URL_ACUSE") != null ? System.getenv("URL_ACUSE") : "";
	private static final String URL_DECLA_DOMICILIOS = System.getenv("URL_DECLA_DOMICILIOS") != null ? System.getenv("URL_DECLA_DOMICILIOS") : "";
	private static final String TIPO_SOLICITUD = "tipoSolicitud";
	private static final String DATO_SOLICITADO = "datoSolicitado";
	private final DAOConsultaHistorialInterface daoConsulta;
	private final WebClient client;
	
	public ServiceConsultaHistorialImpl(Vertx vertx, WebClient client) {
		daoConsulta = DAOConsultaHistorialInterface.create(vertx);
		this.client = client;
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public Future<JsonObject> consultaHistorial(String curp, String rfc, String nombre, String idUsrDecnet,
			Integer collName, JsonObject usuario) {
		return Future.future(consulta -> {
			JsonObject bit = new JsonObject();
			if (nombre == null && (rfc == null || Pattern.matches(PATTERN_RFC, rfc))) {
				Integer opcion = seleccionarOpcion(curp,rfc,idUsrDecnet);
				String datoCapturado = seleccionarDatoCapturado(curp,rfc,idUsrDecnet, opcion);
				if (verificarParametro(opcion, datoCapturado)) {
					daoConsulta.busquedaHistorialIdUsuario(datoCapturado, opcion, collName).setHandler(cons -> {
						bit.put(TIPO_SOLICITUD,obtenerTipoSolicitud(opcion));
						bit.put(DATO_SOLICITADO, datoCapturado);
						bit.put(PROP_USUARIO, usuario);
						if (cons.succeeded()) {
							daoConsulta.guardarBitacoraOPER(bit, true, collName).setHandler(bitacora -> {
								if (bitacora.succeeded()) {
									consulta.handle(Future.succeededFuture(crearHistorialConNotas(cons.result())));
								} else {
									LOGGER.error(ERROR_BITACORA_TRUE);
									LOGGER.info(bitacora.cause());
									consulta.fail(ERROR_BITACORA);
								}
							});
						} else {
							daoConsulta.guardarBitacoraOPER(bit, false, collName).setHandler(bitacora -> {
								if (bitacora.succeeded()) {
									LOGGER.error("Historial no obtenido, bitacora false");
									LOGGER.info(cons.cause());
									consulta.fail("Historial no obtenido");
								} else {
									LOGGER.error(ERROR_BITACORA_FALSE);
									LOGGER.info(bitacora.cause());
									consulta.fail(ERROR_BITACORA);
								}
							});
						}
					});
				} else {
					LOGGER.error("El dato no es correcto");
					consulta.fail("El dato proporcionado no es corrrecto");
				}
			} else if ( (nombre != null && Pattern.matches(PATTERN_NOMBRE,nombre)) || (rfc != null && Pattern.matches(PATTERN_RFC_DIEZ, rfc))) {
				daoConsulta.busquedaPorNombreOrfc(genererBusquedaNombre(nombre), rfc, collName).setHandler(busqueda -> {
					bit.put(TIPO_SOLICITUD, nombre != null ? EnumTipoSolicitud.CONSULTA_NOMBRE.name() : EnumTipoSolicitud.CONSULTA_RFC_DIEZ_POS.name());
					bit.put(DATO_SOLICITADO, nombre != null ? nombre : rfc);
					bit.put(PROP_USUARIO, usuario);
					if (busqueda.succeeded()) {
						daoConsulta.guardarBitacoraOPER(bit, true, collName).setHandler(bitacora -> {
							if (bitacora.succeeded()) {
								if (busqueda.result() != null) {
									consulta.handle(Future.succeededFuture(
											crearRespuestaExitosa(verificarReservados(busqueda.result().getList()),
													busqueda.result().size())));
								} else {
									consulta.handle(
											Future.succeededFuture(crearRespuestaExitosa(new ArrayList<>(), 0)));
								}
							} else {
								LOGGER.error(ERROR_BITACORA_TRUE);
								consulta.fail(ERROR_BITACORA);
							}
						});

					} else {
						daoConsulta.guardarBitacoraOPER(bit, false, collName).setHandler(bitacora -> {
							if (bitacora.succeeded()) {
								LOGGER.error("Busqueda por normbre no obtenida, bitacora false");
								consulta.fail("Busqueda por normbre no obtenida");
							} else {
								LOGGER.error(ERROR_BITACORA_FALSE);
								consulta.fail(ERROR_BITACORA);
							}
						});
					}
				});
			} else {
				LOGGER.error("El dato no es correcto");
				consulta.fail("El dato proporcionado no es corrrecto");
			}
		});
	}

	public List<JsonObject> generarJsonHistorial(JsonArray histoData) {
		List<JsonObject> historial = new ArrayList<>();
		histoData.stream().forEach(data -> {
			JsonObject historico = new JsonObject()
					.put(PROP_ID_USUARIO,
							((JsonObject) data).getJsonObject(PROP_DECLARANTE).getInteger(PROP_ID_USR_DECNET))
					.put(PROP_NUM_DECLARACION,
							((JsonObject) data).getJsonObject(PROP_DECLARACION).getString(PROP_NUM_DECLARACION))
					.put(PROP_FECHA_RECEPCION,
							((JsonObject) data).getJsonObject(PROP_DECLARACION).getString(PROP_FECHA_RECEPCION)
									.substring(0, 10))
					.put(PROP_COLL_NAME,
							((JsonObject) data).getJsonObject(PROP_INSTITUCION_RECEPTORA).getInteger(PROP_COLL_NAME))
					.put(PROP_TIPO_DECLARACION,
							((JsonObject) data).getJsonObject(PROP_DECLARACION).getString(PROP_TIPO_DECLARACION))
					.put(PROP_NUMERO_COMPROBANTE,
							((JsonObject) data).getJsonObject(PROP_DECLARACION).getString(PROP_NUMERO_COMPROBANTE))
					.put(PROP_ANIO, ((JsonObject) data).getJsonObject(PROP_DECLARACION).getInteger(PROP_ANIO));
			
			if(((JsonObject) data).getString("declaOrigen") != null) {
				historico.put("declaOrigen", ((JsonObject) data).getString("declaOrigen"));
			}

			if (((JsonObject) data).getJsonObject(PROP_DECLARANTE).getString(PROP_DEPENDENCIA) != null) {
				historico.put(PROP_INSTITUCION_RECEPTORA,
						((JsonObject) data).getJsonObject(PROP_DECLARANTE).getString(PROP_DEPENDENCIA).toUpperCase());
			} else if (((JsonObject) data).getJsonObject(PROP_DECLARANTE)
					.getJsonObject(PROP_ENTE_CARGO_COMISION) != null) {
				historico.put(PROP_INSTITUCION_RECEPTORA, ((JsonObject) data).getJsonObject(PROP_DECLARANTE)
						.getJsonObject(PROP_ENTE_CARGO_COMISION).getString(PROP_DEPENDENCIA).toUpperCase());
			}

			
			historial.add(historico);
		});
		return historial;
	}
	
	@Override
	public Future<JsonObject> obtenerDomicilios(String numeroDeclaracion, Integer collName, JsonObject jsonBody, Integer idUsuario) {
		return Future.future(declaracion -> {
			consultaDomicilios(numeroDeclaracion, collName, idUsuario).setHandler(domi ->{
				JsonObject bit = new JsonObject();
				bit.put(TIPO_SOLICITUD, EnumTipoSolicitud.DOMICILIOS_EN_DECLARACION.name());
				bit.put(DATO_SOLICITADO, numeroDeclaracion);
				bit.put(PROP_USUARIO, jsonBody.getJsonObject(PROP_USUARIO));
				bit.put(PROP_JUSTIFICACION, jsonBody.getString(PROP_JUSTIFICACION));
				if(domi.succeeded()) {
					List<DomicilioDTO> domicilios = new ArrayList<DomicilioDTO>();
					
					crearArrayDocimilios(domi.result().getJsonObject("domicilioDeclarante").getJsonObject("domicilio"),  "domicilioDeclarante", domicilios, "DECLARANTE");
					
					if(domi.result().getJsonObject("datosEmpleoCargoComision") != null
							&& domi.result().getJsonObject("datosEmpleoCargoComision").getJsonArray("empleoCargoComision") != null 
							&& !domi.result().getJsonObject("datosEmpleoCargoComision").getJsonArray("empleoCargoComision").isEmpty()) {
						domi.result().getJsonObject("datosEmpleoCargoComision").getJsonArray("empleoCargoComision").stream().forEach(e -> {
							crearArrayDocimilios( ((JsonObject) e).getJsonObject("domicilio"),  "datosEmpleoCargoComision", domicilios, null);
						});
					}
					
					if( domi.result().getJsonObject("datosParejas") != null &&
						!domi.result().getJsonObject("datosParejas").getBoolean("ninguno")) {
						if(domi.result().getJsonObject("datosParejas").getJsonArray("datosParejas") != null 
							&& !domi.result().getJsonObject("datosParejas").getJsonArray("datosParejas").isEmpty()) {
							domi.result().getJsonObject("datosParejas").getJsonArray("datosParejas").stream().forEach(e -> {
								if( ((JsonObject) e).getJsonObject("domicilioDependienteEconomico") != null) {
									crearArrayDocimilios(((JsonObject) e).getJsonObject("domicilioDependienteEconomico").getJsonObject("domicilio"),  "datosParejas", domicilios, "pareja");
								}
							});
						}
					}
					if(domi.result().getJsonObject("bienesInmuebles") != null
						&& !domi.result().getJsonObject("bienesInmuebles").getBoolean("ninguno")) {
						if(domi.result().getJsonObject("bienesInmuebles").getJsonArray("bienesInmuebles") != null
							&& !domi.result().getJsonObject("bienesInmuebles").getJsonArray("bienesInmuebles").isEmpty()) {
							domi.result().getJsonObject("bienesInmuebles").getJsonArray("bienesInmuebles").stream().forEach(e -> {
								crearArrayDocimilios(((JsonObject) e).getJsonObject("domicilio"),  "bienesInmuebles", domicilios, ((JsonObject) e).getJsonObject("titular").getString("valor"));
							});
						}
					}
					if(domi.result().getJsonObject("prestamoComodato") != null && 
						!domi.result().getJsonObject("prestamoComodato").getBoolean("ninguno")) {
						if(domi.result().getJsonObject("prestamoComodato").getJsonArray("prestamo") != null 
							&& !domi.result().getJsonObject("prestamoComodato").getJsonArray("prestamo").isEmpty()) {
							domi.result().getJsonObject("prestamoComodato").getJsonArray("prestamo").stream().forEach(e -> {
								if(((JsonObject)e).getJsonObject("inmueble") !=  null) {
									String propietario = "sin declarar";
									if(((JsonObject)e).getJsonObject("duenoTitular").getJsonObject("personaFisica") != null) {
										propietario = ((JsonObject)e).getJsonObject("duenoTitular").getJsonObject("personaFisica").getString("nombre");
									}
									
									if(((JsonObject)e).getJsonObject("duenoTitular").getJsonObject("personaMoral") != null) {
										propietario = ((JsonObject)e).getJsonObject("duenoTitular").getJsonObject("personaMoral").getString("nombre");
									}
									crearArrayDocimilios(((JsonObject) e).getJsonObject("inmueble").getJsonObject("domicilio"),  "prestamoComodato", domicilios, propietario);
								}
							});
						}
					}
					daoConsulta.guardarBitacoraOPER(bit, true, collName).setHandler(bitacora -> {
						if (bitacora.succeeded()) {
							declaracion.handle(Future.succeededFuture(new JsonObject().put("estatus", BOOLEAN_TRUE).put("domicilios", new JsonArray(domicilios)))); 
						} else {
							LOGGER.error(ERROR_BITACORA_FALSE);
							declaracion.fail(ERROR_BITACORA);
						}
					});
				}else {
					LOGGER.fatal("No se logró obtener domicilios {0}",domi.cause());
					daoConsulta.guardarBitacoraOPER(bit, false, collName).setHandler(bitacora -> {
						if (bitacora.succeeded()) {
							LOGGER.error("Busqueda de declaración no éxitosa, bitacora false");
							declaracion.fail("Busqueda de declaración no éxitosa");
						} else {
							LOGGER.error(ERROR_BITACORA_FALSE);
							declaracion.fail(ERROR_BITACORA);
						}
					});
				}
			});
		});
		
	}

	public JsonObject crearRespuestaExitosa(JsonObject historial) {
		return new JsonObject().put(PROP_ESTATUS, BOOLEAN_TRUE).put(PROP_MENSAJE, MENSAJE_HISTORIAL_EXITO)
				.put(PROP_CABECERA, new JsonObject().put(PROP_RESERVADO,
						historial.getJsonObject(PROP_CABECERA).getInteger(PROP_RESERVADO) == null).put(PROP_NOMBRE, historial.getJsonObject(PROP_CABECERA).getString(PROP_NOMBRE)))
				.put(PROP_HISTORIAL, generarJsonHistorial(historial.getJsonArray(PROP_HISTORIAL)));
	}

	public boolean verificarParametro(Integer opcion, String dato) {
		boolean verificar = false;

		if (opcion == 1 && Pattern.matches(PATTERN_CURP, dato)) {
			verificar = true;
		} else if (opcion == 2 && ((Pattern.matches(PATTERN_RFC, dato) || Pattern.matches(PATTERN_RFC_DIEZ, dato)) && !dato.contains(" ") && dato.length() < 14)) {
			verificar = true;
		} else if (opcion == 3) {
			try {
				if (Integer.parseInt(dato) > 0) {
					verificar = true;
				}
			} catch (Exception e) {
				LOGGER.error("No se puede parsear el dato");
				return verificar;
			}

		}
		return verificar;
	}

	@Override
	public Future<JsonObject> obtenerDeclaracion(JsonObject parametros) {
		return Future.future(declaracion -> {
			if (validaJsonObtenerDeclaracionAcuse(parametros, true)) {

				consultaDeclaracionPDF(parametros.getJsonObject(PROP_JSON_A_CONSULTAR)).setHandler(pdf -> {
					if (pdf.succeeded()) {
						parametros.put(TIPO_SOLICITUD, EnumTipoSolicitud.DECLARACION.name());
						daoConsulta
								.guardarBitacoraOPER(parametros, true, parametros.getJsonObject(PROP_JSON_A_CONSULTAR)
										.getJsonObject(PROP_DECLARACION).getInteger(PROP_COLL_NAME))
								.setHandler(bitacora -> {
									if (bitacora.succeeded()) {
										declaracion.handle(Future
												.succeededFuture(crearRespuestaExitosaDeclaracionPDF(pdf.result())));
									} else {
										LOGGER.error(ERROR_BITACORA_TRUE);
										declaracion.fail(ERROR_BITACORA);
									}
								});
					} else {
						daoConsulta
								.guardarBitacoraOPER(parametros, false, parametros.getJsonObject(PROP_JSON_A_CONSULTAR)
										.getJsonObject(PROP_DECLARACION).getInteger(PROP_COLL_NAME))
								.setHandler(bitacora -> {
									if (bitacora.succeeded()) {
										LOGGER.error("Declaración pdf no obtenida, bitacora false");
										declaracion.fail("Declaración pdf no obtenida");
									} else {
										LOGGER.error(ERROR_BITACORA_FALSE);
										declaracion.fail(ERROR_BITACORA);
									}
								});
					}
				});
			} else {
				LOGGER.error(ERROR_DATOS_PETICION);
				declaracion.fail(ERROR_DATOS_PETICION);
			}
		});
	}

	@Override
	public Future<JsonObject> obtenerAcuse(JsonObject parametros) {
		return Future.future(declaracion -> {
			if (validaJsonObtenerDeclaracionAcuse(parametros, false)) {
				consultaAcusePDF(parametros.getJsonObject(PROP_JSON_A_CONSULTAR)).setHandler(pdf -> {
					if (pdf.succeeded()) {
						parametros.put(TIPO_SOLICITUD, EnumTipoSolicitud.ACUSE.name());
						daoConsulta
								.guardarBitacoraOPER(parametros, true, parametros.getJsonObject(PROP_JSON_A_CONSULTAR)
										.getJsonObject(PROP_DECLARACION).getInteger(PROP_COLL_NAME))
								.setHandler(bitacora -> {
									if (bitacora.succeeded()) {
										declaracion.handle(Future
												.succeededFuture(crearRespuestaExitosaDeclaracionPDF(pdf.result())));
									} else {
										LOGGER.error(ERROR_BITACORA_TRUE);
										declaracion.fail(ERROR_BITACORA);
									}
								});
					} else {
						daoConsulta
								.guardarBitacoraOPER(parametros, false, parametros.getJsonObject(PROP_JSON_A_CONSULTAR)
										.getJsonObject(PROP_DECLARACION).getInteger(PROP_COLL_NAME))
								.setHandler(bitacora -> {
									if (bitacora.succeeded()) {
										LOGGER.error("Acuse pdf no obtenido, bitacora false");
										declaracion.fail("Acuse no obtenido");
									} else {
										LOGGER.error(ERROR_BITACORA_FALSE);
										declaracion.fail(ERROR_BITACORA);
									}
								});
					}
				});
			} else {
				LOGGER.error(ERROR_DATOS_PETICION);
				declaracion.fail(ERROR_DATOS_PETICION);
			}
		});
	}

	public JsonObject crearRespuestaExitosa(List<JsonObject> datos, int size) {
		return new JsonObject().put(PROP_ESTATUS, BOOLEAN_TRUE)
				.put(PROP_MENSAJE, size == 200 ? "200" : SOLICITUD_EXITOSA).put(PROP_DATA, datos);
	}

	public JsonObject crearRespuestaExitosaDeclaracionPDF(String delcaracionPDF) {
		return new JsonObject().put(PROP_ESTATUS, BOOLEAN_TRUE).put(PROP_MENSAJE, MENSAJE_DECLA_PDF_EXITO)
				.put(PROP_DATA, delcaracionPDF.substring(1, delcaracionPDF.length() - 1));
	}

	public boolean validaJsonObtenerDeclaracionAcuse(JsonObject params, boolean justificacion) {
		boolean val = true;

		if (params.getJsonObject(PROP_USUARIO) == null
				|| (justificacion && params.getString(PROP_JUSTIFICACION) == null)
				|| params.getJsonObject(PROP_JSON_A_CONSULTAR) == null) {
			val = false;
		}
		return val;
	}

	private Future<String> consultaDeclaracionPDF(JsonObject params) {
		return Future.future(declaracion -> 
			client.postAbs(URL_DECLARACION_PDF).timeout(20000).sendJsonObject(params, res -> {
				if (res.succeeded() && res.result().statusCode() == HttpResponseStatus.OK.code()) {
					declaracion.handle(Future.succeededFuture(res.result().bodyAsString()));
				} else {
					LOGGER.error(ERROR_OBTENER_PDF);
					LOGGER.error(res);
					declaracion.fail(ERROR_OBTENER_PDF);
				}
			})
		);
	}

	private Future<String> consultaAcusePDF(JsonObject params) {
		return Future.future(declaracion -> 
			client.postAbs(URL_ACUSE_PDF).timeout(20000).sendJsonObject(params, res -> {
				if (res.succeeded() && res.result().statusCode() == HttpResponseStatus.OK.code()) {
					declaracion.handle(Future.succeededFuture(res.result().bodyAsString()));
				} else {
					LOGGER.error(ERROR_OBTENER_ACUSE);
					LOGGER.error(res);
					declaracion.fail(ERROR_OBTENER_ACUSE);
				}
			})
		);
	}

	public String genererBusquedaNombre(String nombre) {
            if (nombre != null){
		StringBuilder strNombre = new StringBuilder();
		for (String palabra : nombre.split(" ")) {
			strNombre.append("\"").append(palabra).append("\" ");
		}
		strNombre.delete(strNombre.length() - 1, strNombre.length());
		return strNombre.toString();
            }
            return nombre;
	}

	public List<JsonObject> verificarReservados(List<JsonObject> list) {
		list.stream().forEach(item -> 
			item.put(PROP_RESERVADO, item.getInteger(PROP_RESERVADO) == null)
		);

		return list;
	}
	
	public Integer seleccionarOpcion(String curp, String rfc, String idUsrDecnet) {
		Integer opcion = null;
		
		if(curp != null) {
			opcion = 1;
		}else if(rfc != null) {
			opcion = 2;
		}else if(idUsrDecnet != null) {
			opcion = 3;
		}
		
		return opcion;
	}
	
	public String seleccionarDatoCapturado(String curp, String rfc, String idUsrDecnet, Integer opcion) {
		String datoCapturado = null;
		
		if(opcion == 1) {
			datoCapturado = curp;
		}else if(opcion == 2) {
			datoCapturado = rfc;
		}else if(opcion == 3) {
			datoCapturado = idUsrDecnet;
		}
		
		return datoCapturado;
	}
	
	public String obtenerTipoSolicitud(int opcion) {
		String tipo = "";
		
		if(opcion == 1) {
			tipo = EnumTipoSolicitud.HISTORIAL_CURP.name();
		}else if(opcion == 2) {
			tipo = EnumTipoSolicitud.HISTORIAL_RFC.name();
		}else if(opcion == 3) {
			tipo = EnumTipoSolicitud.HISTORIAL_ID_USR.name();
		}
		
		return tipo;
	}
	
	public JsonObject crearHistorialConNotas(JsonObject histoCompleto) {
		List<JsonObject> historial = generarJsonHistorial(histoCompleto.getJsonArray("historial"));
		Map<String, JsonArray> notas = new HashMap<>();
		List<JsonObject> historial1 = historial.stream()
			.peek(item->{
				if("NOTA".equals(item.getString("tipoDeclaracion"))) {
					if(notas.containsKey(item.getString("declaOrigen"))) {
						notas.put(item.getString("declaOrigen"), notas.get(item.getString("declaOrigen")).add(item));
					}else {
						JsonArray nts = new JsonArray();
						nts.add(item);
						notas.put(item.getString("declaOrigen"), nts);
					}
				}
			})
			.filter(fil -> "INICIO".equals(fil.getString("tipoDeclaracion")) 
                                || "AVISO".equals(fil.getString("tipoDeclaracion"))
                                || "CONCLUSION".equals(fil.getString("tipoDeclaracion")) 
                                || "MODIFICACION".equals(fil.getString("tipoDeclaracion")))
			.collect(Collectors.toList());
		
		historial1.stream().forEach(item -> {
					item.put("notas", notas.get(item.getString("numeroDeclaracion")));	
		});
		
		return new JsonObject().put("estatus", true).put("mensaje", "Historial creado con éxito")
				.put("cabecera", histoCompleto.getJsonObject("cabecera")).put("historial", historial1);

	}
	
	private Future<JsonObject> consultaDomicilios(String numeroDeclaracion, Integer collName, Integer idUsuario) {
		JsonObject consulta = new  JsonObject();
		consulta.put("numeroDeclaracion", numeroDeclaracion);
		consulta.put("collName", collName);
		consulta.put("idUsuario", idUsuario);
		return Future.future(declaracion -> 
			client.postAbs(URL_DECLA_DOMICILIOS).timeout(20000).sendJsonObject(consulta, res -> {
				if (res.succeeded() && res.result().statusCode() == HttpResponseStatus.OK.code()) {
					declaracion.handle(Future.succeededFuture(res.result().bodyAsJsonObject().getJsonObject("declaracion")));
				} else {
					LOGGER.error("Error obteniendo la declaración parta domicilios");
					LOGGER.error(res.cause());
					declaracion.fail("Error obteniendo la declaración parta domicilios");
				}
			})
		);
	}
	
	public void crearArrayDocimilios(JsonObject domicilio, String seccion,List<DomicilioDTO> domicilios, String propietario) {
		DomicilioDTO domi = new DomicilioDTO();
		domi.setPropietario(propietario);
		domi.setSeccion(seccion);
		
		if("MEXICO".equals(domicilio.getString("ubicacion"))) {
			domi.setUbicacion("MÉXICO");
			domi.setPais("MÉXICO");
			domi.setEntidadProvincia(domicilio.getJsonObject("domicilioMexico").getJsonObject("entidadFederativa").getString("valor"));
			domi.setMunicipioAlcaldia(domicilio.getJsonObject("domicilioMexico").getJsonObject("municipioAlcaldia").getString("valor"));
			domi.setCiudadLocalidad(domicilio.getJsonObject("domicilioMexico").getString("coloniaLocalidad"));
			domi.setCalle(domicilio.getJsonObject("domicilioMexico").getString("calle"));
			domi.setCodigoPostal(domicilio.getJsonObject("domicilioMexico").getString("codigoPostal"));
			domi.setNumeroExterior(domicilio.getJsonObject("domicilioMexico").getString("numeroExterior"));
			domi.setNumeroInterior(domicilio.getJsonObject("domicilioMexico").getString("numeroInterior"));
		}else if("EXTRANJERO".equals(domicilio.getString("ubicacion"))) {
			domi.setUbicacion("EXTRANJERO");
			domi.setPais(domicilio.getJsonObject("domicilioExtranjero").getJsonObject("pais").getString("valor"));
			domi.setEntidadProvincia(domicilio.getJsonObject("domicilioExtranjero").getJsonObject("estadoProvincia").getString("valor"));
			domi.setCiudadLocalidad(domicilio.getJsonObject("domicilioExtranjero").getString("ciudadLocalidad"));
			domi.setCalle(domicilio.getJsonObject("domicilioExtranjero").getString("calle"));
			domi.setCodigoPostal(domicilio.getJsonObject("domicilioExtranjero").getString("codigoPostal"));
			domi.setNumeroExterior(domicilio.getJsonObject("domicilioExtranjero").getString("numeroExterior"));
			domi.setNumeroInterior(domicilio.getJsonObject("domicilioExtranjero").getString("numeroInterior"));
		}
		
		domicilios.add(domi);
	}

}
