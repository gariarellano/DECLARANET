/**
 * @(#)Propiedades.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.enums.campos.EnumActividadLaboral;
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
public class PropActividadLab {

    /*ACTIVIDAD LABORAL*/
    /**
     * Metodo para crear la propiedad de fecha de ingreso
     */
    public static PropiedadesValidarDTO crearPropFechaIngreso(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));

        return new PropiedadesValidarDTO(
                EnumActividadLaboral.FECHA_INGRESO.getCampo(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearPropFechaEgreso(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));

        return new PropiedadesValidarDTO(
                EnumActividadLaboral.FECHA_EGRESO.getCampo(),
                valor,
                parametros,
                true);
    }

    /*CON SALARIO*/

    /**
     * Metodo para crear la propiedad de salario mensual neto
     */
    public static PropiedadesValidarDTO crearPropSalario(Integer valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYOR_QUE, 0));

        return new PropiedadesValidarDTO(
                EnumActividadLaboral.SALARIO.getCampo(),
                valor,
                parametros,
                true);
    }

}
