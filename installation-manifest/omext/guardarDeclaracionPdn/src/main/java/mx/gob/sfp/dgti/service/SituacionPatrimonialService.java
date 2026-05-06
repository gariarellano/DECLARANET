/**
 * SituacionPatrimonialService.java Apr 2, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Objects;
import mx.gob.sfp.dgti.dao.CatalogoDAOImpl;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadAnualAnt;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadLaboral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumBien;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCatalogo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCorreo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDatosPersonales;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumIngresoNetoD;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumRemuneracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoSector;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaPago;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumValorConformeA;
import mx.gob.sfp.dgti.enums.Adeudo;
import mx.gob.sfp.dgti.enums.Bien;
import mx.gob.sfp.dgti.enums.CamposPDN;
import mx.gob.sfp.dgti.enums.CatalogosPdn;
import mx.gob.sfp.dgti.enums.Comodato;
import mx.gob.sfp.dgti.enums.Curricular;
import mx.gob.sfp.dgti.enums.DatosEmpleo;
import mx.gob.sfp.dgti.enums.DatosG;
import mx.gob.sfp.dgti.enums.DocObtenido;
import mx.gob.sfp.dgti.enums.Encabezados;
import mx.gob.sfp.dgti.enums.ExperienciaLab;
import mx.gob.sfp.dgti.enums.Ingresos;
import mx.gob.sfp.dgti.enums.IngresosPorTipoDec;
import mx.gob.sfp.dgti.enums.Inversion;
import mx.gob.sfp.dgti.enums.ModulosPDN;
import mx.gob.sfp.dgti.enums.Otros;
import mx.gob.sfp.dgti.enums.TipoDeclaracion;
import mx.gob.sfp.dgti.enums.TipoOperacion;
import mx.gob.sfp.dgti.enums.Ubicacion;

/**
 * @author Miriam Sanchez programador07
 * @modifiedBy programador09
 * @since Apr 2, 2020
 */
public class SituacionPatrimonialService extends GenericosPdnService {

