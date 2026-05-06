/**
 * ObtenerClavePdnService.java Apr 1, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.dao.CatalogoDAOImpl;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadFinanciera;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadIndustrial;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadLaboral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumBien;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCatalogo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDatosPersonales;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumEnajenacionBien;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumMontoMoneda;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumPersona;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumPersonaMoral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumSectorPrivado;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumSectorPublico;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumServiciosProfesionales;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumTelefono;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumTransmisor;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoSector;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoBienPrestamo;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoPersona;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;
import mx.gob.sfp.dgti.enums.Adeudo;
import mx.gob.sfp.dgti.enums.AmbitoPublico;
import mx.gob.sfp.dgti.enums.AmbitoSector;
import mx.gob.sfp.dgti.enums.Bien;
import mx.gob.sfp.dgti.enums.CamposPDN;
import mx.gob.sfp.dgti.enums.CatalogosPdn;
import mx.gob.sfp.dgti.enums.Comodato;
import mx.gob.sfp.dgti.enums.DatosEmpleo;
import mx.gob.sfp.dgti.enums.Encabezados;
import mx.gob.sfp.dgti.enums.ExperienciaLab;
import mx.gob.sfp.dgti.enums.Ingresos;
import mx.gob.sfp.dgti.enums.IngresosPorTipoDec;
import mx.gob.sfp.dgti.enums.Interes;
import mx.gob.sfp.dgti.enums.Inversion;
import mx.gob.sfp.dgti.enums.NivelOrdenGobierno;
import mx.gob.sfp.dgti.enums.Otros;
import mx.gob.sfp.dgti.enums.TipoPersona;
import mx.gob.sfp.dgti.enums.Ubicacion;
import mx.gob.sfp.dgti.enums.UnidadMedida;

/**
 * @author Miriam Sanchez programador07
 * @modifiedBy programador09
 * @since Apr 1, 2020
 */
