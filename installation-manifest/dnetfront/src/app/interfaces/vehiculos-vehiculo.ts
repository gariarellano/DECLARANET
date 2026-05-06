export interface Vehiculo {
    id: string;
    idPosicionVista: number;
    registroHistorico: boolean;
    verificar: boolean;
    tipoOperacion: string;
    tipoVehiculo: {
        id: string;
        valor: string;
    };
    tipoVehiculoOtro: string;
    titular: {
        id: number;
        valor: string;
        valorUno: string
    };
    transmisores: [ // al menos uno
        {
            id: string;
            idPosicionVista: string;
            relacionConTitular: {
                id: string;
                valor: string;
            };
            tipoPersona: string; // Fisica; Moral
            personaFisica: {
                // @persona
                nombre: string;
                primerApellido: string;
                segundoApellido: string;
                rfc: string
            };
            personaMoral: {
                // @personaMoral
                nombre: string;
                rfc: string
            }
        }
    ];
    marca: string;
    modelo: string;
    anio: string;
    numeroSerieRegistro: string;
    terceros: [// al menos uno
        {
            id: string;
            idPosicionVista: string;
            tipoPersona: string; // Fisica; Moral
            personaFisica: {
                // @persona
                nombre: string;
                primerApellido: string;
                segundoApellido: string;
                rfc: string
            };
            personaMoral: {
                // @personaMoral
                nombre: string;
                rfc: string
            }
        }
    ];
    lugarRegistro: {
        // @localizacion

        ubicacion: string;

        localizacionMexico: {
            entidadFederativa: {
                id: number;
                valor: string
            }
        };

        localizacionExtranjero: { // uno u otro
            pais: {
                id: number;
                valor: string
            }
        }
    };
    formaAdquisicion: {
        id: string;
        valor: string
    };
    formaPago: string;
    valorAdquisicion: {
        // @montoMoneda
        moneda: {
            id: string;
            valor: string
        };
        monto: string
    };
    fechaAdquisicion: string;
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