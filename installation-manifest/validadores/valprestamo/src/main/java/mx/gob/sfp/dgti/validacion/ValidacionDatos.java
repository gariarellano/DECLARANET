package mx.gob.sfp.dgti.validacion;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.VehiculoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoBienPrestamo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.dto.InmuebleDTO;
import mx.gob.sfp.dgti.dto.PrestamoComodatoDTO;
import mx.gob.sfp.dgti.dto.PrestamoDTO;
import mx.gob.sfp.dgti.enums.EnumCampos;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase de validaciones para el módulo de datos generales de la declaración
 * 
 * @author Programador04
 * @since 19/11/2019
 */
public class ValidacionDatos {

	/**
	 * CONSTANTE URL CATALGOOS
	 */
	private static final String URL_CATALOGOS = "URL_CATALOGOS";

	/**
	 * URL para catalogos
	 */
	private static final String URL_PATH_CATALOGOS = System.getenv(URL_CATALOGOS);
	

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);

	private static final String MODULO_VEHICULO= "vehiculo";
	/**
	 * WebCLient
	 */
	private WebClient client;

	public ValidacionDatos(Vertx vertx) {

		client = WebClient.create(vertx);
	}

	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param DatosGeneralesDTO Módulo de datos generales
	 * @return future
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Future<ModuloDTO> obtenerValidaciones(PrestamoComodatoDTO datosGen) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_PRESTAMO_COMODATO.getModulo());

		new ExectValidations(client, URL_PATH_CATALOGOS).ejecutarValidacionesRx(obtenerModulo(datosGen, modulo), modulo)
				.doOnComplete(() -> {
					LOGGER.info("=== doOnComplete()");
					future.complete(modulo);
				}).doOnError(e -> {
					LOGGER.info("=== doOnError()");
					future.fail("error");
				}).subscribe();

		return future;
	}

	/**
	 * Se genera el módulo de préstamo o comodato, sus propiedades y submódulos con
	 * sus validaciones correspondientes
	 * 
	 * @param PrestamoComodatoDTO Módulo de datos generales
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(PrestamoComodatoDTO datos, ModuloDTO modulo) {

		ModuloValidarDTO moduloDatosG = new ModuloValidarDTO(EnumModulo.I_PRESTAMO_COMODATO.getModulo());
		// 0
		moduloDatosG.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		// 1
		moduloDatosG.getListPropsTovalidate().add(new PropiedadesValidarDTO("ninguno", datos.getNinguno()));

		if (Boolean.TRUE.equals(datos.getNinguno())) {
			moduloDatosG.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(datos.getPrestamo(), "prestamo"));
		}else {
			if (datos.getPrestamo() == null) {
				moduloDatosG.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(datos.getPrestamo(), "prestamo"));
			}else {
				for (PrestamoDTO prestamo : datos.getPrestamo()) {
					crearValidacionesPrestamo(moduloDatosG, prestamo, datos.getEncabezado(), modulo);
				}
			}
		}
		return moduloDatosG;
	}

	private static void crearValidacionesPrestamo(ModuloValidarDTO moduloDatosG, PrestamoDTO prestamo,
			EncabezadoDTO encabezado, ModuloDTO modulo) {
		ModuloValidarDTO pres = new ModuloValidarDTO("Prestamo");
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
		PropBase.crearPropTipoOperacion(prestamo, encabezado, propiedades);
		if (!prestamo.isVerificar()) {
			modulo.setIncompleto(true);
		} else {

			propiedades.add(PropBase.crearId(prestamo.getId()));
			propiedades.add(new PropiedadesValidarDTO("tipoBien", prestamo.getTipoBien()));
			propiedades.get(propiedades.size() - 1).getListValToExec()
					.add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumTipoBienPrestamo.class));

			propiedades.add(PropBase.crearModuloDebeSerNoNulo(prestamo.getDuenoTitular(), "duenoTitular"));
			if (prestamo.getDuenoTitular() != null)
				pres.getListModuloshijos().add(ValPersona.crearPersona(prestamo.getDuenoTitular(), "duenoTitular"));

			if (!(prestamo.getVehiculo() != null && prestamo.getInmueble() != null)) {
				if (EnumTipoBienPrestamo.VEHICULO.name().equals(prestamo.getTipoBien().name())) {
					propiedades.add(PropBase.crearModuloDebeSerNoNulo(prestamo.getVehiculo(), MODULO_VEHICULO));
					if (prestamo.getVehiculo() != null)
						crearValidacionVehiculo(pres, prestamo.getVehiculo(), encabezado);
					propiedades.add(PropBase.crearModuloDebeSerNulo(prestamo.getInmueble(), "inmueble"));
				} else {
					propiedades.add(PropBase.crearModuloDebeSerNoNulo(prestamo.getInmueble(), "inmueble"));
					if (prestamo.getInmueble() != null) {
						crearValidacionInmueble(pres, prestamo.getInmueble());
					}
					propiedades.add(PropBase.crearModuloDebeSerNulo(prestamo.getVehiculo(), MODULO_VEHICULO));
				}
			}
			propiedades.add(PropBase.crearPropUnSoloObjetoNoNulo(
					new Object[] { prestamo.getVehiculo(), prestamo.getInmueble() },
					new String[] { MODULO_VEHICULO, "Inmueble" }));
			propiedades.add(new PropiedadesValidarDTO("tipoOperacion", prestamo.getTipoOperacion()));
			propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumTipoOperacion.class));
			propiedades.add(new PropiedadesValidarDTO("relacionConTitular", prestamo.getRelacionConTitular()));
			if(prestamo.getRelacionConTitular() != null) {
				propiedades.get(propiedades.size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE,0));
			}
			pres.setListPropsTovalidate(propiedades);
			moduloDatosG.getListModuloshijos().add(pres);
		}
	}

	private static void crearValidacionInmueble(ModuloValidarDTO pres, InmuebleDTO inmueble) {
		ModuloValidarDTO inmu = new ModuloValidarDTO("Inmueble");
		// 0
		inmu.getListPropsTovalidate()
				.add(PropBase.crearModuloDebeSerNoNulo(inmueble.getTipoInmueble(), "tipoInmueble"));

		List<PropiedadesValidarDTO> propiedades = new ArrayList<PropiedadesValidarDTO>();
		PropBase.crearPropCatalogoOtro("tipoInmueble", EnumCatalogos.CAT_TIPO_BIEN_INMUEBLE.name(),
				inmueble.getTipoInmueble(), true, inmueble.getTipoInmuebleOtro(), propiedades);
		for (PropiedadesValidarDTO propiedadesValidarDTO : propiedades) {
			inmu.getListPropsTovalidate().add(propiedadesValidarDTO);
		}

		inmu.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(inmueble.getDomicilio(), "Domicilio"));

		if (inmueble.getDomicilio() != null)
			inmu.getListModuloshijos().add(ValDomicilio.crearDomicilio(inmueble.getDomicilio(), "Domicilio"));

		pres.getListModuloshijos().add(inmu);
	}

	private static void crearValidacionVehiculo(ModuloValidarDTO pres, VehiculoDTO vehiculo, EncabezadoDTO encabezado) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(MODULO_VEHICULO);
		moduloValidar.getListPropsTovalidate().add(new PropiedadesValidarDTO("anio", vehiculo.getAnio()));
		if (vehiculo.getAnio() != null && vehiculo.getAnio() > encabezado.getAnio()) {
				moduloValidar.getListPropsTovalidate().get(0).getListValToExec()
						.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MENOR_QUE, (encabezado.getAnio() + 2)));
		}

		moduloValidar.getListPropsTovalidate().add(Propiedades.crearMarca(vehiculo.getMarca()));
		moduloValidar.getListPropsTovalidate().get(1).getListValToExec()
				.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, 101));
		// 2
		moduloValidar.getListPropsTovalidate().add(Propiedades.crearModelo(vehiculo.getModelo()));
		moduloValidar.getListPropsTovalidate().get(2).getListValToExec()
				.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, 51));
		// 3
		moduloValidar.getListPropsTovalidate()
				.add(Propiedades.crearNumSerieRegistro(vehiculo.getNumeroSerieRegistro()));
		moduloValidar.getListPropsTovalidate().get(3).getListValToExec()
				.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, 51));

		moduloValidar.getListModuloshijos()
				.add(ValDomicilio.crearLocalizacion(vehiculo.getLugarRegistro(), EnumCampos.LUGAR_REGISTRO.getCampo()));

		List<PropiedadesValidarDTO> propiedades = new ArrayList<PropiedadesValidarDTO>();
		PropBase.crearPropCatalogoOtro("tipoVehiculo", EnumCatalogos.CAT_TIPO_VEHICULO.name(),
				vehiculo.getTipoVehiculo(), true, vehiculo.getTipoVehiculoOtro(), propiedades);
		for (PropiedadesValidarDTO propiedadesValidarDTO : propiedades) {
			moduloValidar.getListPropsTovalidate().add(propiedadesValidarDTO);
		}
		pres.getListModuloshijos().add(moduloValidar);
	}

}
