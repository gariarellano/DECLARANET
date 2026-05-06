/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.clienteweb;

/**
 * Enum que contiene los tipos de validaciuones que se tienen 
 * programados para este utileria de regresar mensajes.
 * 
 * @author cgarias
 * @since 26/09/2019
 */
public enum EnumCliente {
    CATALOGO("catalogo"),
    INFO("info"),
    ESTADO("estado"),
    RESPUESTA_CORRECTA("CORRECTO"),
    RESPUESTA("respuesta")

    ;
    private final String valor;

    EnumCliente(String valor){
        this.valor = valor;
    }
    
    public String getValor(){
        return valor;
    }
}