    /**
     * Se genera el json con la estructura del objecto de situacionPatrimonial
     *
     * @param original
     * @param tipoDeclaracion
     * @param completa
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraSituacionP(JsonObject original, String tipoDeclaracion, boolean completa) {

        JsonObject declaracionEstructuradaPdn = new JsonObject();
        JsonObject declaracionOriginal = original.getJsonObject(CamposPDN.DECLARACION.getCampo());
        boolean modificacion = EnumTipoDeclaracion.MODIFICACION.name().equals(tipoDeclaracion);

        declaracionEstructuradaPdn.put(CamposPDN.TIPO.getCampo(), TipoDeclaracion.valueOf(tipoDeclaracion).getClavePdn());
        declaracionEstructuradaPdn.put(ModulosPDN.I_DATOS_GENERALES.getModulo(),
                crearEstructuraDatosGenerales(declaracionOriginal.getJsonObject(EnumModulo.I_DATOS_GENERALES.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.I_DATOS_CURRICULARES.getModulo(),
                crearEstructuraDatosCurriculares(declaracionOriginal.getJsonObject(EnumModulo.I_DATOS_CURRICULARES.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.I_DATOS_EMPLEO.getModulo(),
                crearEstructuraDatosEmpleo(declaracionOriginal.getJsonObject(EnumModulo.I_DATOS_EMPLEO.getModulo()), tipoDeclaracion));

        declaracionEstructuradaPdn.put(ModulosPDN.I_EXPERIENCIA_LABORAL.getModulo(),
                crearEstructuraExperienciaLab(declaracionOriginal.getJsonObject(EnumModulo.I_EXPERIENCIA_LABORAL.getModulo())));

        declaracionEstructuradaPdn.put(ModulosPDN.I_INGRESOS_NETOS.getModulo(),
                crearEstructuraIngresos(declaracionOriginal.getJsonObject(EnumModulo.I_INGRESOS_NETOS.getModulo()), tipoDeclaracion, completa));

        if (!modificacion) {
            declaracionEstructuradaPdn.put(ModulosPDN.I_ACTIVIDAD_ANUAL_ANT.getModulo(),
                    crearEstrucActividadAnualAnt(
                            declaracionOriginal.getJsonObject(EnumModulo.I_ACTIVIDAD_ANUAL_ANT.getModulo()), tipoDeclaracion, completa));
        }
        if (completa) {
            declaracionEstructuradaPdn.put(ModulosPDN.I_BIENES_INMUEBLES.getModulo(),
                    crearEstructuraInmuebles(declaracionOriginal.getJsonObject(EnumModulo.I_BIENES_INMUEBLES.getModulo())));

            declaracionEstructuradaPdn.put(ModulosPDN.I_VEHICULOS.getModulo(),
                    crearEstructuraVehiculos(declaracionOriginal.getJsonObject(EnumModulo.I_VEHICULOS.getModulo())));

            declaracionEstructuradaPdn.put(ModulosPDN.I_BIENES_MUEBLES.getModulo(),
                    crearEstructuraMuebles(declaracionOriginal.getJsonObject(EnumModulo.I_BIENES_MUEBLES.getModulo())));

            declaracionEstructuradaPdn.put(ModulosPDN.I_INVERSIONES_CUENTAS_VALORES.getModulo(),
                    crearEstructuraInversiones(declaracionOriginal.getJsonObject(EnumModulo.I_INVERSIONES.getModulo())));

            declaracionEstructuradaPdn.put(ModulosPDN.I_ADEUDOS_PASIVOS.getModulo(),
                    crearEstructuraAdeudos(declaracionOriginal.getJsonObject(EnumModulo.I_ADEUDOS_PASIVOS.getModulo())));

            declaracionEstructuradaPdn.put(ModulosPDN.I_PRESTAMO_COMODATO.getModulo(),
                    crearEstructuraComodato(declaracionOriginal.getJsonObject(EnumModulo.I_PRESTAMO_COMODATO.getModulo())));
        }

        return declaracionEstructuradaPdn;
    }

    /**
     * Se genera el json del módulo de datos generales con la estrutura que
     * requiere PDN
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraDatosGenerales(JsonObject original) {

        JsonObject datosGenerales = new JsonObject();
        JsonObject correo = new JsonObject();
        JsonObject rfc = new JsonObject();

        correo.put(EnumCorreo.INSTITUCIONAL.getCampo(), original.getJsonObject(DatosG.CORREO_ELECTRONICO.getCampo())
                .getString(EnumCorreo.INSTITUCIONAL.getCampo()));

        rfc.put(EnumDatosPersonales.RFC.getCampo(), original.getJsonObject(DatosG.DATOS_PERSONALES.getCampo())
                .getString(EnumDatosPersonales.RFC.getCampo()).substring(0, 10));
        rfc.put(CamposPDN.HOMOCLAVE.getCampo(), original.getJsonObject(DatosG.DATOS_PERSONALES.getCampo())
                .getString(EnumDatosPersonales.RFC.getCampo()).substring(10, 13));

        datosGenerales.put(EnumDatosPersonales.NOMBRE.getCampo(),
                original.getJsonObject(DatosG.DATOS_PERSONALES.getCampo()).getString(EnumDatosPersonales.NOMBRE.getCampo()));
        datosGenerales.put(EnumDatosPersonales.PRIMER_APELLIDO.getCampo(), original
                .getJsonObject(DatosG.DATOS_PERSONALES.getCampo()).getString(EnumDatosPersonales.PRIMER_APELLIDO.getCampo()));
        datosGenerales.put(EnumDatosPersonales.SEGUNDO_APELLIDO.getCampo(), original
                .getJsonObject(DatosG.DATOS_PERSONALES.getCampo()).getString(EnumDatosPersonales.SEGUNDO_APELLIDO.getCampo()));
        datosGenerales.put(EnumDatosPersonales.CURP.getCampo(),
                original.getJsonObject(DatosG.DATOS_PERSONALES.getCampo()).getString(EnumDatosPersonales.CURP.getCampo()));

        datosGenerales.put(EnumDatosPersonales.RFC.getCampo(), rfc);
        datosGenerales.put(DatosG.CORREO_ELECTRONICO.getCampo(), correo);

        return datosGenerales;
    }

    /**
     * Se genera el json del módulo de datos curriculares con la estrutura que
     * requiere PDN
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraDatosCurriculares(JsonObject original) {

        JsonObject datosCurriculares = new JsonObject();
        JsonArray escolaridad = new JsonArray();

        if (original.getJsonArray(Curricular.ESCOLARIDAD.getCampo()) != null) {
            original.getJsonArray(Curricular.ESCOLARIDAD.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                JsonObject escolaridadReg = new JsonObject();
                JsonObject doc = registro.getJsonObject(Curricular.DOCUMENTO_OBTENIDO.getCampo());

                escolaridadReg.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                        registro.getString(
                                EnumGeneral.TIPO_OPERACION.getCampo()) == null || registro.getString(EnumGeneral.TIPO_OPERACION.getCampo()).equals("NINGUNO") ?
                                TipoOperacion.SIN_CAMBIO.getClavePdn() : 
                                TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());
                escolaridadReg.put(Curricular.CARRERA_AREA.getCampo(), registro.getString(Curricular.CARRERA_AREA.getCampo()));
                escolaridadReg.put(Curricular.ESTATUS.getCampo(), registro.getString(Curricular.ESTATUS.getCampo()));
                escolaridadReg.put(DocObtenido.FECHA_OBTENCION.getCampo(),
                        doc.getString(DocObtenido.FECHA_OBTENCION.getCampo()));
                escolaridadReg.put(Curricular.DOCUMENTO_OBTENIDO.getCampo(), doc.getString(DocObtenido.TIPO.getCampo()));

                JsonObject institucion = new JsonObject()
                        .put(Curricular.NOMBRE.getCampo(), registro.getString(Curricular.INSTITUCION_EDUCATIVA.getCampo()))
                        .put(Curricular.UBICACION.getCampo(),
                                Ubicacion.valueOf(registro.getString(Curricular.UBICACION.getCampo())).getClave());

                Integer idNivel = registro.getJsonObject(Curricular.NIVEL.getCampo()).getInteger(EnumGeneral.ID.getCampo());

                escolaridadReg.put(Curricular.INSTITUCION_EDUCATIVA.getCampo(), institucion);
                escolaridadReg.put(Curricular.NIVEL.getCampo(),
                        crearObjetoCatalogo(CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_NIVEL_ACADEMICO, idNivel),
                                registro.getJsonObject(Curricular.NIVEL.getCampo()).getString(EnumCatalogo.VALOR.getCampo())));

                escolaridad.add(escolaridadReg);
            });
        }
        datosCurriculares.put(Curricular.ESCOLARIDAD.getCampo(), escolaridad);

        return datosCurriculares;
    }

    /**
     * Mòdulo de datos de empleo
     *
     * @param original
     * @param tipoDeclaracion
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraDatosEmpleo(JsonObject original, String tipoDeclaracion) {

        JsonObject empleoCargo = new JsonObject();

        if (original.getJsonArray(DatosEmpleo.EMPLEO_CARGO.getCampo()) != null) {

            JsonArray array = original.getJsonArray(DatosEmpleo.EMPLEO_CARGO.getCampo());
            JsonArray otrosdatosEmpleoArray = new JsonArray();

            for (int i = 0; i < array.size(); i++) {

                JsonObject registro = array.getJsonObject(i);

                if (0 == i) {
                    empleoCargo.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    obtenerEmpleoCargo(empleoCargo, registro);

                } else {
                    JsonObject otroEmpleoCargo = new JsonObject();
                    obtenerEmpleoCargo(otroEmpleoCargo, registro);
                    otrosdatosEmpleoArray.add(otroEmpleoCargo);
                }
            }
            if (EnumTipoDeclaracion.MODIFICACION.name().equals(tipoDeclaracion)) {
                empleoCargo.put(DatosEmpleo.CUENTA_OTRO_CARGO_PUB.getCampo(), array.size() > 1 ? true : false);
                empleoCargo.put(DatosEmpleo.OTRO_EMPLEO_CARGO.getCampo(), otrosdatosEmpleoArray);
            }
        }
        return empleoCargo;
    }

    /**
     * Mòdulo de experiencia laboral
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraExperienciaLab(JsonObject original) {

        JsonObject experienciaLab = new JsonObject();
        JsonArray experiencias = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        experienciaLab.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(ExperienciaLab.EXPERIENCIA_LABORAL.getCampo()) != null) {
            original.getJsonArray(ExperienciaLab.EXPERIENCIA_LABORAL.getCampo()).forEach(item -> {

                JsonObject registro = (JsonObject) item;
                JsonObject experiencia = new JsonObject();
                JsonObject actividadLaboral = registro.getJsonObject(EnumActividadLaboral.ACTIVIDAD_LABORAL.getCampo());
                JsonObject ambito = actividadLaboral.getJsonObject(EnumActividadLaboral.AMBITO_SECTOR.getCampo());

                experiencia.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                        TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                experiencia.put(EnumActividadLaboral.FECHA_INGRESO.getCampo(),
                        actividadLaboral.getString(EnumActividadLaboral.FECHA_INGRESO.getCampo()));
                experiencia.put(EnumActividadLaboral.FECHA_EGRESO.getCampo(),
                        actividadLaboral.getString(EnumActividadLaboral.FECHA_EGRESO.getCampo()));
                experiencia.put(EnumActividadLaboral.UBICACION.getCampo(),
                        Ubicacion.valueOf(actividadLaboral.getString(EnumActividadLaboral.UBICACION.getCampo())).getClave());

                if (Objects.equals(EnumAmbitoSector.PUBLICO.getId(), ambito.getInteger(Encabezados.ID.getCampo()))) {
                    obtenerExperienciaLabPub(experiencia, actividadLaboral);

                } else {
                    obtenerExperienciaLabPrivOtro(experiencia, actividadLaboral);
                }

                experiencias.add(experiencia);
            });
        }

        experienciaLab.put(ExperienciaLab.EXPERIENCIA.getCampo(), experiencias);
        return experienciaLab;
    }

    /**
     * Mòdulo de ingresos
     *
     * @param original
     * @param tipoDeclaracion
     * @param completa
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraIngresos(JsonObject original, String tipoDeclaracion, boolean completa) {

        JsonObject ingresos = new JsonObject();

        ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.REMUNERACION_CARGO_PUBLICO, tipoDeclaracion),
                obtenerMonto(original.getJsonObject(EnumIngresoNetoD.REMUNERACION_NETA_CP.getCampo())));

        ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.OTROS_INGRESOS_TOTAL, tipoDeclaracion),
                obtenerMonto(original.getJsonObject(EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo())));

        ingresos.put(Ingresos.ACTIVIDAD_INDUSTRIAL_CE.getCampo(), obtenerActividadIndustrial(
                original.getJsonArray(EnumIngresoNetoD.ACTIVIDAD_INDUSTRIAL_CE.getCampo())));

        ingresos.put(EnumIngresoNetoD.ACTIVIDAD_FINANCIERA.getCampo(), obtenerActividadFinanciera(
                original.getJsonArray(EnumIngresoNetoD.ACTIVIDAD_FINANCIERA.getCampo())));

        ingresos.put(EnumIngresoNetoD.SERVICIOS_PROFESIONALES.getCampo(), obtenerServiciosProfesionales(
                original.getJsonArray(EnumIngresoNetoD.SERVICIOS_PROFESIONALES.getCampo())));

        ingresos.put(EnumIngresoNetoD.OTROS_INGRESOS.getCampo(), obtenerOtrosIngresos(
                original.getJsonArray(EnumIngresoNetoD.OTROS_INGRESOS.getCampo())));

        if (EnumTipoDeclaracion.MODIFICACION.name().equals(tipoDeclaracion)
                || EnumTipoDeclaracion.CONCLUSION.name().equals(tipoDeclaracion)) {
            ingresos.put(EnumIngresoNetoD.ENAJENACION_BIENES.getCampo(), obtenerEnajenacionB(
                    original.getJsonArray(EnumIngresoNetoD.ENAJENACION_BIENES.getCampo())));
        }

        ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.INGRESO_NETO_DEC, tipoDeclaracion), obtenerMonto(
                original.getJsonObject(EnumIngresoNetoD.INGRESO_NETO_D.getCampo())
                .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));

        if (completa) {
            ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.TOTAL_INGRESOS_NETOS, tipoDeclaracion), obtenerMonto(
                        original.getJsonObject(EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo())
                        .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));
            ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.TOTAL_INGRESOS_NETOS_GENERAL, tipoDeclaracion), obtenerMonto(
                    original.getJsonObject(EnumIngresoNetoD.INGRESO_NETO_D.getCampo())
                    .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));
        } else {
            ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.TOTAL_INGRESOS_NETOS, tipoDeclaracion), obtenerMonto(
                    original.getJsonObject(EnumIngresoNetoD.INGRESO_NETO_D.getCampo())
                    .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));
            ingresos.put(obtenerEtiquetaIngresosPorTipoDec(IngresosPorTipoDec.TOTAL_INGRESOS_NETOS_GENERAL, tipoDeclaracion), obtenerMonto(
                    original.getJsonObject(EnumIngresoNetoD.INGRESO_NETO_D.getCampo())
                    .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));
        }

        return ingresos;
    }

    /**
     * Mòdulo de actividad anual anterior
     *
     * @param original
     * @param tipoDeclaracion
     * @param completa
     * @return {@link JsonObject}
     */
    public JsonObject crearEstrucActividadAnualAnt(JsonObject original, String tipoDeclaracion, boolean completa) {

        JsonObject actAnual = new JsonObject();
        JsonObject actAnualOrig = original.getJsonObject(EnumActividadAnualAnt.ACTIVIDAD_ANUAL.getCampo());

        boolean fueServidorPub = original.getBoolean(EnumActividadAnualAnt.SERVIDOR_PUBLICO.getCampo());
        actAnual.put(EnumActividadAnualAnt.SERVIDOR_PUBLICO.getCampo(), fueServidorPub);

        if (fueServidorPub && actAnualOrig != null) {

            actAnual.put(Ingresos.FECHA_INGRESO.getCampo(),
                    actAnualOrig.getString(EnumActividadAnualAnt.FECHA_INICIO.getCampo()));

            actAnual.put(EnumActividadAnualAnt.FECHA_CONCLUSION.getCampo(),
                    actAnualOrig.getString(EnumActividadAnualAnt.FECHA_CONCLUSION.getCampo()));

            actAnual.put(EnumIngresoNetoD.REMUNERACION_NETA_CP.getCampo(),
                    obtenerMonto(actAnualOrig.getJsonObject(EnumIngresoNetoD.REMUNERACION_NETA_CP.getCampo())));

            actAnual.put(EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo(),
                    obtenerMonto(actAnualOrig.getJsonObject(EnumIngresoNetoD.OTROS_INGRESOS_T.getCampo())));

            actAnual.put(Ingresos.ACTIVIDAD_INDUSTRIAL_CE.getCampo(), obtenerActividadIndustrial(
                    actAnualOrig.getJsonArray(EnumIngresoNetoD.ACTIVIDAD_INDUSTRIAL_CE.getCampo())));

            actAnual.put(EnumIngresoNetoD.ACTIVIDAD_FINANCIERA.getCampo(), obtenerActividadFinanciera(
                    actAnualOrig.getJsonArray(EnumIngresoNetoD.ACTIVIDAD_FINANCIERA.getCampo())));

            actAnual.put(EnumIngresoNetoD.SERVICIOS_PROFESIONALES.getCampo(), obtenerServiciosProfesionales(
                    actAnualOrig.getJsonArray(EnumIngresoNetoD.SERVICIOS_PROFESIONALES.getCampo())));

            actAnual.put(EnumIngresoNetoD.OTROS_INGRESOS.getCampo(), obtenerOtrosIngresos(
                    actAnualOrig.getJsonArray(EnumIngresoNetoD.OTROS_INGRESOS.getCampo())));

            if (EnumTipoDeclaracion.INICIO.name().equals(tipoDeclaracion)
                    || EnumTipoDeclaracion.CONCLUSION.name().equals(tipoDeclaracion)) {

                actAnual.put(EnumIngresoNetoD.ENAJENACION_BIENES.getCampo(), obtenerEnajenacionB(
                        actAnualOrig.getJsonArray(EnumIngresoNetoD.ENAJENACION_BIENES.getCampo())));
            }

            actAnual.put(Ingresos.INGRESO_NETO_ANUAL.getCampo(), obtenerMonto(
                    actAnualOrig.getJsonObject(EnumIngresoNetoD.INGRESO_NETO_D.getCampo())
                    .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));

            if (completa) {
                actAnual.put(Ingresos.TOTAL_INGRESOS_ANUALES.getCampo(), obtenerMonto(
                        actAnualOrig.getJsonObject(EnumIngresoNetoD.TOTAL_INGRESOS_NETOS.getCampo())
                        .getJsonObject(EnumRemuneracion.REMUNERACION.getCampo())));
            }
        }

        return actAnual;
    }

