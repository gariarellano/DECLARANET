/**
 * @(#)Propiedades.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoFkDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumDomicilio;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosFk;
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
public class PropDomicilio {

     /** PROPIEDADES PARA DOMICILIO MEXICO Y EXTRANJERO*/

    /**
     * Metodo para crear la propiedad de calle que se validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropCalle(String valor) {
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.TRESCIENTOS_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.CALLE.getNombre(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de numero exterior validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropNumExt(String valor) {
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        //parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CINCUENTA_Y_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.NUMERO_EXT.getNombre(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de numero interior validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropNumInt(String valor) {
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        //parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CINCUENTA_Y_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.NUMERO_INT.getNombre(),
                valor,
                parametros,
                false);
    }

    /**
     * Metodo para crear la propiedad de codigo postal validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropCodigoPostalMexico(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CP_MEXICO));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.SEIS.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.CODIGO_POSTAL.getNombre(),
                valor,
                parametros,
                true);
    }

    /**
     *
     * @param valor
     * @param municipio
     * @return
     */
    public static PropiedadesValidarDTO crearPropCodigoPostalMexico(String valor, CatalogoFkDTO municipio) {
        Integer idCodigo;
        try {
            idCodigo = Integer.parseInt(valor);
        } catch (Exception e) {
            idCodigo = null;
        }
        if(valor.length() == 5 && idCodigo != null && municipio != null && municipio.getId() != null) {
            CatalogoFkDTO catalogo = new CatalogoFkDTO(idCodigo, valor, municipio.getId());
            return PropBase.crearPropCatalogo(
                    EnumDomicilio.CODIGO_POSTAL.getNombre(), EnumCatalogosFk.CAT_CODIGO_POSTAL.name(),
                    catalogo, true);
        } else {
            List<ParametrosValicacionDTO> parametros = new ArrayList<>();
            parametros.add(new ParametrosValicacionDTO(EnumValidacion.CP_MEXICO));

            return new PropiedadesValidarDTO(
                    EnumDomicilio.CODIGO_POSTAL.getNombre(),
                    valor,
                    parametros,
                    true);
        }
    }

    /**
     * Metodo para crear la propiedad de codigo postal validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropCodigoPostal(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, EnumCantidad.CERO.getId()));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.ONCE.getId()));


        return new PropiedadesValidarDTO(
                EnumDomicilio.CODIGO_POSTAL.getNombre(),
                valor,
                parametros,
                true);
    }

    /** PROPIEDADES PARA DOMICILIO MEXICO */

    /**
     * Metodo para crear la propiedad de numero exterior validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropColonia(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.COLONIA.getNombre(),
                valor,
                parametros,
                true);
    }

    /** PROPIEDADES PARA DOMICILIO EXTRANJERO*/

    /**
     * Metodo para crear la propiedad de ciudad o localidad validara
     *
     * @param valor valor
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropCiudadLoc(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.CIUDAD_LOCALIDAD.getNombre(),
                valor,
                parametros,
                true);
    }

    /**
     * Metodo para crear la propiedad de estado o provincia validara
     *
     * @param valor valor a validar
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
    public static PropiedadesValidarDTO crearPropEstadoProv(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.CIENTO_UNO.getId()));

        return new PropiedadesValidarDTO(
                EnumDomicilio.ESTADO_PROVINCIA.getNombre(),
                valor,
                parametros,
                true);
    }

}
