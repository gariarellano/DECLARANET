package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.general.BienInmuebleDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienMuebleDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienesInmueblesDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienesMueblesDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.IngresosModulosDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.VehiculoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.VehiculosDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumIngresoNetoD;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumMotivoBaja;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValIngresosNetos;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase de validaciones para el módulo de ingresos netos del declarante
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 13/11/2019
 */
public class ValidacionDatos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);
	private static final String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
        private static final String FIEL_DECLARANTE = "DECLARANTE";
	private final static String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS);
	private final WebClient client;
    

	public ValidacionDatos(Vertx vertx) {
		client = WebClient.create(vertx);
	}
	
	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param ExperienciaLaboralDTO Módulo de ingresos netos del declarante
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(IngresosModulosDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_INGRESOS_NETOS.getModulo());

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				obtenerModulo(datos, modulo), modulo
		).doOnComplete(() -> {
			future.complete(modulo);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error");
		})
				.subscribe();
		
		return future;
	}

	/**
	 * Se genera el módulo de ingresos netos del declarante
	 * validaciones correspondientes
	 * 
	 * @param ExperienciaLaboralDTO Módulo de ingresos netos del declarante
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(IngresosModulosDTO ingresosModulos, ModuloDTO moduloDTO) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.I_INGRESOS_NETOS.getModulo());

		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(ingresosModulos.getIngresos().getEncabezado(), EnumGeneral.ENCABEZADO.getCampo()));
		
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(ingresosModulos.getIngresos().getAclaracionesObservaciones()));
		if(ingresosModulos.getIngresos().getEncabezado() != null ) {
			ValIngresosNetos.obtenerIngresosN(modulo, ingresosModulos.getIngresos());
		
			if((EnumTipoDeclaracion.MODIFICACION.equals(ingresosModulos.getIngresos().getEncabezado().getTipoDeclaracion()) 
					|| EnumTipoDeclaracion.CONCLUSION.equals(ingresosModulos.getIngresos().getEncabezado().getTipoDeclaracion()))
					&& EnumTipoFormatoDeclaracion.COMPLETO.equals(ingresosModulos.getIngresos().getEncabezado().getTipoFormato())
				) {
					
				if(ingresosModulos.getBienesInmuebles() != null
						&& ingresosModulos.getBienesMuebles() != null
						&& ingresosModulos.getVehiculos() != null) {
					
					LOGGER.info("Módulos != null se procede a obtener enajenacion de bienes");
					obtenerEnajenacionBOtrosMod(modulo, ingresosModulos);
				
				} else {
					moduloDTO.setIncompleto(true);
				}
			}
		}

		return modulo;
	}

	/**
	 * @param modulo
	 * @param ingresosModulos
	 */
	private ModuloValidarDTO obtenerEnajenacionBOtrosMod(ModuloValidarDTO modulo, IngresosModulosDTO ingresosModulos) {
		Long suma = obtenerEnajenacionBVehiculos(ingresosModulos.getVehiculos())
					 + obtenerEnajenacionBMuebles(ingresosModulos.getBienesMuebles())
					 + obtenerEnajenacionBInmuebles(ingresosModulos.getBienesInmuebles());
		
		PropiedadesValidarDTO enajenacionBienesTotal = new PropiedadesValidarDTO(
					EnumIngresoNetoD.ENAJENACION_BIENES.getCampo(), 
					suma, 
					ValIngresosNetos.validacionMontos(ValIngresosNetos.obtenerSuma(ingresosModulos.getIngresos().getEnajenacionBienes())));
		
		modulo.getListPropsTovalidate().add(enajenacionBienesTotal);
		
		return modulo;
	}

	/**
	 * @param inmuebles
	 * @return
	 */
	private Long obtenerEnajenacionBInmuebles(BienesInmueblesDTO inmuebles) {
		long suma = 0;
		
		if(inmuebles != null && inmuebles.getBienesInmuebles() != null) {
			for(BienInmuebleDTO inmueble: inmuebles.getBienesInmuebles()) {

				if(inmueble.getMotivoBaja() != null 
						&& inmueble.getMotivoBaja().getId() == EnumMotivoBaja.VENTA.getId()
						&& inmueble.getValorVenta() != null
                                                && FIEL_DECLARANTE.equals(inmueble.getTitular().getValor())
                                        ) {
					suma = suma + inmueble.getValorVenta().getMonto();
				}
			}
		}
		LOGGER.info("suma inmuebles " + suma );
		return suma;
	}

	/**
	 * @param muebles
	 * @return
	 */
	private Long obtenerEnajenacionBMuebles(BienesMueblesDTO muebles) {
		long suma = 0;
		
		if(muebles != null && muebles.getBienesMuebles() != null) {
			for(BienMuebleDTO mueble: muebles.getBienesMuebles()) {

				if(mueble.getMotivoBaja() != null 
						&& mueble.getMotivoBaja().getId() == EnumMotivoBaja.VENTA.getId()
						&& mueble.getValorVenta() != null
                                                && FIEL_DECLARANTE.equals(mueble.getTitular().getValor())
                                        ) {
					suma = suma + mueble.getValorVenta().getMonto();
				}
			}
		}
		LOGGER.info("suma muebles " + suma );
		return suma;
	}

	/**
	 * @param vehiculos
	 * @return
	 */
	private Long obtenerEnajenacionBVehiculos(VehiculosDTO vehiculos) {
		long suma = 0;

		if(vehiculos != null && vehiculos.getVehiculos() != null) {
			for(VehiculoDTO vehiculo: vehiculos.getVehiculos()) {

				if(vehiculo.getMotivoBaja() != null 
						&& vehiculo.getMotivoBaja().getId() == EnumMotivoBaja.VENTA.getId()
						&& vehiculo.getValorVenta() != null
                                                && FIEL_DECLARANTE.equals(vehiculo.getTitular().getValor())
                                        ) {
					suma = suma + vehiculo.getValorVenta().getMonto();
				}
			}
		}
		LOGGER.info("suma vehiculos " + suma );
		return suma;
	}

}
