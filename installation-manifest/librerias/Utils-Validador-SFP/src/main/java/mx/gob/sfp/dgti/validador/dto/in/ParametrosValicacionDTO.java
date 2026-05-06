/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.validador.dto.in;

import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.Objects;

/**
 * Clase que contiene las propiedades de la validacion a ejecutar 
 * para su propiedad padre.
 * 
 * @author cgarias
 * @since 26/09/2019
 */
public class ParametrosValicacionDTO {
    /**
     * Enum que indica el tipo de validacion que se realizara a la propiedad padre.
     */
    private final EnumValidacion enumValidacion;
    /**
     * Prmer parametro para realizar la evaluación, en caso de ser necesario.
     */
    private Object priValToEval;
    /**
     * Segundo parametro para realizar la evaluacion en caso de ser necesario.
     */
    private Object segValToEval;
    //private Object terValToEval;// en caso de ser necesaria, realizar la implementacion (descomentar y crear contructor de asignacion).
    
    /**
     * Constructor principal de la clase.
     * 
     * @param enumValidacion Enum que indica que validacion se le va a aplicar
     */
    public ParametrosValicacionDTO(EnumValidacion enumValidacion){
        this.enumValidacion = Objects.requireNonNull(enumValidacion,"Enum de validación no debe ser nulo");
    }
    /**
     * Constructor principal de la clase para evaluar con un parametro.
     * 
     * @param enumValidacion Enum que indica que validacion se le va a aplicar
     * @param priValToEval en caso de que la evaluación tenga un valor con el que se tenga que evaluar.
     */
    public ParametrosValicacionDTO(EnumValidacion enumValidacion, Object priValToEval){
        this.enumValidacion = Objects.requireNonNull(enumValidacion, "Enum de validación no debe ser nulo");
        this.priValToEval = priValToEval;
    }
    /**
     * Constructor principal de la clase para evaluar con un parametro. 
     * 
     * @param enumValidacion Enum que indica que validacion se le va a aplicar
     * @param priValToEval en caso de que la evaluación tenga un valor con el que se tenga que evaluar.
     * @param segValToEval 
     */
    public ParametrosValicacionDTO(EnumValidacion enumValidacion, Object priValToEval, Object segValToEval){
        this.enumValidacion = Objects.requireNonNull(enumValidacion, "Enum de validación no debe ser nulo");
        this.priValToEval = priValToEval;
        this.segValToEval = segValToEval;
    }

    /**
     * @return the enumTipoValidacion
     */
    public EnumValidacion getEnumValidacion() {
        return enumValidacion;
    }

    /**
     * @return the priValToEval
     */
    public Object getPriValToEval() {
        return priValToEval;
    }

    /**
     * @return the segValToEval
     */
    public Object getSegValToEval() {
        return segValToEval;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParametrosValicacionDTO{");
        sb.append("enumValidacion=").append(enumValidacion);
        sb.append(", priValToEval=").append(priValToEval);
        sb.append(", segValToEval=").append(segValToEval);
        sb.append('}');
        return sb.toString();
    }
}
