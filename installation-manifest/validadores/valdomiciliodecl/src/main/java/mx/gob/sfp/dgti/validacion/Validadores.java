/**
 * @(#)ValidacionDomDecl.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.dto.DomicilioDeclaranteDTO;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de el domicilio del declarante.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
public class Validadores {

	/**
	 * El logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Validadores.class);

	/**
	 * El domicilio
	 */
	static final String DOMICILIO = "domicilio";

	/**
	 * CONSTANTE URL CATALGOOS
	 */
	static final String URL_CATALOGOS_ENV = "URL_CATALOGOS";

	/**
	 * URL para catalogos
	 */
	static final String URL_CATALOGOS = 	System.getenv(URL_CATALOGOS_ENV);

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
	public Future<ModuloDTO> validar(DomicilioDeclaranteDTO domicilioDeclarante) {

		Promise<ModuloDTO> promise = Promise.promise();

		ModuloDTO moduloDto = new ModuloDTO(EnumModulo.I_DOMICILIO_DECLARANTE.getModulo());

		//Agregar modulos
		List<ModuloValidarDTO> modulos = new ArrayList<>();
		//Agregar propiedades
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		if (domicilioDeclarante.isVerificar()) {
			modulos.add(ValDomicilio.crearDomicilio(domicilioDeclarante.getDomicilio(), DOMICILIO));
			propiedades.add(PropBase.crearPropAclaraciones(domicilioDeclarante.getAclaracionesObservaciones()));
		} else {
			moduloDto.setIncompleto(true);
		}

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				new ModuloValidarDTO(EnumModulo.I_DOMICILIO_DECLARANTE.getModulo(),
						modulos,
						propiedades) ,
				moduloDto
		)
				.doOnComplete(() -> promise.complete(moduloDto))
				.doOnError(e -> {
					LOGGER.info("=== doOnError()");
					promise.fail("error: " + e);
				})
				.subscribe();

        return promise.future();
	}

}
