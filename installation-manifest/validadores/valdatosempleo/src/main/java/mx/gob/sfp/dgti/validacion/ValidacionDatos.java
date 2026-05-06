package mx.gob.sfp.dgti.validacion;



import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosUnoFk;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoRemuneracion;
import mx.gob.sfp.dgti.dto.DatosEmpleoCargoComisionDTO;
import mx.gob.sfp.dgti.dto.EmpleoCargoComisionDTO;
import mx.gob.sfp.dgti.util.Constantes;
import mx.gob.sfp.dgti.utils.ExectValidations;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.validaciones.ValCabeceraDecla;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.utils.validaciones.ValMonto;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase de validaciones para el módulo de datos de empleo de la declaración
 * 
 * @author programador04
 * @since 07/11/2019
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
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final String MODULO_EMPLEO = "Empleo";
	
	
	/**
	 * WebCLient
	 */
	private WebClient client;

	/**
	 * El logger
	 */
	static final Logger LOGGER = LoggerFactory.getLogger(ValidacionDatos.class);

	public ValidacionDatos(Vertx vertx) {

		client = WebClient.create(vertx);
	}
	
	/**
	 * Se construye el módulo con propiedades y se asignan las validaciones por c/u.
	 * 
	 * @param DatosEmpleoCargoComisionDTO Módulo datos de empleo
	 * @return future
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Future<ModuloDTO> obtenerValidaciones(DatosEmpleoCargoComisionDTO datosEmp) {
		Future<ModuloDTO> future = Future.future();
		ModuloDTO modulo = new ModuloDTO(EnumModulo.I_DATOS_EMPLEO.getModulo());
		ModuloValidarDTO empleo = new ModuloValidarDTO(MODULO_EMPLEO);
		crearModuloValidarDatosEmp(datosEmp, empleo, modulo);
		new ExectValidations(client, URL_PATH_CATALOGOS).ejecutarValidacionesRx(
				empleo,	modulo
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

	
	private void crearModuloValidarDatosEmp(DatosEmpleoCargoComisionDTO datosEmp, ModuloValidarDTO empleo, ModuloDTO modulo) {
		empleo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(datosEmp, MODULO_EMPLEO));
		empleo.getListPropsTovalidate().add(PropBase.crearPropAclaraciones(datosEmp.getAclaracionesObservaciones()));
		empleo.getListPropsTovalidate().add(new PropiedadesValidarDTO("Empleos", datosEmp.getEmpleoCargoComision()));
		
		if(datosEmp.getEmpleoCargoComision() != null) {
			for (EmpleoCargoComisionDTO empleos: datosEmp.getEmpleoCargoComision()) {
				if(empleos.isVerificar()) {
					ModuloValidarDTO empl = new ModuloValidarDTO(MODULO_EMPLEO);
					List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
			        PropBase.crearPropTipoOperacion(empleos, datosEmp.getEncabezado(), propiedades);
			        propiedades.add(new PropiedadesValidarDTO("idPosicionVista", empleos.getIdPosicionVista()));
			        propiedades.add(PropBase.crearId(empleos.getId()));
			        propiedades.add(PropBase.crearModuloDebeSerNoNulo( empleos.getEnte(), "Ente"));
					if(empleos.getEnte() != null) 
						ValCabeceraDecla.creaModuloValidacionEnteInstitucion(empl, empleos.getEnte());
					propiedades.add(new PropiedadesValidarDTO("areaAdscripcion", empleos.getAreaAdscripcion()));
					propiedades.get(propiedades.size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,101));
					propiedades.add(new PropiedadesValidarDTO("empleoCargoComision", empleos.getEmpleoCargoComision()));
					propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,101));
					propiedades.add(PropBase.crearModuloDebeSerNoNulo( empleos.getNivelJerarquico(), "NivelJerarquico"));
					propiedades.add(PropBase.crearPropCatalogo(
			                "NivelJerarquico", EnumCatalogosUnoFk.CAT_NIVEL_JERARQUICO.name(),
			                empleos.getNivelJerarquico(), true));
					propiedades.add(new PropiedadesValidarDTO("nivelEmpleoCargoComision", empleos.getNivelEmpleoCargoComision()));
					propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,51));
					propiedades.add(new PropiedadesValidarDTO("contratadoPorHonorarios", empleos.getContratadoPorHonorarios()));
					empl.getListModuloshijos().add(ValMonto.crearMonto(empleos.getRemuneracionNeta(), "Remuneracion"));
					propiedades.add(new PropiedadesValidarDTO("tipoRemuneracion", empleos.getTipoRemuneracion()));
					propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumTipoRemuneracion.class));
					
					propiedades.add(new PropiedadesValidarDTO("funcionPrincipal", empleos.getFuncionPrincipal()));
					propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,101));
					propiedades.add(new PropiedadesValidarDTO("fechaEncargo", empleos.getFechaEncargo()));
					if(empleos.getFechaEncargo() != null) { 
						propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
                        if (!EnumTipoDeclaracion.MODIFICACION.name().equals(datosEmp.getEncabezado().getTipoDeclaracion().name()) && !validaFechasEncargo(datosEmp, empleos.getFechaEncargo()).equals(Constantes.EMPTY)){
                        	propiedades.get(propiedades.size()- 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, validaFechasEncargo(datosEmp, empleos.getFechaEncargo())));
                        }
					}
					if(empleos.getFechaEncargo() != null && ((EnumTipoDeclaracion.INICIO.name().equals(datosEmp.getEncabezado().getTipoDeclaracion().name())) || (EnumTipoDeclaracion.CONCLUSION.name().equals(datosEmp.getEncabezado().getTipoDeclaracion().name()))))  
							propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, datosEmp.getEncabezado().getFechaEncargo()));
					
					if(empleos.getFechaEncargo() != null && (EnumTipoDeclaracion.MODIFICACION.name().equals(datosEmp.getEncabezado().getTipoDeclaracion().name())))
						propiedades.get(propiedades.size() - 1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORQUE, format.format(new Date())));
					
					propiedades.add(PropBase.crearModuloDebeSerNoNulo( empleos.getTelefonoOficina(), "telefono"));
					
					if(empleos.getTelefonoOficina() != null) {
						ModuloValidarDTO tel = new ModuloValidarDTO("telefono");
						tel.getListPropsTovalidate().add(new PropiedadesValidarDTO("numero", empleos.getTelefonoOficina().getNumero()));
						if(empleos.getTelefonoOficina().getNumero() != null) {
							tel.getListPropsTovalidate().get(0).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.TELEFONO_CASA));
							tel.getListPropsTovalidate().get(0).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,16));
						}
						tel.getListPropsTovalidate().add(new PropiedadesValidarDTO("extension", empleos.getTelefonoOficina().getExtension()));
						if(empleos.getTelefonoOficina().getExtension() != null) {
							tel.getListPropsTovalidate().get(1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.TELEFONO_CASA));
							tel.getListPropsTovalidate().get(1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,8));
						}
						empl.getListModuloshijos().add(tel);	
					}
					empl.getListModuloshijos().add(ValDomicilio.crearDomicilio(empleos.getDomicilio(), EnumModulo.I_DATOS_EMPLEO.getModulo()));
					
					empl.setListPropsTovalidate(propiedades);
					empleo.getListModuloshijos().add(empl);
				} else {
					modulo.setIncompleto(true);
				}
			}
		}
	}
	
	private static  String validaFechasEncargo(DatosEmpleoCargoComisionDTO datosEmp, String fechaEncargo) {
            String fecha = Constantes.EMPTY;
            for (EmpleoCargoComisionDTO emp: datosEmp.getEmpleoCargoComision()) {
                            if(fechaEncargo!= null && emp.getFechaEncargo() != null && !fechaEncargo.equals(emp.getFechaEncargo())) {
                                    fecha =  emp.getFechaEncargo();
                                    break;
                            }
            }
            return fecha;
	}
	
	
	@SuppressWarnings("deprecation")
	public Future<JsonObject> verificaEncargo(DatosEmpleoCargoComisionDTO datosEncargo) {
		Future<JsonObject> jsonRespuesta = Future.future();
		if(datosEncargo.getEncabezado().getNivelJerarquico().getFk() < obtenerEmpleoMayorCargo(datosEncargo).getInteger("fk")) {
				jsonRespuesta.handle(Future.succeededFuture(obtenerEmpleoMayorCargo(datosEncargo)));
		} else {
				jsonRespuesta.handle(Future.succeededFuture(new JsonObject()));
		}
			
		return jsonRespuesta;
	}
	
	private JsonObject obtenerEmpleoMayorCargo(DatosEmpleoCargoComisionDTO datosEncargo) {
		int max = 0;
		JsonObject cargo = new JsonObject();
		for (EmpleoCargoComisionDTO empleo : datosEncargo.getEmpleoCargoComision()) {
			if (empleo.getNivelJerarquico().getFk() > max) {
				max = empleo.getNivelJerarquico().getFk();
				cargo = (JsonObject)Json.decodeValue(Json.encode(empleo.getNivelJerarquico()));
			}	
		}
		return cargo;
	}
	
}
