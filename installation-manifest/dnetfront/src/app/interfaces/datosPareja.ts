export interface DatosPareja {
  ninguno: boolean;
  datosParejas: [{
    id: number;
    idPosicionVista: string;
    registroHistorico: boolean;
    tipoOperacion: string;
    datosPersonales: {
      nombre: string;
      primerApellido: string;
      segundoApellido: string;
      rfc: string;
      fechaNacimiento: string;
    };
    relacionConDeclarante: string;
    ciudadanoExtranjero: {
      esExtranjero: boolean;
      curp: string;
    };
    esDependienteEconomico: boolean;
    habitaDomicilioDeclarante: boolean;
    domicilioDependienteEconomico: {
      lugarDondeReside: string;
      domicilio: {
        ubicacion: string;
        domicilioMexico: {
          calle: string;
          codigoPostal: string;
          coloniaLocalidad: string;
          entidadFederativa: {
            id: number;
            valor: string;
          };
              municipioAlcaldia: {
                id: number;
                valor: string;
                fk: number;
              };
        };
        domicilioExtranjero: {
          calle: string;
          ciudadLocalidad: string;
          codigoPostal: string;
          estadoProvincia: string;
          numeroExterior: string;
          numeroInterior: string;
          pais: {
            id: number;
            valor: string;
          };
        };
      };
    };
    ninguno?: boolean;
    actividadLaboral?: {
      ambitoSector: {
        id: number;
        valor: string;
      };
      ambitoSectorOtro?: string;
      sectorPublico: {
        nivelOrdenGobierno:string;
        ambitoPublico:string;
        nombreEntePublico: string;
        areaAdscripcion: string;
        empleoCargoComision: string;
        funcionPrincipal: string;
      };
      sectorPrivadoOtro: {
        nombreEmpresaSociedadAsociacion: string;
        rfc: string;
        area: string;
        empleoCargo: string;
        sector: {
          id: number;
          valor: string;
        };
        sectorOtro?: string;
        proveedorContratistaGobierno: boolean;
      };
      fechaIngreso: string;
      salarioMensualNeto: number;
    };
    verificar: boolean;
  }];
  aclaracionesObservaciones: string;
  // verificar: boolean;
}
