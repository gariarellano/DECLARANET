/**
 * @(#)PropMonto.java 03/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.propiedades;

import mx.gob.sfp.dgti.declaracion.enums.campos.EnumMontoMoneda;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con funciones estaticas que crean los objetos requeridos para la validacion de campos
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 03/11/2019
 */
public class PropMonto {

    /**
     * Metodo para crear la propiedad de Monto
     */
    public static PropiedadesValidarDTO crearPropMonto(Long valor) {

        List<ParametrosValicacionDTO> parametros = new ArrayList<>();
        parametros.add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));

        return new PropiedadesValidarDTO(
                EnumMontoMoneda.MONTO.getCampo(),
                valor,
                parametros,
                true);
    }
}
