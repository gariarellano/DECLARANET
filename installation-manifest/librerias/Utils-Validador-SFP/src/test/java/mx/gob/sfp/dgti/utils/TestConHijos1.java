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

import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase que realiza el armado 
 * de objetos para realiza las pruebas.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class TestConHijos1 {
    
    public TestConHijos1(ModuloValidarDTO dtoValidadciones){
        
        PropiedadesValidarDTO dtoPropDg = new PropiedadesValidarDTO("PRIMER PRO", "AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        
        
        dtoValidadciones.getListPropsTovalidate().add(dtoPropDg);        
        
        ModuloValidarDTO dtoval = new ModuloValidarDTO("------- HIJO - ---------");
        PropiedadesValidarDTO dtoPropDgPrime = new PropiedadesValidarDTO("CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgPrime.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
        PropiedadesValidarDTO dtoPropDgSeg = new PropiedadesValidarDTO("RFC","AICC900114D0S");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
//        dtoValidadciones.getListModuloshijos().add(dtoval);//PRIMER HIJO IGUAL
        dtoPropDgPrime = new PropiedadesValidarDTO("CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgPrime.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
        dtoPropDgSeg = new PropiedadesValidarDTO("RFC","AICC900114D0S");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        dtoValidadciones.getListModuloshijos().add(dtoval);//PRIMER HIJO IGUAL
    }
    
}
