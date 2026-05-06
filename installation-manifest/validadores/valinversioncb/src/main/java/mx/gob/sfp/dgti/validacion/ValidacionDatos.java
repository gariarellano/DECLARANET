package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.LocalizacionInversionDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosFk;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosUno;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCoopropiedad;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;
import mx.gob.sfp.dgti.dto.InversionCuentaValDTO;
import mx.gob.sfp.dgti.dto.InversionDTO;
import mx.gob.sfp.dgti.util.EnumInversion;
import mx.gob.sfp.dgti.util.EnumInversionCB;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropPersona;
import mx.gob.sfp.dgti.utils.validaciones.ValGenerales;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase de validaciones para el módulo de inversiones, cuentas bancarias
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 13/11/2019
 */
public class ValidacionDatos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);
	private final static String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
	private final static String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS) != null ? System.getenv(NOMBRE_URL_CATALOGOS) 
			: "https://dnet-catalogosorigen-staging.dkla8s.funcionpublica.gob.mx/validar";
	private WebClient client;

	public ValidacionDatos(Vertx vertx) {
		client = WebClient.create(vertx);
	}
	
	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param ExperienciaLaboralDTO Módulo de inversiones, cuentas bancarias
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(InversionCuentaValDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_INVERSIONES.getModulo());

		new ExectValidations(client, URL_CATALOGOS).ejecutarValidacionesRx(
				obtenerModulo(datos, modulo), modulo
		).doOnComplete(() -> {
			LOGGER.info("=== doOnComplete()");
			future.complete(modulo);

		}).doOnError(e -> {
			LOGGER.info("=== doOnError()");
			future.fail("error");
		})
				.subscribe();
		
		return future;
	}

	/**
	 * Se genera el módulo de inversiones del declarante
	 * validaciones correspondientes
	 * 
	 * @param ingresos Módulo de inversiones
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(InversionCuentaValDTO datos, ModuloDTO moduloDTO) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.I_INVERSIONES.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		
		if(!datos.isNinguno()) {
			
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(datos.getInversion(), EnumInversionCB.INVERSION.getCampo()));
			
			if(datos.getInversion() != null) {
				for(InversionDTO inversion: datos.getInversion()) {
					
					if(!inversion.isVerificar()) {
						moduloDTO.setIncompleto(true);
						continue;
					}
					
					modulo.getListModuloshijos().add(obtenerInversion(inversion, datos.getEncabezado()));
					
				}
			}
			
		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(
					datos.getInversion(), EnumInversionCB.INVERSION.getCampo()));
		}
        return modulo;
	}
	
	/**
	 * Método para obtener la inversion
	 * @param inversion
	 * @return ModuloValidarDTO	
	 */
	private ModuloValidarDTO obtenerInversion(InversionDTO inversion, EncabezadoDTO encabezado) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumInversionCB.INVERSION.getCampo());
		
		PropiedadesValidarDTO cuentaContrato = new PropiedadesValidarDTO(EnumInversion.CUENTA_CONTRATO.getCampo(), 
        		inversion.getNumeroCuentaContrato(), ValGenerales.validacionesCadenaAlfa(EnumCantidad.CINCUENTA_Y_UNO.getId()), true);

		PropBase.crearPropTipoOperacion(inversion, encabezado, modulo.getListPropsTovalidate());

		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(inversion.getUbicacion(), EnumInversion.UBICACION.getCampo()));
		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(inversion.getTitular(), EnumInversion.TITULAR.getCampo()));
		modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(inversion.getLocalizacionInversion(), EnumInversion.LOCALIZACION.getCampo()));
		modulo.getListPropsTovalidate().add(PropBase.crearIdPosicionVista(inversion.getIdPosicionVista()));
		modulo.getListPropsTovalidate().add(PropBase.crearId(inversion.getId()));
		modulo.getListPropsTovalidate().add(cuentaContrato);
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(inversion.getSaldo(), EnumInversion.SALDO.getCampo()));

		if(inversion.getSaldo() != null) {
			modulo.getListModuloshijos().add(ValMonto.crearMonto(inversion.getSaldo(), EnumInversion.SALDO.getCampo()));
		} 
			
		if(inversion.getUbicacion() != null && inversion.getLocalizacionInversion() != null) {
			modulo.getListModuloshijos().add(
					obtenerLocalizacionInv(inversion.getUbicacion(), inversion.getLocalizacionInversion(), EnumInversion.LOCALIZACION.getCampo()));
		}

		if(inversion.getTitular() != null && EnumCoopropiedad.COOPROPIEDAD.getDescripcion().equals(inversion.getTitular().getValorUno())) {

			//Al menos un tercero es requerido
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(inversion.getTerceros(), EnumInversion.TERCEROS.getCampo()));
			
		    if(inversion.getTerceros() != null) {
		    	
		    	for(PersonaDTO persona: inversion.getTerceros()) {
		        	modulo.getListModuloshijos().add(ValPersona.crearPersona(persona, EnumInversion.TERCEROS.getCampo()));
		        }
		    }
		} else {
		    modulo.getListPropsTovalidate().add(
		    		PropBase.crearModuloDebeSerNulo(inversion.getTerceros(), EnumInversion.TERCEROS.getCampo()));
		}

		modulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
        		EnumInversion.TITULAR.getCampo(), EnumCatalogosUno.CAT_TITULAR.name(),
        		inversion.getTitular(), true
        ));
		
		modulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
        		EnumInversion.TIPO_INVERSION.getCampo(), EnumCatalogos.CAT_TIPO_INVERSION.name(),
        		inversion.getTipoInversion(), true
        ));
		
		modulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
        		EnumInversion.SUBTIPO_INVERSION.getCampo(), EnumCatalogosFk.CAT_SUBTIPO_INVERSION.name(),
        		inversion.getSubTipoInversion(), true
        ));
		
		return modulo;
	}

	private ModuloValidarDTO obtenerLocalizacionInv(EnumUbicacion ubicacion, LocalizacionInversionDTO localizacionI, String nombre) {
		ModuloValidarDTO submodulo = new ModuloValidarDTO(nombre);
		
		submodulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(localizacionI.getInstitucionRazonSocial(), EnumInversion.INSTITUCION_RAZONS.getCampo()));
		
		if(localizacionI.getInstitucionRazonSocial() != null) {
		
			if(EnumUbicacion.MEXICO.equals(ubicacion)) {
				submodulo.getListModuloshijos().add(
						ValPersona.crearPersonaMoral(localizacionI.getInstitucionRazonSocial(), false));
			}
			
			else if(EnumUbicacion.EXTRANJERO.equals(ubicacion)) {
				
				submodulo.getListPropsTovalidate().add(
						PropPersona.crearPropNombrePersMoral(localizacionI.getInstitucionRazonSocial().getNombre()));
				
				submodulo.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
		        		EnumDomicilio.PAIS.getNombre(), EnumCatalogos.CAT_PAIS.name(),
		        		localizacionI.getPais(), false
		        ));
			}
		}
		return submodulo;
	}
	
}
