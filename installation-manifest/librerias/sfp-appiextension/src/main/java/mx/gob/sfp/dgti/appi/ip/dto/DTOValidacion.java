/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "SFP-AppliExtension" Sistema que permite realizar la validacion de
 * los tokens y transacciones generados por el proveedor de identidad
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.appi.ip.dto;

import java.io.Serializable;
 
/**
 *
 * @author cgarias
 */
public class DTOValidacion implements Serializable{
    
    /**
    * Propiedad que serializa la clase en tiempo de ejecucion. 
    */
    private static final long serialVersionUID = 2602965987384188698L;

    private String transaccion;
    private String curp;
    private String authorization;
    /**
    * @return the transaccion
    */
    public String getTransaccion() {
            return transaccion;
    }
    /**
     * @param transaccion the transaccion to set
     */
    public void setTransaccion(String transaccion) {
            this.transaccion = transaccion;
    }
    /**
     * @return the curp
     */
    public String getCurp() {
            return curp;
    }
    /**
     * @param curp the curp to set
     */
    public void setCurp(String curp) {
            this.curp = curp;
    }

    /**
     * @return the authorization
     */
    public String getAuthorization() {
        return authorization;
    }

    /**
     * @param authorization the autorization to set
     */
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
