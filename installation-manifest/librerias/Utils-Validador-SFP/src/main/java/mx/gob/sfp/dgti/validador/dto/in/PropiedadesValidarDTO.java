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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que contiene las propiedades para ejecutar validaciones.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class PropiedadesValidarDTO implements Serializable {
    
    /**
     * Propiedad que contiene el nombre identificador del campo.
     */
    private String  nombreCampo;
    /**
     * Propiedad que contendra el valor del objeto a validar.
     */
    private Object propiedadValidar;
    /**
     * Propiedad que indica si el valor de propiedadValidar debe venir con con contenido
     */
    private boolean obligatorio;
    
    /**
     * Propiedad que contiene las validaciones a aplicar a la propiedad.
     */
    private List<ParametrosValicacionDTO> listValToExec;

    /**
     * Constructor principal de la clase que asigna el valor de la propiedad para realizar las validaciones.
     *
     * @param nombreCampo Nombre del campo en el que se esta ejecutando la validación.
     * @param propiedadValidar Valor que contiene la propiedad a evaluar.
     */
    public PropiedadesValidarDTO(String nombreCampo , Object propiedadValidar){

        this.nombreCampo = Objects.requireNonNull(nombreCampo,"Nombre del campo no debe ser null");
        this.propiedadValidar = propiedadValidar;
        this.listValToExec = new ArrayList<>();
        this.obligatorio = true;
    }
    /**
     * Constructor principal de la clase que asigna el valor de la propiedad para realizar las validaciones
     * y adicional inicializa la bandera de obligatorio.
     *
     * @param nombreCampo Nombre del campo en el que se esta ejecutando la validación.
     * @param propiedadValidar Valor que contiene la propiedad a evaluar.
     * @param obligatorio Bandera que indica si la propiedad a validar puede venir null o no.
     */
    public PropiedadesValidarDTO(String nombreCampo , Object propiedadValidar, boolean obligatorio){
        this.nombreCampo = Objects.requireNonNull(nombreCampo,"Nombre del campo no debe ser null");
        this.propiedadValidar = propiedadValidar;
        this.listValToExec = new ArrayList<>();
        this.obligatorio = obligatorio;
    }
    
    /**
     * Constructor principal de la clase que asigna el valor de la propiedad para realizar las validaciones.
     *
     * @param nombreCampo Nombre del campo en el que se esta ejecutando la validación.
     * @param propiedadValidar Valor que contiene la propiedad a evaluar.
     * @param listValToExec lista que contiene las reglas de validacion a ejecutar.
     */
    public PropiedadesValidarDTO(String nombreCampo, Object propiedadValidar, List<ParametrosValicacionDTO> listValToExec){
        this.nombreCampo = Objects.requireNonNull(nombreCampo,"Nombre del campo no debe ser null");
        this.propiedadValidar = propiedadValidar;
        this.listValToExec = Objects.requireNonNull(listValToExec, "Lista de validaciones no debe ser null");
        this.obligatorio = true;
    }
    
    /**
     * Constructor principal de la clase que asigna el valor de la propiedad para realizar las validaciones 
     * y adicional inicializa la bandera de obligatorio.
     *
     * @param nombreCampo Nombre del campo en el que se esta ejecutando la validación.
     * @param propiedadValidar Valor que contiene la propiedad a evaluar.
     * @param listValToExec lista que contiene las reglas de validacion a ejecutar.
     * @param obligatorio Bandera que indica si la propiedad a validar puede venir null o no.
     */
    public PropiedadesValidarDTO(String nombreCampo, Object propiedadValidar, List<ParametrosValicacionDTO> listValToExec, boolean obligatorio){
        this.nombreCampo = Objects.requireNonNull(nombreCampo,"Nombre del campo no debe ser null");
        this.propiedadValidar = propiedadValidar;
        this.listValToExec = Objects.requireNonNull(listValToExec, "Lista de validaciones no debe ser null");        
        this.obligatorio = obligatorio;
    }
    
    /**
     * @return the propiedadValidar
     */
    public Object getPropiedadValidar() {
        return propiedadValidar;
    }

    /**
     * @param propiedadValidar the propiedadValidar to set
     */
    public void setPropiedadValidar(Object propiedadValidar) {
        this.propiedadValidar = propiedadValidar;
    }

    /**
     * @return the listValToExec
     */
    public List<ParametrosValicacionDTO> getListValToExec() {
        return listValToExec;
    }

    /**
     * @param listValToExec the listValToExec to set
     */
    public void setListValToExec(List<ParametrosValicacionDTO> listValToExec) {
        this.listValToExec = listValToExec;
    }

    /**
     * @return the nombreCampo
     */
    public String getNombreCampo() {
        return nombreCampo;
    }

    /**
     * @param nombreCampo the nombreCampo to set
     */
    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    /**
     * @return the obligatorio
     */
    public boolean isObligatorio() {
        return obligatorio;
    }

    /**
     * @param obligatorio the obligatorio to set
     */
    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PropiedadesValidarDTO{");
        sb.append("nombreCampo='").append(nombreCampo).append('\'');
        sb.append(", propiedadValidar=").append(propiedadValidar);
        sb.append(", obligatorio=").append(obligatorio);
        sb.append(", listValToExec=").append(listValToExec);
        sb.append('}');
        return sb.toString();
    }
}
