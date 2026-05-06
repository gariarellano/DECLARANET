/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "SFP-AppliExtension" Sistema que permite realizar la validacion de
 * los tokens y transacciones generados por el proveedor de identidad
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.appi.ip.enu;

/**
 * Enum que permite seleccionar la petición al ambiente seleccionado.
 * 
 * @author cgarias
 * @since 22/08/2019
 */ 
public enum EnumPathsPermisos {
    PRODUCTION("http://"+System.getenv("DOMINIO_KONG")+"/seguridad/asignacion-token/#colName#/#sistema#/#curp#"),
    STAGING("http://"+System.getenv("DOMINIO_KONG")+"/seguridad/asignacion-token/#colName#/#sistema#/#curp#"),
    PRUEBAS("http://"+System.getenv("DOMINIO_KONG")+"/seguridad/asignacion-token/#colName#/#sistema#/#curp#");
    
    private final String url;    
    
    EnumPathsPermisos(String url){
        this.url = url;        
    }    

    public String getUrl(){
        return url;
    }
}
