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

import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErrorMensajesDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que permite ralizar la validacion del proceso de ejecución
 * de validaciones de propiedades.
 * 
 * @author cgarias
 */
public class mainPlano {
    public static void main(String args[]){
        List<PropiedadesValidarDTO> listPropVali = new ArrayList<>();
        new TestPlano().llenadoDeValoresParaValidar(listPropVali);
        
        List<PropiedadesErrorDTO> listPropErro = new ArrayList<>();
        new ExectValidations().ejecutaValidacioes(listPropVali, listPropErro);
        
        for(PropiedadesErrorDTO index :listPropErro){
            System.out.println(" Nombre Campo :"+index.getNombreCampo()+" Valor :"+index.getPropiedadValor());
            for(ErrorMensajesDTO indexPr : index.getListErrorMensajes()){
                System.out.println("IdError:"+indexPr.getErrorId()+" MENSAJE:"+indexPr.getMensaje());    
            }
        }
    }
}
