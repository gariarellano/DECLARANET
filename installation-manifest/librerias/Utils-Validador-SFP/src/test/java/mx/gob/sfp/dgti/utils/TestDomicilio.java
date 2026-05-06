/**
 * @(#)TestDomicilio.java 14/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.utils;

import mx.gob.sfp.dgti.declaracion.dto.general.DomicilioDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoFkDTO;
import mx.gob.sfp.dgti.declaracion.dto.individual.DomicilioMexicoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumUbicacion;
import mx.gob.sfp.dgti.utils.validaciones.ValDomicilio;
import mx.gob.sfp.dgti.validador.dto.out.ErrorMensajesDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *  Test de validacion de domicilio
 *
 * @author pavel.martinez
 * @since 14/10/2019
 */
public class TestDomicilio {

    /**
     * Constructor
     */
    public TestDomicilio() {
    }

    /**
     * Test
     */
    @Test
    public void validaDomicilio() {

        System.out.println("--------------TEST DOMICILIO--------------------");

        DomicilioDTO domicilio = new DomicilioDTO();
        DomicilioMexicoDTO domicilioMexico = new DomicilioMexicoDTO();
        domicilioMexico.setCalle("OLMO");
        domicilioMexico.setCodigoPostal("50350");//No debe de ser un código postal válido
        domicilioMexico.setColoniaLocalidad("San Jerónimo");
        domicilioMexico.setEntidadFederativa(new CatalogoDTO(1, "AGUASCALIENTES"));
        domicilioMexico.setMunicipioAlcaldia(new CatalogoFkDTO(1,"AGUASCALIENTES", 1));
        domicilioMexico.setNumeroExterior("28 bis a");
        domicilioMexico.setNumeroInterior("4");

        EnumUbicacion ubicacion = EnumUbicacion.MEXICO;
        domicilio.setDomicilioMexico(domicilioMexico);
        domicilio.setUbicacion(ubicacion);

        ModuloDTO moduloDto = new ModuloDTO("domicilioDeclarante");
        new ExectValidations().ejecutaValidaciones(ValDomicilio.crearDomicilio(domicilio, "domicilioDeclarante"), moduloDto);
        System.out.println("ModuloDTO: " + moduloDto);
        
        for(PropiedadesErrorDTO index :moduloDto.getErrores()){
            System.out.println(" Nombre Campo :"+index.getNombreCampo()+" Valor :"+index.getPropiedadValor());
            for(ErrorMensajesDTO indexPr : index.getListErrorMensajes()){
                System.out.println("IdError:"+indexPr.getErrorId()+" MENSAJE:"+indexPr.getMensaje());    
            }
        }

        assertEquals(0, moduloDto.getErrores().size());

    }
}
