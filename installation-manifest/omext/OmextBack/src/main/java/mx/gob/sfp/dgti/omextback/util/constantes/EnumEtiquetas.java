/**
 * @(#)EnumEtiquetas.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

/**
 * Interface para el servicio VerificadorASImpl
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
public enum EnumEtiquetas {

        INICIA_GRAPHQL("=== Inicia GraphQLDataFetchers");

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
