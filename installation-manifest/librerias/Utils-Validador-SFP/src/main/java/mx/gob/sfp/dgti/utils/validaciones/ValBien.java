/**
 * @(#)ValBien.java 11/11/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.base.EncabezadoDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.BienDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.PersonaDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.TransmisorDTO;
import mx.gob.sfp.dgti.declaracion.enums.campos.EnumBien;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.*;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.utils.propiedades.PropBien;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase con las validaciones de un bien: bienes inmuebles, bienes muebles y vehiculos.
 *
 * @author Pavel Alexei Martinez Regalado
 * @since 11/11/2019
 */
public class ValBien {

    /**
     * Metodo para definir el modulo de domicilio
     *
     * @param bien: objeto de bien mueble, bien inmueble o
     * @return Objeto ModuloValidarDTO
     *
     * @author Pavel Alexei Martinez Regalado
     * @since 12/09/2019
     */
	public static ModuloValidarDTO crearBien(BienDTO bien, String nombre, EncabezadoDTO encabezado) {

        List<PropiedadesValidarDTO> propiedades = new ArrayList<>();
        List<ModuloValidarDTO> modulos = new ArrayList<>();

        // validar el tipoOperacion
        PropBase.crearPropTipoOperacion(bien, encabezado, propiedades);

        // validor el id
        propiedades.add(PropBase.crearId(bien.getId()));

        // validar el idPosicionVista
        propiedades.add(PropBase.crearIdPosicionVista(bien.getIdPosicionVista()));

        //Validar CAT_TITULAR
        propiedades.add(PropBase.crearPropCatalogo(EnumBien.TITULAR.getNombre(), EnumCatalogosUno.CAT_TITULAR.name(),
                bien.getTitular(), true));

        //Validar terceros en caso de que sea necesario
	    if(bien.getTitular() != null && bien.getTitular().getValorUno()
                .equals(EnumCoopropiedad.COOPROPIEDAD.getDescripcion())) {
	        //Al menos un tercero es requerido
            propiedades.add(PropBase.crearModuloDebeSerNoNulo(bien.getTerceros(),
                    EnumBien.TERCEROS.getNombre()));
            if(bien.getTerceros() != null) {
                for (PersonaDTO tercero : bien.getTerceros()) {
                    modulos.add(ValPersona.crearPersona(tercero, EnumBien.TERCEROS.getNombre()));
                }
            }
        } else {
            propiedades.add(PropBase.crearModuloDebeSerNulo(bien.getTerceros(),
                    EnumBien.TERCEROS.getNombre()));
        }

	    //Validar transmisores
        propiedades.add(PropBase.crearModuloDebeSerNoNulo(bien.getTransmisores(),
                EnumBien.TRANSMISORES.getNombre()));
        if(bien.getTransmisores() != null) {
            for (TransmisorDTO transmisor : bien.getTransmisores()) {
                modulos.add(ValPersona.crearTransmisor(transmisor, EnumBien.TRANSMISORES.getNombre()));
            }
        }
        //Validar CAT_FORMA_ADQUISICION
        propiedades.add(PropBase.crearPropCatalogo(EnumBien.FORMA_ADQUISICION.getNombre(),EnumCatalogos.CAT_FORMA_ADQUISICION_BIEN.name(),
                bien.getFormaAdquisicion(), true));

        propiedades.add(PropBase.crearValidacionFechaContraFechaEncargo(bien.getFechaAdquisicion(),
                encabezado, EnumBien.FECHA_ADQUISICION.getNombre(), true));

        propiedades.add(PropBase.crearObligatoria(bien.getFormaPago(), EnumBien.FORMA_PAGO.getNombre()));

        //VALIDAR QUE CUANDO LA FORMA DE PAGO SEA
        if (bien.getFormaAdquisicion() != null && bien.getFormaPago() != null ) {
            propiedades.add(PropBien.crearFormaPago(bien.getFormaPago(), bien.getFormaAdquisicion()));
        }

        if (bien.getValorAdquisicion() != null ) {
            modulos.add(ValMonto.crearMonto(bien.getValorAdquisicion(), EnumBien.VALOR_ADQUISICION.getNombre()));
        } else {
            propiedades.add(PropBase.crearModuloDebeSerNoNulo(bien.getValorAdquisicion(), EnumBien.VALOR_ADQUISICION.getNombre()));
        }

        if(bien.getTipoOperacion().equals(EnumTipoOperacion.BAJA)) {
            //AGREGAR CAT_MOTIVO_BAJA
            PropBase.crearPropCatalogoOtro(EnumBien.MOTIVO_BAJA.getNombre(), EnumCatalogos.CAT_MOTIVO_BAJA_BIEN.name(),
                    bien.getMotivoBaja(), true,
                    bien.getMotivoBajaOtro(), propiedades);

            if(bien.getMotivoBaja() != null && bien.getMotivoBaja().getId().equals(EnumMotivoBaja.VENTA.getId())) {
                propiedades.add(PropBase.crearModuloDebeSerNoNulo(bien.getValorVenta(), EnumBien.VALOR_VENTA.getNombre()));
                if(bien.getValorVenta() != null ) {
                    modulos.add(ValMonto.crearMonto(bien.getValorVenta(), EnumBien.VALOR_VENTA.getNombre()));
                }
            } else {
                propiedades.add(PropBase.crearModuloDebeSerNulo(bien.getValorVenta(), EnumBien.VALOR_VENTA.getNombre()));
            }

        } else {
            propiedades.add(PropBase.crearModuloDebeSerNulo(bien.getMotivoBaja(), EnumBien.MOTIVO_BAJA.getNombre()));
            propiedades.add(PropBase.crearModuloDebeSerNulo(bien.getValorVenta(), EnumBien.VALOR_VENTA.getNombre()));
        }

		ModuloValidarDTO moduloValidar = new ModuloValidarDTO(nombre, bien.getIdPosicionVista());

		moduloValidar.setListPropsTovalidate(propiedades);
		moduloValidar.setListModuloshijos(modulos);

		return moduloValidar;
	}
}
