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

import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoFkDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.DomicilioMexicoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoPoder;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase que realiza el armado 
 * de objetos para realizar pruebas.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class TestConHijos {
    
    public TestConHijos(ModuloValidarDTO dtoValidadciones){
        
        PropiedadesValidarDTO dtoPropDg = new PropiedadesValidarDTO("PRIMER PRO", "AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
        //dtoPropDg.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.RFC));
        dtoValidadciones.getListPropsTovalidate().add(dtoPropDg);
                
        PropiedadesValidarDTO dtoPropDgEQ = new PropiedadesValidarDTO( "valor1", 8);//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgEQ.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_IGUAL, 8));
        dtoPropDgEQ.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYOR_QUE, 1));
        dtoPropDgEQ.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MENOR_QUE, 9));
        dtoPropDgEQ.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYORQUE_MENORQUE, 6, 10));
        dtoValidadciones.getListPropsTovalidate().add(dtoPropDgEQ);//propiedad y validaciones del padre
        
        dtoPropDgEQ = new PropiedadesValidarDTO( "valor1", "IGUALDAD EN TEXTO");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgEQ.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, "IGUALDAD EN TEXTO"));
        dtoValidadciones.getListPropsTovalidate().add(dtoPropDgEQ);//propiedad y validaciones del padre
        
        dtoPropDgEQ = new PropiedadesValidarDTO( "valor1", "TEXTO DIF");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgEQ.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NO_IGUAL_A, "DIFERENTE"));
        dtoValidadciones.getListPropsTovalidate().add(dtoPropDgEQ);//propiedad y validaciones del padre
        
        PropiedadesValidarDTO dtoPropEntero = new PropiedadesValidarDTO( "Valor entero", 4382754);//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropEntero.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
        
        PropiedadesValidarDTO dtoPropAlfanumerica = new PropiedadesValidarDTO( "Valor", "Alfanumerica1");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropAlfanumerica.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_ALFANUMERICA));
        
        PropiedadesValidarDTO dtoPropNumerico = new PropiedadesValidarDTO( "Valor numerico", "33.9347");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropNumerico.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ES_NUMERO));
        
        dtoValidadciones.getListPropsTovalidate().add(dtoPropAlfanumerica);//propiedad 
        
//        dtoValidadciones.getListPropsTovalidate().add(dtoPropDg);//propiedad y validaciones del padre
//        dtoValidadciones.getListPropsTovalidate().add(dtoPropDgEQ);//propiedad y validaciones del padre
//        dtoValidadciones.getListPropsTovalidate().add(dtoPropEntero);//propiedad y validaciones del padre
        dtoValidadciones.getListPropsTovalidate().add(dtoPropNumerico);//propiedad y validaciones del padre
        
        ModuloValidarDTO dtoval = new ModuloValidarDTO("------- HIJO PRIMER GEN 1 ----------");
        PropiedadesValidarDTO dtoPropDgPrime = new PropiedadesValidarDTO("CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgPrime.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
        PropiedadesValidarDTO dtoPropDgSeg = new PropiedadesValidarDTO("RFC","AICC900114DS0");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        
        dtoPropDgSeg = new PropiedadesValidarDTO("DEBE SER NULL","");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.OBJETO_U_PROPIEDAD_DEBE_SER_NULL, null));//validacion de java
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        
        dtoPropDgSeg = new PropiedadesValidarDTO("AL MENOS UNO DEBE VENIR NO NULL","");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.AL_MENOS_UN_OBJETO_NO_DEBE_SER_NULL, new Object[]{null, null, "por los menos uno"}));//validacion de java
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        
        
