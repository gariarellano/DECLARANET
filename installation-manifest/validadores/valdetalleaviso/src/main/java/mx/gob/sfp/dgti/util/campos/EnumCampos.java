/**
 * @(#)EnumCampos.java 30/01/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 30/01/2020
 */
public enum EnumCampos {

    DETALLE_AVISO("detalleAvisoCambioDependencia"),
    ENTE("ente"),
    EMPLEO_CONCLUYE("empleoCargoComisionConcluye"),
    EMPLEO_INICIA("empleoCargoComisionInicia"),
    FECHA_INICIO("fechaInicioEncargo"),
    FECHA_CONCLUYE("fechaConclusionEncargo"),
    EMPLEO("empleoCargoComision"),
    NIVEL_JERARQUICO("nivelJerarquico"),
    CONTRATO_HONORARIOS("contratadoPorHonorarios"),
    NIVEL("nivelEmpleoCargoComision"),
    AREA_ADSCRIPCION("areaAdscripcion"),
    DOMICILIO("domicilio"),
    ACLARACIONES("aclaracionesObservaciones");

    private final String campo;

    /**
     * Constructor con parametros
     * @param campo
     */
    EnumCampos(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
    public String getCampo() {
        return campo;
    }

}
