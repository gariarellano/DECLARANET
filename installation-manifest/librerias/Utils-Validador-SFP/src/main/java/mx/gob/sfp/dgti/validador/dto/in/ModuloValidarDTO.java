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
 * Clase que contendra las propiedades a validar
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class ModuloValidarDTO implements  Serializable{    
    /**
     * Propiedad que debe de contener el nombre del módulo que se pretende validar.
     */
    private String nombreModulo;
    /**
     * Posicion en caso de que se tenga una lista de modulos
     */
    private String posicion;
    /**
     * Propiedad que contiene los Objetos para ejecutar sus validaciones.
     */
    private List<PropiedadesValidarDTO> listPropsTovalidate;
    /**
     * Propieda que contiene los módulos hijos del propio módulo que permitira realizar las validaciones corresondientes por propiedad.
     */
    private List<ModuloValidarDTO> listModuloshijos;

    /**
     * Contructor principal de la clase.
     * No permite tener null en los objetos.
     * 
     * @param nombreModulo Cadena que describe el nombre del módulo
     */
    public ModuloValidarDTO(String nombreModulo){
        this.nombreModulo = Objects.requireNonNull(nombreModulo, "Nombre del módulo no debe ser nulo");
        this.listPropsTovalidate = new ArrayList<>();
        this.listModuloshijos = new ArrayList<>();        
    }

    /**
     * Constructor para cuando se necesite indicar la posicion de un modulo: cuando este en una lista
     *
     * @param nombreModulo Cadena que describe el nombre del modulo
     * @param posicion Posicion recibida y generada en el el front que permite ubicar el modulo
     */
    public ModuloValidarDTO(String nombreModulo, String posicion) {
        this.nombreModulo = nombreModulo;
        this.posicion = posicion;
        this.listPropsTovalidate = new ArrayList<>();
        this.listModuloshijos = new ArrayList<>(); 
    }

    /**
     * Contructor principal de la clase.
     * No permite tener null en los objetos.
     * @param nombreModulo Cadena que describe el nombre del módulo
     * @param listModuloshijos Lista de objetos hijos a validar
     */
     public ModuloValidarDTO(String nombreModulo, List<ModuloValidarDTO> listModuloshijos){
        this.nombreModulo = Objects.requireNonNull(nombreModulo, "Nombre del módulo no debe ser nulo");
        this.listModuloshijos = Objects.requireNonNull(listModuloshijos, "Lista de modulos Hijo no debe ser null");        
        this.listPropsTovalidate = new ArrayList<>();
    }
     
    /**
     * Contructor principal de la clase.
     * No permite tener null en los objetos.
     *
     * @param nombreModulo Cadena que describe el nombre del módulo
     * @param listModuloshijos Lista de objetos hijos a validar
     * @param listPropsTovalidate lista de objetos propiedades a validar
     */
    public ModuloValidarDTO(String nombreModulo, List<ModuloValidarDTO> listModuloshijos, List<PropiedadesValidarDTO> listPropsTovalidate){
        this.nombreModulo = Objects.requireNonNull(nombreModulo, "Nombre del módulo no debe ser nulo");
        this.listModuloshijos = Objects.requireNonNull(listModuloshijos, "Lista de modulos Hijo no debe ser null");        
        this.listPropsTovalidate = Objects.requireNonNull(listPropsTovalidate, "Lista de Propiedades a validar no debe ser null");        ;
    }
    
    /**
     * @return the nombreModulo
     */
    public String getNombreModulo() {
        return nombreModulo;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    /**
     * @param nombreModulo the nombreModulo to set
     */
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    /**
     * @return the listPropsTovalidate
     */
    public List<PropiedadesValidarDTO> getListPropsTovalidate() {
        return listPropsTovalidate;
    }

    /**
     * @param listPropsTovalidate the listPropsTovalidate to set
     */
    public void setListPropsTovalidate(List<PropiedadesValidarDTO> listPropsTovalidate) {
        this.listPropsTovalidate = listPropsTovalidate;
    }

    /**
     * @return the listModuloshijos
     */
    public List<ModuloValidarDTO> getListModuloshijos() {
        return listModuloshijos;
    }

    /**
     * @param listModuloshijos the listModuloshijos to set
     */
    public void setListModuloshijos(List<ModuloValidarDTO> listModuloshijos) {
        this.listModuloshijos = listModuloshijos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModuloValidarDTO{");
        sb.append("nombreModulo='").append(nombreModulo).append('\'');
        sb.append(", posicion='").append(posicion).append('\'');
        sb.append(", listPropsTovalidate=").append(listPropsTovalidate);
        sb.append(", listModuloshijos=").append(listModuloshijos);
        sb.append('}');
        return sb.toString();
    }
}
