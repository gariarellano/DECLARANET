export interface BienMueble{
    id: number,
    idPosicionVista: number,
    registroHistorico:boolean,
    verificar:boolean,
    tipoOperacion: string,
    tipoBien: {
        id: number,
        valor: string
    },
    tipoBienOtro:string,
    titular: {
        id: number,
        valor: string,
        valorUno: string
    },
    descripcionGeneralBien: string,
    formaAdquisicion: {
        id: number,
        valor: string
    }, //CATALOGO
    formaPago: string, //CRÉDITO,CONTADO,NO APLICA
    valorAdquisicion: {
        //@montoMoneda
        moneda: {
            id: number,
            valor: string
        },
        monto: number
    },
    fechaAdquisicion: string,
    terceros: [//al menos uno
        {
            id: string,
            idPosicionVista: number,
            tipoPersona: string, //Fisica, Moral
            personaFisica: {
                //@persona
                nombre: string,
                primerApellido: string,
                segundoApellido: string,
                rfc: string
            },
            personaMoral: {
                //@personaMoral
                nombre: string,
                rfc: string
            }
        }
    ],
    transmisores: [//al menos uno
        {
            id: string,
            idPosicionVista: number,
            relacionConTitular: {
                id: number,
                valor: string,
            },
            tipoPersona: string, //Fisica, Moral
            personaFisica: {
                //@persona
                nombre: string,
                primerApellido: string,
                segundoApellido: string,
                rfc: string
            },
            personaMoral: {
                //@personaMoral
                nombre: string,
                rfc: string
            }
        }
    ],
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