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
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErroresDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Clase para probar validaciones de numeros, mayor que, menor que
 * 
 * @author pavel.martinez
 * @since 27/02/2020
 */
public class TestMayorMenor {

    @Test
    public void testNumeros() {
        ModuloValidarDTO dtoValidadciones = new ModuloValidarDTO("Fechas");

        PropiedadesValidarDTO menorQue = new PropiedadesValidarDTO("menor_que", "6");
        menorQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MENOR_QUE, "6"));

        dtoValidadciones.getListPropsTovalidate().add(menorQue);

        ErroresDTO erroresDto = new ErroresDTO();
        new ExectValidations().ejecutaValidaciones(erroresDto, dtoValidadciones);

        assertEquals(1, erroresDto.getListErorres().size());
    }
}
