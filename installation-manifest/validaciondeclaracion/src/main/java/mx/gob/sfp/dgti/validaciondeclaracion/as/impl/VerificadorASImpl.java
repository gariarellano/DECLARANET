/**
 * @(#)VerificadorASImpl.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.as.impl;

import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceException;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoFormatoDeclaracion;
import mx.gob.sfp.dgti.validaciondeclaracion.as.VerificadorAS;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.RespuestaDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumModulo;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumTipoResp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Interface para el servicio VerificadorASImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class VerificadorASImpl implements VerificadorAS {

    /**
     * Logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(VerificadorASImpl.class);

    /**
     * MOdulos aceptados para declaracion inicial
     */
    static final Set<String> modulosDeclCompleta = new HashSet<>(
            Arrays.asList(
                    EnumModulo.I_DATOS_GENERALES.getModulo(),
                    EnumModulo.I_DOMICILIO_DECLARANTE.getModulo(),
                    EnumModulo.I_DATOS_CURRICULARES.getModulo(),
                    EnumModulo.I_DATOS_EMPLEO.getModulo(),
                    EnumModulo.I_EXPERIENCIA_LABORAL.getModulo(),
                    EnumModulo.I_DATOS_PAREJA.getModulo(),
                    EnumModulo.I_DATOS_DEPENDIENTE.getModulo(),
                    EnumModulo.I_INGRESOS_NETOS.getModulo(),
                    EnumModulo.I_ACTIVIDAD_ANUAL_ANT.getModulo(),
                    EnumModulo.I_BIENES_INMUEBLES.getModulo(),
                    EnumModulo.I_BIENES_MUEBLES.getModulo(),
                    EnumModulo.I_VEHICULOS.getModulo(),
                    EnumModulo.I_INVERSIONES.getModulo(),
                    EnumModulo.I_ADEUDOS_PASIVOS.getModulo(),
                    EnumModulo.I_PRESTAMO_COMODATO.getModulo(),
                    EnumModulo.II_PARTICIPACION_EMPRESAS.getModulo(),
                    EnumModulo.II_PARTICIPACION_INSTITUCIONES.getModulo(),
                    EnumModulo.II_APOYOS_BENEFICIOS.getModulo(),
                    EnumModulo.II_REPRESENTACION.getModulo(),
                    EnumModulo.II_CLIENTES_PRINCIPALES.getModulo(),
                    EnumModulo.II_BENEFICIOS_PRIVADOS.getModulo(),
                    EnumModulo.II_FIDEICOMISOS.getModulo())
    );

    /**
     * MOdulos aceptados para declaracion completa y de modificacion
     */
    static final Set<String> modulosDeclCompletaModificacion = new HashSet<>(
            Arrays.asList(
                    EnumModulo.I_DATOS_GENERALES.getModulo(),
                    EnumModulo.I_DOMICILIO_DECLARANTE.getModulo(),
                    EnumModulo.I_DATOS_CURRICULARES.getModulo(),
                    EnumModulo.I_DATOS_EMPLEO.getModulo(),
                    EnumModulo.I_EXPERIENCIA_LABORAL.getModulo(),
                    EnumModulo.I_DATOS_PAREJA.getModulo(),
                    EnumModulo.I_DATOS_DEPENDIENTE.getModulo(),
                    EnumModulo.I_INGRESOS_NETOS.getModulo(),
                    EnumModulo.I_BIENES_INMUEBLES.getModulo(),
                    EnumModulo.I_BIENES_MUEBLES.getModulo(),
                    EnumModulo.I_VEHICULOS.getModulo(),
                    EnumModulo.I_INVERSIONES.getModulo(),
                    EnumModulo.I_ADEUDOS_PASIVOS.getModulo(),
                    EnumModulo.I_PRESTAMO_COMODATO.getModulo(),
                    EnumModulo.II_PARTICIPACION_EMPRESAS.getModulo(),
                    EnumModulo.II_PARTICIPACION_INSTITUCIONES.getModulo(),
                    EnumModulo.II_APOYOS_BENEFICIOS.getModulo(),
                    EnumModulo.II_REPRESENTACION.getModulo(),
                    EnumModulo.II_CLIENTES_PRINCIPALES.getModulo(),
                    EnumModulo.II_BENEFICIOS_PRIVADOS.getModulo(),
                    EnumModulo.II_FIDEICOMISOS.getModulo())
    );

    /**
     * MOdulos aceptados para declaracion inicial
     */
    static final Set<String> modulosDeclSimplificada = new HashSet<>(
            Arrays.asList(
                    EnumModulo.I_DATOS_GENERALES.getModulo(),
                    EnumModulo.I_DOMICILIO_DECLARANTE.getModulo(),
                    EnumModulo.I_DATOS_CURRICULARES.getModulo(),
                    EnumModulo.I_DATOS_EMPLEO.getModulo(),
                    EnumModulo.I_EXPERIENCIA_LABORAL.getModulo(),
                    EnumModulo.I_INGRESOS_NETOS.getModulo(),
                    EnumModulo.I_ACTIVIDAD_ANUAL_ANT.getModulo())
    );

    /**
     * MOdulos aceptados para declaracion inicial
     */
    static final Set<String> modulosDeclSimplificadaModificacion = new HashSet<>(
            Arrays.asList(
                    EnumModulo.I_DATOS_GENERALES.getModulo(),
                    EnumModulo.I_DOMICILIO_DECLARANTE.getModulo(),
                    EnumModulo.I_DATOS_CURRICULARES.getModulo(),
                    EnumModulo.I_DATOS_EMPLEO.getModulo(),
                    EnumModulo.I_EXPERIENCIA_LABORAL.getModulo(),
                    EnumModulo.I_INGRESOS_NETOS.getModulo())
    );

    
    /**
     * Modulos aceptados para aviso por cambio de dependencia
     */
    static final Set<String> modulosAvisoCambioDep = new HashSet<>(
            Arrays.asList(
                    EnumModulo.I_DATOS_GENERALES.getModulo(),
                    EnumModulo.I_DOMICILIO_DECLARANTE.getModulo(),
                    EnumModulo.CAMBIO_DEPENDENCIA.getModulo())
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public Completable validarDeclaracion(JsonObject declaracionCompleta, Set<String> mandados) {

        boolean validado = true;
        String tipoFormato = declaracionCompleta.getJsonObject(EnumCampos.DECLARACION.getNombre())
                .getJsonObject(EnumCampos.ENCABEZADO.getNombre()).getString(EnumCampos.TIPO_FORMATO.getNombre());

        Set<String> modulosMandados = new HashSet<>(mandados);

        if (tipoFormato.equals(EnumTipoFormatoDeclaracion.SIMPLIFICADO.name())) {
            modulosMandados.removeAll(modulosDeclSimplificada);
        } else {
            modulosMandados.removeAll(modulosDeclCompleta);
        }
        if (!modulosMandados.isEmpty()) {
            validado = false;
        }

        if(validado) {
            return Completable.complete();
        }
        RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.MODULOS_INCORRECTOS.name());
        return Completable.error(new ServiceException(
                EnumTipoResp.MODULOS_INCORRECTOS.getId(),
                EnumTipoResp.MODULOS_INCORRECTOS.name(),
                respuesta.toJson()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Completable validarNotaAclaratoria(JsonObject notas, Set<String> mandados) {

        boolean validado = true;

        JsonObject encabezadoReferencia = notas.getJsonObject(EnumCampos.DECLARACION.getNombre())
                .getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                .getJsonObject(EnumCampos.DECLARACION_ORIGEN.getNombre())
                .getJsonObject(EnumCampos.ENCABEZADO.getNombre());

        String tipoFormato = encabezadoReferencia.getString(EnumCampos.TIPO_FORMATO.getNombre());
        String tipoDeclaracion = encabezadoReferencia.getString(EnumCampos.TIPO_DECLARACION.getNombre());

        Set<String> modulosMandados = new HashSet<>(mandados);

        if(tipoDeclaracion.equals(EnumTipoDeclaracion.AVISO.getDescripcion())) {
            modulosMandados.removeAll(modulosAvisoCambioDep);
        } else {
            if (tipoFormato.equals(EnumTipoFormatoDeclaracion.SIMPLIFICADO.name())) {
                modulosMandados.removeAll(modulosDeclSimplificada);
            } else {
                modulosMandados.removeAll(modulosDeclCompleta);
            }

        }
        if (!modulosMandados.isEmpty()) {
            validado = false;
        }
        if(validado) {
            return Completable.complete();
        }
        RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.MODULOS_INCORRECTOS.name());
        return Completable.error(new ServiceException(
                EnumTipoResp.MODULOS_INCORRECTOS.getId(),
                EnumTipoResp.MODULOS_INCORRECTOS.name(),
                respuesta.toJson()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Completable validarAvisoCambio(JsonObject avisoCambio, Set<String> mandados) {

        String tipoDeclaracion = avisoCambio.getJsonObject(EnumCampos.DECLARACION.getNombre())
                .getJsonObject(EnumCampos.ENCABEZADO.getNombre()).getString(EnumCampos.TIPO_DECLARACION.getNombre());

        if(!tipoDeclaracion.equals(EnumTipoDeclaracion.AVISO.name())) {
            RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.NO_ES_AVISO.name());
            return Completable.error(new ServiceException(
                    EnumTipoResp.NO_ES_AVISO.getId(),
                    EnumTipoResp.NO_ES_AVISO.name(),
                    respuesta.toJson()
            ));
        }

        Set<String> modulosMandados = new HashSet<>(mandados);
        modulosMandados.removeAll(modulosAvisoCambioDep);

        boolean validado = true;
        if (!modulosMandados.isEmpty()) {
            validado = false;
        }
        if(validado) {
            return Completable.complete();
        }
        RespuestaDTO respuesta = new RespuestaDTO(EnumTipoResp.MODULOS_INCORRECTOS.name());
        return Completable.error(new ServiceException(
                EnumTipoResp.MODULOS_INCORRECTOS.getId(),
                EnumTipoResp.MODULOS_INCORRECTOS.name(),
                respuesta.toJson()
        ));
    }

    /**
     * Funcion para activar firmado
     *
     * @param declaracion: declaracion mandada
     * @param algunoIncompleto: indica que durante la validacion de la declaracion hay algun modulo incompleto
     * @param mandados: lista de modulos mandados
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    public static void activarFirmado(JsonObject declaracion, Boolean algunoIncompleto, Set<String> mandados) {

        String tipoFormato = declaracion.getJsonObject(
                EnumCampos.ENCABEZADO.getNombre()).getString(EnumCampos.TIPO_FORMATO.getNombre());
        String tipoDeclaracion = declaracion.getJsonObject(
                EnumCampos.ENCABEZADO.getNombre()).getString(EnumCampos.TIPO_DECLARACION.getNombre());

        if(algunoIncompleto.booleanValue()) {
            declaracion.put(EnumCampos.FIRMADA.getNombre(), false);
            return;
        }

        Set<String> modulosMandados = new HashSet<>(mandados);

        Set<String> totalModulos;
        if (tipoFormato.equals(EnumTipoFormatoDeclaracion.SIMPLIFICADO.name())) {
            if(tipoDeclaracion.equals(EnumTipoDeclaracion.MODIFICACION.name())) {
                totalModulos = new HashSet<>(modulosDeclSimplificadaModificacion);
            } else {
                totalModulos = new HashSet<>(modulosDeclSimplificada);
            }

        } else {
            if(tipoDeclaracion.equals(EnumTipoDeclaracion.MODIFICACION.name())) {
                totalModulos = new HashSet<>(modulosDeclCompletaModificacion);
            } else {
                totalModulos = new HashSet<>(modulosDeclCompleta);
            }
        }
        totalModulos.removeAll(modulosMandados);

        boolean firmado = totalModulos.isEmpty();
        declaracion.put(EnumCampos.FIRMADA.getNombre(), firmado);
    }

    /**
     * Funcion que activa el firmado en un aviso por cambio de dependencia
     *
     * @param declaracion: declaracion mandada
     * @param mandados: lista de modulos mandados
     *
     * @author pavel.martinez
     * @since 05/02/2020
     */
    public static void activarFirmadoAviso(JsonObject declaracion, Set<String> mandados) {

        Set<String> modulosMandados = new HashSet<>(mandados);
        Set<String> totalModulos;

        totalModulos = new HashSet<>(modulosAvisoCambioDep);
        totalModulos.removeAll(modulosMandados);

        boolean firmado = totalModulos.isEmpty();
        declaracion.put(EnumCampos.FIRMADA.getNombre(), firmado);
    }

    public static void activarFirmadoNotas(JsonObject nota) {
        nota.put(EnumCampos.FIRMADA.getNombre(), true);
    }

    /**
     * Funcion para agregar solo los modulos que no son nulos
     *
     * @param declaracionCompleta: la declaracion mandada
     *
     * @return Set<String> solo con los modulos que no son nulos
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    public static Set<String> obtenerModulosNoNulos(JsonObject declaracionCompleta) {

        JsonObject declaracion = declaracionCompleta.getJsonObject(
                EnumCampos.DECLARACION.getNombre()).getJsonObject(EnumCampos.DECLARACION.getNombre());

        Set<String> modulosMandados = new HashSet<>(declaracion.fieldNames());
        Set<String> modulosNoNulos = new HashSet<>();
        modulosMandados.forEach(modulo -> {
            if(declaracion.getJsonObject(modulo) != null){
                modulosNoNulos.add(modulo);
            }
        });
        return modulosNoNulos;
    }

}
