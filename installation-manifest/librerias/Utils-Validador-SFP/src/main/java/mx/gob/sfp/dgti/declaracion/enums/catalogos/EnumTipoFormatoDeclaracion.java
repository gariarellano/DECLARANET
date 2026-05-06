/**
 * @(#)EnumTipoFormatoDeclaracion.java 14/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.enums.catalogos;

import java.util.Arrays;

public enum EnumTipoFormatoDeclaracion {

    SIMPLIFICADO(1, "SIMPLIFICADO", "S"),
    COMPLETO(2,"COMPLETO", "C");

    private  final Integer id;
    private final String descripcion;
    private final String valorUno;

    private EnumTipoFormatoDeclaracion(Integer id, String descripcion, String valorUno) {
            this.id = id;
            this.descripcion = descripcion;
            this.valorUno = valorUno;
    }

    /**
     * @return the id
     */
    public Integer getId() {
            return id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
            return descripcion;
    }

    /**
     * @return the valorUno
     */
    public String getValorUno() {
        return valorUno;
    }

    /**
     * Funcion para obtener el Enum Tipo de Formato por el valorUno
     */
    public static EnumTipoFormatoDeclaracion obtenerEnumPorValorUno(String valorUno) {
        return Arrays.asList(values()).stream()
                .filter(enumF -> enumF.getValorUno().equals(valorUno))
                .findFirst()
                .orElse(null);
    }
}
