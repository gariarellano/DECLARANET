/**
 * @(#)ModuloAvisoDTO.java 12/02/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dto;

import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoUnoFkDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;

/**
 * DTO para el modulo de detalle del aviso por cambio de dependencia
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 12/02/2020
 */
public class ModuloAvisoDTO extends ModuloDTO {

    /**
     * Bandera
     */
    private CatalogoUnoFkDTO niverJerarquico;

    /**
     * Constructor
     *
     * @param niverJerarquico
     */
    public ModuloAvisoDTO(CatalogoUnoFkDTO niverJerarquico) {
        this.niverJerarquico = niverJerarquico;
    }

    /**
     * Constructor
     *
     * @param modulo
     */
    public ModuloAvisoDTO(String modulo) {
        super(modulo);
    }

    /**
     * Constructor
     *
     * @param modulo
     * @param niverJerarquico
     */
    public ModuloAvisoDTO(String modulo, CatalogoUnoFkDTO niverJerarquico) {
        super(modulo);
        this.niverJerarquico = niverJerarquico;
    }

    public CatalogoUnoFkDTO getNiverJerarquico() {
        return niverJerarquico;
    }

    public void setNiverJerarquico(CatalogoUnoFkDTO niverJerarquico) {
        this.niverJerarquico = niverJerarquico;
    }

    @Override
    public String toString() {
        return "ModuloAvisoDTO{" +
                "niverJerarquico=" + niverJerarquico +
                "} " + super.toString();
    }
}
