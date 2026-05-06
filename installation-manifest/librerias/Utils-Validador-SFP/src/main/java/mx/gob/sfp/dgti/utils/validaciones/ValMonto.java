/**
 * @(#)ValMonto.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.individual.MontoMonedaDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumMontoMoneda;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogos;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropMonto;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de monto moneda
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
public class ValMonto {

    /**
     * Metodo para definir el submodulo de monto
     *
     * @param monto: objeto con el monto de la moneda
	 * @param nombre: objeto con el nombre del campo
     * @return Objeto ModuloValidarDTO
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 03/11/2019
     */
	public static ModuloValidarDTO crearMonto(MontoMonedaDTO monto, String nombre) {

		if(monto == null) {monto = new MontoMonedaDTO();}
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(nombre);
		List<PropiedadesValidarDTO> propiedades = new ArrayList<>();

		propiedades.add(PropMonto.crearPropMonto(monto.getMonto()));
		// CAT_MONEDA
		propiedades.add(PropBase.crearPropCatalogo(
				EnumMontoMoneda.MONEDA.getCampo(), EnumCatalogos.CAT_MONEDA.name(),
				monto.getMoneda(), true
		));

		moduloValidar.setListPropsTovalidate(propiedades);

		return moduloValidar;
	}

}