public class GenericosPdnService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GenericosPdnService.class);
    private static final String EMPTY = "";
    private static final String ESPACIO = " ";

    /**
     * @param registro
     * @return {@link String}
     */
    public String obtenerAmbitoPublico(JsonObject registro) {
        return AmbitoPublico.valueOf(registro.getJsonObject(Encabezados.ENTE.getCampo())
                .getString(EnumSectorPublico.AMBITO_PUBLICO.getCampo())).getClavePdn();
    }

    /**
     * @param registro
     * @return {@link String}
     */
    public String obtenerNivelGobierno(JsonObject registro) {
        return NivelOrdenGobierno.valueOf(registro.getJsonObject(Encabezados.ENTE.getCampo())
                .getJsonObject(EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo())
                .getString(EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo())).getClavePdn();
    }

    /**
     * @param modulo
     * @param registro
     * @return {@link JsonObject}
     */
    public JsonObject obtenerDomicilio(JsonObject modulo, JsonObject registro) {

        if (registro.getJsonObject(EnumDomicilio.DOMICILIO_MEXICO.getNombre()) != null) {

            JsonObject domicilioMexico = registro.getJsonObject(EnumDomicilio.DOMICILIO_MEXICO.getNombre());
            JsonObject entidad = domicilioMexico.getJsonObject(EnumDomicilio.ENTIDAD_FED.getNombre());
            JsonObject municipio = domicilioMexico.getJsonObject(EnumDomicilio.MUNICIPIO.getNombre());

            entidad.put(Encabezados.CLAVE.getCampo(),
                    CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_ENTIDAD_FEDERATIVA,
                            entidad.getInteger(Encabezados.ID.getCampo())));
            entidad.remove(Encabezados.ID.getCampo());

            municipio.put(Encabezados.CLAVE.getCampo(),
                    CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_MUNICIPIO_ALCALDIA,
                            municipio.getInteger(Encabezados.ID.getCampo())));
            municipio.remove(Encabezados.ID.getCampo());
            municipio.remove(Encabezados.FK.getCampo());

            domicilioMexico.put(EnumDomicilio.ENTIDAD_FED.getNombre(), entidad);
            domicilioMexico.put(EnumDomicilio.MUNICIPIO.getNombre(), municipio);

            modulo.put(EnumDomicilio.DOMICILIO_MEXICO.getNombre(), domicilioMexico);

        } else if (registro.getJsonObject(EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre()) != null) {

            JsonObject extranjero = registro.getJsonObject(EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre());
            Integer idPais = extranjero.getJsonObject(Inversion.PAIS.getCampo()).getInteger(Encabezados.ID.getCampo());
            extranjero.remove(Inversion.PAIS.getCampo());
            extranjero.put(Inversion.PAIS.getCampo(), CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_PAIS,
                    idPais));

            modulo.put(EnumDomicilio.DOMICILIO_EXTRANJERO.getNombre(), extranjero);
        }

        return modulo;
    }

    /**
     * Obtener experiencia laboral de ambito publico
     *
     * @param experienciaPdn
     * @param actividadLab
     * @return {@link JsonObject}
     */
    public JsonObject obtenerExperienciaLabPub(JsonObject experienciaPdn, JsonObject actividadLab) {
        experienciaPdn.put(EnumActividadLaboral.AMBITO_SECTOR.getCampo(),
                crearObjetoCatalogo(AmbitoSector.PUBLICO.getClavePdn(), EnumAmbitoSector.PUBLICO.getDescripcion()));

        obtenerSectorPublico(actividadLab.getJsonObject(EnumActividadLaboral.SECTOR_PUBLICO.getCampo()), experienciaPdn);
        return actividadLab;
    }

    /**
     * Obtener experiencia laboral de ambito privado/otro
     *
     * @param experienciaPdn
     * @param actividadLab
     * @return {@link JsonObject}
     */
    public JsonObject obtenerExperienciaLabPrivOtro(JsonObject experienciaPdn, JsonObject actividadLab) {

        if (EnumAmbitoSector.PRIVADO.getId().equals(
                actividadLab.getJsonObject(EnumActividadLaboral.AMBITO_SECTOR.getCampo())
                .getInteger(Encabezados.ID.getCampo()))) {

            experienciaPdn.put(EnumActividadLaboral.AMBITO_SECTOR.getCampo(),
                    crearObjetoCatalogo(AmbitoSector.PRIVADO.getClavePdn(), EnumAmbitoSector.PRIVADO.getDescripcion()));

        } else if (EnumAmbitoSector.OTRO.getId().equals(
                actividadLab.getJsonObject(EnumActividadLaboral.AMBITO_SECTOR.getCampo())
                .getInteger(Encabezados.ID.getCampo()))) {

            experienciaPdn.put(EnumActividadLaboral.AMBITO_SECTOR.getCampo(), crearObjetoCatalogo(
                    AmbitoSector.OTRO.getClavePdn(),
                    actividadLab.getString(ExperienciaLab.AMBITO_SECTOR_OTRO.getCampo())));
        }
        obtenerSectorPrivado(actividadLab.getJsonObject(EnumActividadLaboral.SECTOR_PRIVADO.getCampo()), experienciaPdn);

        return actividadLab;
    }

    /**
     * Obtener la estructura del objeto de tipo catalogo
     *
     * @param clave
     * @param valor
     * @return {@link JsonObject}
     */
    public JsonObject crearObjetoCatalogo(String clave, String valor) {
        return new JsonObject()
                .put(Encabezados.CLAVE.getCampo(), clave)
                .put(EnumCatalogo.VALOR.getCampo(), valor);
    }

    /**
     * Asignar las propiedades del sector publico
     *
     * @param sector
     * @param registroPdn
     * @return {@link JsonObject}
     */
    private JsonObject obtenerSectorPublico(JsonObject sector, JsonObject registroPdn) {

        registroPdn.put(EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo(),
                "MUNICIPAL".equals(sector.getString(EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo()))
                        ? "MUNICIPAL_ALCALDIA"
                        : sector.getString(EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo())
        );
        registroPdn.put(EnumSectorPublico.AMBITO_PUBLICO.getCampo(),
                sector.getString(EnumSectorPublico.AMBITO_PUBLICO.getCampo()) != null && !sector.getString(EnumSectorPublico.AMBITO_PUBLICO.getCampo()).equals("ORGANISMO_AUTONOMO") ?
                        AmbitoPublico.valueOf(sector.getString(EnumSectorPublico.AMBITO_PUBLICO.getCampo())).getClavePdn() : AmbitoPublico.ORGANISMOS_AUTONOMOS.getClavePdn());
        registroPdn.put(EnumSectorPublico.NOMBRE_ENTE_PUBLICO.getCampo(),
                sector.getString(EnumSectorPublico.NOMBRE_ENTE_PUBLICO.getCampo()));
        registroPdn.put(EnumSectorPublico.AREA_ADSCRIPCION.getCampo(),
                sector.getString(EnumSectorPublico.AREA_ADSCRIPCION.getCampo()));
        registroPdn.put(EnumSectorPublico.EMPLEO_CARGO_COMISION.getCampo(),
                sector.getString(EnumSectorPublico.EMPLEO_CARGO_COMISION.getCampo()));
        registroPdn.put(EnumSectorPublico.FUNCION_PRINCIPAL.getCampo(),
                sector.getString(EnumSectorPublico.FUNCION_PRINCIPAL.getCampo()));
        return registroPdn;
    }

    /**
     * Asignar las propiedades del sector privado
     *
     * @param sectorPriv
     * @param registroPdn
     * @return {@link JsonObject}
     */
    private JsonObject obtenerSectorPrivado(JsonObject sectorPriv, JsonObject registroPdn) {

        registroPdn.put(EnumSectorPrivado.NOMBRE_EMPRESA.getCampo(), sectorPriv.getString(EnumSectorPrivado.NOMBRE_EMPRESA.getCampo()));
        registroPdn.put(EnumSectorPrivado.RFC.getCampo(), sectorPriv.getString(EnumSectorPrivado.RFC.getCampo()));
        registroPdn.put(EnumSectorPrivado.AREA.getCampo(), sectorPriv.getString(EnumSectorPrivado.AREA.getCampo()));
        registroPdn.put(CamposPDN.PUESTO.getCampo(), sectorPriv.getString(EnumSectorPrivado.EMPLEO_CARGO.getCampo()));

        JsonObject sector = sectorPriv.getJsonObject(EnumSectorPrivado.SECTOR.getCampo());

        Integer idSector = sector.getInteger(Encabezados.ID.getCampo());
        String valSector = idSector.equals(Otros.OTROS_ID.getId())
                ? sectorPriv.getString(ExperienciaLab.SECTOR_OTRO.getCampo())
                : sector.getString(EnumCatalogo.VALOR.getCampo());

        registroPdn.put(EnumSectorPrivado.SECTOR.getCampo(),
                crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_SECTOR_PRIVADO, idSector), valSector));

        return registroPdn;
    }

    /**
     * Obtener el objeto de monto
     *
     * @param remuneracion
     * @return {@link JsonObject}
     */
    public JsonObject obtenerMonto(JsonObject remuneracion) {
        JsonObject monto = null;

        if (remuneracion != null) {

            monto = new JsonObject();
            monto.put(EnumCatalogo.VALOR.getCampo(), remuneracion.getLong(EnumMontoMoneda.MONTO.getCampo()));
            monto.put(EnumMontoMoneda.MONEDA.getCampo(),
                    CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_MONEDA,
                            remuneracion.getJsonObject(EnumMontoMoneda.MONEDA.getCampo()).getInteger(Encabezados.ID.getCampo())));
        }

        return monto;
    }

    /**
     * Obtener el array de actividades industriales, comerciales y empresariales
     *
     * @param actividadesOrig
     * @return {@link JsonObject}
     */
    public JsonObject obtenerActividadIndustrial(JsonArray actividadesOrig) {

        JsonObject actividadICE = new JsonObject();
        JsonArray actividades = new JsonArray();

        if (actividadesOrig != null) {
            long total = 0;

            for (Object item : actividadesOrig) {
                JsonObject rem = ((JsonObject) item).getJsonObject(EnumRemuneracion.REMUNERACION.getCampo());
                JsonObject actividad = new JsonObject();

                actividad.put(EnumRemuneracion.REMUNERACION.getCampo(), obtenerMonto(rem));

                actividad.put(EnumActividadIndustrial.NOMBRE_RAZON_SOCIAL.getCampo(),
                        ((JsonObject) item).getString(EnumActividadIndustrial.NOMBRE_RAZON_SOCIAL.getCampo()));

                actividad.put(EnumActividadIndustrial.TIPO_NEGOCIO.getCampo(),
                        ((JsonObject) item).getString(EnumActividadIndustrial.TIPO_NEGOCIO.getCampo()));

                total += rem.getLong(EnumMontoMoneda.MONTO.getCampo());
                actividades.add(actividad);
            }

            actividadICE.put(Ingresos.ACTIVIDADES.getCampo(), actividades);
            actividadICE.put(Ingresos.REMUNERACION_TOTAL.getCampo(), new JsonObject()
                    .put(EnumCatalogo.VALOR.getCampo(), total)
                    .put(EnumMontoMoneda.MONEDA.getCampo(), Ingresos.MXN.getCampo()));
        }

        return actividadICE;
    }

    /**
     * Obtener el array de actividades financieras
     *
     * @param actividadesOrig
     * @return {@link JsonObject}
     */
    public JsonObject obtenerActividadFinanciera(JsonArray actividadesOrig) {

        JsonObject actividadFin = new JsonObject();
        JsonArray actividades = new JsonArray();

        if (actividadesOrig != null) {
            long total = 0;

            for (Object item : actividadesOrig) {
                JsonObject rem = ((JsonObject) item).getJsonObject(EnumRemuneracion.REMUNERACION.getCampo());
                JsonObject instrumento = ((JsonObject) item).getJsonObject(EnumActividadFinanciera.TIPO_INSTRUMENTO.getCampo());
                JsonObject actividad = new JsonObject();

                Integer idInstrumento = instrumento.getInteger(Encabezados.ID.getCampo());
//				String valInstrumento = idInstrumento.equals(Otros.OTROS_ID.getId()) 
//						? ((JsonObject) item).getString(Ingresos.TIPO_INSTRUMENTO_OTRO.getCampo())
//						: instrumento.getString(EnumCatalogo.VALOR.getCampo());
                String valInstrumento = instrumento.getString(EnumCatalogo.VALOR.getCampo());

                actividad.put(EnumRemuneracion.REMUNERACION.getCampo(), obtenerMonto(rem));

                actividad.put(EnumActividadFinanciera.TIPO_INSTRUMENTO.getCampo(), crearObjetoCatalogo(
                        CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_INSTRUMENTO, idInstrumento), valInstrumento));

                total += rem.getLong(EnumMontoMoneda.MONTO.getCampo());
                actividades.add(actividad);
            }

            actividadFin.put(Ingresos.ACTIVIDADES.getCampo(), actividades);
            actividadFin.put(Ingresos.REMUNERACION_TOTAL.getCampo(), new JsonObject()
                    .put(EnumCatalogo.VALOR.getCampo(), total)
                    .put(EnumMontoMoneda.MONEDA.getCampo(), Ingresos.MXN.getCampo()));
        }

        return actividadFin;
    }

    /**
     * Obtener el array de servicios profesionales
     *
     * @param actividadesOrig
     * @return {@link JsonObject}
     */
    public JsonObject obtenerServiciosProfesionales(JsonArray actividadesOrig) {

        JsonObject serviciosProf = new JsonObject();
        JsonArray servicios = new JsonArray();

        if (actividadesOrig != null) {
            long total = 0;

            for (Object item : actividadesOrig) {
                JsonObject rem = ((JsonObject) item).getJsonObject(EnumRemuneracion.REMUNERACION.getCampo());
                JsonObject servicio = new JsonObject();

                servicio.put(EnumRemuneracion.REMUNERACION.getCampo(), obtenerMonto(rem));

                servicio.put(EnumServiciosProfesionales.TIPO_SERVICIO.getCampo(),
                        ((JsonObject) item).getString(EnumServiciosProfesionales.TIPO_SERVICIO.getCampo()));

                total += rem.getLong(EnumMontoMoneda.MONTO.getCampo());
                servicios.add(servicio);
            }

            serviciosProf.put(Ingresos.SERVICIOS.getCampo(), servicios);
            serviciosProf.put(Ingresos.REMUNERACION_TOTAL.getCampo(), new JsonObject()
                    .put(EnumCatalogo.VALOR.getCampo(), total)
                    .put(EnumMontoMoneda.MONEDA.getCampo(), Ingresos.MXN.getCampo()));
        }

        return serviciosProf;
    }

    /**
     * Obtener el array de otros ingresos
     *
     * @param actividadesOrig
     * @return {@link JsonObject}
     */
    public JsonObject obtenerOtrosIngresos(JsonArray actividadesOrig) {

        JsonObject serviciosProf = new JsonObject();
        JsonArray servicios = new JsonArray();

        if (actividadesOrig != null) {
            long total = 0;

            for (Object item : actividadesOrig) {
                JsonObject rem = ((JsonObject) item).getJsonObject(EnumRemuneracion.REMUNERACION.getCampo());
                JsonObject servicio = new JsonObject();

                servicio.put(EnumRemuneracion.REMUNERACION.getCampo(), obtenerMonto(rem));

                servicio.put(EnumServiciosProfesionales.TIPO_INGRESO.getCampo(),
                        ((JsonObject) item).getString(EnumServiciosProfesionales.TIPO_INGRESO.getCampo()));

                total += rem.getLong(EnumMontoMoneda.MONTO.getCampo());
                servicios.add(servicio);
            }

            serviciosProf.put(Ingresos.INGRESOS.getCampo(), servicios);
            serviciosProf.put(Ingresos.REMUNERACION_TOTAL.getCampo(), new JsonObject()
                    .put(EnumCatalogo.VALOR.getCampo(), total)
                    .put(EnumMontoMoneda.MONEDA.getCampo(), Ingresos.MXN.getCampo()));
        }

        return serviciosProf;
    }

    /**
     * Obtener el array de enajenacion de bienes
     *
     * @param bienes
     * @return {@link JsonObject}
     */
    public JsonObject obtenerEnajenacionB(JsonArray bienes) {

        JsonObject enajenacionB = new JsonObject();
        JsonArray registros = new JsonArray();

        if (bienes != null) {
            long total = 0;

            for (Object item : bienes) {
                JsonObject rem = ((JsonObject) item).getJsonObject(EnumRemuneracion.REMUNERACION.getCampo());
                JsonObject servicio = new JsonObject();

                servicio.put(EnumRemuneracion.REMUNERACION.getCampo(), obtenerMonto(rem));
                servicio.put(Ingresos.TIPO_BIEN.getCampo(),
                        ((JsonObject) item).getString(EnumEnajenacionBien.TIPO_BIEN.getCampo()));

                total += rem.getLong(EnumMontoMoneda.MONTO.getCampo());
                registros.add(servicio);
            }

            enajenacionB.put(Ingresos.BIENES.getCampo(), registros);
            enajenacionB.put(Ingresos.REMUNERACION_TOTAL.getCampo(), new JsonObject()
                    .put(EnumCatalogo.VALOR.getCampo(), total)
                    .put(EnumMontoMoneda.MONEDA.getCampo(), Ingresos.MXN.getCampo()));
        }

        return enajenacionB;
    }

    /**
     * Obtener array de terceros
     *
     * @param original
     * @return {
     * @JsonArray}
     */
    public JsonArray obtenerTerceros(JsonObject original) {
        JsonArray terceros = new JsonArray();

        if (original.getJsonArray(EnumBien.TERCEROS.getNombre()) != null) {
            original.getJsonArray(EnumBien.TERCEROS.getNombre()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                JsonObject tercero = new JsonObject();

                tercero.put(EnumPersona.TIPO_PERSONA.getCampo(),
                        TipoPersona.valueOf(registro.getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

                if (EnumTipoPersona.PERSONA_FISICA.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {

                    JsonObject personaOri = registro.getJsonObject(EnumPersona.PERSONA_FISICA.getCampo());
                    tercero.put(EnumActividadIndustrial.NOMBRE_RAZON_SOCIAL.getCampo(), obtenerNombreDePersonaFisica(personaOri));
                    tercero.put(EnumDatosPersonales.RFC.getCampo(), personaOri.getString(EnumDatosPersonales.RFC.getCampo()));

                } else if (EnumTipoPersona.PERSONA_MORAL.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {

                    JsonObject personaOri = registro.getJsonObject(EnumPersona.PERSONA_MORAL.getCampo());
                    tercero.put(EnumActividadIndustrial.NOMBRE_RAZON_SOCIAL.getCampo(),
                            personaOri.getString(EnumDatosPersonales.NOMBRE.getCampo()));
                    tercero.put(EnumDatosPersonales.RFC.getCampo(), personaOri.getString(EnumDatosPersonales.RFC.getCampo()));
                }

                terceros.add(tercero);
            });
        }
        return terceros;
    }

    /**
     * Obtener array de transmisores
     *
     * @param original
     * @return {
     * @JsonArray}
     */
    public JsonArray obtenerTransmisores(JsonObject original) {
        JsonArray transmisores = new JsonArray();

        if (original.getJsonArray(EnumBien.TRANSMISORES.getNombre()) != null) {
            original.getJsonArray(EnumBien.TRANSMISORES.getNombre()).forEach(item -> {

                JsonObject registro = (JsonObject) item;

                if (EnumTipoPersona.PERSONA_MORAL.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {
                    JsonObject transmisor = new JsonObject();

                    transmisor.put(EnumPersona.TIPO_PERSONA.getCampo(),
                            TipoPersona.valueOf(registro.getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

                    transmisor.put(EnumActividadIndustrial.NOMBRE_RAZON_SOCIAL.getCampo(),
                            registro.getJsonObject(EnumPersona.PERSONA_MORAL.getCampo())
                            .getString(EnumDatosPersonales.NOMBRE.getCampo()));

                    transmisor.put(EnumDatosPersonales.RFC.getCampo(),
                            registro.getJsonObject(EnumPersona.PERSONA_MORAL.getCampo())
                            .getString(EnumDatosPersonales.RFC.getCampo()));

                    Integer idRelacion = registro.getJsonObject(EnumTransmisor.RELACION_CON_TITULAR.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valRelacion = idRelacion.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Bien.RELACION_OTRO.getCampo())
                            : registro.getJsonObject(EnumTransmisor.RELACION_CON_TITULAR.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    transmisor.put(Bien.RELACION.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_RELACION_BIENES, idRelacion), valRelacion));

                    transmisores.add(transmisor);
                }

            });
        }
        return transmisores;
    }

    /**
     * Obtener de un String el valor o un vacìo
     *
     * @param valor
     * @return <@String>
     */
    public String obtenerValorOVacio(String valor) {
        if (valor != null) {
            return valor;
        }
        return EMPTY;
    }

    /**
     * Obtener el objeto para otorgante de credito
     *
     * @param registro
     * @return <@JsonObject>
     */
    public JsonObject obtenerOtorganteCredito(JsonObject registro) {
        JsonObject otorgante = null;

        if (registro != null) {

            otorgante = new JsonObject();
            otorgante.put(EnumPersona.TIPO_PERSONA.getCampo(),
                    TipoPersona.valueOf(registro.getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

//			if(EnumTipoPersona.PERSONA_FISICA.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {
//				JsonObject personaOri = registro.getJsonObject(EnumPersona.PERSONA_FISICA.getCampo());
//				otorgante.put(Adeudo.NOMBRE_INSTITUCION.getCampo(), obtenerNombreDePersonaFisica(personaOri));
//				otorgante.put(EnumDatosPersonales.RFC.getCampo(), personaOri.getString(EnumDatosPersonales.RFC.getCampo()));
//			} else 
            if (EnumTipoPersona.PERSONA_MORAL.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {
                JsonObject personaOri = registro.getJsonObject(EnumPersona.PERSONA_MORAL.getCampo());

                otorgante.put(Adeudo.NOMBRE_INSTITUCION.getCampo(),
                        personaOri.getString(EnumDatosPersonales.NOMBRE.getCampo()));
                otorgante.put(EnumDatosPersonales.RFC.getCampo(), personaOri.getString(EnumDatosPersonales.RFC.getCampo()));
            }
        }
        return otorgante;
    }

    /**
     * Obtener el objeto de tipo bien para el mòdulo de comodato
     *
     * @param registro
     * @return <@JsonObject>
     */
    public JsonObject obtenerTipoBienParaComodato(JsonObject registro) {

        JsonObject tipoBien = new JsonObject();
        JsonObject bien = new JsonObject();

        if (EnumTipoBienPrestamo.INMUEBLE.name().equals(registro.getString(Bien.TIPO_BIEN.getCampo()))) {

            Integer idInmueble
                    = registro.getJsonObject(Bien.INMUEBLE.getCampo()).getJsonObject(Bien.TIPO_INMUEBLE.getCampo()).getInteger(Encabezados.ID.getCampo());
            String valInmueble = idInmueble.equals(Otros.OTROS_ID.getId())
                    ? registro.getJsonObject(Bien.INMUEBLE.getCampo()).getString(Bien.TIPO_INMUEBLE_OTRO.getCampo())
                    : registro.getJsonObject(Bien.INMUEBLE.getCampo()).getJsonObject(Bien.TIPO_INMUEBLE.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

            bien.put(Bien.TIPO_INMUEBLE.getCampo(),
                    crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_BIEN_INMUEBLE, idInmueble), valInmueble));
            tipoBien.put(Bien.INMUEBLE.getCampo(), bien);

        } else if (EnumTipoBienPrestamo.VEHICULO.name().equals(registro.getString(Bien.TIPO_BIEN.getCampo()))) {

            Integer idTipoVehiculo = registro.getJsonObject(Bien.VEHICULO.getCampo()).getJsonObject(Bien.TIPO_VEHICULO.getCampo()).getInteger(Encabezados.ID.getCampo());
            String valTipoVehi = idTipoVehiculo.equals(Otros.OTROS_ID.getId())
                    ? registro.getJsonObject(Bien.VEHICULO.getCampo()).getString(Bien.TIPO_VEHICULO_OTRO.getCampo())
                    : registro.getJsonObject(Bien.VEHICULO.getCampo()).getJsonObject(Bien.TIPO_VEHICULO.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

            bien.put(Comodato.TIPO.getCampo(), crearObjetoCatalogo(
                    CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_VEHICULO, idTipoVehiculo), valTipoVehi));

            bien.put(Comodato.MARCA.getCampo(),
                    registro.getJsonObject(Bien.VEHICULO.getCampo()).getString(Bien.MARCA.getCampo()));
            bien.put(Comodato.MODELO.getCampo(),
                    registro.getJsonObject(Bien.VEHICULO.getCampo()).getString(Bien.MODELO.getCampo()));
            bien.put(Comodato.ANIO.getCampo(),
                    registro.getJsonObject(Bien.VEHICULO.getCampo()).getInteger(Bien.ANIO.getCampo()));

            tipoBien.put(Bien.VEHICULO.getCampo(), bien);
        }

        return tipoBien;
    }

    /**
     * Obtener el objeto de tipo bien para el mòdulo de comodato
     *
     * @param original
     * @return <@JsonObject>
     */
    public JsonObject obtenerDuenoTitularParaComodato(JsonObject original) {

        JsonObject duenoTitular = new JsonObject();
        JsonObject registro = original.getJsonObject(Comodato.DUENO_TITULAR.getCampo());

        duenoTitular.put(Comodato.TIPO_DUENO_TITULAR.getCampo(),
                TipoPersona.valueOf(registro.getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

        if (EnumTipoPersona.PERSONA_MORAL.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {
            JsonObject personaOri = registro.getJsonObject(EnumPersona.PERSONA_MORAL.getCampo());

            duenoTitular.put(Comodato.NOMBRE_TITULAR.getCampo(), personaOri.getString(EnumDatosPersonales.NOMBRE.getCampo()));
            duenoTitular.put(EnumDatosPersonales.RFC.getCampo(), personaOri.getString(EnumDatosPersonales.RFC.getCampo()));
            duenoTitular.put("relacionConTitular", original.getString(Comodato.RELACION_CON_TITULAR.getCampo()));
        }

        return duenoTitular;
    }

    /**
     * Obtener localizacion de inversiones
     *
     * @param registro
     * @return <@JsonObject>
     */
    public JsonObject obtenerLocalizacion(JsonObject registro) {
        JsonObject localizacion = new JsonObject();
        String clave;

        if (EnumUbicacion.MEXICO.name().equals(registro.getString(EnumActividadLaboral.UBICACION.getCampo()))) {

            localizacion.put(Inversion.PAIS.getCampo(), Ubicacion.MEXICO.getClave());
            localizacion.put(EnumPersonaMoral.RFC.getCampo(), registro.getJsonObject(Inversion.LOCALIZACION.getCampo())
                    .getJsonObject(Inversion.INSTITUCION_RAZONS.getCampo()).getString(EnumPersonaMoral.RFC.getCampo()));
        }

        if (EnumUbicacion.EXTRANJERO.name().equals(registro.getString(EnumActividadLaboral.UBICACION.getCampo()))) {

            clave = registro.getJsonObject(Inversion.LOCALIZACION.getCampo()).getJsonObject(Inversion.PAIS.getCampo()) != null
                    ? CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_PAIS, registro.getJsonObject(Inversion.LOCALIZACION.getCampo())
                            .getJsonObject(Inversion.PAIS.getCampo()).getInteger(Encabezados.ID.getCampo())) : null;

            localizacion.put(Inversion.PAIS.getCampo(), clave);
        }

        localizacion.put(Inversion.INSTITUCION_RAZONS.getCampo(), registro.getJsonObject(Inversion.LOCALIZACION.getCampo())
                .getJsonObject(Inversion.INSTITUCION_RAZONS.getCampo()).getString(Encabezados.NOMBRE.getCampo()));

        return localizacion;
    }

    /**
     * Obtener ubicacion
     *
     * @param registro
     * @return <@JsonObject>
     */
    protected JsonObject obtenerUbicacion(JsonObject registro) {

        JsonObject ubicacion = new JsonObject();
        JsonObject dom = registro.getJsonObject(EnumDomicilio.LOCALIZACION_MEXICO.getNombre());

        if (EnumUbicacion.MEXICO.name().equals(registro.getString(EnumActividadLaboral.UBICACION.getCampo()))) {
            ubicacion.put(EnumDomicilio.PAIS.getNombre(), Ubicacion.MEXICO.getClave());
            ubicacion.put(EnumDomicilio.ENTIDAD_FED.getNombre(), crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_ENTIDAD_FEDERATIVA,
                    dom.getJsonObject(EnumDomicilio.ENTIDAD_FED.getNombre()).getInteger(Encabezados.ID.getCampo())),
                    dom.getJsonObject(EnumDomicilio.ENTIDAD_FED.getNombre()).getString(EnumCatalogo.VALOR.getCampo())));
        }

        if (EnumUbicacion.EXTRANJERO.name().equals(registro.getString(EnumActividadLaboral.UBICACION.getCampo()))) {
            ubicacion.put(EnumDomicilio.PAIS.getNombre(), CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_PAIS,
                    registro.getJsonObject(EnumDomicilio.LOCALIZACION_EXTRANJERO.getNombre())
                    .getJsonObject(EnumDomicilio.PAIS.getNombre())
                    .getInteger(Encabezados.ID.getCampo())));
        }

        return ubicacion;
    }

    /**
     * Se obtiene el nombre de la persona fìsica
     *
     * @param personaOri
     * @return String
     */
    public String obtenerNombreDePersonaFisica(JsonObject personaOri) {
        return personaOri.getString(EnumDatosPersonales.NOMBRE.getCampo())
                + ESPACIO + obtenerValorOVacio(personaOri.getString(EnumDatosPersonales.PRIMER_APELLIDO.getCampo())
                        + ESPACIO + obtenerValorOVacio(personaOri.getString(EnumDatosPersonales.SEGUNDO_APELLIDO.getCampo())));
    }

    /**
     * Obtener el objeto de persona moral con tipoPersona
     *
     * @param registro
     * @return <@JsonObject>
     */
    public JsonObject obtenerPersonaConTipo(JsonObject registro) {
        JsonObject personaConTIpo = new JsonObject();

        personaConTIpo.put(EnumPersona.TIPO_PERSONA.getCampo(),
                TipoPersona.valueOf(registro.getString(EnumPersona.TIPO_PERSONA.getCampo())).getClavePdn());

        if (EnumTipoPersona.PERSONA_MORAL.name().equals(registro.getString(EnumPersona.TIPO_PERSONA.getCampo()))) {
            obtenerPersonaMoral(registro.getJsonObject(EnumPersona.PERSONA_MORAL.getCampo()), personaConTIpo);
        }

        return personaConTIpo;
    }

    /**
     * Obtener sector de la parte de intereses
     *
     * @param registro
     * @return <@JsonObject>
     */
    public JsonObject obtenerSector(JsonObject registro) {
        JsonObject sector = registro.getJsonObject(Interes.SECTOR.getCampo());
        Integer idSector = sector.getInteger(Encabezados.ID.getCampo());
        String valSector = idSector.equals(Otros.OTROS_ID.getId())
                ? registro.getString(ExperienciaLab.SECTOR_OTRO.getCampo())
                : sector.getString(EnumCatalogo.VALOR.getCampo());

        return crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(
                CatalogosPdn.CAT_SECTOR_PRIVADO, idSector), valSector);
    }

    /**
     * Obtener el objecto de persona moral
     *
     * @param registro
     * @param personaMoral
     * @return <@JsonObject>
     */
    public JsonObject obtenerPersonaMoral(JsonObject registro, JsonObject personaMoral) {
        personaMoral.put(Interes.NOMBRE_RAZON_SOCIAL.getCampo(), registro.getString(Encabezados.NOMBRE.getCampo()))
                .put(EnumPersonaMoral.RFC.getCampo(), registro.getString(EnumPersonaMoral.RFC.getCampo()));
        return personaMoral;
    }

    /**
     * Obtener el array con el titular
     *
     * @param id
     * @param valor
     * @return <@JsonArray>
     */
    public JsonArray obtenerTitularSolicitado(Integer id, String valor) {
        JsonArray titularSolicitado = new JsonArray();
        titularSolicitado.add(crearObjetoCatalogo(
                CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TITULAR, id),
                valor));
        return titularSolicitado;
    }

    /**
     * Obtener la superficie
     *
     * @param valor
     * @return <@JsonObject>
     */
    public JsonObject obtenerSuperficie(Integer valor) {
        JsonObject superficie = new JsonObject();
        superficie.put(EnumCatalogo.VALOR.getCampo(), valor);
        superficie.put(CamposPDN.UNIDAD.getCampo(), UnidadMedida.METRO_CUADRADO.getClave());
        return superficie;
    }

    /**
     * Obtener la etiqueta que corresponde de acuerdo al tipo de declaracion
     *
     * @param etiqueta
     * @param tipoDeclaracion
     * @return String
     */
    public String obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec etiqueta, String tipoDeclaracion) {

        if (EnumTipoDeclaracion.INICIO.name().equals(tipoDeclaracion)) {
            return etiqueta.getInicio();
        }

        if (EnumTipoDeclaracion.MODIFICACION.name().equals(tipoDeclaracion)) {
            return etiqueta.getModificacion();
        }

        if (EnumTipoDeclaracion.CONCLUSION.name().equals(tipoDeclaracion)) {
            return etiqueta.getConclusion();
        }
        return etiqueta.getInicio();
    }

    /**
     * Obtener el objeto de empleo cargo
     *
     * @param empleoCargo
     * @param registro
     * @return <@JsonObject>
     */
    public JsonObject obtenerEmpleoCargo(JsonObject empleoCargo, JsonObject registro) {

        empleoCargo.put(EnumSectorPublico.AREA_ADSCRIPCION.getCampo(), registro.getString(EnumSectorPublico.AREA_ADSCRIPCION.getCampo()));
        empleoCargo.put(DatosEmpleo.EMPLEO_CARGO.getCampo(), registro.getString(DatosEmpleo.EMPLEO_CARGO.getCampo()));
        empleoCargo.put(DatosEmpleo.CONTRATADO_HONORARIOS.getCampo(), registro.getBoolean(DatosEmpleo.CONTRATADO_HONORARIOS.getCampo()));

        empleoCargo.put(DatosEmpleo.NIVEL_EMPLEO_CARGO.getCampo(), registro.getString(DatosEmpleo.NIVEL_EMPLEO_CARGO.getCampo()));
        empleoCargo.put(DatosEmpleo.FUNCION_PRINCIPAL.getCampo(), registro.getString(DatosEmpleo.FUNCION_PRINCIPAL.getCampo()));
        empleoCargo.put(DatosEmpleo.FECHA_POSESION.getCampo(), registro.getString(DatosEmpleo.FECHA_ENCARGO.getCampo()));

        empleoCargo.put(EnumSectorPublico.NIVEL_ORDEN_GOBIERNO.getCampo(), obtenerNivelGobierno(registro));

        empleoCargo.put(EnumSectorPublico.AMBITO_PUBLICO.getCampo(), obtenerAmbitoPublico(registro));

        empleoCargo.put(EnumSectorPublico.NOMBRE_ENTE_PUBLICO.getCampo(),
                registro.getJsonObject(Encabezados.ENTE.getCampo()).getString(Encabezados.NOMBRE.getCampo()));

        empleoCargo.put(DatosEmpleo.TELEFONO_OFICINA.getCampo(),
                new JsonObject().put(DatosEmpleo.TELEFONO.getCampo(),
                        registro.getJsonObject(DatosEmpleo.TELEFONO_OFICINA.getCampo()).getString(EnumTelefono.NUMERO.getCampo()))
                .put(DatosEmpleo.EXTENSION.getCampo(),
                        registro.getJsonObject(DatosEmpleo.TELEFONO_OFICINA.getCampo()).getString(DatosEmpleo.EXTENSION.getCampo())));

        obtenerDomicilio(empleoCargo, registro.getJsonObject(EnumDomicilio.DOMICILIO.getNombre()));

        return empleoCargo;
    }
}