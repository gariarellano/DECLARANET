/**
 * @(#)EnumModulo.java 28/01/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Clase con los nombres de los proxies
 * @author pavel
 * @since 28/01/2020
 */
public class Proxies {

    /**
     * Direccion del servicio
     */
    public static final String SERVICE_ADDRESS_LLAMADOR = "llamador-servicios";

    /**
     * Proxy para funcionalidad de vistas
     */
    public static final String SERVICE_VISTAS = "service-vistas";

    /**
     * Proxy para funcionalidad de vistas
     */
    public static final String SERVICE_PERIODOS = "service-periodos";

    /**
     * Proxy para funcionalidad de vistas
     */
    public static final String SERVICE_CUMPLIMIENTO = "service-cumplimiento";

    /**
     * Direccion del s
     */
    public static final String SERVICE_ADDRESS_CONSULTA_HTTP = "consulta-http";

    /**
     * Base de datos OMEXT para el cumplimiento
     */
    public static final String SERVICE_MONGO_CUMPLIMIENTO_OMEXT = "mongo-omext-cumplimiento";

    /**
     * Base de datos OMEXT para el cumplimiento
     */
    public static final String PERIODIC_MONGO = "periodic-mongo";


    private Proxies() {
        throw new IllegalStateException("Utility class");
    }

}
