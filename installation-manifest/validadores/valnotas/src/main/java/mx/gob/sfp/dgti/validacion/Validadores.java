/**
 * @(#)Validadores.java 20/02/2019
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.dto.NotaDTO;
import mx.gob.sfp.dgti.dto.NotasAclaratoriasDTO;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de el domicilio del declarante.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 20/02/2020
 */
public class Validadores {

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Validadores.class);

	/**
	 * El domicilio
	 */
	final static String NOTAS_ACLARATORIAS = "notasAclaratorias";

	/**
	 * CONSTANTE URL CATALGOOS
	 */
	final static String URL_CATALOGOS = "URL_CATALOGOS";

	/**
	 * URL para catalogos
	 */
	final static String urlCatalogos = 	System.getenv(URL_CATALOGOS);

	/**
	 * WebCLient
	 */
	private WebClient client;


	public Validadores(Vertx vertx) {

		client = WebClient.create(vertx);
	}

	/**
	 * Metodo para validar el Domicilio del declarante
	 *
	 * @author Pavel Alexei Martinez Regalado
	 * @since 12/09/2019
	 */
	public Future<ModuloDTO> validar(NotasAclaratoriasDTO notasAclaratoriasDTO, Vertx vertx) throws NoSuchFieldException {
		Future<ModuloDTO> future = Future.future();

		ModuloDTO moduloDto = new ModuloDTO(NOTAS_ACLARATORIAS);

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();
		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		boolean notasVacias = true;

		//Utilizando Java Reflection, las propiedades de notas aclaratorias son sus modulos de esta forma se recorren de
		//forma dinámica y no hay que llamar una por una
		Field[] fields = notasAclaratoriasDTO.getClass().getDeclaredFields();

		for(Field field : fields) {
			try {
				field.setAccessible(true);
				NotaDTO nota = (NotaDTO) field.get(notasAclaratoriasDTO);
				if(nota != null) {
					notasVacias = false;
					modulos.add(crearAclaraciones(field.getName(), nota.getAclaracionesObservaciones()));
				}
			} catch (IllegalAccessException e) {
				LOGGER.info("=== Error al recorrer las notas aclaratorias");
				future.fail("Error al recorrer las notas aclaratorias.");
				return future;
			}
		}

		if(notasVacias) {
			propiedades.add(PropBase.crearModuloDebeSerNoNulo(null, NOTAS_ACLARATORIAS));
		}
		new ExectValidations(client, urlCatalogos).ejecutarValidacionesRx(
				new ModuloValidarDTO(NOTAS_ACLARATORIAS,
						modulos,
						propiedades) ,
				moduloDto
		).doOnComplete(() -> {
			LOGGER.info("=== doOnComplete()");
			future.complete(moduloDto);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error");
		})
		.subscribe();

        return future;
	}

	/**
	 * Metodo para crear un modulo de aclaraciones
	 *
	 * @param nombreNota: Nombre del modulo al que pertenece la nota
	 * @param nota: la nota
	 *
	 * @return: el modulo a validar
	 *
	 * @authot pavel.martinez
	 * @since 21/02/2020
	 */
	private ModuloValidarDTO crearAclaraciones(String nombreNota, String nota) {
		ModuloValidarDTO moduloNota = new ModuloValidarDTO(nombreNota);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		propiedades.add(PropBase.crearPropAclaraciones(nota));
		moduloNota.setListPropsTovalidate(propiedades);
		return moduloNota;
	}


}
