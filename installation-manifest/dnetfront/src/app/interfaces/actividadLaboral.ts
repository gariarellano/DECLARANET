export interface ActividadLaboral {
  ninguno: boolean;
  ambitoSector: {
        id: string,
        valor: string
    },
    sectorPublico: {
        nivelOrdenGobierno: string, // FEDERAL, ESTATAL, MUNICIPAL / ALCALDÍA
        ambitoPublico: string, //EJECUTIVO, LEGISLATIVO, JUDICIAL, ÓRGANO AUTÓNOMO ANTES PODER
        nombreEntePublico: string,
        areaAdscripcion: string,
        empleoCargoComision: string,
        funcionPrincipal: string
    },
    sectorPrivadoOtro: {
        nombreEmpresaSociedadAsociacion: string,
        rfc: string,
        area: string,
        empleoCargo: string,
        sector: {
            id: string,
            valor: string
        }
    },
    fechaIngreso: string;
    salarioMensualNeto: string;
    verificar: boolean;
}
