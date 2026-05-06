package mx.gob.sfp.dgti.service;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.daopxy.DAOMigracionDeclaracionInterface;
import mx.gob.sfp.dgti.migracion.helper.CrearModulosHelper;

import static mx.gob.sfp.dgti.util.Constantes.PROP_DECLARACION;

public class ServiceMigracionDeclaracionImpl implements ServiceMigracionDeclaracionInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMigracionDeclaracionImpl.class);
	private static DAOMigracionDeclaracionInterface daoDecla;

	/**
	 * Constructor de la clase, recibe ub objecto Vertx
	 * 
	 * @param vertx
	 */
	public ServiceMigracionDeclaracionImpl(Vertx vertx) {
		daoDecla = DAOMigracionDeclaracionInterface.create(vertx);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Future<JsonObject> generarJsonDeclaracion(int numDeclaracion) {
		Future<JsonObject> declaracion = Future.future();
		Future<JsonObject> parejasYDepsEconomicos = Future.future();
		Future<JsonObject> mueblesInmueblesVehiculos = Future.future();
		Future<JsonObject> moduloCurricular = Future.future();
		Future<JsonObject> moduloExperiencia = Future.future();
		Future<JsonObject> moduloDatosParejas = Future.future();
		Future<JsonObject> moduloDatosDepEconomicos = Future.future();
		Future<JsonObject> moduloInversiones = Future.future();
		Future<JsonObject> moduloAdeudosPasivos = Future.future();
		Future<JsonObject> moduloBienesMuebles = Future.future();
		Future<JsonObject> moduloBIenesInmuebles = Future.future();
		Future<JsonObject> moduloVehiculos = Future.future();
		
		daoDecla.obtenerDatosCurriculares(numDeclaracion).setHandler(curricular ->{
			if(curricular.succeeded()) {
				moduloCurricular.handle(Future.succeededFuture(CrearModulosHelper.crearJsonDatosCurriculares(curricular.result())));
			} else {
				LOGGER.info(curricular.cause().getMessage());
				moduloCurricular.fail(curricular.cause().getMessage());
			}
		});
		
		daoDecla.obtenerExperienciaLaboral(numDeclaracion).setHandler(experiencia ->{
			if(experiencia.succeeded()) {
				moduloExperiencia.handle(Future.succeededFuture(CrearModulosHelper.crearJsonExperienciaLaboral(experiencia.result())));
			}else {
				LOGGER.info(experiencia.cause().getMessage());
				moduloExperiencia.fail(experiencia.cause().getMessage());
			}
		});
		
		daoDecla.obtenerDatosParejas(numDeclaracion).setHandler(pareja ->{
			if(pareja.succeeded()) {
				moduloDatosParejas.handle(Future.succeededFuture(CrearModulosHelper.crearJsonDatosParejas(pareja.result())));
			} else {
				LOGGER.info(pareja.cause().getMessage());
				moduloDatosParejas.fail(pareja.cause().getMessage());
			}
		});
		
		daoDecla.obtenerDependientesEconomicos(numDeclaracion).setHandler(dependientes ->{
			if(dependientes.succeeded()) {
				moduloDatosDepEconomicos.handle(Future.succeededFuture(CrearModulosHelper.crearJsonDatosDependientesEconomicos(dependientes.result())));
			} else {
				LOGGER.info(dependientes.cause().getMessage());
				moduloDatosDepEconomicos.fail(dependientes.cause().getMessage());
			}
		});
		
		daoDecla.obtenerInversionesCuentasValores(numDeclaracion).setHandler(inversiones ->{
			if(inversiones.succeeded()) {
				moduloInversiones.handle(Future.succeededFuture(CrearModulosHelper.crearJsonInversiones(inversiones.result())));
			} else {
				LOGGER.info(inversiones.cause().getMessage());
				moduloInversiones.fail(inversiones.cause().getMessage());
			}
		});
		
		daoDecla.obtenerAdeudosPasivos(numDeclaracion).setHandler(adeudos ->{
			if(adeudos.succeeded()) {
				moduloAdeudosPasivos.handle(Future.succeededFuture(CrearModulosHelper.crearJsonAdeudosPasivos(adeudos.result())));
			} else {
				LOGGER.info(adeudos.cause().getMessage());
				moduloAdeudosPasivos.fail(adeudos.cause().getMessage());
			}
		});
		
		daoDecla.obtenerBienesMuebles(numDeclaracion).setHandler(muebles ->{
			if(muebles.succeeded()) {
				moduloBienesMuebles.handle(Future.succeededFuture(CrearModulosHelper.crearJsonBienesMuebles(muebles.result())));
			} else {
				LOGGER.info(muebles.cause().getMessage());
				moduloBienesMuebles.fail(muebles.cause().getMessage());
			}
		});
		
		daoDecla.obtenerBienesInmubeles(numDeclaracion).setHandler(inmuebles ->{
			if(inmuebles.succeeded()) {
				moduloBIenesInmuebles.handle(Future.succeededFuture(CrearModulosHelper.crearJsonBienesInmuebles(inmuebles.result())));
			} else {
				LOGGER.info(inmuebles.cause().getMessage());
				moduloBIenesInmuebles.fail(inmuebles.cause().getMessage());
			}
		});
		
		daoDecla.obtenerVehiculos(numDeclaracion).setHandler(vehiculos ->{
			if(vehiculos.succeeded()) {
				moduloVehiculos.handle(Future.succeededFuture(CrearModulosHelper.crearJsonVehiculos(vehiculos.result())));
			} else {
				LOGGER.info(vehiculos.cause().getMessage());
				moduloVehiculos.fail(vehiculos.cause().getMessage());
			}
		});
		
		CompositeFuture.all(moduloBienesMuebles , moduloBIenesInmuebles , moduloVehiculos).setHandler(bienesYVehiculos ->{
			if(bienesYVehiculos.succeeded()) {
				mueblesInmueblesVehiculos.handle(Future.succeededFuture(new JsonObject()
						.mergeIn(moduloBienesMuebles.result())
		 				.mergeIn(moduloBIenesInmuebles.result())
		 				.mergeIn(moduloVehiculos.result())));
			}else {
				 LOGGER.info(bienesYVehiculos.cause().getMessage());
				 mueblesInmueblesVehiculos.fail(parejasYDepsEconomicos.cause().getMessage());
			}
		});
		
		CompositeFuture.all(moduloDatosParejas,moduloDatosDepEconomicos).setHandler(parejaDeps ->{
			if(parejaDeps.succeeded()) {
				parejasYDepsEconomicos.handle(Future.succeededFuture(new JsonObject().mergeIn(moduloDatosParejas.result())
		 				.mergeIn(moduloDatosDepEconomicos.result())));
			}else {
				 LOGGER.info(parejaDeps.cause().getMessage());
				 declaracion.fail(parejaDeps.cause().getMessage());
			}
		});
		
		 CompositeFuture.all(moduloCurricular, moduloExperiencia, moduloInversiones, parejasYDepsEconomicos,moduloAdeudosPasivos, mueblesInmueblesVehiculos ).setHandler(decla->{
			 
			 if(decla.succeeded()) {
				 declaracion.handle(Future.succeededFuture(new JsonObject().put(PROP_DECLARACION, new JsonObject()
						 				.mergeIn(moduloCurricular.result())
						 				.mergeIn(moduloExperiencia.result())
						 				.mergeIn(parejasYDepsEconomicos.result())
						 				.mergeIn(moduloInversiones.result())
						 				.mergeIn(moduloAdeudosPasivos.result())
						 				.mergeIn(mueblesInmueblesVehiculos.result())
						 				.mergeIn(CrearModulosHelper.crearModulosNoPrecargados()))));
			 }else {
				 LOGGER.info(decla.cause().getMessage());
				 declaracion.fail(decla.cause().getMessage());
			 }
		 });
		return declaracion;
	}

}
