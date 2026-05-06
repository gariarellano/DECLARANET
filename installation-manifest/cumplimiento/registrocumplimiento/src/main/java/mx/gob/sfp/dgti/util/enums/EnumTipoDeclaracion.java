/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util.enums;

/**
 *
 * @author gio_j
 */
public enum EnumTipoDeclaracion {
    INICIO("Inicio"), 
    CONCLUSION("Conclusión"), 
    MODIFICACION("Modificación"), 
    AVISO("Aviso");
    
    private final String descripcion;

    private EnumTipoDeclaracion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
}
