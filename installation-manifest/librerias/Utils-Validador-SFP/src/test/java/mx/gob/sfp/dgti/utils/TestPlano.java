/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.utils;

import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que realiza la validación de propiedades
 * 
 * @author cgarias
 * @since 27/09/2019
 */
public class TestPlano {
    
    public void llenadoDeValoresParaValidar(List<PropiedadesValidarDTO> listPorpiedadesValidar){
        List<ParametrosValicacionDTO> listVali = new ArrayList<>();
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_SIN_CARACTERES_ESPECIALES));
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYOR_QUE, 5));
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE, 70));
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MAYORQUE_MENORQUE, 1, 70));
        listPorpiedadesValidar.add(new PropiedadesValidarDTO("Nombre", "Christopher Giovanni Arias Cruz", listVali));
        
        listVali = new ArrayList<>();
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ES_NUMERO));
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
        listPorpiedadesValidar.add(new PropiedadesValidarDTO( "Teleono", "5537303410", listVali));
        
        listVali = new ArrayList<>();
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.CURP));        
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_IGUAL, 18));
        listPorpiedadesValidar.add(new PropiedadesValidarDTO( "CURP", "AICC900114HMCRRH04", listVali));
        
        listVali = new ArrayList<>();
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_IGUAL, 13));
        listVali.add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));
        listPorpiedadesValidar.add(new PropiedadesValidarDTO( "RFC", "AICC900114DS0", listVali));
    }
}
