/**
 * @(#)EnumTipoResp.java 27/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.util;

/**
 * Enum de los tipos de respuesta que existen
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 27/09/2019
 */
public enum EnumTipoResp {
    CORRECTO(1, "Es correcto"),
    CORRECTO_INCOMPLETO(2, "Se guardo, sin embargo tiene observaciones"),
    ERROR_DIGITO(3, "Ocurrio un error en el digito verificador"),
    ERROR_COMUNICACION(4, "Ocurrió un error en la comunicacion"),
    ERROR(5,"Ocurrio un error."),
    CON_OBSERVACIONES(6,"Existen observaciones en la declaración."),
    MODULOS_INCORRECTOS(7, "Faltan secciones para validar"),
    ERROR_GUARDADO(8, "Ocurrió un error en el guardado"),
    NO_ES_AVISO(9, "No se recibe la información de aviso por cambio de dependencia");

    private int id;

    private String mensaje;


    EnumTipoResp(int id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }


}
