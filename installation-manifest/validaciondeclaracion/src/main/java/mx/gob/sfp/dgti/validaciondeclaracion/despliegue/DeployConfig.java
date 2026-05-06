/**
 * @(#)DeployConfig.java 15/08/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.despliegue;

/**
 * Clase de DeployConfig
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 15/08/2019
 */
public class DeployConfig {

    /**
     * Nombre del verticle que se va a desplegar
     */
    private String nombreVerticle;

    /**
     * Numero de instancias que se van a desplegar para el verticle
     */
    private int numInstancias;

    /**
     * Numero de workers que se van a desplegar
     */
    private int numWorkers;

    /**
     * Constructor que recibe todos los parametros posibles.
     *
     * @param nombreVerticle
     * @param numInstancias
     * @param numWorkers
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 15/08/2019
     */
    public DeployConfig(String nombreVerticle, int numInstancias, int numWorkers) {
        this.nombreVerticle = nombreVerticle;
        this.numInstancias = numInstancias;
        this.numWorkers = numWorkers;
    }

    public String getNombreVerticle() {
        return nombreVerticle;
    }

    public int getNumInstancias() {
        return numInstancias;
    }

    public int getNumWorkers() {
        return numWorkers;
    }

}
