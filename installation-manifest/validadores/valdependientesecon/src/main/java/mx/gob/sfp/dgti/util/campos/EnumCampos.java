/**
 * @(#)EnumCampos.java 04/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util.campos;

/**
 * Enums de campos
 * @author pavel.martinez
 * @since 04/11/2019
 */
public enum EnumCampos {

    DEPENDIENTES_ECONOMICOS("dependientesEconomicos"),
    DOMICILIO("domicilio"),
    DOMICILIO_DEP_ECON("domicilioDependienteEconomico"),
    DATOS_PERSONALES("datosPersonales"),
    RELACION_CON_DECL("relacionConDeclarante"),
    ES_DEPENDIENTE_ECON("esDependienteEconomico"),
    CIUDADANO_EXTRANJERO("ciudadanoExtranjero"),
    ES_EXTRANJERO("esExtranjero"),
    CURP("curp"),
    HABITA_DOMICILIO_DECL("habitaDomicilioDeclarante"),
    LUGAR_DONDE_RESIDE("lugarDondeReside"),
    NINGUNO("ninguno"),
    ACTIVIDAD_LABORAL("actividadLaboral");

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
