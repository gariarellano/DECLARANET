/**
 * @(#)Propiedades.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.util.campos.EnumCampos;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
public class Propiedades {

	private Propiedades() {
		throw new IllegalStateException("Utility class");
	}

	private static final DateTimeFormatter isoFecha = DateTimeFormatter.ISO_LOCAL_DATE;

	/**
	 * Fecha a partir de la cual se pudieron hacer avisos por cambio de dependencia
	 */
	private static final String FECHA_OFICIAL= "2018-05-01";

	/**
	 * Numero de dias permitidos
	 */
	private static final int DIAS_PERMITIDOS = 59;

	/**
	 * Propiedad que valida una fecha contra la fecha de encargo o el anio dependiendo
	 *
	 * @param nombre nombre del campo
	 * @param fecha fecha de inicio
	 * @param fechaEncargo fecha de encargo del encabezado
	 * @param fechaAhora fecha de ahora
	 *
	 * @return PropiedadesValidarDTO con las validaciones necesarios
	 */
	public static PropiedadesValidarDTO crearValidacionFechaConcluye(String nombre, String fecha, String fechaEncargo,
																	 String fechaAhora){

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, fechaEncargo));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORIGUAL_QUE, fechaAhora));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORIGUAL_QUE, FECHA_OFICIAL));

		return new PropiedadesValidarDTO(
				nombre,
				fecha,
				parametros,
				true);
	}

	/**
	 * Propiedad que valida la fecha de inicio del nuevo encargo
	 *
	 * @param nombre nombre del campo
	 * @param fecha fecha de inicio
	 * @param fechaConclusion fecha de conclusion del encargo anteror
	 * @param fechaAhora fecha de ahora
	 *
	 * @return PropiedadesValidarDTO con las validaciones necesarios
	 */
	public static PropiedadesValidarDTO crearValidacionFechaInicia(String nombre, String fecha, String fechaConclusion,
																	 String fechaAhora){

		//Obtener fecha de conclusion del encargo anterior mas diez dias
		LocalDate fechaConclusionDate = LocalDate.parse(fechaConclusion);
		String fechaLimite = fechaConclusionDate.plusDays(DIAS_PERMITIDOS).format(isoFecha);

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORQUE, fechaConclusion));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORIGUAL_QUE, fechaLimite));
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORIGUAL_QUE, fechaAhora));

		return new PropiedadesValidarDTO(
				nombre,
				fecha,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de Monto
	 */
	public static PropiedadesValidarDTO crearAreaAdscripcion(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.AREA_ADSCRIPCION.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de EmpleoCargoComision
	 */
	public static PropiedadesValidarDTO crearEmpleoCargoComision(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.EMPLEO.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 * Metodo para crear la propiedad de nivelEmpleoCargoComision
	 */
	public static PropiedadesValidarDTO crearNivelEmpleoCargoComision(String valor) {

		List<ParametrosValicacionDTO> parametros = new ArrayList<>();
		parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CINCUENTA_Y_UNO.getId()));

		return new PropiedadesValidarDTO(
				EnumCampos.NIVEL.getCampo(),
				valor,
				parametros,
				true);
	}

	/**
	 *
	 * @param enteConcluye
	 * @param enteInicia
	 */
	public static void crearComparacionEntes(EnteReceptorDTO enteConcluye, EnteReceptorDTO enteInicia,
											 List<PropiedadesValidarDTO> propiedades) {

		if(enteInicia.getId() != null && enteInicia.getNombre() != null
			&& enteConcluye.getId() != null && enteConcluye.getNombre() != null ) {

			if (enteInicia.getId().equals(enteConcluye.getId())) {
				List<ParametrosValicacionDTO> parametros = new ArrayList<>();
				parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NO_IGUAL_A, enteConcluye.getId()));
				propiedades.add(new PropiedadesValidarDTO(
						EnumCampos.ENTE.getCampo().concat(":id"),
						enteInicia.getId(),
						parametros,
						true));
			}
			if (enteInicia.getNombre().equals(enteConcluye.getNombre())) {
				List<ParametrosValicacionDTO> parametros = new ArrayList<>();
				parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NO_IGUAL_A, enteConcluye.getNombre()));
				propiedades.add(new PropiedadesValidarDTO(
						EnumCampos.ENTE.getCampo().concat(":nombre"),
						enteInicia.getNombre(),
						parametros,
						true));
			}
		}
	}

	/**
	 *
	 * @param ente
	 * @param nombre
	 * @return
	 */
	public static ModuloValidarDTO crearEnte(EnteReceptorDTO ente, String nombre) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(nombre);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(new PropiedadesValidarDTO(
				"id",
				ente.getId(),
				true));

		propiedades.add(new PropiedadesValidarDTO(
				"nombre",
				ente.getNombre(),
				true));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}
}
