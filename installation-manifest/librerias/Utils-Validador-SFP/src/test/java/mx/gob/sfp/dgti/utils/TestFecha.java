package mx.gob.sfp.dgti.utils;

import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErroresDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFecha {

	@Test
	public void testEjecutaValidaciones() {

		ModuloValidarDTO dtoValidadciones = new ModuloValidarDTO("Fechas");

		PropiedadesValidarDTO fechaFormato = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaFormato.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
		
		PropiedadesValidarDTO fechaMayorQue = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaMayorQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORQUE, "20005/02/03"));
		
		PropiedadesValidarDTO fechaMenorQue = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaMenorQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORQUE, "20005/02/03"));

		PropiedadesValidarDTO fechaMayorMenorQue = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaMayorMenorQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORQUE_MENORQUE, "20005/02/03", "20005/02/04"));
		
		PropiedadesValidarDTO fechaMayorMenorIgualQue = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaMayorMenorIgualQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORIGUAL_MENORIGUAL_QUE, "20005/02/03", "20005/02/04"));
		
		PropiedadesValidarDTO fechaMayorIgualQue = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaMayorIgualQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MAYORIGUAL_QUE, "20005/02/03"));
		
		PropiedadesValidarDTO fechaMenorIgualQue = new PropiedadesValidarDTO("FECHA ", "20004/01/01");
		fechaMenorIgualQue.getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORIGUAL_QUE, "20005/02/03"));
		
		dtoValidadciones.getListPropsTovalidate().add(fechaFormato);
		dtoValidadciones.getListPropsTovalidate().add(fechaMayorQue);
		dtoValidadciones.getListPropsTovalidate().add(fechaMenorQue);
		dtoValidadciones.getListPropsTovalidate().add(fechaMayorMenorQue);
		dtoValidadciones.getListPropsTovalidate().add(fechaMayorMenorIgualQue);
		dtoValidadciones.getListPropsTovalidate().add(fechaMayorIgualQue);
		dtoValidadciones.getListPropsTovalidate().add(fechaMenorIgualQue);
		
		ErroresDTO erroresDto = new ErroresDTO();
		new ExectValidations().ejecutaValidaciones(erroresDto, dtoValidadciones);
		
		assertEquals(7, erroresDto.getListErorres().size());

	}

}