    /**
     * Mòdulo de bienes inmuebles
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraInmuebles(JsonObject original) {

        JsonObject bienesInmuebles = new JsonObject();
        JsonArray inmuebles = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        bienesInmuebles.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Bien.BIENES_INMUEBLES.getCampo()) != null && !ninguno) {
            original.getJsonArray(Bien.BIENES_INMUEBLES.getCampo()).forEach(item -> {

                JsonObject titular = ((JsonObject) item).getJsonObject(EnumBien.TITULAR.getNombre());
                if (Encabezados.ID_TITULAR_DECLARANTE.getCampo()
                        .equals(titular.getInteger(Encabezados.ID.getCampo()).toString())) {
                    
                    JsonObject inmueble = new JsonObject();
                    JsonObject registro = ((JsonObject) item);

                    inmueble.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    inmueble.put(EnumBien.TITULAR.getNombre(),
                            obtenerTitularSolicitado(titular.getInteger(Encabezados.ID.getCampo()), titular.getString(EnumCatalogo.VALOR.getCampo())));

                    Integer idInmueble = registro.getJsonObject(Bien.TIPO_INMUEBLE.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valInmueble = idInmueble.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Bien.TIPO_INMUEBLE_OTRO.getCampo())
                            : registro.getJsonObject(Bien.TIPO_INMUEBLE.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    inmueble.put(Bien.TIPO_INMUEBLE.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_BIEN_INMUEBLE, idInmueble), valInmueble));

                    inmueble.put(Bien.PORCENTAJE_PROPIEDAD.getCampo(), registro.getInteger(Bien.PORCENTAJE_PROPIEDAD.getCampo()));
                    inmueble.put(Bien.SUPERFICIE_TERRENO.getCampo(), obtenerSuperficie(registro.getInteger(Bien.SUPERF_TERRENO_M2.getCampo())));
                    inmueble.put(Bien.SUPERFICIE_CONSTR.getCampo(), obtenerSuperficie(registro.getInteger(Bien.SUPERF_CONSTR_M2.getCampo())));
                    inmueble.put(Bien.TERCERO.getCampo(), obtenerTerceros(registro));

                    //Transmisor se mostrara solo si es persona moral
                    inmueble.put(Bien.TRANSMISOR.getCampo(), obtenerTransmisores(registro));

                    inmueble.put(EnumBien.FORMA_ADQUISICION.getNombre(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_FORMA_ADQUISICION_BIEN,
                                    registro.getJsonObject(EnumBien.FORMA_ADQUISICION.getNombre()).getInteger(Encabezados.ID.getCampo())),
                            registro.getJsonObject(EnumBien.FORMA_ADQUISICION.getNombre()).getString(EnumCatalogo.VALOR.getCampo())));

                    inmueble.put(EnumBien.VALOR_ADQUISICION.getNombre(),
                            obtenerMonto(registro.getJsonObject(EnumBien.VALOR_ADQUISICION.getNombre())));

                    inmueble.put(EnumBien.FECHA_ADQUISICION.getNombre(), registro.getString(EnumBien.FECHA_ADQUISICION.getNombre()));
                    inmueble.put(EnumBien.FORMA_PAGO.getNombre(),
                            EnumFormaPago.valueOf(registro.getString(EnumBien.FORMA_PAGO.getNombre())).getDescripcion());
                    inmueble.put(Bien.VALOR_CONFORME_A.getCampo(),
                            EnumValorConformeA.valueOf(registro.getString(Bien.VALOR_CONFORME_A.getCampo())).getDescripcion());

                    if (registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()) != null) {

                        Integer idMotivo = registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()).getInteger(Encabezados.ID.getCampo());
                        String valMotivo = idMotivo.equals(Otros.OTROS_ID.getId())
                                ? registro.getString(Bien.MOTIVO_BAJA_OTRO.getCampo())
                                : registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                        inmueble.put(CamposPDN.MOTIVO_BAJA.getCampo(), crearObjetoCatalogo(
                                CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_MOTIVO_BAJA_BIEN, idMotivo), valMotivo));
                    }
                    inmuebles.add(inmueble);
                }
            });
        }

        bienesInmuebles.put(Bien.BIEN_INMUEBLE.getCampo(), inmuebles);
        return bienesInmuebles;
    }

    /**
     * Mòdulo de vehiculos
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraVehiculos(JsonObject original) {

        JsonObject bienesVehiculos = new JsonObject();
        JsonArray vehiculos = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        bienesVehiculos.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Bien.VEHICULOS.getCampo()) != null && !ninguno) {
            original.getJsonArray(Bien.VEHICULOS.getCampo()).forEach(item -> {

                JsonObject titular = ((JsonObject) item).getJsonObject(EnumBien.TITULAR.getNombre());
                if (Encabezados.ID_TITULAR_DECLARANTE.getCampo()
                        .equals(titular.getInteger(Encabezados.ID.getCampo()).toString())) {
                    
                    JsonObject registro = ((JsonObject) item);
                    JsonObject vehiculo = new JsonObject();

                    vehiculo.put(EnumBien.TITULAR.getNombre(),
                            obtenerTitularSolicitado(titular.getInteger(Encabezados.ID.getCampo()), titular.getString(EnumCatalogo.VALOR.getCampo())));

                    vehiculo.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());



                    Integer idTipoVehiculo = registro.getJsonObject(Bien.TIPO_VEHICULO.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valTipoVehi = idTipoVehiculo.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Bien.TIPO_VEHICULO_OTRO.getCampo())
                            : registro.getJsonObject(Bien.TIPO_VEHICULO.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    vehiculo.put(Bien.TIPO_VEHICULO.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_VEHICULO, idTipoVehiculo), valTipoVehi));

                    vehiculo.put(Bien.MARCA.getCampo(), registro.getString(Bien.MARCA.getCampo()));
                    vehiculo.put(Bien.MODELO.getCampo(), registro.getString(Bien.MODELO.getCampo()));
                    vehiculo.put(Bien.ANIO.getCampo(), registro.getInteger(Bien.ANIO.getCampo()));
                    vehiculo.put(Bien.TERCERO.getCampo(), obtenerTerceros(registro));

                    //Transmisor se mostrara solo si es persona moral
                    vehiculo.put(Bien.TRANSMISOR.getCampo(), obtenerTransmisores(registro));

                    vehiculo.put(EnumBien.FORMA_ADQUISICION.getNombre(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_FORMA_ADQUISICION_BIEN,
                                    registro.getJsonObject(EnumBien.FORMA_ADQUISICION.getNombre()).getInteger(Encabezados.ID.getCampo())),
                            registro.getJsonObject(EnumBien.FORMA_ADQUISICION.getNombre()).getString(EnumCatalogo.VALOR.getCampo())));

                    vehiculo.put(EnumBien.VALOR_ADQUISICION.getNombre(),
                            obtenerMonto(registro.getJsonObject(EnumBien.VALOR_ADQUISICION.getNombre())));

                    vehiculo.put(EnumBien.FECHA_ADQUISICION.getNombre(), registro.getString(EnumBien.FECHA_ADQUISICION.getNombre()));
                    vehiculo.put(EnumBien.FORMA_PAGO.getNombre(),
                            EnumFormaPago.valueOf(registro.getString(EnumBien.FORMA_PAGO.getNombre())).getDescripcion());

                    if (registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()) != null) {

                        Integer idMotivo = registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()).getInteger(Encabezados.ID.getCampo());
                        String valMotivo = idMotivo.equals(Otros.OTROS_ID.getId())
                                ? registro.getString(Bien.MOTIVO_BAJA_OTRO.getCampo())
                                : registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                        vehiculo.put(CamposPDN.MOTIVO_BAJA.getCampo(), crearObjetoCatalogo(
                                CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_MOTIVO_BAJA_BIEN, idMotivo), valMotivo));
                    }
                    vehiculos.add(vehiculo);
                }
            });
        }

        bienesVehiculos.put(Bien.VEHICULO.getCampo(), vehiculos);
        return bienesVehiculos;
    }

    /**
     * Mòdulo de bienes muebles
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraMuebles(JsonObject original) {

        JsonObject bienesMuebles = new JsonObject();
        JsonArray muebles = new JsonArray();

        boolean ninguno = original.getBoolean(Bien.NINGUNO.getCampo());
        bienesMuebles.put(Bien.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Bien.BIENES_MUEBLES.getCampo()) != null && !ninguno) {
            original.getJsonArray(Bien.BIENES_MUEBLES.getCampo()).forEach(item -> {

                JsonObject titular = ((JsonObject) item).getJsonObject(EnumBien.TITULAR.getNombre());
                if (Encabezados.ID_TITULAR_DECLARANTE.getCampo()
                        .equals(titular.getInteger(Encabezados.ID.getCampo()).toString())) {
                    JsonObject registro = ((JsonObject) item);
                    JsonObject mueble = new JsonObject();

                    mueble.put(EnumBien.TITULAR.getNombre(),
                            obtenerTitularSolicitado(titular.getInteger(Encabezados.ID.getCampo()), titular.getString(EnumCatalogo.VALOR.getCampo())));

                    mueble.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    Integer idMueble = registro.getJsonObject(Bien.TIPO_MUEBLE.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valMueble = idMueble.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Bien.TIPO_MUEBLE_OTRO.getCampo())
                            : registro.getJsonObject(Bien.TIPO_MUEBLE.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    mueble.put(Bien.TIPO_MUEBLE.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_BIEN_MUEBLE, idMueble), valMueble));

                    mueble.put(Bien.TERCERO.getCampo(), obtenerTerceros(registro));

                    //Transmisor se mostrara solo si es persona moral
                    mueble.put(Bien.TRANSMISOR.getCampo(), obtenerTransmisores(registro));

                    mueble.put(EnumBien.FORMA_ADQUISICION.getNombre(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_FORMA_ADQUISICION_BIEN,
                                    registro.getJsonObject(EnumBien.FORMA_ADQUISICION.getNombre()).getInteger(Encabezados.ID.getCampo())),
                            registro.getJsonObject(EnumBien.FORMA_ADQUISICION.getNombre()).getString(EnumCatalogo.VALOR.getCampo())));

                    mueble.put(EnumBien.VALOR_ADQUISICION.getNombre(),
                            obtenerMonto(registro.getJsonObject(EnumBien.VALOR_ADQUISICION.getNombre())));

                    mueble.put(EnumBien.FECHA_ADQUISICION.getNombre(), registro.getString(EnumBien.FECHA_ADQUISICION.getNombre()));
                    mueble.put(EnumBien.FORMA_PAGO.getNombre(),
                            EnumFormaPago.valueOf(registro.getString(EnumBien.FORMA_PAGO.getNombre())).getDescripcion());
                    mueble.put(Bien.DESCRIPCION_GENERAL_BIEN.getCampo(),
                            registro.getString(Bien.DESCRIPCION_GENERAL_BIEN.getCampo()));

                    if (registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()) != null) {

                        Integer idMotivo = registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()).getInteger(Encabezados.ID.getCampo());
                        String valMotivo = idMotivo.equals(Otros.OTROS_ID.getId())
                                ? registro.getString(Bien.MOTIVO_BAJA_OTRO.getCampo())
                                : registro.getJsonObject(CamposPDN.MOTIVO_BAJA.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                        mueble.put(CamposPDN.MOTIVO_BAJA.getCampo(), crearObjetoCatalogo(
                                CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_MOTIVO_BAJA_BIEN, idMotivo), valMotivo));
                    }
                    muebles.add(mueble);
                }
            });
        }

        bienesMuebles.put(Bien.BIEN_MUEBLE.getCampo(), muebles);
        return bienesMuebles;
    }

    /**
     * Mòdulo de inversiones, cuentas, valores
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraInversiones(JsonObject original) {

        JsonObject inversiones = new JsonObject();
        JsonArray arrayInversiones = new JsonArray();

        boolean ninguno = original.getBoolean(Inversion.NINGUNO.getCampo());
        inversiones.put(Inversion.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Inversion.INVERSION.getCampo()) != null && !ninguno) {
            original.getJsonArray(Inversion.INVERSION.getCampo()).forEach(item -> {

                JsonObject titular = ((JsonObject) item).getJsonObject(EnumBien.TITULAR.getNombre());
                if (Encabezados.ID_TITULAR_DECLARANTE.getCampo().equals(titular.getInteger(Encabezados.ID.getCampo()).toString())) {
                    JsonObject registro = ((JsonObject) item);
                    JsonObject inversion = new JsonObject();

                    inversion.put(EnumBien.TITULAR.getNombre(),
                            obtenerTitularSolicitado(titular.getInteger(Encabezados.ID.getCampo()), titular.getString(EnumCatalogo.VALOR.getCampo())));

                    inversion.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    inversion.put(Inversion.TIPO_INVERSION.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_INVERSION,
                                    registro.getJsonObject(Inversion.TIPO_INVERSION.getCampo()).getInteger(Encabezados.ID.getCampo())),
                            registro.getJsonObject(Inversion.TIPO_INVERSION.getCampo()).getString(EnumCatalogo.VALOR.getCampo())));

                    inversion.put(Inversion.SUBTIPO_INVERSION.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_SUBTIPO_INVERSION,
                                    registro.getJsonObject(Inversion.SUBTIPO_INVERSION.getCampo()).getInteger(Encabezados.ID.getCampo())),
                            registro.getJsonObject(Inversion.SUBTIPO_INVERSION.getCampo()).getString(EnumCatalogo.VALOR.getCampo())));

                    inversion.put(Bien.TERCERO.getCampo(), obtenerTerceros(registro));

                    inversion.put(Inversion.LOCALIZACION.getCampo(), obtenerLocalizacion(registro));
                    
                    arrayInversiones.add(inversion);
                }
            });
        }

        inversiones.put(Inversion.INVERSION.getCampo(), arrayInversiones);
        return inversiones;
    }

    /**
     * Mòdulo de adeudos pasivos
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraAdeudos(JsonObject original) {

        JsonObject adeudosPasivos = new JsonObject();
        JsonArray adeudos = new JsonArray();

        boolean ninguno = original.getBoolean(Inversion.NINGUNO.getCampo());
        adeudosPasivos.put(Inversion.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Adeudo.ADEUDOS.getCampo()) != null && !ninguno) {
            original.getJsonArray(Adeudo.ADEUDOS.getCampo()).forEach(item -> {

                JsonObject titular = ((JsonObject) item).getJsonObject(EnumBien.TITULAR.getNombre());
                if (Encabezados.ID_TITULAR_DECLARANTE.getCampo().equals(titular.getInteger(Encabezados.ID.getCampo()).toString())) {
                    JsonObject registro = ((JsonObject) item);
                    JsonObject adeudo = new JsonObject();

                    adeudo.put(EnumBien.TITULAR.getNombre(),
                            obtenerTitularSolicitado(titular.getInteger(Encabezados.ID.getCampo()), titular.getString(EnumCatalogo.VALOR.getCampo())));

                    adeudo.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                            TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                    Integer idAdeudo = registro.getJsonObject(Adeudo.TIPO_ADEUDO.getCampo()).getInteger(Encabezados.ID.getCampo());
                    String valAdeudo = idAdeudo.equals(Otros.OTROS_ID.getId())
                            ? registro.getString(Adeudo.TIPO_ADEUDO_OTRO.getCampo())
                            : registro.getJsonObject(Adeudo.TIPO_ADEUDO.getCampo()).getString(EnumCatalogo.VALOR.getCampo());

                    adeudo.put(Adeudo.TIPO_ADEUDO.getCampo(), crearObjetoCatalogo(
                            CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_TIPO_ADEUDO, idAdeudo), valAdeudo));

                    adeudo.put(EnumBien.FECHA_ADQUISICION.getNombre(), registro.getString(EnumBien.FECHA_ADQUISICION.getNombre()));

                    adeudo.put(Bien.TERCERO.getCampo(), obtenerTerceros(registro));

                    adeudo.put(Adeudo.MONTO_ORIGINAL.getCampo(), obtenerMonto(registro.getJsonObject(Adeudo.MONTO_ORIGINAL.getCampo())));

                    adeudo.put(Adeudo.OTORGANTE_CREDITO.getCampo(),
                            obtenerOtorganteCredito(registro.getJsonObject(Adeudo.OTORGANTE_CREDITO.getCampo())));

                    adeudo.put(Adeudo.LOCALIZACION.getCampo(),
                            new JsonObject().put(EnumDomicilio.PAIS.getNombre(),
                                    CatalogoDAOImpl.obtenerClavePdnCat(CatalogosPdn.CAT_PAIS,
                                            registro.getJsonObject(Adeudo.PAIS_ADEUDO.getCampo()).getInteger(Encabezados.ID.getCampo()))));
                    adeudos.add(adeudo);
                }
            });
        }

        adeudosPasivos.put(Adeudo.ADEUDO.getCampo(), adeudos);
        return adeudosPasivos;
    }

    /**
     * Mòdulo de comodato
     *
     * @param original
     * @return {@link JsonObject}
     */
    public JsonObject crearEstructuraComodato(JsonObject original) {

        JsonObject prestamoComodato = new JsonObject();
        JsonArray prestamos = new JsonArray();

        boolean ninguno = original.getBoolean(Inversion.NINGUNO.getCampo());
        prestamoComodato.put(Inversion.NINGUNO.getCampo(), ninguno);

        if (original.getJsonArray(Comodato.PRESTAMO.getCampo()) != null && !ninguno) {
            original.getJsonArray(Comodato.PRESTAMO.getCampo()).forEach(item -> {

                JsonObject registro = ((JsonObject) item);
                JsonObject prestamo = new JsonObject();

                prestamo.put(EnumGeneral.TIPO_OPERACION.getCampo(),
                        TipoOperacion.valueOf(registro.getString(EnumGeneral.TIPO_OPERACION.getCampo())).getClavePdn());

                prestamo.put(Bien.TIPO_BIEN.getCampo(), obtenerTipoBienParaComodato(registro));
                prestamo.put(Comodato.DUENO_TITULAR.getCampo(), obtenerDuenoTitularParaComodato(registro));
                prestamos.add(prestamo);
            });
        }
        prestamoComodato.put(Comodato.PRESTAMO.getCampo(), prestamos);
        return prestamoComodato;
    }
}