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
public enum EnumPathsData {
    PRODUCTION(
            "http://"+System.getenv("DOMINIO_KONG")+"/identidad/registro/private/user/:id?curp=:CURP&secretkey=:SECRET",
            System.getenv("IP_DOMINIO_FRONT")
    ),
    STAGING(
            "http://"+System.getenv("DOMINIO_KONG")+"/identidad/registro/private/user/:id?curp=:CURP&secretkey=:SECRET",
            System.getenv("IP_DOMINIO_FRONT")
    );
    
    private final String url;
    private final String senderPet;
    
    EnumPathsData(String url, String senderPet){
        this.url = url;
        this.senderPet = senderPet;
    }    

    public String getUrl(){
        return url;
    }
    
    public String getUrlPet(){
        return senderPet;
    }
}
