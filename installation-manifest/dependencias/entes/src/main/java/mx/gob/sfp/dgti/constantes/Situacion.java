/**
 * @(#)Situacion.java 23/04/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

import java.util.Arrays;

/**
     * Enum con la situacion del ente
     *
     * @author pavel.martinez
     * @since 23/04/2020
     */
    public enum Situacion {
        NORMAL("NORMAL", "El ente se encuentra en situacion normal."),
        TRANSICION("TRANSICION", "El ente se encuentra en situacion de transicion."),
        LIQUIDACION("LIQUIDACION", "El ente se encuentra en situacion de liquidacion.");

        /**
         * Id del estatus
         */
        private final String id;

        /*
         * Descripcion de la respuesta para el usuario
         */
        private final String descripcion;

        /**
         * Metodo constructor
         *
         * @param id: id del estatus
         * @param descripcion: descripcion del estatus
         *
         * @author pavel.martinez
         * @since 23/04/2020
         */
        Situacion(String id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public String getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

    /**
     *
     * @param situacionString
     * @return
     */
    public static Situacion obtenerEnum(String situacionString) {
        return Arrays.asList(values()).stream()
                .filter(situacion -> situacion.getId().equals(situacionString))
                .findFirst()
                .orElse(null);
    }

}
