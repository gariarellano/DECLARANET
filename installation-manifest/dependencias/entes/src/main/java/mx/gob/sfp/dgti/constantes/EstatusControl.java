/**
 * @(#)EstatusControl.java 05/04/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.constantes;

/**
     * Enum con el estatus de control del ente
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 05/04/2019
     */
    public enum EstatusControl {
        ACTIVO(1, "El ente se encuentra activo."),
        INACTIVO(0, "El ente se encuentra inactivo.");

        /**
         * Id del estatus
         */
        private final int id;

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
         * @author Pavel Alexei Martinez Regalado aka programador02
         * @since 19/02/2019
         */
        EstatusControl(int id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public int getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

}
