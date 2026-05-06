/**
 * @(#)Propiedades.java 02/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.base.RegistroBaseDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumCantidad;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumGeneral;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoOperacion;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos en general
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 02/11/2019
 */
public class PropBase {

    private static final int CATALOGO_OTRO = 9999;

    /**
     * Metodo para crear la propiedad de rfc
     */
    public static PropiedadesValidarDTO crearPropAclaraciones(String valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ACLARACIONES));
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, EnumCantidad.DOS_MIL_UNO.getId()));
        
        return new PropiedadesValidarDTO(
                EnumGeneral.ACLARACIONES.getCampo(),
                valor,
                parametros,
                false);
    }

    /**
     * Metodo para validar el tipo de operacion. Aplican las siguientes reglas:
     *
     * Cuando registroHistorico == true:
     *  - tipoOperacion puede ser null
     *  - tipoOperacion no puede tener el valor AGREGAR, los otros valores estan permitidos
     *
     * Cuando registroHistorico == false:
     *  - tipoOperacion no puede se null
     *  - tipoOperacion puede tener el valor AGREGAR o BAJA, o el equivalente es que no tenga los valores SIN_CAMBIOS ni
     *    MODIFICAR
     *  - si tipoOperacion tiene el valor BAJA: tipoDeclaracion debe ser CONCLUSION o MODIFICACION o el
     *    equivalente seria que no sea INICIO o AVISO
     *
     * @param registro: registro que incluye el tipo de operacion y el registro historico para realizar la validacion
     * @param propiedades: lista de propiedades en la validacion
     * @param encabezado: encabezado de la declaracion que incluye el tipoDeclaracion
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 23/03/2020
     */
    public static void crearPropTipoOperacion(RegistroBaseDTO registro, EncabezadoDTO encabezado,
                                                               List<PropiedadesValidarDTO> propiedades) {
        if (registro.isRegistroHistorico()) {
            if(registro.getTipoOperacion() != null
                    && registro.getTipoOperacion().getDescripcion().equals(EnumTipoOperacion.AGREGAR.getDescripcion())) {
                List<ParametrosValicacionDTO> parametros = new ArrayList<>();
                parametros.add(
                        new ParametrosValicacionDTO(
                                EnumValidacion.CADENA_NO_IGUAL_A,
                                EnumTipoOperacion.AGREGAR.getDescripcion()));
                propiedades.add(new PropiedadesValidarDTO(
                        EnumGeneral.TIPO_OPERACION.getCampo(),
                        registro.getTipoOperacion().getDescripcion(),
                        parametros,
                        false));
            }
        } else {
            if(registro.getTipoOperacion() != null)  {
                List<ParametrosValicacionDTO> parametros = new ArrayList<>();

                parametros.add(
                        new ParametrosValicacionDTO(
                                EnumValidacion.CADENA_NO_IGUAL_A,
                                EnumTipoOperacion.MODIFICAR.getDescripcion()));
                parametros.add(
                        new ParametrosValicacionDTO(
                                EnumValidacion.CADENA_NO_IGUAL_A,
                                EnumTipoOperacion.SIN_CAMBIO.getDescripcion()));

                if (registro.getTipoOperacion().getDescripcion().equals(EnumTipoOperacion.BAJA.getDescripcion())) {
                    List<ParametrosValicacionDTO> parametrosTipoD = new ArrayList<>();
                    parametrosTipoD.add(
                            new ParametrosValicacionDTO(
                                    EnumValidacion.CADENA_NO_IGUAL_A,
                                    EnumTipoDeclaracion.INICIO.getDescripcion()));
                    parametrosTipoD.add(
                            new ParametrosValicacionDTO(
                                    EnumValidacion.CADENA_NO_IGUAL_A,
                                    EnumTipoDeclaracion.AVISO.getDescripcion()));

                    propiedades.add(new PropiedadesValidarDTO(
                            EnumGeneral.TIPO_DECLARACION.getCampo(),
                            encabezado.getTipoDeclaracion().getDescripcion(),
                            parametrosTipoD,
                            true));
                }
                propiedades.add(new PropiedadesValidarDTO(
                        EnumGeneral.TIPO_OPERACION.getCampo(),
                        registro.getTipoOperacion().getDescripcion(),
                        parametros,
                        true));
            } else {
                propiedades.add(
                        crearObligatoria(registro.getTipoOperacion(), EnumGeneral.TIPO_OPERACION.getCampo()));
            }
        }
    }

    /**
     * Metodo para crear la propiedad de catalogo validara
     *
     * @param campo: nombre del campo en el objeto
     * @param nombreCatalogo: nombre del catalogo al que se llamara
     * @param catalogo: objeto con el catalogo
     * @param obligatorio: indica si el catálogo es obligatorio
     *
     * @return objeto ModuloValidarDTO con sus respectivas validaciones
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 02/11/2019
     */
    public static PropiedadesValidarDTO crearPropCatalogo(String campo, String nombreCatalogo,
                                                          CatalogoDTO catalogo, boolean obligatorio) {
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();

            parametros.add(new ParametrosValicacionDTO(EnumValidacion.CATALOGO, nombreCatalogo));
            return new PropiedadesValidarDTO(
                    campo,
                    catalogo,
                    parametros,
                    obligatorio);
    }

    /**
     * Metodo para crear la validacion de catalogos que permiten el valor de otro.
     *
     * @param campoCatalogo: nombre del campo en el objeto de catalogo
     * @param nombreCatalogo: nombre del catalogo al que se llamara
     * @param catalogo: objeto con el catalogo
     * @param obligatorio: indica si el catálogo es obligatorio
     * @param valorOtro: valor del otro
     * @param propiedades: lista de propiedades a agregar
     *
     * @return objeto ModuloValidarDTO con sus respectivas validaciones
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 29/12/2019
     */
    public static void crearPropCatalogoOtro(
                       String campoCatalogo, String nombreCatalogo, CatalogoDTO catalogo, boolean obligatorio,
                       String valorOtro, List<PropiedadesValidarDTO> propiedades) {

        propiedades.add(PropBase.crearPropCatalogo(campoCatalogo, nombreCatalogo, catalogo, obligatorio));
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        boolean otroObligatorio = false;
        if(catalogo != null && catalogo.getId().intValue() == CATALOGO_OTRO) {
            otroObligatorio = true;
            parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 0));
        } else {

            parametros.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_IGUAL, 0));
        }
        propiedades.add(new PropiedadesValidarDTO(
                campoCatalogo.concat("Otro"),
                valorOtro,
                parametros,
                otroObligatorio)
        );
    }

    /**
     * Metodo para validar que por lo menos uno de los objetos no sea nul
     *
     * Ejemplo:
     *
     * propiedades.add(PropDom.crearPropUnSoloObjetoNoNulo(
     * 					new Object[]{
     * 							domicilio.getDomicilioMexico(),
     * 							domicilio.getDomicilioExtranjero()},
     * 					new String[]{
     * 							EnumNombre.DOMICILIO_MEXICO.getNombre(),
     * 							EnumNombre.DOMICILIO_EXTRANJERO.getNombre()
     *                                        }));
     *
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
    
    /**
     * Método que realiza la validacion de que al menos un Objeto no debe ir nulo.
     * 
     * @param objetos   Arreglo de objetos que contiene los valores de los objetos a validar.
     * @param campos    Arreglo de Cadenas que pertenecen a los campos a validar.
     * 
     * @return PropiedadesValidarDTO Objeto con formato para realizar la validación.
     */
    public static PropiedadesValidarDTO crearPropAlMenosUnObjetoNoNulo(Object[] objetos, String[] campos) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.UN_SOLO_OBJETO_NO_NULO, objetos));

        return new PropiedadesValidarDTO(
                Arrays.stream(campos).collect(Collectors.joining("-")),
                "",
                parametros,
                true);
    }

    /**
     * Metodo para crear una propiedad que valide que un modulo o una lista sean nulos
     */
    public static PropiedadesValidarDTO crearModuloDebeSerNulo(Object modulo, String campo) {
        
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.OBJETO_DEBE_SER_NULO, modulo));

        return new PropiedadesValidarDTO(
                campo,
                "",
                parametros,
                true);
    }
    
    /**
     * Método realiza la validacion de que el objeto u propiedad sea null.
     * 
     * @param propiedad Objeto que contiene la propiedad, debe ser null
     * @param nombre    Nombre del campo que hacer refecencia a su propiedad.
     * 
     * @return PropiedadesValidarDTO Objeto de propiedad que se manda validar.
     */
    public static PropiedadesValidarDTO crearPropDebeSerNulo(Object propiedad, String nombre) {
        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.OBJETO_U_PROPIEDAD_DEBE_SER_NULL, propiedad));
        return new PropiedadesValidarDTO(nombre, "", parametros);
    }

    /**
     * Metodo para crear una propiedad que valide que un modulo o una lista sean no nulos
     */
    public static PropiedadesValidarDTO crearModuloDebeSerNoNulo(Object modulo, String campo) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.OBJETO_DEBE_SER_NO_NULO, modulo));

        return new PropiedadesValidarDTO(
                campo,
                "",
                parametros,
                true);
    }
    
    /**
     * Obtener una propiedad obligatoria
     *
     * @param propiedad valor del campo
     * @param nombre nombre del campo
     *
     * @return PropiedadesValidarDTO
     */
    public static PropiedadesValidarDTO crearObligatoria(Object propiedad, String nombre) {

    	return new PropiedadesValidarDTO(
                nombre,
                propiedad,
                true);
    }

    /**
     * Propiedad que valida un id
     *
     * @param id el id
     *
     * @return PropiedadesValidarDTO
     */
    public static PropiedadesValidarDTO crearId( String id) {

        List<ParametrosValicacionDTO> parametrosId = new ArrayList<>();
        parametrosId.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 0));

        return new PropiedadesValidarDTO(
                EnumGeneral.ID.getCampo(),
                id,
                parametrosId,
                false);
    }

    /**
     * Propiedad que valida el id posicion vista
     *
     * @param id el id posicion vista
     *
     * @return PropiedadesValidarDTO
     */
    public static PropiedadesValidarDTO crearIdPosicionVista(String id) {

        List<ParametrosValicacionDTO> parametrosId = new ArrayList<>();
        parametrosId.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 0));

        return new PropiedadesValidarDTO(
                EnumGeneral.ID_POSICION_VISTA.getCampo(),
                id,
                parametrosId,
                true);
    }

    /**
     * Propiedad que valida una fecha contra la fecha de encargo o el anio dependiendo
     *
     * @param fecha fecha que se va a validar
     * @param encabezado informacion sobre la declaracion necesaria para hacer estas validaciones
     * @param nombre nombre del campo de la fecha
     *
     * @return PropiedadesValidarDTO con las validaciones necesarios
     */
    public static PropiedadesValidarDTO crearValidacionFechaContraFechaEncargo(
            String fecha, EncabezadoDTO encabezado, String nombre, boolean obligatorio){

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));

        if(EnumTipoDeclaracion.INICIO.equals(encabezado.getTipoDeclaracion()) ||
               EnumTipoDeclaracion.CONCLUSION.equals(encabezado.getTipoDeclaracion())) {

            parametros.add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORIGUAL_QUE, encabezado.getFechaEncargo()));

        } else if(EnumTipoDeclaracion.MODIFICACION.equals(encabezado.getTipoDeclaracion())){

            parametros.add(new ParametrosValicacionDTO(
                    EnumValidacion.FECHA_MENORIGUAL_QUE,
                    (encabezado.getAnio() - 1) + "-12-31" ));
        }

        return new PropiedadesValidarDTO(
                nombre,
                fecha,
                parametros,
                obligatorio);
    }

}
