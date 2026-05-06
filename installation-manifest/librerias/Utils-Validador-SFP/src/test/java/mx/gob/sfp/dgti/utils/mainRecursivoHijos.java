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
import mx.gob.sfp.dgti.validador.dto.out.ErroresDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;

import java.util.concurrent.CompletableFuture;

/**
 * Clase main para realizar la 
 * ejecucion de llas pruebas unitarias en 
 * caso de no detectar el problema.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class mainRecursivoHijos {
 
    public static void main(String args[]){
        System.out.println("-------------ejecutaValidaciones!-----");
        ModuloValidarDTO dtoValidadciones = new ModuloValidarDTO("-------------PADRE-------------");
        new TestConHijos(dtoValidadciones);//Asisgna valores a objeto
        
        CompletableFuture<?>[] futuresComp = new CompletableFuture[2];
        
        ErroresDTO erroresDto = new ErroresDTO();
        futuresComp[0] = CompletableFuture.runAsync(()->{
            new ExectValidations().ejecutaValidaciones(erroresDto, dtoValidadciones);
        });
        
        ModuloDTO moduloDto = new ModuloDTO("NOMBRE-MODULO-PADRE-DE-PADRES");
        futuresComp[1] = CompletableFuture.runAsync(()->{
            new ExectValidations().ejecutaValidaciones(dtoValidadciones,moduloDto);
        });
        
        CompletableFuture.allOf(futuresComp).join();   
        
        mainRecursivoHijos.visualizaErrores(erroresDto);
        
        mainRecursivoHijos.visualizaErrores(moduloDto);
    }
    
    private static void visualizaErrores(ErroresDTO erroresDto){
        
        if (!erroresDto.getListModHijos().isEmpty()){
            erroresDto.getListModHijos().stream().forEach((index) -> {
                visualizaErrores(index);
            });
        }
        System.out.println("--------------VISUALIZACIUON DE ERRORES--------------------");
        System.out.println("Para el modulo:"+erroresDto.getNombreModulo()+" sus propiedades :");
        for(PropiedadesErrorDTO index: erroresDto.getListErorres()){
            System.out.println( "Nombre campo:"+ index.getNombreCampo() +"propiedad valor :"+index.getPropiedadValor()); //validacio ejecutada
            System.out.println("# errores val = "+index.getListErrorMensajes().size());//NUMERO DE ERRORRES DE LAS VALIDACIONES
            index.getListErrorMensajes().stream().forEach((indexEr) -> {
                System.out.println("error encontrado id :"+indexEr.getErrorId()+ " mensaje "+indexEr.getMensaje());
            });
        }
    }
    
    private static void visualizaErrores(ModuloDTO moduloDto){
        System.out.println("--------------VISUALIZACIUON DE ERRORES as modulo--------------------");
        System.out.println("Para el modulo:"+moduloDto.getModulo()+" sus propiedades :");
        for(PropiedadesErrorDTO index: moduloDto.getErrores()){
            System.out.println("Posicion=0 Nombre campo= "+ index.getNombreCampo() +"propiedad valor="+index.getPropiedadValor()); //validacio ejecutada
            System.out.println("# errores val = "+index.getListErrorMensajes().size());//NUMERO DE ERRORRES DE LAS VALIDACIONES
            index.getListErrorMensajes().stream().forEach((indexEr) -> {
                System.out.println("error encontrado id :"+indexEr.getErrorId()+ " mensaje "+indexEr.getMensaje());
            });
        }
    }
}
