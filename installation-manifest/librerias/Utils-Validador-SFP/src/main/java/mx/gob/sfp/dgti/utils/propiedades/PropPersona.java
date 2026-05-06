/**
 * @(#)PropPers.java 02/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDatosPersonales;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumPersonaMoral;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/09/2019
 */
public class PropPersona {

    /*PERSONA FISICA Y DATOS PERSONALES*/

    /**
     * Metodo para crear la propiedad de fecha de ingreso
     */
    public static PropiedadesValidarDTO crearPropNombre(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_ACENTOS_MAY_MIN));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.TREINTA_Y_SEIS.getId()));
        
        return new PropiedadesValidarDTO(
                EnumDatosPersonales.NOMBRE.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de fecha de ingreso
     */
    public static PropiedadesValidarDTO crearPropPrimerAp(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_ACENTOS_MAY_MIN));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.TREINTA_Y_SEIS.getId()));
        
        return new PropiedadesValidarDTO(
                EnumDatosPersonales.PRIMER_APELLIDO.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de fecha de ingreso
     */
    public static PropiedadesValidarDTO crearPropSegundoAp(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_ACENTOS_MAY_MIN));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.TREINTA_Y_SEIS.getId()));
        
        return new PropiedadesValidarDTO(
                EnumDatosPersonales.SEGUNDO_APELLIDO.getCampo(),
                valor,
                parametros,
                false);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropRfc(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));

        return new PropiedadesValidarDTO(
                EnumDatosPersonales.RFC.getCampo(),
                valor,
                parametros,
                true);
    }
    
    /**
     * Metodo para crear la propiedad para rfc 
     */
    public static PropiedadesValidarDTO crearPropRfc(String valor, String nombre) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));

        return new PropiedadesValidarDTO(
                nombre,
                valor,
                parametros,
                false);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropCurp(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CURP));

        return new PropiedadesValidarDTO(
                EnumDatosPersonales.CURP.getCampo(),
                valor,
                parametros,
                false);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropFechaNacimiento(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));

        return new PropiedadesValidarDTO(
                EnumDatosPersonales.FECHA_NACIMIENTO.getCampo(),
                valor,
                parametros,
                true);
    }

    /*PERSONA MORAL*/

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropNombrePersMoral(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumPersonaMoral.NOMBRE.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropRfcPersMoral(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.RFC_PM_A_DOCE));

        return new PropiedadesValidarDTO(
                EnumPersonaMoral.RFC.getCampo(),
                valor,
                parametros,
                true);
    }
    
    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropRfcPersMoral(String valor, String nombre) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.RFC_PM_A_DOCE));

        return new PropiedadesValidarDTO(
                nombre,
                valor,
                parametros,
                true);
    }
    
}
