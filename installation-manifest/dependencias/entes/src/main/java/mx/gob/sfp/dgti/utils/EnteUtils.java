/**
 * @(#)EnteUtils.java 11/02/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 * 
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.constantes.EstatusControl;
import mx.gob.sfp.dgti.constantes.Situacion;
import mx.gob.sfp.dgti.dto.*;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Utils para ente
 * 
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 11/02/2019
 */
public class EnteUtils {

	/**
	 * Logger
	 */
	final Logger logger = Logger.getLogger(EnteUtils.class);

	/**
	 * Funcion constructor
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public EnteUtils() {

	}

	/**
	 * Funcion para obtener el objeto JsonObject con las condiciones dinamicas necesarias
	 * para obtener los otros entes con la misma llave, formada por los identificadores
	 * existentes.
	 *
	 * @param ente para obtener los ids necesarios
	 * @return JsonObject: objeto con las condiciones necesarias para filtrar en Mongo
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public JsonObject obtenerCondicionesLlave(EnteDTO ente) {
		JsonObject condiciones = new JsonObject();

		condiciones.put("datosDeControl.activo", EstatusControl.ACTIVO.getId());
		if (ente != null) {
			if(ente.getRamo() != null) {
				condiciones.put("ramo", ente.getRamo());
			} else {
				condiciones.put("ramo", new JsonObject().put("$exists",false));
			}
			if(ente.getUnidadResponsable() != null) {
				condiciones.put("unidadResponsable", ente.getUnidadResponsable());
			} else {
				condiciones.put("unidadResponsable", new JsonObject().put("$exists",false));
			}

			if(ente.getPoder() != null) {
				condiciones.put("poder", ente.getPoder());
			} else {
				condiciones.put("poder", new JsonObject().put("$exists",false));
			}

			if(ente.getTipoEntidad() != null) {
				condiciones.put("tipoEntidad", ente.getTipoEntidad());
			} else {
				condiciones.put("tipoEntidad", new JsonObject().put("$exists",false));
			}

			NivelGobiernoDTO nivelGobierno = ente.getNivelGobierno();
			if(nivelGobierno != null && nivelGobierno.getNivelGobierno() != null) {
				condiciones.put("nivelGobierno.nivelGobierno", nivelGobierno.getNivelGobierno());
				EntidadFederativaDTO entidad = nivelGobierno.getEntidadFederativa();
				if(entidad != null && entidad.getIdEntidadFederativa() != null) {
					condiciones.put("nivelGobierno.entidadFederativa.idEntidadFederativa",
							entidad.getIdEntidadFederativa());
					MunicipioDTO municipio = entidad.getMunicipio();
					if(municipio != null && municipio.getIdMunicipio() != null) {
						condiciones.put("nivelGobierno.entidadFederativa.municipio.idMunicipio",
								municipio.getIdMunicipio());
					} else {
						condiciones.put("nivelGobierno.entidadFederativa.municipio.idMunicipio",
								new JsonObject().put("$exists",false));
					}
				} else {
					condiciones.put("nivelGobierno.entidadFederativa.idEntidadFederativa",
							new JsonObject().put("$exists",false));
				}
			} else {
				condiciones.put("nivelGobierno.nivelGobierno",
						new JsonObject().put("$exists",false));
			}


		}

		return condiciones;
	}

	/**
	 * Funcion para obtener el objeto JsonObject con las condiciones dinamicas necesarias
	 * para obtener los otros entes con la misma llave, formada por los identificadores
	 * existentes.
	 *
	 * @param params para obtener los ids necesarios
	 * @return JsonObject: objeto con las condiciones necesarias para filtrar en Mongo
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public JsonObject obtenerCondicionesParam(ParamConsultaEnteDTO params) {
		JsonObject condiciones = new JsonObject();
		if (params != null) {
			if(params.getId() != null) {
				condiciones.put("_id", params.getId());
			}
			if(params.getRamo() != null) {
				condiciones.put("ramo", params.getRamo());
			}
			if(params.getUnidadResponsable() != null) {
				condiciones.put("unidadResponsable", params.getUnidadResponsable());
			}
			if(params.getPoder() != null) {
				condiciones.put("poder", params.getPoder());
			}

			if(params.getNivelGobierno() != null) {
				condiciones.put("nivelGobierno.nivelGobierno", params.getNivelGobierno());
			}
			if(params.getIdEntidadFederativa() != null) {
				condiciones.put("nivelGobierno.entidadFederativa.idEntidadFederativa", params.getIdEntidadFederativa());
			}
			if(params.getIdMunicipio() != null) {
				condiciones.put("nivelGobierno.entidadFederativa.municipio.idMunicipio", params.getIdMunicipio());
			}
			if(params.getTipoEntidad() != null) {
				condiciones.put("tipoEntidad", params.getTipoEntidad());
			}
			if(params.getSegNac() != null) {
				condiciones.put("segNac", params.getSegNac());
			}

			if(params.getSituacion() == null || params.getSituacion().getId().equals(Situacion.NORMAL.getId())) {
				condiciones.put("$or", new JsonArray()
						.add(new JsonObject().put("datosDeControl.situacion", Situacion.NORMAL.getId()))
						.add(new JsonObject().putNull("datosDeControl.situacion"))
						.add(new JsonObject().put("datosDeControl.situacion", new JsonObject().put("$exists",false))));
			} else {
				condiciones.put("datosDeControl.situacion", params.getSituacion());
			}
		}
		logger.info(condiciones);
		return condiciones;
	}

	/**
	 * Funcion para validar si se han hecho cambios en el ente y requiere insertar un nuevo ente
	 *
	 * @param enteNuevo: ente que se esta evaluando, que ha sido editado
	 * @param enteBase: ente con el mismo id pero con el valor que se encuentra
	 * en la base de datos
	 *
	 * @return int: valor
	 * 		0: Actualizar solo info
	 * 		1: Es requerido ente nuevo, forzoso
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public int evaluarEntes(EnteDTO enteNuevo, EnteDTO enteBase) {

		if(enteNuevo.getEnteDesc() != null && enteBase.getEnteDesc() != null) {
			if(enteNuevo.getNombreCorto() != null && enteBase.getNombreCorto() != null) {

				if(enteNuevo.getRamo() != null) {
					if(!enteNuevo.getRamo().equals(enteBase.getRamo())){
						return 1;
					}
				} else if(!(enteBase.getRamo() == null)) {
					return 1;
				}

				if(enteNuevo.getUnidadResponsable() != null) {
					if(!enteNuevo.getUnidadResponsable().equals(enteBase.getUnidadResponsable())){
						return 1;
					}
				} else if(!(enteBase.getUnidadResponsable() == null)) {
					return 1;
				}
				if (enteNuevo.getRfc() != null) {
					if(!enteNuevo.getRfc().equals(enteBase.getRfc())) {
						return 1;
					}
				} else if(enteBase.getRfc()!= null) {
					return 1;
				}

				if (enteNuevo.getPoder() != null) {
					if (!enteNuevo.getPoder().equals(enteBase.getPoder())) {
						return 1;
					}
				} else if(!(enteBase.getPoder() == null)) {
					return 1;
				}

				if (enteNuevo.getTipoEntidad() != null) {
					if (!enteNuevo.getTipoEntidad().equals(enteBase.getTipoEntidad())) {
						return 1;
					}
				} else if(!(enteBase.getTipoEntidad() == null)) {
					return 1;
				}

				if (enteNuevo.getNivelGobierno() != null) {
					if (!enteNuevo.getNivelGobierno().equals(enteBase.getNivelGobierno())) {
						return 1;
					}
				} else if(!(enteBase.getNivelGobierno() == null)) {
					return 1;
				}

				JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();

				Double distanciaDesc = jaroWinkler.apply(enteNuevo.getEnteDesc(), enteBase.getEnteDesc());
				Double distanciaNombreCorto = jaroWinkler.apply(enteNuevo.getNombreCorto(), enteBase.getNombreCorto());

				logger.info("distanciaDesc: " + distanciaDesc);
				logger.info("distanciaNombreCorto: " + distanciaNombreCorto);

				if (distanciaDesc.doubleValue() < 0.9 || distanciaNombreCorto.doubleValue() < 0.9) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * Funcion para validar si se han hecho cambios en el ente y requiere insertar un nuevo ente
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public void agregarDatosDeControl(JsonObject enteJson) {
		String fechaAccion =
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
					.format(Calendar.getInstance().getTime());

		JsonObject datosDeControl = enteJson.getJsonObject("datosDeControl");
		if(datosDeControl == null) {
			enteJson.put("datosDeControl", new JsonObject());
		}
		if(enteJson.getString("_id") != null && !enteJson.getString("_id").isEmpty()) {
			//No es nuevo registro
			enteJson.getJsonObject("datosDeControl")
				.put("fechaUltimaActualiza", new JsonObject().put("$date",fechaAccion));
		} else {
			//Es nuevo registro
			enteJson.getJsonObject("datosDeControl")
				.put("fechaRegistro", new JsonObject().put("$date",fechaAccion));
			enteJson.getJsonObject("datosDeControl")
				.put("fechaUltimaActualiza", new JsonObject().put("$date",fechaAccion));
			if(enteJson.getJsonObject("datosDeControl").getJsonObject("usuarioRegistra") == null) {
				enteJson.getJsonObject("datosDeControl")
						.put("usuarioRegistra",
								enteJson.getJsonObject("datosDeControl").getInteger("usuarioActualiza"));
			}

		}
		enteJson.getJsonObject("datosDeControl").remove("fechaBaja");
		enteJson.getJsonObject("datosDeControl").put("activo", EstatusControl.ACTIVO.getId());
	}

	/**
	 * Funcion para inicializar el JsonObject con los datos que se van a actualizar
	 * para una baja.
	 *
	 * @param actualizableJson: datos que se actualizaran en la baja
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 22/02/2019
	 */
	public void agregarDatosBaja(JsonObject actualizableJson) {
		actualizableJson.put("$set",
				new JsonObject()
						.put("datosDeControl.activo", EstatusControl.INACTIVO.getId())
						.put("datosDeControl.fechaBaja", new JsonObject()
								.put("$date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
									.format(Calendar.getInstance().getTime()))));
	}

	/**
	 * Funcion para inicializar el JsonObject con los datos que se van a actualizar
	 * para una definir la situacion.
	 *
	 * @param actualizableJson: datos que se actualizaran
	 * @param situacion: situacion que se actualizara
	 *
	 * @author Pavel Alexei Martinez Regalado aka programador02
	 * @since 23/04/2020
	 */
	public void agregarDatosSituacion(JsonObject actualizableJson, Situacion situacion) {

		actualizableJson.put("$set",
				new JsonObject().put("datosDeControl.situacion", situacion)
						.putNull("datosDeControl.fechaBaja")
						.put("datosDeControl.activo", 1)
						.put("datosDeControl.fechaUltimaActualiza",
								new JsonObject().put("$date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
										.format(Calendar.getInstance().getTime()))));
	}

}
