/*
 * Enum generico que permite identificar el tipo de firmado de un a declaracion.
 */
package mx.gob.sfp.dgti.util.enums;

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
