/**
 * @(#)EnumEtiquetas.java 19/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.util;

/**
 * Enum de los tipos de respuesta que existen
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 19/12/2019
 */
public enum EnumEtiquetas {
    LLAMADOR_CATALOGOS("=== LlamadorCatalogosImpl()"),
    CATALOGO_AGREGADO("==== Catalogo agregado: "),
    OCURRIO_ALGO("=== Ocurrio un algo y se corto el flujo: \n"),
    ERROR_SERVICIO("=== Error en servicio: ")
    ;

    private final String valor;

    EnumEtiquetas(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
