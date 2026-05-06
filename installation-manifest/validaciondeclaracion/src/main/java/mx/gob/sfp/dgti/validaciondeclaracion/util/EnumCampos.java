/**
 * @(#)EnumCampos 17/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.util;

public enum EnumCampos {

        DECLARACION("declaracion"),
        ESTADO("estado"),
        ENCABEZADO("encabezado"),
        PARAMETROS("parametros"),
        TIPO_FORMATO("tipoFormato"),
        NUMERO_DECLARACION("numeroDeclaracion"),
        FECHA_ENCARGO("fechaEncargo"),
        TIPO_DECLARACION("tipoDeclaracion"),
        NIVEL_JERARQUICO("nivelJerarquico"),
        VALOR_UNO("valorUno"),
        FIRMADA("firmada"),
        ANIO("anio"),
        SERVICIO_GUARDAR_DECLARACION("guardarDeclaracion"),
        SERVICIO_GUARDAR_AVISO("guardarAviso"),
        SERVICIO_GUARDAR_NOTAS("guardarNotas"),
        MENSAJE_ERROR_EN_GUARDADO("errorMessage"),
        DECLARACION_ORIGEN("declaracionOrigen");

        private final String nombre;

        /**
         * Constructor con parametros
         * @param nombre
         */
        EnumCampos(String nombre) {
            this.nombre = nombre;
        }

        /**
         * @return the modulo
         */
        public String getNombre() {
            return nombre;
        }

}
