/**
 * InteresService.java Apr 2, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.dao.CatalogoDAOImpl;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadLaboral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCatalogo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumParticipacion;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumPersona;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumPersonaMoral;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumParticipante;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoPersona;
import mx.gob.sfp.dgti.enums.Bien;
import mx.gob.sfp.dgti.enums.CamposPDN;
import mx.gob.sfp.dgti.enums.CatalogosPdn;
import mx.gob.sfp.dgti.enums.Encabezados;
import mx.gob.sfp.dgti.enums.Interes;
import mx.gob.sfp.dgti.enums.ModulosPDN;
import mx.gob.sfp.dgti.enums.NivelOrdenGobierno;
import mx.gob.sfp.dgti.enums.Otros;
import mx.gob.sfp.dgti.enums.TipoOperacion;
import mx.gob.sfp.dgti.enums.TipoPersona;
import mx.gob.sfp.dgti.enums.Ubicacion;

/**
 * @author Miriam Sanchez programador07
 * @modifiedBy programador09
 * @since Apr 2, 2020
 */
public class InteresService extends GenericosPdnService {

    /**
     * Se genera el json con la estructura del objecto de intereses
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraIntereses(JsonObject original) {
        JsonObject declaracionEstructuradaPdn = new JsonObject();
        JsonObject declaracionOriginal = original.getJsonObject(CamposPDN.DECLARACION.getCampo());

        declaracionEstructuradaPdn.put(ModulosPDN.II_PARTICIPACION.getModulo(),
                crearEstructuraParticipaciones(declaracionOriginal.getJsonObject(EnumModulo.II_PARTICIPACION_EMPRESAS.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.II_PARTICIPACION_TOMA_D.getModulo(),
                crearEstructuraParticInstituciones(declaracionOriginal.getJsonObject(EnumModulo.II_PARTICIPACION_INSTITUCIONES.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.II_APOYOS.getModulo(),
                crearEstructuraApoyos(declaracionOriginal.getJsonObject(EnumModulo.II_APOYOS_BENEFICIOS.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.II_REPRESENTACION.getModulo(),
                crearEstructuraRepresentacion(declaracionOriginal.getJsonObject(EnumModulo.II_REPRESENTACION.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.II_CLIENTES_PRINCIPALES.getModulo(),
                crearEstructuraClientesP(declaracionOriginal.getJsonObject(EnumModulo.II_CLIENTES_PRINCIPALES.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.II_BENEFICIOS_PRIVADOS.getModulo(),
                crearEstructuraBeneficios(declaracionOriginal.getJsonObject(EnumModulo.II_BENEFICIOS_PRIVADOS.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.II_FIDEICOMISOS.getModulo(),
                crearEstructuraFideicomisos(declaracionOriginal.getJsonObject(EnumModulo.II_FIDEICOMISOS.getModulo())));

        return declaracionEstructuradaPdn;
    }

    /**
     * Se genera el json del módulo de participaciones en empresas
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraParticipaciones(JsonObject original) {
        JsonObject participacionEmpresas = new JsonObject();
        JsonArray participaciones = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        participacionEmpresas.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(EnumParticipacion.PARTICIPACIONES.getCampo()) != null) {
            original.getJsonArray(EnumParticipacion.PARTICIPACIONES.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                if (EnumParticipante.DECLARANTE.name().equals(registro.getString(Interes.PARTICIPANTE.getCampo()))) {
                    JsonObject participacion = new JsonObject();

                    participacion.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());
                    participacion.put(Interes.TIPO_RELACION.getCampo(), registro.getString(Interes.PARTICIPANTE.getCampo()));

                    participacion.put(Interes.NOMBRE_EMPRESA.getCampo(),
                            registro.getJsonObject(Interes.NOMBRE_EMPRESA.getCampo()).getString(Encabezados.NOMBRE.getCampo()));
                    participacion.put(EnumPersonaMoral.RFC.getCampo(),
                            registro.getJsonObject(Interes.NOMBRE_EMPRESA.getCampo())
                            .getString(EnumPersonaMoral.RFC.getCampo()));

                    participacion.put(Interes.PORCENTAJE_PART.getCampo(), registro.getInteger(Interes.PORCENTAJE_PART.getCampo()));
                    participacion.put(Interes.RECIBE_REMUNERACION.getCampo(), registro.getBoolean(Interes.RECIBE_REMUNERACION.getCampo()));

                    JsonObject tipoPart = registro.getJsonObject(Interes.TIPO_PARTICIPACION.getCampo());

                    Integer idParticipacion = tipoPart.getInteger(Encabezados.ID.getCampo());
                    String valParticipacion = idParticipacion.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Interes.TIPO_PARTICIPACION_OTRO.getCampo())
                            : tipoPart.getString(EnumCatalogo.VALOR.getCampo());

                    participacion.put(Interes.TIPO_PARTICIPACION.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_PARTICIPACION,
                                    idParticipacion),
                            valParticipacion));

                    participacion.put(Interes.MONTO_MENSUAL.getCampo(), obtenerMonto(registro.getJsonObject(Interes.MONTO_MENSUAL.getCampo())));
                    participacion.put(EnumActividadLaboral.UBICACION.getCampo(), obtenerUbicacion(registro.getJsonObject(Interes.LOCALIZACION.getCampo())));
                    participacion.put(Interes.SECTOR.getCampo(), obtenerSector(registro));

                    participaciones.add(participacion);
                }
            });
        }

        participacionEmpresas.put(Interes.PARTICIPACION.getCampo(), participaciones);

        return participacionEmpresas;
    }

    /**
     * Se genera el json del módulo de participacion de instituciones
     *
     * @param original
     * @return {@link JsonObject}
     */
    private JsonObject crearEstructuraParticInstituciones(JsonObject original) {
        JsonObject participacionInstituciones = new JsonObject();
        JsonArray participaciones = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        participacionInstituciones.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(EnumParticipacion.PARTICIPACIONES.getCampo()) != null) {
            original.getJsonArray(EnumParticipacion.PARTICIPACIONES.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                if (EnumParticipante.DECLARANTE.name().equals(registro.getString(Interes.PARTICIPANTE.getCampo()))) {
                    JsonObject participacion = new JsonObject();

                    participacion.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());
                    participacion.put(Interes.TIPO_RELACION.getCampo(), registro.getString(Interes.PARTICIPANTE.getCampo()));

                    Integer idInstitucion = registro.getJsonObject(Interes.TIPO_INSTITUCION.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valInstitucion = idInstitucion.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Interes.TIPO_INSTITUCION_OTRO.getCampo())
                            : registro.getJsonObject(Interes.TIPO_INSTITUCION.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    participacion.put(Interes.TIPO_INSTITUCION.getCampo(),
                            crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_INSTITUCION, idInstitucion), valInstitucion));

                    participacion.put(Interes.PUESTO_ROL.getCampo(), registro.getString(Interes.PUESTO_ROL.getCampo()));
                    participacion.put(Interes.FECHA_INICIO.getCampo(), registro.getString(Interes.FECHA_INICIO.getCampo()));
                    participacion.put(Interes.RECIBE_REMUNERACION.getCampo(), registro.getBoolean(Interes.RECIBE_REMUNERACION.getCampo()));

                    participacion.put(Interes.MONTO_MENSUAL.getCampo(), obtenerMonto(registro.getJsonObject(Interes.MONTO_MENSUAL.getCampo())));
                    participacion.put(EnumActividadLaboral.UBICACION.getCampo(), obtenerUbicacion(registro.getJsonObject(Interes.LOCALIZACION.getCampo())));
                    participaciones.add(participacion);
                }
            });
        }
        participacionInstituciones.put(Interes.PARTICIPACION.getCampo(), participaciones);

        return participacionInstituciones;
    }

    /**
     * Se genera el json del módulo de apoyos
     *
     * @param original
     * @return {@link JsonObject}
     */
    private JsonObject crearEstructuraApoyos(JsonObject original) {
        JsonObject apoyos = new JsonObject();
        JsonArray apoyosArray = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        apoyos.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Interes.APOYOS.getCampo()) != null) {
            original.getJsonArray(Interes.APOYOS.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                if (EnumParticipante.DECLARANTE.name().equals(
                        registro.getJsonObject(Interes.BENEFICIARIO_PROGRAMA.getCampo()).getString(EnumCatalogo.VALOR.getCampo()))) {
                    JsonObject apoyo = new JsonObject();

                    apoyo.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    apoyo.put(Interes.BENEFICIARIO_PROGRAMA.getCampo(),
                            crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_BENEFICIARIO_PROGRAMA,
                                            registro.getJsonObject(Interes.BENEFICIARIO_PROGRAMA.getCampo()).getInteger(Encabezados.ID.getCampo())),
                                    registro.getJsonObject(Interes.BENEFICIARIO_PROGRAMA.getCampo()).getString(EnumCatalogo.VALOR.getCampo())));

                    apoyo.put(Interes.NOMBRE_PROGRAMA.getCampo(), registro.getString(Interes.NOMBRE_PROGRAMA.getCampo()));
                    apoyo.put(Interes.INSTITUCION_OTORGANTE.getCampo(), registro.getString(Interes.INSTITUCION_OTORGANTE.getCampo()));

                    apoyo.put(Interes.NIVEL_ORDEN_GOB.getCampo(),
                            NivelOrdenGobierno.valueOf(registro.getString(Interes.NIVEL_ORDEN_GOB.getCampo())).getClavePdn());

                    Integer idApoyo = registro.getJsonObject(Interes.TIPO_APOYO.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valApoyo = idApoyo.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Interes.TIPO_APOYO_OTRO.getCampo())
                            : registro.getJsonObject(Interes.TIPO_APOYO.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    apoyo.put(Interes.TIPO_APOYO.getCampo(),
                            crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_APOYO, idApoyo), valApoyo));

                    apoyo.put(Interes.FORMA_RECEPCION.getCampo(), registro.getString(Interes.FORMA_RECEPCION.getCampo()));

                    apoyo.put(Interes.MONTO_APOYO_MENSUAL.getCampo(), obtenerMonto(registro.getJsonObject(Interes.MONTO_APOYO_MENSUAL.getCampo())));
                    apoyo.put(Interes.ESPECIFIQUE_APOYO.getCampo(), registro.getString(Interes.ESPECIFIQUE_APOYO.getCampo()));
                    apoyosArray.add(apoyo);
                }                
            });
        }
        apoyos.put(Interes.APOYO.getCampo(), apoyosArray);

        return apoyos;
    }

    /**
     * Se genera el json del módulo de representaciones
     *
     * @param original
     * @return {@link JsonObject}
     */
    private JsonObject crearEstructuraRepresentacion(JsonObject original) {
        JsonObject representaciones = new JsonObject();
        JsonArray representacionArray = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        representaciones.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Interes.REPRESENTACIONES.getCampo()) != null) {
            original.getJsonArray(Interes.REPRESENTACIONES.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                if (EnumParticipante.DECLARANTE.name().equals(registro.getString(Interes.PARTICIPANTE.getCampo()))) {
                    JsonObject representacion = new JsonObject();

                    representacion.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    representacion.put(Interes.TIPO_RELACION.getCampo(), registro.getString(Interes.PARTICIPANTE.getCampo()));

                    representacion.put(Interes.TIPO_REPRESENTACION.getCampo(), registro.getString(Interes.TIPO_REPRESENTACION.getCampo()));
                    representacion.put(Interes.FECHA_IN_REPRESENTACION.getCampo(), registro.getString(Interes.FECHA_IN_REPRESENTACION.getCampo()));

                    representacion.put(EnumPersona.TIPO_PERSONA.getCampo(), TipoPersona.valueOf(
                            registro.getJsonObject(Interes.REPRES_REPRES.getCampo()).getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

                    if (EnumTipoPersona.PERSONA_MORAL.name().equals(registro.getJsonObject(Interes.REPRES_REPRES.getCampo()).getString(EnumPersona.TIPO_PERSONA.getCampo()))) {
                        obtenerPersonaMoral(
                                registro.getJsonObject(Interes.REPRES_REPRES.getCampo())
                                .getJsonObject(EnumPersona.PERSONA_MORAL.getCampo()), representacion);
                    }

                    representacion.put(Interes.RECIBE_REMUNERACION.getCampo(), registro.getBoolean(Interes.RECIBE_REMUNERACION.getCampo()));
                    representacion.put(Interes.MONTO_MENSUAL.getCampo(), obtenerMonto(registro.getJsonObject(Interes.MONTO_MENSUAL.getCampo())));
                    representacion.put(EnumActividadLaboral.UBICACION.getCampo(), obtenerUbicacion(registro.getJsonObject(Interes.LOCALIZACION.getCampo())));
                    representacion.put(Interes.SECTOR.getCampo(), obtenerSector(registro));
                    
                    representacionArray.add(representacion);
                }                
            });
        }
        representaciones.put(Interes.REPRESENTACION.getCampo(), representacionArray);

        return representaciones;
    }

    /**
     * Se genera el json del módulo de clientes principales
     *
     * @param original
     * @return {@link JsonObject}
     */
    private JsonObject crearEstructuraClientesP(JsonObject original) {
        JsonObject clientesPrincipales = new JsonObject();
        JsonArray clientes = new JsonArray();

        boolean ninguno = original.getJsonArray(Interes.CLIENTES.getCampo()) == null || original.getJsonArray(Interes.CLIENTES.getCampo()).isEmpty();

        clientesPrincipales.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Interes.CLIENTES.getCampo()) != null) {
            original.getJsonArray(Interes.CLIENTES.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                if (EnumParticipante.DECLARANTE.name().equals(registro.getString(Interes.PARTICIPANTE.getCampo()))) {
                    JsonObject cliente = new JsonObject();
                    cliente.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    cliente.put(Interes.TIPO_RELACION.getCampo(), registro.getString(Interes.PARTICIPANTE.getCampo()));

                    cliente.put(Interes.REALIZA_ACT_LUC.getCampo(), original.getBoolean(Interes.REALIZA_ACT_LUC.getCampo()));

                    cliente.put(Interes.EMPRESA.getCampo(), new JsonObject()
                            .put(Interes.NOMBRE_EMPRESA_SERVICIO.getCampo(), registro.getString(Interes.NOMBRE_EMPRESA_SERVICIO.getCampo()))
                            .put(EnumPersonaMoral.RFC.getCampo(), registro.getString(Interes.RFC_EMPRESA.getCampo())));

                    JsonObject clientePrincipal = new JsonObject()
                            .put(EnumPersona.TIPO_PERSONA.getCampo(), TipoPersona.valueOf(
                                            registro.getJsonObject(Interes.CLIENTE_PRINCIPAL.getCampo()).getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

                    if (EnumTipoPersona.PERSONA_MORAL.name().equals(
                            registro.getJsonObject(Interes.CLIENTE_PRINCIPAL.getCampo()).getString(EnumPersona.TIPO_PERSONA.getCampo()))) {

                        obtenerPersonaMoral(registro.getJsonObject(Interes.CLIENTE_PRINCIPAL.getCampo())
                                .getJsonObject(EnumPersona.PERSONA_MORAL.getCampo()), clientePrincipal);
                    }

                    cliente.put(Interes.CLIENTE_PRINCIPAL.getCampo(), clientePrincipal);

                    cliente.put(Interes.MONTO_APROX_GANANCIA.getCampo(), obtenerMonto(registro.getJsonObject(Interes.MONTO_APROX_GANANCIA.getCampo())));
                    cliente.put(EnumActividadLaboral.UBICACION.getCampo(), obtenerUbicacion(registro.getJsonObject(Interes.LOCALIZACION.getCampo())));
                    cliente.put(Interes.SECTOR.getCampo(), obtenerSector(registro));
                 
                    clientes.add(cliente);
                }
            });
        }
        clientesPrincipales.put(Interes.CLIENTE.getCampo(), clientes);

        return clientesPrincipales;
    }

    /**
     * Se genera el json del módulo de beneficios privados
     *
     * @param original
     * @return {@link JsonObject}
     */
    private JsonObject crearEstructuraBeneficios(JsonObject original) {
        JsonObject beneficiosPrivados = new JsonObject();
        JsonArray benPrivados = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        beneficiosPrivados.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Interes.BENEFICIOS.getCampo()) != null) {
            original.getJsonArray(Interes.BENEFICIOS.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                
                if (EnumParticipante.DECLARANTE.name().equals(
                        registro.getJsonObject(Interes.BENEFICIARIO.getCampo()).getString(EnumCatalogo.VALOR.getCampo()))) {
                    
                    JsonObject beneficio = new JsonObject();

                    beneficio.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    beneficio.put(Interes.BENEFICIARIO.getCampo(), new JsonArray().add(
                            crearObjetoCatalogo(
                                    CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_BENEFICIARIO_PROGRAMA,
                                            registro.getJsonObject(Interes.BENEFICIARIO.getCampo()).getInteger(Encabezados.ID.getCampo())),
                                    registro.getJsonObject(Interes.BENEFICIARIO.getCampo()).getString(EnumCatalogo.VALOR.getCampo()))
                    ));

                    Integer idBeneficio = registro.getJsonObject(Interes.TIPO_BENEFICIO.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valBeneficio = idBeneficio.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Interes.TIPO_BENEFICIO_OTRO.getCampo())
                            : registro.getJsonObject(Interes.TIPO_BENEFICIO.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    beneficio.put(Interes.TIPO_BENEFICIO.getCampo(),
                            crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_BENEFICIO, idBeneficio), valBeneficio));

                    JsonObject otorgante = new JsonObject().put(EnumPersona.TIPO_PERSONA.getCampo(), TipoPersona.valueOf(
                            registro.getJsonObject(Interes.OTORGANTE.getCampo()).getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

                    if (EnumTipoPersona.PERSONA_MORAL.name().equals(
                            registro.getJsonObject(Interes.OTORGANTE.getCampo()).getString(EnumPersona.TIPO_PERSONA.getCampo()))) {

                        obtenerPersonaMoral(registro.getJsonObject(Interes.OTORGANTE.getCampo())
                                .getJsonObject(EnumPersona.PERSONA_MORAL.getCampo()), otorgante);
                    }

                    beneficio.put(Interes.OTORGANTE.getCampo(), otorgante);
                    beneficio.put(Interes.FORMA_RECEPCION.getCampo(), registro.getString(Interes.FORMA_RECEPCION.getCampo()));
                    beneficio.put(Interes.ESP_BENEFICIO.getCampo(), registro.getString(Interes.ESP_BENEFICIO.getCampo()));
                    beneficio.put(Interes.MONTO_MENSUAL_APROX.getCampo(), obtenerMonto(registro.getJsonObject(Interes.MONTO_MENSUAL_APROX.getCampo())));
                    beneficio.put(Interes.SECTOR.getCampo(), obtenerSector(registro));
                    benPrivados.add(beneficio);
                }
            });
        }
        beneficiosPrivados.put(Interes.BENEFICIO.getCampo(), benPrivados);

        return beneficiosPrivados;
    }

    /**
     * Se genera el json del módulo de fideicomisos
     *
     * @param original
     * @return {@link JsonObject}
     */
    private JsonObject crearEstructuraFideicomisos(JsonObject original) {
        JsonObject fideicomisos = new JsonObject();
        JsonArray fideicomisoArray = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        fideicomisos.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Interes.FIDEICOMISOS.getCampo()) != null) {
            original.getJsonArray(Interes.FIDEICOMISOS.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                if (EnumParticipante.DECLARANTE.name().equals(registro.getString(Interes.PARTICIPANTE.getCampo()))) {
                    JsonObject fideicomiso = new JsonObject();

                    fideicomiso.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());
                    fideicomiso.put(Interes.TIPO_RELACION.getCampo(), registro.getString(Interes.PARTICIPANTE.getCampo()));

                    fideicomiso.put(Interes.TIPO_FIDEICOMISO.getCampo(), registro.getString(Interes.TIPO_FIDEICOMISO.getCampo()));
                    fideicomiso.put(Interes.TIPO_PARTICIPACION.getCampo(), registro.getString(Interes.TIPO_PARTICIPACION.getCampo()));
                    fideicomiso.put(Interes.RFC_FIDEICOMISO.getCampo(), registro.getString(Interes.RFC_FIDEICOMISO.getCampo()));

                    fideicomiso.put(Interes.FIDEICOMITENTE.getCampo(), obtenerPersonaConTipo(registro.getJsonObject(Interes.FIDEICOMITENTE.getCampo())));
                    fideicomiso.put(Interes.FIDUCIARIO.getCampo(), obtenerPersonaMoral(registro.getJsonObject(Interes.FIDUCIARIO.getCampo()), new JsonObject()));
                    fideicomiso.put(Interes.FIDEICOMISARIO.getCampo(), obtenerPersonaConTipo(registro.getJsonObject(Interes.FIDEICOMISARIO.getCampo())));
//					fideicomiso.put(Interes.LOCALIZACION.getCampo(), registro.getString(Interes.ESP_BENEFICIO.getCampo()));
                    fideicomiso.put(Interes.SECTOR.getCampo(), obtenerSector(registro));
                    fideicomiso.put(Interes.EXTRANJERO.getCampo(), Ubicacion.valueOf(registro.getString(Interes.LOCALIZACION.getCampo())).getClave());
                    fideicomisoArray.add(fideicomiso);
                }
            });
        }
        fideicomisos.put(Interes.FIDEICOMISO.getCampo(), fideicomisoArray);

        return fideicomisos;
    }
}