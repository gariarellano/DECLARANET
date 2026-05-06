/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum que describe el tipo de firmado de una declaración
 * 
 * @author cgarias
 * @since 17/12/2019
 */
public enum EnumTipoFirma {
    FIEL("Firma Electronica SAT."),
    FUP("Firma Usuario Contraseña");
    
    private final String descricpion;

    /**
     * Constructor principal de enum.
     * @param descricpion Cadena que describe el tipo de firmado de una decalración.
     */
    private EnumTipoFirma(String descricpion) {
        this.descricpion = descricpion;
    }
    
    public String getDescripcion(){
        return descricpion;
    }
}
