/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.utils;

import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErroresDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Clase que permiter realizar el testUnit del proyecto.
 * 
 * @author cgarias
 * @since 26/09/2019
 */
public class ExectValidationsTest {
    
    public ExectValidationsTest() {
    }

    /**
     * Test of ejecutaValidaciones method, of class ExectValidations.
     */
    @Test
    public void testEjecutaValidaciones() {
        System.out.println("ejecutaValidaciones");
        ModuloValidarDTO dtoValidadciones = new ModuloValidarDTO("Valor");
        new TestConHijos(dtoValidadciones);//Asisgna valores a objeto
        
        ErroresDTO erroresDto = new ErroresDTO();
        new ExectValidations().ejecutaValidaciones(erroresDto, dtoValidadciones);
        
        ModuloDTO moduloDto = new ModuloDTO("NOMBRE-MODULO");
        new ExectValidations().ejecutaValidaciones(dtoValidadciones, moduloDto);

        assertEquals(0, erroresDto.getListErorres().size()+moduloDto.getErrores().size());
    }

    /**
     * Test of ejecutaValidacioes method, of class ExectValidations.
     */
    @Test
    public void testEjecutaValidacioes() {
        System.out.println("ejecutaValidacioes");        
        List<PropiedadesValidarDTO> listPropVali = new ArrayList<>();
        new TestPlano().llenadoDeValoresParaValidar(listPropVali);
        
        List<PropiedadesErrorDTO> listPropErro = new ArrayList<>();
        
        ExectValidations instance = new ExectValidations();
        instance.ejecutaValidacioes(listPropVali, listPropErro);
        
        assertEquals(0, listPropErro.size());
    }
    
}
