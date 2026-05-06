/**
 * @(#)EnumCampos 17/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.util;

/**
 * Interface para el servicio VerificadorASImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public enum EnumEtiquetas {

        INICIA_GRAPHQL("=== Inicia GraphQLDataFetchers"),
        GUARDAR_DECLARACION("=== Guardar declaracion()"),
        ERROR_INESPERADO("=== Ocurrio un error inesperado:\n"),
        REPARTIR_DECLARACION("==== RepartirDeclaracion");

        private final String nombre;

        /**
         * Constructor con parametros
         * @param nombre
         */
        EnumEtiquetas(String nombre) {
            this.nombre = nombre;
        }

        /**
         * @return the modulo
         */
        public String getNombre() {
            return nombre;
        }

}
