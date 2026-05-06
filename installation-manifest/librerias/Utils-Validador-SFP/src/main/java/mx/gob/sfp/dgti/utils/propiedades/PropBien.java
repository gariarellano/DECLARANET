/**
 * @(#)PropBien.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumBien;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaAdquiscion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumFormaPago;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 11/11/2019
 */
public class PropBien {

    /**
     * Metodo para crear la propiedad de tipo de domicilio
     */
    public static PropiedadesValidarDTO crearFechaAdquisicion(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));

        return new PropiedadesValidarDTO(
                EnumBien.FECHA_ADQUISICION.getNombre(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear propiedad para la forma de pago cuando es compra
     */
    public static PropiedadesValidarDTO crearFormaPago(EnumFormaPago formaPago, CatalogoDTO formaAdquisicion) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        if (formaAdquisicion.getId().intValue() != EnumFormaAdquiscion.COMPRAVENTA.getId()) {
            parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, EnumFormaPago.NO_APLICA.getDescripcion()));
        } else {
            parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NO_IGUAL_A, EnumFormaPago.NO_APLICA.getDescripcion()));
        }

        return new PropiedadesValidarDTO(
                EnumBien.FORMA_PAGO.getNombre(),
                formaPago.getDescripcion(),
                parametros,
                true);
    }

}
