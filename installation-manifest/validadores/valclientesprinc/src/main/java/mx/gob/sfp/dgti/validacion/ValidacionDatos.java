package mx.gob.sfp.dgti.validacion;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.dto.ClienteDTO;
import mx.gob.sfp.dgti.dto.ClientesPrincipalesDTO;
import mx.gob.sfp.dgti.util.EnumCliente;
import mx.gob.sfp.dgti.util.EnumClienteP;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropPersona;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.utils.validaciones.ValGenerales;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.utils.validaciones.ValPersona;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * Clase de validaciones para el módulo clientes principales
 * 
 * @author Miriam Sánchez Sánchez programador07
 * @since 25/11/2019
 */
public class ValidacionDatos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);
	private final static String NOMBRE_URL_CATALOGOS = "URL_CATALOGOS";
	private final static String URL_CATALOGOS = System.getenv(NOMBRE_URL_CATALOGOS);
	private WebClient client;

	public ValidacionDatos(Vertx vertx) {
		client = WebClient.create(vertx);
	}

	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param ClientesPrincipalesDTO Módulo de clientes principales
	 * @return future
	 */
	public Future<ModuloDTO> obtenerValidaciones(ClientesPrincipalesDTO datos, Vertx vertx) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.II_CLIENTES_PRINCIPALES.getModulo());

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
	 * Se genera el módulo de clientes principales
	 * validaciones correspondientes
	 * 
	 * @param ingresos Módulo de clientes principales
	 * @return ModuloValidarDTO
	 */
	private ModuloValidarDTO obtenerModulo(ClientesPrincipalesDTO datos, ModuloDTO moduloDTO) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumModulo.II_CLIENTES_PRINCIPALES.getModulo());
		modulo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datos.getAclaracionesObservaciones()));
		
		if(datos.isRealizaActividadLucrativa()) {
			
			modulo.getListPropsTovalidate().add(
					PropBase.crearModuloDebeSerNoNulo(datos.getClientes(), EnumClienteP.CLIENTES.getCampo()));
			
			if(datos.getClientes() != null) {
				for(ClienteDTO cliente: datos.getClientes()) {
					
					if(!cliente.isVerificar()) {
						moduloDTO.setIncompleto(true);
						continue;
					}
					
					modulo.getListModuloshijos().add(obtenerCliente(cliente, datos.getEncabezado()));
				}
			}
			
		} else {
			modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(
					datos.getClientes(), EnumClienteP.CLIENTES.getCampo()));
		}
        return modulo;
	}
	
	/**
	 * Método para obtener el cliente
	 * @param cliente
	 * @param encabezado
	 * @return ModuloValidarDTO	
	 */
	private ModuloValidarDTO obtenerCliente(ClienteDTO cliente, EncabezadoDTO encabezado) {
		ModuloValidarDTO modulo = new ModuloValidarDTO(EnumClienteP.CLIENTES.getCampo());

		PropiedadesValidarDTO empresa = new PropiedadesValidarDTO(EnumCliente.NOMBRE_EMPRESA.getCampo(), 
        		cliente.getNombreEmpresaServicio(), ValGenerales.validacionesCadena(EnumCantidad.CIENTO_UNO.getId()));

		//Agregar validacion de tipoOperacion
		PropBase.crearPropTipoOperacion(cliente, encabezado, modulo.getListPropsTovalidate());

		modulo.getListPropsTovalidate().add(empresa);
		modulo.getListPropsTovalidate().add(PropBase.crearId(cliente.getId()));
		modulo.getListPropsTovalidate().add(PropBase.crearObligatoria(cliente.getParticipante(), EnumCliente.PARTICIPANTE.getCampo()));
		
		if(cliente.getRfcEmpresa() != null && !cliente.getRfcEmpresa().isEmpty()) {
			modulo.getListPropsTovalidate().add(PropPersona.crearPropRfcPersMoral(cliente.getRfcEmpresa(), EnumCliente.RFC_EMPRESA.getCampo()));
		}
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(cliente.getClientePrincipal(), EnumCliente.CLIENTE_PRINCIPAL.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(cliente.getMontoAproximadoGanancia(), EnumCliente.MONTO_APROX_GANANCIA.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(cliente.getSector(), EnumCliente.SECTOR.getCampo()));
		
		modulo.getListPropsTovalidate().add(
				PropBase.crearModuloDebeSerNoNulo(cliente.getLocalizacion(), EnumCliente.LOCALIZACION.getCampo()));

		if(cliente.getClientePrincipal() != null) {
			modulo.getListModuloshijos().add(ValPersona.crearPersona(cliente.getClientePrincipal(), EnumCliente.CLIENTE_PRINCIPAL.getCampo()));
		}
		
		if(cliente.getMontoAproximadoGanancia() != null) {
			modulo.getListModuloshijos().add(
					ValMonto.crearMonto(cliente.getMontoAproximadoGanancia(), EnumCliente.MONTO_APROX_GANANCIA.getCampo()));
		} 
		
		if(cliente.getLocalizacion() != null) {
			modulo.getListModuloshijos().add(
					ValDomicilio.crearLocalizacion(cliente.getLocalizacion(), EnumCliente.LOCALIZACION.getCampo()));
		}
		
		PropBase.crearPropCatalogoOtro(
        		EnumCliente.SECTOR.getCampo(), EnumCatalogos.CAT_SECTOR_PRIVADO.name(),
        		cliente.getSector(), true,
				cliente.getSectorOtro(), modulo.getListPropsTovalidate()
        );
		
		return modulo;
	}

}
