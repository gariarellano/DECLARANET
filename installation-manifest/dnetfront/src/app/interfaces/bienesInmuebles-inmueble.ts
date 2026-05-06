export interface BienInmueble {

    id: number,
    idPosicionVista: number,
    registroHistorico: boolean,
    verificar: boolean,
    tipoOperacion: string,
    tipoInmueble: {
        id: string,
        valor: string,
    },
    tipoInmuebleOtro: string,
    titular: {
        id: number,
        valor: string,
        valorUno: string
    },
    porcentajePropiedad: number,
    superficieTerrenoM2: number,
    superficieConstruccionM2: number,
    terceros:
    {
        id: number,
        idPosicionVista: string,
        tipoPersona: string,
        personaFisica: {

            nombre: string,
            primerApellido: string,
            segundoApellido: string,
            rfc: string
        },
        personaMoral: {
            nombre: string,
            rfc: string
        }
    },
    transmisores: [
        {
            id: number,
            idPosicionVista: string,
            relacionConTitular: {
                id: string,
                valor: string,
                clavePdn: string
            },
            tipoPersona: string,
            personaFisica: {
                nombre: string,
                primerApellido: string,
                segundoApellido: string,
                rfc: string
            },
            personaMoral: {
                nombre: string,
                rfc: string
            }
        }
    ],
    formaAdquisicion: {
        id: number,
        valor: string
    },
    formaPago: string,
    valorAdquisicion: {
        //@montoMoneda
        moneda: {
            id: number,
            valor: string
        },
        monto: number
    },
    fechaAdquisicion: string,
    datoIdentificacion: string,
    valorConformeA: string, //Escritura publica,sentencia,contrato
    domicilio: {
        //@domicilio
        ubicacion: string, //Mexico, Extranjero
        domicilioMexico: { //uno u otro
            calle: string,
            codigoPostal: string,
            coloniaLocalidad: string,
            entidadFederativa: {
                id: number,
                valor: string,
            },
            municipioAlcaldia: {
                fk: number,
                id: number,
                valor: string
            }
            numeroExterior: string,
            numeroInterior: string
        },

        domicilioExtranjero: { //uno u otro
            calle: string,
            ciudadLocalidad: string,
            codigoPostal: string,
            estadoProvincia: string,
            numeroExterior: string,
            numeroInterior: string,
            pais: {
                id: number,
                valor: string
            }
        }
    },
    motivoBaja: {
        id: number;
        valor: string
    };
    motivoBajaOtro: string;
    valorVenta: {
        moneda: {
            id: number;
            valor: string
        };
        monto: number
    };
}