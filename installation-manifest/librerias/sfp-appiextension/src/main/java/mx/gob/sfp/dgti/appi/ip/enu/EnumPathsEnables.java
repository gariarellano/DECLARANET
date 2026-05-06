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
public enum EnumPathsEnables {
    PRODUCTION("http://"+System.getenv("DOMINIO_KONG")+"/identidad/login/public/validate-response"),
    STAGING("http://"+System.getenv("DOMINIO_KONG")+"/identidad/login/public/validate-response"),
    REVIEW("http://"+System.getenv("DOMINIO_KONG")+"/identidad/login/public/validate-response");
    
    private final String url;
    
    EnumPathsEnables(String url){
        this.url = url;
    }    

    public String getUrl(){
        return url;
    }
}
