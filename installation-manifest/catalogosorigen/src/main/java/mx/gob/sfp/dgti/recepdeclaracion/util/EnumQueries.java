/**
 * @(#)EnumQueries.java 19/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.recepdeclaracion.util;

/**
 * Enum de los tipos queries
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 19/12/2019
 */
public enum EnumQueries {
    QUERY("query"),

    CONSULTA_INDIVIDUAL("query {catalogo : obtenerCatalogoActivo {id valor}}"),
    CONSULTA_INDIDUAL_UNO("query {catalogo : obtenerCatalogoActivo {id valor valorUno}}"),
    CONSULTA_INDIVIDUAL_FK("query {catalogo : obtenerCatalogoActivo {id valor fk}}"),
    CONSULTA_INDIVIDUAL_UNO_FK("query {catalogo : obtenerCatalogoActivo {id valor valorUno fk}}"),
    CONSULTA_VALIDAR("query validarCatalogo($registro: CatalogoValidar!){validarInfoCatalogo(registro: $registro)}"),
    CONSULTA_ENTES("query {catalogo : obtenerEntes(filtro:{poder:EJECUTIVO nivelGobierno:FEDERAL}) " +
            "{id nombre : enteDesc nivelOrdenGobierno : nivelGobierno{" +
            " nivelOrdenGobierno : nivelGobierno entidadFederativa{" +
            " id : idEntidadFederativa valor: entidadFederativaDesc municipio{ id: idMunicipio valor :  municipioDesc " +
            "}}} ambitoPublico : poder }}")
    ;

    private final String valor;

    EnumQueries(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
