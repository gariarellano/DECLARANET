/**
 * @(#)Propiedades.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumSectorPrivado;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumSectorPublico;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 02/11/2019
 */
public class PropSector {

    /*SECTOR PUBLICO*/

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropNombreEnte(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumSectorPublico.NOMBRE_ENTE_PUBLICO.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropAreaAdscrip(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumSectorPublico.AREA_ADSCRIPCION.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropEmpleo(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumSectorPublico.EMPLEO_CARGO_COMISION.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropFuncion(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumSectorPublico.FUNCION_PRINCIPAL.getCampo(),
                valor,
                parametros,
                true);
    }

    /*SECTOR PRIVADO*/

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropNomEmpr(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumSectorPrivado.NOMBRE_EMPRESA.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de rfc
     */
    public static PropiedadesValidarDTO crearPropRfc(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.RFC_PM_A_DOCE));

        return new PropiedadesValidarDTO(
                EnumSectorPrivado.RFC.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropArea(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumSectorPrivado.AREA.getCampo(),
                valor,
                parametros,
                true);
    }


    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropUnSoloObjetoNoNulo(Object[] objetos, String[] campos) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.UN_SOLO_OBJETO_NO_NULO, objetos));

        return new PropiedadesValidarDTO(
                Arrays.stream(campos).collect(Collectors.joining("-")),
                "",
                parametros,
                true);
    }
}
