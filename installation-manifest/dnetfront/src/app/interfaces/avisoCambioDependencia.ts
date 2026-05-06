export interface DetalleAvisoCambioDependencia {

    empleoCargoComisionConcluye: {
        ente: {
            ambitoPublico: string,
            id: string,
            nivelOrdenGobierno: {
                entidadFederativa: string,
                nivelOrdenGobierno: string
            },
            nombre: string
        },
        nivelJerarquico: {
            id: number,
            valor: string,
            fk: number,
            valorUno: string
          },
        fechaConclusionEncargo: string

    },

    empleoCargoComisionInicia: {
        id: number,
        ente: {
            ambitoPublico: string,
            id: string,
            nivelOrdenGobierno: {
                entidadFederativa: {
                    id: number,
                    valor: string
                },
                nivelOrdenGobierno: string
            },
            nombre: string
        },
        fechaInicioEncargo: string,
        empleoCargoComision: string,
        nivelJerarquico: {
            id: number,
            valor: string,
            fk: number,
            valorUno: string
        },
        contratadoPorHonorarios: boolean,
        nivelEmpleoCargoComision: string,
        areaAdscripcion: string,
        domicilio: {
            //@domicilio
            ubicacion: string, //México, Extranjero
            domicilioMexico: { //uno u otro
                calle: string,
                codigoPostal: number,
                coloniaLocalidad: string,
                entidadFederativa: {
                    id: number,
                    valor: string
                },
                municipioAlcaldia: {
                    id: number,
                    valor: string,
                    fk: number
                },
                numeroExterior: string,
                numeroInterior: string
            },
            domicilioExtranjero: { //uno u otro
                calle: string,
                ciudadLocalidad: string,
                codigoPostal: number,
                estadoProvincia: string,
                numeroExterior: string,
                numeroInterior: string,
                pais: {
                    id: number,
                    valor: string
                }
            }
        },

    },

    aclaracionesObservaciones: string,
}

// export interface AvisoCambioDatos{
//     aclaracionesObservaciones: string,
//     detalleAvisoCambioDependencia: Array <DetalleAvisoCambioDependencia>,
//     ninguno: boolean
// }