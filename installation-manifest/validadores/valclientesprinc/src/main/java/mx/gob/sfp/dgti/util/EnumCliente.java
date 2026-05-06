/**
 * @(#)EnumCliente.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.util;

/**
 * Enum con los campos del módulo de inversion
 * @author Miriam Sanchez Sánchez programador07 
 * @since 25/11/2019
 */
public enum EnumCliente {

	REALIZA_ACT_LUC("realizaActividadLucrativa"),
	PARTICIPANTE("participante"),
	NOMBRE_EMPRESA("nombreEmpresaServicio"),
	RFC_EMPRESA("rfcEmpresa"),
	CLIENTE_PRINCIPAL("clientePrincipal"),
	SECTOR("sector"),
	MONTO_APROX_GANANCIA("montoAproximadoGanancia"),
	LOCALIZACION("localizacion");
	
	private final String campo;

    /**
     * Constructor con parametros
     * @param id 
	 */
    EnumCliente(String campo) {
        this.campo = campo;
    }

    /**
     * @return the campo
     */
	public String getCampo() {
		return campo;
	}

}