//        dtoValidadciones.getListModuloshijos().add(dtoval);//PRIMER HIJO IGUAL
        dtoPropDgPrime = new PropiedadesValidarDTO("CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgPrime.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
        
        dtoPropDgSeg = new PropiedadesValidarDTO("RFC","AICC900114DS0");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        dtoPropDgSeg = new PropiedadesValidarDTO("CORREO_INSTITUCIONAL-CORREO_PERSONAL","institucional@gmail.com-personal@gmail.com");//PROPIEDADES A VALIDAR
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_TEXTO_AL_MENOS_UNO, "institucional@gmail.com", "personal@gmail.com"));//VALIDACIÓN DE QUE 1 CAMPO ES OBLIGATORIO
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        
        ModuloValidarDTO dtovalSecond = new ModuloValidarDTO("------- HIJO SEGUNDA GEN----------");
        PropiedadesValidarDTO dtoPropHSegPriProp = new PropiedadesValidarDTO("CURP","AICC900114HMCRHM04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegPriProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegPriProp);
        
        PropiedadesValidarDTO dtoPropHSegSegProp = new PropiedadesValidarDTO("RFC","AICC900114DS0");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        dtoPropHSegSegProp = new PropiedadesValidarDTO("VALIDACION DE OBLIGATORIO","REQUIER UN VALOR EN ESTE CAMPO", true);//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_SIN_CARACTERES_ESPECIALES));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        dtoPropHSegSegProp = new PropiedadesValidarDTO("VALIDACION NO OBLIGATORIO NULO",null, false);//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        dtoPropHSegSegProp = new PropiedadesValidarDTO("VALIDACION DE OBLIGATORIO","AICC900114DS0", false);//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        dtoPropHSegSegProp = new PropiedadesValidarDTO("Validacion existe enum ",EnumAmbitoPoder.EJECUTIVO.name());//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumAmbitoPoder.class));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        
        dtoval.getListModuloshijos().add(dtovalSecond);//NIETO DE PRIMER HIJO PRIMER GENERACION
        dtoValidadciones.getListModuloshijos().add(dtoval);//PRIMER HIJO PRIMER GENERACION
        
        
        dtoval = new ModuloValidarDTO("------- HIJO PRIMER GEN 2 ----------");
        dtoPropDgPrime = new PropiedadesValidarDTO("CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgPrime.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
        dtoPropDgSeg = new PropiedadesValidarDTO("RFC","AICC900114DS0");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
//        dtoValidadciones.getListModuloshijos().add(dtoval);//PRIMER HIJO IGUAL
        dtoPropDgPrime = new PropiedadesValidarDTO("CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgPrime.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
        dtoPropDgSeg = new PropiedadesValidarDTO("RFC","AICC900114DS0");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropDgSeg.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
        
        
        dtovalSecond = new ModuloValidarDTO("------- NIETO 1 DE HIJO SEGUNDO ----------");
        dtoPropHSegPriProp = new PropiedadesValidarDTO("CURP","AICC900114HMCRHM04");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegPriProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
//        dtoPropDgPrime.getListValToExec().add(EnumValidator.REGEX_RFC);//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegPriProp);
        dtoPropHSegSegProp = new PropiedadesValidarDTO("RFC","AICC900114DS0");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        dtoPropHSegSegProp = new PropiedadesValidarDTO("LETRAS Y  GUIENES","vacio");//PROPIEDAD A VLAIDAR DEL PADRE
        dtoPropHSegSegProp.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_TEXTO_MINUSCULAS_GIONES));//REGEX A APLICAR A PROPIEDAD
        dtovalSecond.getListPropsTovalidate().add(dtoPropHSegSegProp);
        
        
        DomicilioDTO domicilio = new DomicilioDTO();
        DomicilioMexicoDTO domicilioMexico = new DomicilioMexicoDTO();
        domicilioMexico.setCalle("OLMO");
        domicilioMexico.setCodigoPostal("90000");//No debe de ser un código postal válido
        domicilioMexico.setColoniaLocalidad("San Jerónimo");
        domicilioMexico.setEntidadFederativa(new CatalogoDTO(1, "AGUASCALIENTES"));
        domicilioMexico.setMunicipioAlcaldia(new CatalogoFkDTO(1,"AGUASCALIENTES", 1));
        domicilioMexico.setNumeroExterior("28 bis a");
        domicilioMexico.setNumeroInterior("4");

        EnumUbicacion ubicacion = EnumUbicacion.MEXICO;
        domicilio.setDomicilioMexico(domicilioMexico);
        domicilio.setUbicacion(ubicacion);
        
        dtoval.getListModuloshijos().add(ValDomicilio.crearDomicilio(domicilio, "domicilioDeclarante"));
        
        dtoval.getListModuloshijos().add(dtovalSecond);
        dtoValidadciones.getListModuloshijos().add(dtoval);//Segundo HIJO PRIMER GENERACION
    }
    
}
